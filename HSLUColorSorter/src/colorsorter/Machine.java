// Hauptklasse, die gestartet werden muss für den Betrieb der Sortiermaschine.
// Versetzt die Maschine in eine Art Standby, erst nach erhalt von Befehlen 
// über Bluetooth werden entsprechende Aktionen gestartet.

package colorsorter;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Machine {
	// Globale Variablen, auf die von mehreren Klassen aus zugegriffen wird
	// Gibt an ob die Maschine gerade läuft.
	public static boolean machinerunning; 
	
	// Gibt an ob gerade ein Stein auf dem Band ist.
	public static boolean stoneonfeederband; 
	
	// Motor, der das Förderband antreibt.
	public static RegulatedMotor feederband; 
	
	// Motor, der den Schieber antreibt.
	public static RegulatedMotor slider;
	
	// Die Zeit in ms seit welcher kein Stein mehr erkannt wurde
	public static double timewithoutstoneonfeederband;
	
	// Gibt an ob die Maschine keine Steine mehr hat
	public static boolean machineoutofstone;
	
	// Die Anzahl der Steine die im gesamten Betrieb schon gezählt wurden
	// als gesamte Variable und als Variable für jede einzelne Farbe
	public static int stonescounted;
	public static int redstonescounted, yellowstonescounted, 
	greenstonescounted, bluestonescounted;
	
	// Zeit in ms, nach dem sich die Maschine ausschalten soll wenn kein
	// Stein mehr angekommen ist.
	public static double automaticoff;
	
	// Die IPs von EV3 und RasPi sowie Sockets die verwendet werden
	public static String ipev3 = "10.0.1.1";
	public static String ipraspi = "10.0.1.2";
	public static int socket1 = 5000;
	public static int socket2 = 5556; //Aktuell nicht benutzt.
	
	// Die ID des Geräts, zur Wiedererkennung falls mehrere Maschinen
	// im Verbund sein sollten
	public static String deviceid = "LU-BOT";
	
	// Filter-Variablen. Geben an ob die jeweilige Farbe sortiert werden
	// soll, oder nicht (Wenn nicht, fährt der Stein bis ans Ende durch)
	public static boolean dropred, dropyellow, dropblue, dropgreen;
	
	// Button-Objekt für die Tasten auf dem EV3, in unserem Fall aber 
	// nur benutzt um die LEDs neben den Tasten anzusteuern.
	public static Button b1;
	
	// Main-Methode, startet bei Aufruf der Klasse.
	public static void main(String[] args) {
		
		// Nachricht ausgeben und Licht des EV3 auf Grün schalten
		System.out.println("Maschine angeschaltet.");
		Button.LEDPattern(6);
		
		// Ruft die ResetRobot.reset-Methode auf, welche die Variablen
		// auf Ihre Standard-/Ursprungswerte setzt.
		ResetMachine.reset();
		
		// Die Klasse CommandListener als Thread starten,
		// diese hört auf ankommende Befehle des Raspberry Pi
		CommandListener cl = new CommandListener();
		cl.start();
	}	
}
