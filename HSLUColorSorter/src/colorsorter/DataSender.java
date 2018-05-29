package colorsorter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataSender extends Thread {
	
	protected String command;
	protected String color;
	protected String rgbvalue;
	
	public DataSender (String command, String color, String rgbvalue) {
		this.command=command;
		this.color=color;
		this.rgbvalue=rgbvalue;
	}
	
	public DataSender (String command) {
		this.command=command;
	}
	
	public void run(){
		try {
			if (command.equals("response")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Response\":\"done\"}");
			}
			if (command.equals("scannedstone")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Color\":\""+color+"\",\"RGB_value\":\""+rgbvalue+"\"}");
			}
			else if (command.equals("status")) {
				sendMessageToRaspi("{\"DeviceID\":\""+Machine.deviceid+"\",\"Machine_running\":\""+Machine.machinerunning+"\",\"Stones_scanned\":\""+Machine.stonescounted+"\"}");
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToRaspi(String message) throws IOException {
		Socket clientSocket = new Socket(Machine.ipraspi, Machine.socket1);
	    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	    outToServer.writeBytes(message);
	    clientSocket.close();
	}
	
}
