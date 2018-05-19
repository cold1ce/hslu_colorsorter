//Diese Klasse kann einen neuen Thread starten, der auf einkommende Befehle 
//auf Port 5555 hört. Je nach Befehl werden unterschiedliche Aktionen der 
//Maschine ausgeführt.


package colorsorter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lejos.hardware.Sound;


public class CommandListener extends Thread {

public final static int[] PIANO = new int[]{4, 25, 500, 7000, 5}; 

		public void run(){
		   // Aufbau eines Loops und Aufrufen der Methode Auslagerung die eine Dauerhafte Socket Verbindung aufbaut
			while(true) {
			   try {
				   listenForRaspiCommands();
			   } 
			   catch (IOException e) { 
				   e.printStackTrace();
			   } 
			   catch (ParseException e) {
				   e.printStackTrace();
			   }
		   }
		}
	
		public static void listenForRaspiCommands() throws IOException, ParseException {
			System.out.println("Höre auf weitere Befehle...");
			// Aufbau des Sockets & Akzeptieren von eingehenden Verbindungen
			ServerSocket welcomeSocket = new ServerSocket(5555);
		    Socket connectionSocket = welcomeSocket.accept();
		    // Auslesen des ankommenden Befehls, darauf schließen des Sockets.
		    BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	        String clientSentence = reader.readLine();
	        Sound.playNote(PIANO, 900, 100);
	        System.out.println("Angekommener Befehl: "+clientSentence);
	        connectionSocket.close();
	        welcomeSocket.close();
	        
	        // Auslesen des ankommenden Befehls (Dieser wird als JSON-Objekt übertragen)
		    JSONParser parser = new JSONParser();
		    JSONObject obj = (JSONObject) parser.parse(clientSentence);
		    
		    String commands = (String) obj.get("commands"); 
		    
		    // Auswerten, was der eingegangene Befehl bedeutet
		    // Danach Ausführen der passenden Methode
		    if (commands.equals("start")) {
		    	if (Sortierer.machinerunning == true) {
		    		System.out.println("Gerät läuft bereits!");
		    	}
		    	else if (Sortierer.machinerunning == false) {
		    		startev3();
		    	}
		    }
		    else if (commands.equals("stop")) {
		    	if (Sortierer.machinerunning == true) {
		    		stopev3();
		    	}
		    	else if (Sortierer.machinerunning == false) {
		    		System.out.println("Maschine läuft noch gar nicht!");
		    	}
		    }
		    else if (commands.equals("filterspecificcolor")) {
		    	String color = (String) obj.get("color");
		    	filterspecificcolorev3(color);
		    }
		    else if (commands.equals("filterspecificcolorandnumber")) {
		    	String color = (String) obj.get("color");
		    	int number = (int) obj.get("number");
		    	filterspecificcolorandnumberev3(color,number);
		    }
		    else if(commands.equals("filterspecificcolorandnumberlist")) {
		    	   String colorlist = (String) obj.get("colorlist");
		    	   String numberlist = (String) obj.get("colorlist");
		    }
		    else {
		    	//Befehl nicht aufgeführt.
		    }
		}
		
	    protected static void startev3() throws IOException {
	    	Sortierer.machinerunning = true;
	    	Sortierer.machineoutofstone = false;
	 	   	Scannen s1 = new Scannen();
	 	   	s1.start();
	    }
	    
	    protected static void stopev3() {
	    	Sortierer.machinerunning = false;
	    	
	    }
	    
	    protected static void filterspecificcolorev3(String color) {
	 	   // Es wird nach einer speziellen Farbe gefiltert
	 	   // Es wird auf eine Antwort gewartet
	    }
	    
	    protected static void filterspecificcolorandnumberev3(String color,int number) {
	 	   // Es wird nach einer speziellen Farbe und einer entsprechenden Anzahl gefiltert
	 	   // Es wird auf eine Antwort gewartet
	    }
	    
	    protected static void filterspecificcolorandnumberlistev3(ArrayList<String> colornumbercolorlist,ArrayList<Integer> colornumbernumberlist) {
	 	   // Es wird nach speziellen Farben und entsprechenden Anzahlen gefiltert
	 	   // Es wird auf eine Antwort gewartet
	    }
}
