package raspi;
import java.io.*;
import java.net.*;

public class DataTransfer {
   public static void main(String argv[]) throws Exception{
	  System.out.println("started");
      String sentence;
      String modifiedSentence;
      System.out.println("Erzeuge Buffered Reader");
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Socket");
      Socket clientSocket = new Socket("10.0.1.1", 5555);
      System.out.println("Output Stream");
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      System.out.println("inFrom Server Buffered Reader");
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      System.out.println("Sentence");
      sentence = inFromUser.readLine();
      sentence = "Mein Test String zur Uebertragung";
      outToServer.writeBytes(sentence + '\n');
      modifiedSentence = inFromServer.readLine();
      System.out.println("FROM SERVER: " + modifiedSentence);
      clientSocket.close();
   }
}