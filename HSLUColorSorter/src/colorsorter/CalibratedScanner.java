package colorsorter;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class CalibratedScanner extends Thread {

	RMISampleProvider sampleProvider = null;
	public static boolean insertbricks;
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 
	RemoteEV3 ev3 = null;
	
	public void run() {
		
		try {
			ev3 = new RemoteEV3("192.168.137.2");
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Create Provider");
		sampleProvider=ev3.createSampleProvider("S1", "lejos.hardware.sensor.EV3ColorSensor", "RGB");
		System.out.println("Created Provider successfully");
		float[] samples=new float[3];
		Delay.msDelay(3000);
		
		while (!Thread.currentThread().isInterrupted()) {
			Brick brick = BrickFinder.getDefault();
			
			
			while (TestKlasse.arbeite == true) {
				try {
					samples=sampleProvider.fetchSample();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (samples[0]>=0.06) {
					//Kann nur noch Gelb oder Rot sein
					if (samples[1]>=0.06) {
						System.out.println("Gelb");
						Sound.playNote(PIANO, 450, 2);
					}
					else if (samples[1]<0.06){
						System.out.println("Rot");
						Sound.playNote(PIANO, 400, 2);
					}
				}
				else if (samples[0]<0.06) {
					//Kann nur noch Gelb oder Blau sein
					if (samples[2]>=0.035 && samples[1]>0.02) {
						System.out.println("Blau");
						Sound.playNote(PIANO, 350, 2);
					}
					else if (samples[2]<0.035 && samples[1]>0.02) {
						System.out.println("Grün");
						Sound.playNote(PIANO, 300, 2);
					}
				}
				else {
					System.out.println("???");
				}
			Delay.msDelay(200);
			}	
			try {
				sampleProvider.close();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
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

