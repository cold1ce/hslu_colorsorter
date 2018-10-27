//Diese Klasse started einen neuen Thread, der auf eingehende Befehle 
//h�rt. Je nach Befehl werden unterschiedliche Aktionen der 
//Maschine ausgef�hrt.


package colorsorter;

//N�tige Imports f�r die Verbindung, und die JSON-Objekte
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lejos.hardware.Sound;

public class CommandListener extends Thread {

	//Um T�ne abspielen zu k�nnen.
	public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 

		public void run() {
			// Aufbau eines Loops und Aufrufen der Methode 
			// listenForRaspiCommand die eine Verbindung aufbaut und
			// auf Befehle wartet.
			while(true) {
			   try {
				   listenForRaspiCommand();
			   } 
			   catch (IOException e) { 
				   e.printStackTrace();
			   } 
			   catch (ParseException e) {
				   e.printStackTrace();
			   }
		   }
		}
	
		public static void listenForRaspiCommand() throws IOException, ParseException {
			System.out.println("Warte auf neuen Befehl...");
			// Aufbau des Sockets & Akzeptieren von eingehenden Verbindungen
			ServerSocket welcomeSocket = new ServerSocket(Machine.socket1);
		    Socket connectionSocket = welcomeSocket.accept();
		    
		    // Auslesen des ankommenden Befehls, darauf schlie�en des Sockets.
		    BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	        String clientSentence = reader.readLine();
	        System.out.println("CS:"+clientSentence);
	        // Sound abspielen, der signalisiert, wenn ein Befehl den EV3 erreicht hat
	        Sound.playNote(PIANO, 900, 100);
	        
	        //System.out.println("Angekommener Befehl: "+clientSentence);
	        connectionSocket.close();
	        welcomeSocket.close();
	        
	        // Auslesen des ankommenden Befehls (Dieser wird als JSON-Objekt �bertragen)
		    JSONParser parser = new JSONParser();
		    JSONObject obj = (JSONObject) parser.parse(clientSentence);
		    String command = (String) obj.get("command"); 
		    
		    
		    System.out.println("Befehl war:"+command);
		    // Auswerten, was der eingegangene Befehl bedeutet
		    // Danach Ausf�hren der passenden Methode
		    
		    // Falls der Command "Start" lautet
		    if (command.equals("start")) {
		    	if (Machine.machinerunning == true) {
		    		//System.out.println("Ger�t l�uft bereits!");
		    		DataSender d1 = new DataSender("responsefailure");
		    		d1.start();
		    	}
		    	else if (Machine.machinerunning == false) {
		    		startev3();
		    	}
		    }
		    
		    // Falls der Command "Stop" lautet
		    else if (command.equals("stop")) {
		    	if (Machine.machinerunning == true) {
		    		stopev3();
		    	}
		    	else if (Machine.machinerunning == false) {
		    		System.out.println("Maschine l�uft noch gar nicht!");
		    		DataSender d1 = new DataSender("responsefailure");
		    		d1.start();
		    	}
		    }
		    // Falls der Command "getstatus" lautet
		    else if (command.equals("getstatus")) {
		    	getStatusev3();
		    }
	    
		    // Falls der Command "filter" lautet
		    else if (command.equals("filter")) {
		    	String color = (String) obj.get("color");
		    	String status = (String) obj.get("status");
		    	if (status.equalsIgnoreCase("true") || status.equalsIgnoreCase("false")) {
		    		boolean statusbool = Boolean.valueOf(status);
		    		System.out.println("Color ist:" +color);
		    		filter(color, statusbool); 
		    	}

		    }
		    else {
		    	System.out.println("Unbekannter Befehl!");
		    	DataSender d1 = new DataSender("responsefailure");
	    		d1.start();
		    }
		}
		
		// Setzt den Sortiervorgang wieder in Betrieb
	    protected static void startev3() throws IOException {
	    	Machine.machinerunning = true;
	    	Machine.machineoutofstone = false;
	 	   	ColorSorter s1 = new ColorSorter();
	 	   	s1.start();
	 	   	DataSender d1 = new DataSender("responsedone");
	 	   	d1.start();
	    }
	    
	    // Stopt den Sortiervorgang
	    protected static void stopev3() {
	    	Machine.machinerunning = false;
	    	//Anmerkung: Hier wird das Senden von Daten in der ColorSorter
	    	//Methode durchgef�hrt.
	    }
	    
	    // Sendet ein Datenpaket mit dem aktuellen Status des
	    // Roboters zur�ck an den Raspberry Pi
	    protected static void getStatusev3() throws IOException {
	    	DataSender d1 = new DataSender("status");
	    	d1.start();
	    }
	    
	    // Setzt die globalen Filter-Variablen auf den Status der
	    // in der eingehenden Nachricht �bermittelt wurde
	    protected static void filter(String color, boolean status) {
	    	//System.out.println("color "+color);
	 	   	if (color.equals("yellow")) {
	 	   		Machine.dropyellow = status;
	 	   	}
	 	   	if (color.equals("red")) {
	 	   		Machine.dropred = status;
	 	   	}
	 	   	if (color.equals("green")) {
	 	   		Machine.dropgreen = status;
	 	   	}
	 	   	if (color.equals("blue")) {
	 	   		Machine.dropblue = status;
	 	   	}
	 	   DataSender d1 = new DataSender("responsedone");
	 	   d1.start();
	    }
}
