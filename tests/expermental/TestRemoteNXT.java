package expermental;

import java.io.IOException;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.remote.RemoteMotor;
import main.Constants;

public class TestRemoteNXT {
	
	
	public static void main(String []args) throws IOException{
		
		CableRemoteNXT robotSlave =new CableRemoteNXT(Constants.SLAVE_NAME);
		try {
			robotSlave = new CableRemoteNXT(Constants.SLAVE_NAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RemoteMotor motorA = robotSlave.A;
		RemoteMotor motorB = robotSlave.B;
		
		motorA.setAcceleration(300);
		motorB.setAcceleration(300);
		motorA.rotate(-120,true);
		motorB.rotate(-120,true);
		
		// wait 5 seconds
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the launcher will be interrupted by another thread
		}

			motorA.setAcceleration(3000);
			motorB.setAcceleration(3000);
			//rotation made by the three motors to have a good launch angle
			motorA.rotate(125,true);
			motorB.rotate(125,true);
		
		
	}

}
