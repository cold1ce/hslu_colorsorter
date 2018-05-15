//Diese Klasse muss als normale Java-Anwendung ausgeführt werden. Per Tastatur-Input
//auf dem Rechner können Befehle an den EV3 simuliert werden. (start, stop, etc.)

package colorsorter;

import java.io.*;
import java.net.*;

public class CommandEmulator {
   public static void main(String argv[]) throws Exception{
	  int x = 0;
	  while (x<100) {
	      String sentence;
	      String modifiedSentence;
	      System.out.println("Befehl eintippen und mit Enter bestätigen:");
	      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	      Socket clientSocket = new Socket("10.0.1.1", 5555);
	      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	      sentence = inFromUser.readLine();
	      System.out.println("An den Server wurde folgendes gesendet: "+sentence);
	      outToServer.writeBytes(sentence);
	      clientSocket.close();
	      x++;
	  }
   }
}