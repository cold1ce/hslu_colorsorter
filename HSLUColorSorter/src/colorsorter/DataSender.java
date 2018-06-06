// Diese Klasse übermittelt verschiedene Daten des Roboters an den
// mit Bluetooth angebundenen Raspberry Pi

package colorsorter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataSender extends Thread {
	
	protected String command;
	protected String color;
	protected String rgbvalue;
	protected int stonescounted;
	
	public DataSender (String command, String color, String rgbvalue) {
		this.command=command;
		this.color=color;
		this.rgbvalue=rgbvalue;
	}
	
	public DataSender (String command) {
		this.command=command;
	}
	
	public DataSender (String command, int stonescounted) {
		this.command=command;
		this.stonescounted=stonescounted;
	}
	
	public void run(){
		try {
			if (command.equals("responsedone")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Response\":\"done\"}");
			}
			if (command.equals("responsefailure")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Response\":\"failure\"}");
			}
			else if (command.equals("scannedstone")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Color\":\""+color+"\",\"RGB_value\":\""+rgbvalue+"\"}");
			}
			else if (command.equals("status")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Response\":\"done\",\"Machine_running\":\""+Machine.machinerunning+"\",\"Stones_scanned\":\""+Machine.stonescounted+"\"}");
			}
			else if (command.equals("responsemachinestopped")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Response\":\"done\",\"Total_stones_scanned\":\""+Machine.stonescounted+"\"}");
				Machine.stonescounted = 0;
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Eigentliche Übermittlungsmethode, in welcher der String im Argument der Methode
	// übermittelt wird.
	public void sendMessageToRaspi(String message) throws IOException {
		Socket clientSocket = new Socket(Machine.ipraspi, Machine.socket1);
	    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	    outToServer.writeBytes(message);
	    clientSocket.close();
	}
	
}
