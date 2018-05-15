package colorsorter;

public class Sortierer {
	public static boolean machinerunning;
	public static boolean auflegen;
	
	public static void main(String[] args) {
		System.out.println("Programm gestartet.");
		
		InitRobot.resetOnStart();
		
		//Den Listener starten
		System.out.println("Listener-Thread gestartet.");
		CommandListener cl = new CommandListener();
		cl.start();
	}	
}
