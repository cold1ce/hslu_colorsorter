// Diese Klasse steuert den kompletten Scan- und Abwurfprozess des
// Roboters, und sendet auch Nachrichten zurück an den Raspberry Pi

package colorsorter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import lejos.hardware.Audio;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ColorSorter extends Thread{
	
	// Initialisieren der benötigten Objekte für den Farbsensor
	public static EV3ColorSensor colorSensor;
	public static SampleProvider colorProvider;
	public static SensorMode color;
	
	// In diesem float-Array werden nacher die 3 Werte für 
	// R G und B abgelegt.
	public static float[] colorSample;
	
	// Um nacher Töne abspielen zu können
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 
	
	// Der EV3 als Objekt, um den Sensor ansprechen zu können.
	public static Brick brick;

	
	
	public void run() {
		//LED des EV3 auf schnell blinkendes Grün umstellen
		Button.LEDPattern(7);
		
		// Den Farbsensor auf Port 1 registrieren
		brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1");
		colorSensor = new EV3ColorSensor(s1);
		
		// Den Modus des Farbsensors auf RGB einstellen, anschließend
		// 3 Plätze im colorSample-Array schaffen (für R, G und B)
		SensorMode color = colorSensor.getRGBMode();
		colorSample = new float[3];
		
		
		// Förderband starten, davor 3 Töne abspielen
		Machine.feederband = new EV3LargeRegulatedMotor(MotorPort.A);
		Sound.playNote(PIANO, 150, 600);
		Delay.msDelay(400);
		Sound.playNote(PIANO, 150, 600);
		Delay.msDelay(400);
		Sound.playNote(PIANO, 150, 600);
		Delay.msDelay(1000);
		Machine.feederband.setSpeed(40);
		Machine.feederband.backward();
		System.out.println("Foerderband gestartet.");
		
		//Farbe auf grün blinkend stellen
		Button.LEDPattern(4);
		
		//Neuen Thread StoneSlider aufrufen, zuständig für das Auflegen der Steine auf das Förderband
		StoneSlider a1 = new StoneSlider();
		a1.start();
		System.out.println("Auflegen der Steine aktiviert.");
				
		// Solange die Schleife nicht durch Tastendruck unterbrochen wird oder durch einen Befehl 
		// des EV3 ausgeschaltet wird: Scanne permanent alle 50ms das Förderband.
		Machine.timewithoutstoneonfeederband = 0.0;
		while (!Thread.currentThread().isInterrupted() && Machine.machinerunning == true) {
			// Wenn zu lange kein Stein mehr kommt, Maschine abschalten!
			if (Machine.timewithoutstoneonfeederband >= Machine.automaticoff) {
				Machine.machineoutofstone = true;
				Machine.machinerunning = false;
				break;
			}
			
			// Abrufen des aktuellen Samples, ein Sample ist der RGB-Wert, der vom Sensor zu genau
			// dem Zeitpunkt des Aufrufs gemessen wurde.
			color.fetchSample(colorSample, 0);
			
			// Das Sample überprüfen ob es die Werte aufweist, die zu einem Stein gehören könnten.
			// Wenn das so ist, bekommt die Variable colorid je nach Farbe eine Zahl, anhand der
			// die Farbe identifiziert werden kann zugewiesen (1=Gelb, 2=Rot, 3=Blau, 4=Grün)
			int colorid = checkForColor(colorSample);
			
			// Wenn das aktuell gemesene Sample zu einem Stein gehört:
			if (colorid > 0 ) {
				
				// Zähler für Zeit ohne Stein wieder resetten.
				Machine.timewithoutstoneonfeederband = 0.0;
				
				// Wenn ein Filter auf diesen Stein gesetzt ist, d.h dieser
				// auch abgeworfen werden soll:
				if (checkForDrop(colorid) == true) {	
					// Den Zähler für die Steine um 1 erhöhen
					Machine.stonescounted += 1;
					
					System.out.println("Farbe "+getColorName(colorid)+" wurde erkannt. Werfe Stein ab...");
					
					// Eine Meldung an den Raspberry Pi schicken, dass ein Stein erkannt wurde, welcher nun 
					// abgeworfen wird.
					DataSender d1 = new DataSender("scannedstone", getColorName(colorid), giveRGBString(colorSample));
					d1.start();
					
					// Den Abwurf-Mechanismus starten.
			    	abwerfen(colorid);
				}
			}
			
			// Alle 50ms wird das Förderband gescannt, wird kein Stein erkannt, wird
			// der Zähler um 0.05 erhöht.
			else {
				Machine.timewithoutstoneonfeederband += 0.05;
			}
		
		//Künstliche Verzögerung für das nächste Abfragen eines Sensorwertes.
		Delay.msDelay(50);
			
		}
		
		// Falls die Maschine per Befehl angehalten wird, halte das Förderband an
		// und deaktiviere den Sensor. Setze die LED am EV3 auf Gelb.
		colorSensor.close();
		Machine.feederband.stop();
		System.out.println("Scan Thread vorbei.");
		Machine.feederband.close();
		DataSender d1 = new DataSender("responsemachinestopped");
		
		
		Button.LEDPattern(6);
		this.interrupt();
		
		
	}
	
	//Überprüft ein Farb-Sample. Wenn die Daten im Farbspektrum eines Steins
	//liegen, wird der je nach Farbe ein Farbcode zurückgegeben.
	//(0=Kein Stein, 1=Gelb, 2=Rot, 3=Blau, 4=Grün)
	public static int checkForColor (float[] colorSample) {
			int colorid = -1;
			if (colorSample[0] >= 0.13 && colorSample[1] >= 0.085) {
				//Sound.playNote(PIANO, 200, 100);
				colorid = 1;
			}
			else if (colorSample[0] >= 0.07 && colorSample[0] <= 0.13 && colorSample[1] < 0.04) {
				//Sound.playNote(PIANO, 400, 100);
				colorid = 2;
			}
			else if (colorSample[0] <= 0.05 && colorSample[1] <= 0.055 && colorSample[1] >= 0.02 && colorSample[2] >= 0.035) {
				//Sound.playNote(PIANO, 600, 100);
				colorid = 3;
			}
			else if (colorSample[0] <= 0.03 && colorSample[1] <= 0.075 && colorSample[1] >= 0.04 && colorSample[2] < 0.035) {
				//Sound.playNote(PIANO, 800, 100);
				colorid = 4;
			}
			else {
				colorid = 0;
			}	
			return colorid;
		}

	//Aktiviert den Abwurfmechanismus, je nach übergebener Farb-ID wird das Förderband
	//schnell an die richtige Stelle gefahren und der Stein abgeworfen.
	public static void abwerfen(int farbeid) {
		Sound.playNote(PIANO, 400, 100);
		if (farbeid == 1) {
			RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.C);
			m1.setSpeed(400);
			m1.rotate(60);
			m1.close();
			Machine.feederband.setSpeed(200);
			Delay.msDelay(1700);
			Machine.feederband.stop();
			RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.C);
			m11.setSpeed(75);
			m11.rotate(120);
			m11.close();
			Delay.msDelay(300);
			Machine.feederband.setSpeed(40);
			Machine.feederband.backward();
		}
		else if (farbeid == 2) {
			RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.C);
			m1.setSpeed(400);
			m1.rotate(-60);
			m1.close();
			Machine.feederband.setSpeed(200);
			Delay.msDelay(1700);
			Machine.feederband.stop();
			RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.C);
			m11.setSpeed(75);
			m11.rotate(-120);
			m11.close();
			Delay.msDelay(300);
			Machine.feederband.setSpeed(40);
			Machine.feederband.backward();
		}
		else if (farbeid == 3) {
			RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.B);
			m1.setSpeed(400);
			m1.rotate(60);
			m1.close();
			Machine.feederband.setSpeed(200);
			Delay.msDelay(2600);
			Machine.feederband.stop();
			RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.B);
			m11.setSpeed(75);
			m11.rotate(120);
			m11.close();
			Delay.msDelay(300);
			Machine.feederband.setSpeed(40);
			Machine.feederband.backward();
		}
		else if (farbeid == 4) {
			RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.B);
			m1.setSpeed(400);
			m1.rotate(-60);
			m1.close();
			Machine.feederband.setSpeed(200);
			Delay.msDelay(2600);
			Machine.feederband.stop();
			RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.B);
			m11.setSpeed(75);
			m11.rotate(-120);
			m11.close();
			Delay.msDelay(300);
			Machine.feederband.setSpeed(40);
			Machine.feederband.backward();
		}	
	}
	
	//Methode überprüft ob das aktuelle Sample auf einen Stein hinweist oder nicht
	//Je nachdem wird ein wahr/falsch zurückgegeben.
	public static boolean checkForBrick(float[] colorSample) {
		if (colorSample[0] >= 0.01 && colorSample[1] >= 0.015 && colorSample[1] >= 0.006) {
			return true;
		}
		else {
			return false;
		}		
	}
	
	//Gibt aus einem Sample die gerundeten EV3-RGB-Werte zurück. 
	public static String printRGB(float[] colorSample) {
		return "R: "+(Math.round(1000.0 * colorSample[0]) / 1000.0)+"R: "+(Math.round(1000.0 * colorSample[1]) / 1000.0)+"B: "+(Math.round(1000.0 * colorSample[2]) / 1000.0);
	}
	
	//Gibt aus einem Sample die gerundeten 255-RGB-Werte mit Kommata zurück. 
	public static String giveRGBString(float[] colorSample) {
		int[] colorSampleInt = new int[colorSample.length];
		for (int i=0; i < colorSample.length; i++) {
			//Umrechnen auf die offizielle RGB-Farbspanne mit Werten von 0 - 255
			colorSample[i] = colorSample[i]*1000;
			colorSampleInt[i] = (int) Math.round(colorSample[i]);
		}
		return (colorSampleInt[0]+", "+colorSampleInt[1]+", "+colorSampleInt[2]);
	}
	
	public static boolean checkForDrop(int colorid) {
		boolean drop = false;
		
		if (colorid == 1 && Machine.dropyellow == true) {
			drop = true;
		}
		if (colorid == 2 && Machine.dropred == true) {
			drop = true;
		}
		if (colorid == 3 && Machine.dropblue == true) {
			drop = true;
		}
		if (colorid == 4 && Machine.dropgreen == true) {
			drop = true;
		}
		return drop; 
	}
	
	//Wandelt die ColorID in den Farbnamen um, welcher als String zurückgegeben wird.
	public static String getColorName(int colorid) {
			String ret = "";
			if (colorid == 1) {ret = "yellow";}
			else if (colorid == 2) {ret = "red";}
			else if (colorid == 3) {ret = "blue";}
			else if (colorid == 4) {ret = "green";}
			else { ret = "Unbekannt";}
			return ret;
		}	
}
