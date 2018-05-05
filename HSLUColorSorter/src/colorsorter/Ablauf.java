package colorsorter;

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

public class Ablauf {
	static RegulatedMotor m1 = new EV3LargeRegulatedMotor(MotorPort.C);
	static RegulatedMotor m2 = new EV3LargeRegulatedMotor(MotorPort.B);
	static RegulatedMotor m3 = new EV3LargeRegulatedMotor(MotorPort.A);
	static RegulatedMotor m4 = new EV3LargeRegulatedMotor(MotorPort.D);
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 
	static EV3ColorSensor colorSensor;
	SampleProvider colorProvider;
	static float[] colorSample;
	
	public static void main(String[] args) {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1");
		colorSensor = new EV3ColorSensor(s1);
		SensorMode color = colorSensor.getRGBMode();
		colorSample = new float[color.sampleSize()];
		//audio = brick.getAudio();
		//audio.playSample(new File("green.wav"), 100);
		colorSensor.setFloodlight(false);
		//colorSensor.setFloodlight(Color.WHITE);
		//colorSensor.setFloodlight(Color.RED);
		System.out.println("Zeug eingestellt...");
		
		
		
		
		Sound.playNote(PIANO, 350, 100);
		Delay.msDelay(500);
		Sound.playNote(PIANO, 350, 100);
		Delay.msDelay(500);
		Sound.playNote(PIANO, 350, 100);
		Delay.msDelay(1000);
		color.fetchSample(colorSample, 0);//Das der Sensor grichd isch
		while (!Thread.currentThread().isInterrupted()) {
			m3.setSpeed(55);
			m3.backward();
			m4.setSpeed(40);
			m4.rotate(-85);
			Delay.msDelay(150);
			m4.rotate(85);
			Delay.msDelay(700);
			boolean waitforstone = true;
			
				color.fetchSample(colorSample, 0);
				if (colorSample[0] > 0.009 && colorSample[1] > 0.0012 && colorSample[1] > 0.0065) {
					//waitforstone = false;
					//Delay.msDelay(100);
					//m3.stop();
					//color.fetchSample(colorSample, 0);
					if (colorSample[0]>=0.06) {
						//Kann nur noch Gelb oder Rot sein
						if (colorSample[1]>=0.06) {
							System.out.println("Gelb");
							Sound.playNote(PIANO, 450, 2);
							Abwurf(1);
						}
						else if (colorSample[1]<0.06){
							System.out.println("Rot");
							Sound.playNote(PIANO, 400, 2);
							Abwurf(2);
						}
					}
					else if (colorSample[0]<0.06) {
						//Kann nur noch Gelb oder Blau sein
						if (colorSample[1]<=0.047 && colorSample[2]>=0.035 && colorSample[1]>0.02) {
							System.out.println("Blau");
							Sound.playNote(PIANO, 350, 2);
							Abwurf(3);
						}
						else if (colorSample[1]>=0.047 && colorSample[2]<0.035 && colorSample[1]>0.02) {
							System.out.println("Grün");
							Sound.playNote(PIANO, 300, 2);
							Abwurf(4);
						}
					}
					else {
						System.out.println("???");
					}
				}
					
				Delay.msDelay(50);
			
			
			
			
			
			
			
			
		}
		

	}
	
	public static void Abwurf(int farbeid) {
		if (farbeid == 1) {
			m3.setSpeed(150);
			m3.backward();
			m1.rotate(60);
			Delay.msDelay(1700);
			m3.stop();
			m1.setSpeed(200);
			m1.rotate(120);
			
		}
		else if (farbeid == 2) {
			m3.setSpeed(150);
			m3.backward();
			m1.rotate(-60);
			Delay.msDelay(1700);
			m3.stop();
			m1.setSpeed(200);
			m1.rotate(-120);
		}
		else if (farbeid == 3) {
			m3.setSpeed(150);
			m3.backward();
			m2.rotate(60);
			Delay.msDelay(3000);
			m3.stop();
			m2.setSpeed(200);
			m2.rotate(120);
		}
		else if (farbeid == 4) {
			m3.setSpeed(150);
			m3.backward();
			m2.rotate(-60);
			Delay.msDelay(3000);
			m3.stop();
			m2.setSpeed(200);
			m2.rotate(-120);
		}
	}
	
	

}
