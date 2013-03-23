/*
* @author Sean Lawlor
* @date November 3, 2011
* @class ECSE 211 - Design Principle and Methods
*/
package oldCommunication;

import java.io.DataInputStream;
import java.io.IOException;


import lejos.nxt.LCD;

/*
 * Static parsers for parsing data off the communication channel
 * 
 * The order of data is defined in the Server's Transmission class
 */

/**
 * This class processes the incoming transmission data that is sent to the NXT via bluetooth
 * @author charles
 *
 */
public class OldParseTransmission {
	
	/**
	 * Processes the data sent via buetooth to the NXT, into useful information that can be
	 * used the by the NXT
	 * @param dis The {@code DataInputStream} opened during the bluetooth connection initialization
	 * @return A {@link Transmission} object containing information the 
	 */
	public static oldTransmission parse (DataInputStream dis) {
		oldTransmission trans = null;
		try {
			
			while (dis.available() <= 0)
				Thread.sleep(10); // spin waiting for data
			
			trans = new oldTransmission();
			trans.goalX = dis.readInt();
			ignore(dis);
			trans.goalY = dis.readInt();
			
			return trans;
		} catch (IOException e) {
			// failed to read transmitted data
			LCD.drawString("IO Ex", 0, 7);
			return trans;
		} catch (InterruptedException e) {
			return trans;
		}
		
	}
	
	/**
	 * Ignores the character that is transmitted through the bluetooth connection
	 * @param dis The {@code DataInputStream} opened during the bluetooth connection initialization
	 * @throws IOException
	 */
	public static void ignore(DataInputStream dis) throws IOException {
		dis.readChar();
	}
	
}
