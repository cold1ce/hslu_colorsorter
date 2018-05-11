package colorsorter;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataInputCommandsEV3 {   
	
	public static void main(String argv[]) throws Exception {
      String clientSentence; 
      //Socket wird geöffnet 
      ServerSocket welcomeSocket = new ServerSocket(5555);
      Socket connectionSocket = welcomeSocket.accept();
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      clientSentence = inFromClient.readLine();
      
      JSONParser parser = new JSONParser();
      JSONObject obj = (JSONObject) parser.parse(clientSentence);
      String commands = (String) obj.get("commands");   
      
      if(commands.equals("start")) {
    	  startev3();
      }else if(commands.equals("stop")) {
    	  stopev3();
      }else if(commands.equals("filterspecificcolor")) {
    	  String color = (String) obj.get("color");
    	  filterspecificcolorev3(color);
      }else if(commands.equals("filterspecificcolorandnumber")) {
    	  String color = (String) obj.get("color");
    	  int number = (int) obj.get("number");
    	  filterspecificcolorandnumberev3(color,number);
      }else if(commands.equals("filterspecificcolorandnumberlist")) {
    	  // Daten kommen mittels JSON Array an
    	   String colorlist = (String) obj.get("colorlist");
    	   String numberlist = (String) obj.get("colorlist");
    	   
      }else {
    	  // Hier muss eine Execption hin, falls etwas schief läuft
      }
   }
   
   protected static void startev3() {
	   // Es wird auf eine Antwort gewartet
   }
   
   protected static void stopev3() {
	   // Es wird auf keine Antwort gewartet. Dient zum Programm stop
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