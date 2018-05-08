package colorsorter;
import java.io.*;
import java.net.*;

public class DataTransferEV3 {
   public static void main(String argv[]) throws Exception {
      String clientSentence; //Hier soll später der String des Clients reingeschrieben werden
      System.out.println("Oeffne Socket auf 5555...");
      ServerSocket welcomeSocket = new ServerSocket(5555);
      int i = 0; // Zählvariable
      System.out.println("Starte Schleife..");
      while(true) {
    	 System.out.println("Durchlauf " +i);
    	 i++;
    	 System.out.println("ConnectionSocket");
         Socket connectionSocket = welcomeSocket.accept();
         System.out.println("Durchlauf " +i);
         BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
         System.out.println("outToClient");
         DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
         System.out.println("clientSentence");
         clientSentence = inFromClient.readLine();
         System.out.println("Received: " + clientSentence);
         outToClient.writeBytes(clientSentence);
      }
   }
}