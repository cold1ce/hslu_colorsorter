package colorsorter;

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
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Scanner2 extends Thread {


	EV3ColorSensor colorSensor;
	SampleProvider colorProvider;
	float[] colorSample;
	//public static RegulatedMotor m1, m2, m3, m4;
	static Audio audio;
	public static boolean insertbricks;
	static Sound so1;
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 
	
	public void run() {


		while (!Thread.currentThread().isInterrupted()) {
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
			while (TestKlasse.arbeite == true) {
				color.fetchSample(colorSample, 0);
				
				int curr1 = checkColor(colorSample);
				Delay.msDelay(100);
				if (curr1 > 0) {
					int curr2 = checkColor(colorSample);
					Delay.msDelay(100);
					if (curr2 > 0) {
						int curr3 = checkColor(colorSample);
						int coloris = -1;
						if (curr1 == curr2) {
							coloris = curr1;
							System.out.println("Farbe: "+curr1);
						}
						else if (curr2 == curr3) {
							coloris = curr2;
							System.out.println("Farbe: "+curr2);
						}
						else if (curr1 == curr2 && curr1 == curr3) {
							coloris = curr1;
							System.out.println("Farbe: "+curr1);
						}
						else {
							coloris = -1;
						}
						//Abwurf2 a1 = new Abwurf2();
						//a1.run(coloris);
					Delay.msDelay(4000);	
					}
				}
			Delay.msDelay(100);
			}	
			colorSensor.close();
			 
			try {
				Thread.sleep(4000);
				}
			catch (InterruptedException intExc) {
				this.interrupt();
				
				break;
			}
		}
	}

	public int checkColor (float[] colorSample) {
		int colorid = -1;
		if (colorSample[0] >= 0.13 && colorSample[1] >= 0.085) {
			//System.out.println("Gelb");
			Sound.playNote(PIANO, 450, 2);
			System.out.println("R: "+(Math.round(1000.0 * colorSample[0]) / 1000.0)+"R: "+(Math.round(1000.0 * colorSample[1]) / 1000.0)+"B: "+(Math.round(1000.0 * colorSample[2]) / 1000.0));
			colorid = 1;
		}
		else if (colorSample[0] > 0.07 && colorSample[0] <= 0.13 && colorSample[1] < 0.04) {
			//System.out.println("Rot");
			Sound.playNote(PIANO, 400, 2);
			System.out.println("R: "+(Math.round(1000.0 * colorSample[0]) / 1000.0)+"R: "+(Math.round(1000.0 * colorSample[1]) / 1000.0)+"B: "+(Math.round(1000.0 * colorSample[2]) / 1000.0));
			colorid = 2;
		}
		else if (colorSample[0] <= 0.05 && colorSample[1] <= 0.055 && colorSample[1] >= 0.02 && colorSample[2] >= 0.035) {
			//System.out.println("Blau");
			Sound.playNote(PIANO, 350, 2);
			System.out.println("R: "+(Math.round(1000.0 * colorSample[0]) / 1000.0)+"R: "+(Math.round(1000.0 * colorSample[1]) / 1000.0)+"B: "+(Math.round(1000.0 * colorSample[2]) / 1000.0));
			colorid = 3;
		}
		else if (colorSample[0] <= 0.05 && colorSample[1] <= 0.075 && colorSample[1] >= 0.04 && colorSample[2] < 0.035) {
			//System.out.println("Gruen");
			Sound.playNote(PIANO, 300, 2);
			System.out.println("R: "+(Math.round(1000.0 * colorSample[0]) / 1000.0)+"R: "+(Math.round(1000.0 * colorSample[1]) / 1000.0)+"B: "+(Math.round(1000.0 * colorSample[2]) / 1000.0));
			colorid = 4;
		}
		else {
			//System.out.println("---");
			colorid = 0;
		}
		
		return colorid;
	}
}	

