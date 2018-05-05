package colorsorter;

//Nötige Imports
import java.io.File;
import lejos.hardware.*;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;


public class TestKlasse {
	
	EV3ColorSensor colorSensor;
	SampleProvider colorProvider;
	float[] colorSample;
	public static boolean arbeite;
	static Audio audio;
	static Sound so1;
	//Variable gibt an ob gerade Steine eingelegt werden (Für spätere Verwendung angedacht)
	public static boolean insertbricks;
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 

	public static void main(String[] args) throws InterruptedException {
		arbeite = true;
		insertbricks = true;
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		
		
		
		
		
		
		 
		 //TestKlasse colortest = new TestKlasse();
		 
		 //Neuen Thread Grundbetrieb aufrufen, ermöglicht simultanes Abarbeiten mehrerer Prozesse
		 Grundbetrieb g1 = new Grundbetrieb();
		 g1.start();
		 
		 //Neuen Scanner Thread aufrufen
		 Scanner2 sc1 = new Scanner2();
		 sc1.start();
		 
	}
	
	/*public static void rotateMotor1 (int direction) {
		if (direction == 1) {
			m1.rotate(45);
			Delay.msDelay(1200);
			m1.rotate(135);
		}
		else if (direction == 2) {
			m1.rotate(-45);
			Delay.msDelay(1200);
			m1.rotate(-135);
		}		
	}
	public static void rotateMotor2 (int direction) {
		if (direction == 1) {
			m2.rotate(45);
			Delay.msDelay(5000);
			m2.rotate(135);
		}
		else if (direction == 2) {
			m2.rotate(-45);
			Delay.msDelay(5000);
			m2.rotate(-135);
		}		
	}*/
	
	/*public TestKlasse() {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1");
		Port s2 = brick.getPort("S2");
		colorSensor = new EV3ColorSensor(s1);
		colorProvider = colorSensor.getColorIDMode();
		colorSample = new float[colorProvider.sampleSize()];
		EV3TouchSensor t1 = new EV3TouchSensor(s2);
		//audio = brick.getAudio();
		//audio.playSample(new File("green.wav"), 100);
		colorSensor.setFloodlight(true);
		colorSensor.setFloodlight(Color.WHITE);
		Delay.msDelay(1000);
		colorSensor.setFloodlight(Color.RED);
		
		while (Button.ESCAPE.isUp()) {
			int currentDetectedColor = colorSensor.getColorID();
			switch(currentDetectedColor) {
			
			case Color.RED:
				System.out.println("ROT");
				Sound.playNote(PIANO, 450, 2);
				rotateMotor1(1);
				break;
			case Color.YELLOW:
				System.out.println("GELB");
				Sound.playNote(PIANO, 400, 2);
				rotateMotor1(2);
				break;
			case Color.GREEN:
				//System.out.println("GRÜN");
				//rotateMotor2(1);
				break;
			case Color.BLUE:
				//System.out.println("BLAU");
				//Sound.playNote(PIANO, 350, 2);
				//rotateMotor2(2);
				break;

			default:
				System.out.println("---");
				break;
			}
			Delay.msDelay(250);
			
		}
		
		colorSensor.close();
	}*/
	
	   

}
