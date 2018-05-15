package colorsorter;

import java.io.File;
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

public class Sortierer {
	public static boolean machinerunning;
	public static boolean auflegen;
	
	public static void main(String[] args) {
		System.out.println("Programm gestartet.");
		
		//Den Listener starten
		System.out.println("Listener-Thread gestartet.");
		CommandListener cl = new CommandListener();
		cl.start();
	}	
}
