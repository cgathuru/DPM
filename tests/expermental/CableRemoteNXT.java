package expermental;

import java.io.IOException;

import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.RemoteNXT;

public class CableRemoteNXT extends RemoteNXT{

	private static NXTCommConnector connector = RS485.getConnector();
	public CableRemoteNXT(String name)
			throws IOException {
		super(name, connector);
	}
	

}
