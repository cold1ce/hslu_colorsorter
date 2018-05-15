//Diese Klasse steuert den kompletten Scan und Abwurfprozess

package colorsorter;

import java.io.IOException;

import lejos.hardware.Audio;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
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

public class Scannen extends Thread{
	public static EV3ColorSensor colorSensor;
	public static SampleProvider colorProvider;
	public static float[] colorSample;
	public static SensorMode color;
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 
	public static RegulatedMotor m3 = new EV3LargeRegulatedMotor(MotorPort.A);
	public static Audio audio;
	public static Brick brick;
	public static boolean auflegen;
	
	public void run() {
		//Förderband starten, davor Ton abspielen
		System.out.println("Förderband gestartet.");
		Sound.playNote(PIANO, 150, 600);
		Delay.msDelay(400);
		Sound.playNote(PIANO, 150, 600);
		Delay.msDelay(400);
		Sound.playNote(PIANO, 150, 600);
		Delay.msDelay(1000);
		m3.setSpeed(40);
		m3.backward();
		
		brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1");
		colorSensor = new EV3ColorSensor(s1);
		SensorMode color = colorSensor.getRGBMode();
		colorSample = new float[3];
		
		
		
		//Neuen Thread Auflegen aufrufen, zuständig für das Auflegen der Steine auf das Förderband
		System.out.println("Auflegen der Steine aktiviert.");
		auflegen = true;
		Auflegen a1 = new Auflegen();
		a1.start();
				
		//Solange die Schleife nicht durch Tastendruck unterbrochen wird, scanne permanent alle 50ms
		//das Förderband.
		while (!Thread.currentThread().isInterrupted() && Sortierer.machinerunning == true) {
			//Abrufen des aktuellen Samples, ein Sample ist der RGB-Wert zu genau
			//dem Zeitpunkt des Aufrufs.
			color.fetchSample(colorSample, 0);
			//Das Sample überprüfen ob es zu einem Stein gehört oder nicht.
			int colorid = checkForColor(colorSample);
			//Wenn das Sample zu einem Stein gehört, Stein abwerfen.
			if (colorid > 0) {
				System.out.println("Farbe "+getColorName(colorid)+" wurde erkannt.");
				System.out.println("Werfe Stein ab...");
				
				abwerfen(colorid);
				DataSender d1 = new DataSender();
				try {
					d1.run(getColorName(colorid));
				} catch (IOException e) {
					// TODO Automatisch generierter Erfassungsblock
					e.printStackTrace();
				}
			}
			Delay.msDelay(50);
		}		
		colorSensor.close();
		m3.stop();
		m3.close();
		
	}
	//Überprüft ein Farb-Sample. Wenn die Daten im Farbspektrum eines Steins
		//liegen, wird der je nach Farbe ein Farbcode zurückgegeben.
		//(0=Kein Stein, 1=Gelb, 2=Rot, 3=Blau, 4=Grün)
		public static int checkForColor (float[] colorSample) {
			int colorid = -1;
			if (colorSample[0] >= 0.13 && colorSample[1] >= 0.085) {
				Sound.playNote(PIANO, 200, 100);
				colorid = 1;
			}
			else if (colorSample[0] >= 0.07 && colorSample[0] <= 0.13 && colorSample[1] < 0.04) {
				Sound.playNote(PIANO, 400, 100);
				colorid = 2;
			}
			else if (colorSample[0] <= 0.05 && colorSample[1] <= 0.055 && colorSample[1] >= 0.02 && colorSample[2] >= 0.035) {
				Sound.playNote(PIANO, 600, 100);
				colorid = 3;
			}
			else if (colorSample[0] <= 0.03 && colorSample[1] <= 0.075 && colorSample[1] >= 0.04 && colorSample[2] < 0.035) {
				Sound.playNote(PIANO, 800, 100);
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
			if (farbeid == 1) {
				RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.C);
				m1.setSpeed(400);
				m1.rotate(60);
				m1.close();
				m3.setSpeed(200);
				Delay.msDelay(1700);
				m3.stop();
				RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.C);
				m11.setSpeed(75);
				m11.rotate(120);
				m11.close();
				Delay.msDelay(300);
				m3.setSpeed(40);
				m3.backward();
			}
			else if (farbeid == 2) {
				RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.C);
				m1.setSpeed(400);
				m1.rotate(-60);
				m1.close();
				m3.setSpeed(200);
				Delay.msDelay(1700);
				m3.stop();
				RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.C);
				m11.setSpeed(75);
				m11.rotate(-120);
				m11.close();
				Delay.msDelay(300);
				m3.setSpeed(40);
				m3.backward();
			}
			else if (farbeid == 3) {
				RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.B);
				m1.setSpeed(400);
				m1.rotate(60);
				m1.close();
				m3.setSpeed(200);
				Delay.msDelay(2600);
				m3.stop();
				RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.B);
				m11.setSpeed(75);
				m11.rotate(120);
				m11.close();
				Delay.msDelay(300);
				m3.setSpeed(40);
				m3.backward();
			}
			else if (farbeid == 4) {
				RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.B);
				m1.setSpeed(400);
				m1.rotate(-60);
				m1.close();
				m3.setSpeed(200);
				Delay.msDelay(2600);
				m3.stop();
				RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.B);
				m11.setSpeed(75);
				m11.rotate(-120);
				m11.close();
				Delay.msDelay(300);
				m3.setSpeed(40);
				m3.backward();
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
		
		//Gibt aus einem Sample die gerundeten RGB-Werte zurück. 
		public static String printRGB(float[] colorSample) {
			return "R: "+(Math.round(1000.0 * colorSample[0]) / 1000.0)+"R: "+(Math.round(1000.0 * colorSample[1]) / 1000.0)+"B: "+(Math.round(1000.0 * colorSample[2]) / 1000.0);
		}
		
		//Wandelt die ColorID in den Farbnamen um, welcher als String zurückgegeben wird.
		public static String getColorName(int colorid) {
			String ret = "";
			if (colorid == 1) {ret = "gelb";}
			else if (colorid == 2) {ret = "rot";}
			else if (colorid == 3) {ret = "blau";}
			else if (colorid == 4) {ret = "gruen";}
			else { ret = "Unbekannt";}
			return ret;
		}	
}
