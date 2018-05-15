package colorsorter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataSender extends Thread {
	public void run(String data) throws IOException {
	    Socket clientSocket = new Socket("10.0.1.1", 5555);
	    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	    outToServer.writeBytes(data);
	    clientSocket.close();
	}
}
