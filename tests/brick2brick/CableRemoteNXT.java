package brick2brick;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteNXT;
import main.Constants;

public class CableRemoteNXT {
	public static void main(String[] args) throws Exception {
		RemoteNXT nxt = null;	
		int power = 0;
		int mode = 1;
		int motor = 0;
		String motorString = "Motor:";
		String modeString = "Mode:";
		String powerString = "Power:";
		String batteryString = "Battery:";
		String lightString = "Light:";
		String tachoString = "Tacho:";

        NXTCommConnector connector = RS485.getConnector();


        // Now connect
        try {
            LCD.clear();
            LCD.drawString("Connecting...",0,0);
        	nxt = new RemoteNXT(Constants.SLAVE_NAME, connector);
        	LCD.clear();
            LCD.drawString("Type: " + connector, 0, 0);
            LCD.drawString("Connected",0,1);
            Thread.sleep(2000);
        } catch (IOException ioe) {
        	LCD.clear();
            LCD.drawString("Conn Failed",0,0);
            Thread.sleep(2000);
            System.exit(1);
        }

        LCD.clear();
		RemoteMotor[] motors = {nxt.A, nxt.B, nxt.C};
		LightSensor light = new LightSensor(nxt.S2);
		
		nxt.A.forward();
		nxt.B.forward();
		nxt.A.setSpeed(Constants.FORWARD_SPEED);
		nxt.B.setSpeed(Constants.FORWARD_SPEED);

			
	}
	
}
