//Hauptklasse, die gestartet werden muss für den Betrieb der Sortiermaschine.

package colorsorter;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Sortierer {
	//Globale Variablen, die angeben ob die Maschine derzeit läuft und ob
	//derzeit Steine aufgelegt werden.
	public static boolean machinerunning;
	public static boolean stoneonfeederband;
	public static RegulatedMotor feederband;
	public static RegulatedMotor slider;
	public static double timewithoutstoneonfeederband;
	public static boolean machineoutofstone;
	
	public static void main(String[] args) {
		System.out.println("Programm gestartet.");
	
		//Den Listener starten, der Befehle vom Raspberry Pi annimmt.
		System.out.println("Listener-Thread gestartet.");
		CommandListener cl = new CommandListener();
		cl.start();
	}	
}
