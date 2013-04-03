package navigation;
import lejos.nxt.*;

public class Launcher {
	
	public static void drive(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, NXTRegulatedMotor centerMotor) {
		
		
		// reset the motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor, rightMotor, centerMotor }) {
			motor.stop();
			//set the acceleration of the three motors
			motor.setAcceleration(1500);
			motor.setSpeed(1500);
		}
			/*launchFirstSlow(leftMotor, rightMotor, centerMotor);*/
		  for(int i =0; i < 5 ; i++){
			launchSlow(leftMotor, rightMotor, centerMotor);
		}
		  
					
		}
	//slow launch
	private static void launchSlow(NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, NXTRegulatedMotor centerMotor) {
			// wait time at initial position
			try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {
			}
			
			//puts the robot back at the angle it was before it shot

			//rotation of the motor's arm from initial position to pulling position
			centerMotor.rotate(-210,true);
			
			//wait time at pulling position
			try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {
			}
			
			//rotation of the motor's arm from pulling position to ready to launch position
			centerMotor.rotate(130,true);
			
			
			//wait time at ready to launch position
			try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {
			}
			
			//Launches the ball : rotation of the motor's arm from ready to launch to initial position
			centerMotor.rotate(80,true);
			
			rightMotor.rotate(10,true);
			leftMotor.rotate(15, true);
	}
}