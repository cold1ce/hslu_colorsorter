package colorsorter;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class InitRobot {
	static RegulatedMotor m4 = new EV3LargeRegulatedMotor(MotorPort.D);
	
	public static void resetOnStart() {
		m4.setSpeed(75);
		m4.forward();
		Delay.msDelay(3000);
		m4.stop();
	}
	
}
