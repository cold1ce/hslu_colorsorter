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
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Scanner extends Thread {


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
			Port s2 = brick.getPort("S2");
			colorSensor = new EV3ColorSensor(s1);
			colorProvider = colorSensor.getColorIDMode();
			colorSample = new float[colorProvider.sampleSize()];
			//audio = brick.getAudio();
			//audio.playSample(new File("green.wav"), 100);
			colorSensor.setFloodlight(true);
			colorSensor.setFloodlight(Color.WHITE);
			colorSensor.setFloodlight(Color.RED);
			System.out.println("Zeug eingestellt...");
			while (TestKlasse.arbeite == true) {
				int currentDetectedColor = colorSensor.getColorID();
				switch(currentDetectedColor) {
				
					case Color.RED:
						System.out.println("ROT !");
						Sound.playNote(PIANO, 450, 2);
						Abwurf a = new Abwurf();
						a.run(1);
						break;
					case Color.YELLOW:
						System.out.println("GELB !");
						Sound.playNote(PIANO, 400, 2);
						Abwurf b = new Abwurf();
						b.run(2);
						//rotateMotor1(2);
						break;
					case Color.GREEN:
						System.out.println("GRUEN !");
						Sound.playNote(PIANO, 350, 2);
						Abwurf c = new Abwurf();
						c.run(3);
						break;
					case Color.BLUE:
						System.out.println("BLAU !");
						Sound.playNote(PIANO, 300, 2);
						Abwurf d = new Abwurf();
						d.run(4);
						break;
	
					default:
						//System.out.println("---");
						break;
				}
			Delay.msDelay(200);
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
}	

