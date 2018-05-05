package colorsorter;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Abwurf2 extends Thread {
	static Sound so1;
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 


	
	public void run(int farbeid) {
		
		if (farbeid == 1) {
			RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.B);
			m1.rotate(60);
			m1.close();
			Delay.msDelay(4600);
			RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.B);
			m11.rotate(120);
			m11.close();
		}
		else if (farbeid == 2) {
			RegulatedMotor m1 = new EV3MediumRegulatedMotor(MotorPort.B);
			m1.rotate(-60);
			m1.close();
			Delay.msDelay(4600);
			RegulatedMotor m11 = new EV3MediumRegulatedMotor(MotorPort.B);
			m11.rotate(-120);
			m11.close();
		}
		else if (farbeid == 3) {
			RegulatedMotor m2 = new EV3MediumRegulatedMotor(MotorPort.C);
			m2.rotate(60);
			m2.close();
			Delay.msDelay(3500);
			RegulatedMotor m22 = new EV3MediumRegulatedMotor(MotorPort.C);
			m22.rotate(120);
			m22.close();
		}
		else if (farbeid == 4) {
			RegulatedMotor m2 = new EV3MediumRegulatedMotor(MotorPort.C);
			m2.rotate(-60);
			m2.close();
			Delay.msDelay(3500);
			RegulatedMotor m22 = new EV3MediumRegulatedMotor(MotorPort.C);
			m22.rotate(-120);
			m22.close();
		}

		
			try {
				Thread.sleep(1000);
				}
			catch (InterruptedException intExc) {
				this.interrupt();
				//break;
			}
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
}*/

}