package oldCommunication;

import java.io.DataInputStream;
import java.io.IOException;


import lejos.nxt.LCD;
import lejos.nxt.comm.*;
/**
 * This class initializes a bluetooth connection, waits for the data
 * and then allows access to the data after closing the BT channel.
 * 
 * It should be used by calling the constructor which will automatically wait for
 * data without any further user command
 * 
 * Then, once completed, it will allow access to an instance of the Transmission
 * class which has access to all of the data needed
 */
public class oldBluetoothConnection {
	private oldTransmission trans;
	
	public oldBluetoothConnection() {
		LCD.clear();
		LCD.drawString("Starting BT connection", 0, 0);
		
		NXTConnection conn = Bluetooth.waitForConnection();
		DataInputStream dis = conn.openDataInputStream();
		LCD.drawString("Opened DIS", 0, 1);
		this.trans = OldParseTransmission.parse(dis);
		LCD.drawString("Finished Parsing", 0, 2);
		try {
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conn.close();
	}
	
	/**
	 * 
	 * @return A {@link Transmission} instance
	 */
	public oldTransmission getTransmission() {
		return this.trans;
	}
	
	/**
	 * Prints the data that was transmitted
	 */
	public void printTransmission() {
		try {
			LCD.clear();
			LCD.drawString(("Transmitted Values"), 0, 0);
			LCD.drawString("Goal X: " + trans.goalX, 0, 1);
			LCD.drawString("Goal Y: " + trans.goalY, 0, 2);
		} catch (NullPointerException e) {
			LCD.drawString("Bad Trans", 0, 7);
		}
	}
	
}
