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

public class CommandListener extends Thread {

		public static void main(String argv[]) throws Exception {
			ServerSocket welcomeSocket = new ServerSocket(5555);
		    System.out.println("Höre auf Befehle...");
		    Socket connectionSocket = welcomeSocket.accept();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	        String clientSentence = reader.readLine();
	        System.out.println("Client data: "+clientSentence);
	        connectionSocket.close();
	        welcomeSocket.close();
		    //Pascal: JSON hat hier irgendwie noch nicht funktioniert, im Moment reagiert 
		    //er auf ganz normale Strings, die übermittelt werden.
		    JSONParser parser = new JSONParser();
		    JSONObject obj = (JSONObject) parser.parse(clientSentence);
		    String commands = (String) obj.get("commands"); 
		    //String commands = clientSentence;
		    welcomeSocket.close();
		    System.out.println("Empfangener Befehl:" +commands);
		    if (commands.equals("start")) {
		    	startev3();
		    	new CommandListener();
		    }
		    else if (commands.equals("stop")) {
		    	stopev3();
		    	new CommandListener();
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
		    	  // Daten kommen mittels JSON Array an
		    	   String colorlist = (String) obj.get("colorlist");
		    	   String numberlist = (String) obj.get("colorlist");
		    }
		    else {
		    	//nichts
		    }
		}
	    
	    protected static void startev3() throws IOException {
	    	Sortierer.machinerunning = true;
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
