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
				   listenForRaspicommand();
			   } 
			   catch (IOException e) { 
				   e.printStackTrace();
			   } 
			   catch (ParseException e) {
				   e.printStackTrace();
			   }
		   }
		}
	
		public static void listenForRaspicommand() throws IOException, ParseException {
			System.out.println("Warte auf Befehle...");
			// Aufbau des Sockets & Akzeptieren von eingehenden Verbindungen
			ServerSocket welcomeSocket = new ServerSocket(Machine.socket1);
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
		    
		    String command = (String) obj.get("command"); 
		    
		    // Auswerten, was der eingegangene Befehl bedeutet
		    // Danach Ausführen der passenden Methode
		    if (command.equals("start")) {
		    	if (Machine.machinerunning == true) {
		    		System.out.println("Gerät läuft bereits!");
		    	}
		    	else if (Machine.machinerunning == false) {
		    		startev3();
		    	}
		    }
		    else if (command.equals("stop")) {
		    	if (Machine.machinerunning == true) {
		    		stopev3();
		    	}
		    	else if (Machine.machinerunning == false) {
		    		System.out.println("Maschine läuft noch gar nicht!");
		    	}
		    }
		    else if (command.equals("getstatus")) {
		    	getStatus();
		    }
		    
		    
		    else if (command.equals("filter")) {
		    	String color = (String) obj.get("color");
		    	String status = (String) obj.get("status");
		    	if (status.equalsIgnoreCase("true") || status.equalsIgnoreCase("false")) {
		    	    boolean statusbool = Boolean.valueOf(status);
		    	    System.out.println("Color ist:" +color);
			    	filter(color, statusbool); 
		    	}

		    }
		    else if (command.equals("filterspecificcolorandnumber")) {
		    	String color = (String) obj.get("color");
		    	int number = (int) obj.get("number");
		    	filterspecificcolorandnumberev3(color,number);
		    }
		    else if(command.equals("filterspecificcolorandnumberlist")) {
		    	   String colorlist = (String) obj.get("colorlist");
		    	   String numberlist = (String) obj.get("colorlist");
		    }
		    else {
		    	//Befehl nicht aufgeführt.
		    }
		}
		
	    protected static void startev3() throws IOException {
	    	Machine.machinerunning = true;
	    	Machine.machineoutofstone = false;
	 	   	ColorSorter s1 = new ColorSorter();
	 	   	s1.start();
	 	   	DataSender d1 = new DataSender("response");
	 	   	d1.start();
	    }
	    
	    protected static void stopev3() {
	    	Machine.machinerunning = false;
	    	DataSender d1 = new DataSender("response");
	 	   	d1.start();
	    	
	    }
	    protected static void getStatus() throws IOException {
	    	DataSender d1 = new DataSender("status");
	    	d1.start();
	    	
	    	
	    }
	    
	    protected static void filter(String color, boolean status) {
	    	System.out.println("color "+color);
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
