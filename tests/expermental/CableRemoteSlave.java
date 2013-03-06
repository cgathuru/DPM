package expermental;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.Motor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.NXTComm;
import lejos.nxt.remote.NXTCommand;

public class CableRemoteSlave {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NXTCommConnector connector = RS485.getConnector();
		NXTConnection conn = connector.waitForConnection(0, NXTConnection.LCP);
		DataInputStream dis = conn.openDataInputStream();
	    DataOutputStream dos = conn.openDataOutputStream();
	    
	    NXTCommand nxtCommand = new NXTCommand(null);
	}

	
	
}
