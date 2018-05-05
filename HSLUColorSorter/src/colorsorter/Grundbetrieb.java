package colorsorter;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Grundbetrieb extends Thread {
	static Sound so1;
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 
	RegulatedMotor m4 = new EV3LargeRegulatedMotor(MotorPort.D);
	
	public void run() {
		Delay.msDelay(5000);
		while (!Thread.currentThread().isInterrupted()) {
			while (!Thread.currentThread().isInterrupted()) {
				m4.setSpeed(40);
				 m4.rotate(-85);
				 Delay.msDelay(150);
				 m4.rotate(85);
				 Delay.msDelay(4000);
			 }
			try {
				Thread.sleep(1000);
				}
			catch (InterruptedException intExc) {
				this.interrupt();
				break;
			}
		}
	}
	
}
