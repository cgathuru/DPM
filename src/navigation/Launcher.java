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
	//first launch
		private static void launchFirstSlow(NXTRegulatedMotor leftMotor,
				NXTRegulatedMotor rightMotor, NXTRegulatedMotor centerMotor) {
				// wait time at initial position
				try {
					Thread.sleep(5000);
					} catch (InterruptedException e) {
				}
				
				//puts the robot back at the angle it was before it shot
				rightMotor.rotate(12,true);
				leftMotor.rotate(45, true);
				//rotation of the motor's arm from initial position to pulling position
				centerMotor.rotate(-208,true);
				
				//wait time at pulling position
				try {
					Thread.sleep(5000);
					} catch (InterruptedException e) {
				}
				
				//rotation of the motor's arm from pulling position to ready to launch position
				centerMotor.rotate(131,true);
				
				
				//wait time at ready to launch position
				try {
					Thread.sleep(5000);
					} catch (InterruptedException e) {
				}
				
				//Launches the ball : rotation of the motor's arm from ready to launch to initial position
				centerMotor.rotate(77,true);
				
				
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
			rightMotor.rotate(15,true);
			leftMotor.rotate(45, true);
			//rotation of the motor's arm from initial position to pulling position
			centerMotor.rotate(-210,true);
			
			//wait time at pulling position
			try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {
			}
			
			//rotation of the motor's arm from pulling position to ready to launch position
			centerMotor.rotate(133,true);
			
			
			//wait time at ready to launch position
			try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {
			}
			
			//Launches the ball : rotation of the motor's arm from ready to launch to initial position
			centerMotor.rotate(77,true);
			
			
	}
	//fast launch
	private static void launchFast(NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, NXTRegulatedMotor centerMotor) {
			// wait time at initial position
			try {
				Thread.sleep(2000);
				} catch (InterruptedException e) {
			}
			//puts the robot back at the angle it was before it shot
			rightMotor.rotate(15,true);
			leftMotor.rotate(30,true);
			//rotation of the motor's arm from initial position to pulling position
			centerMotor.rotate(-205,true);
			
			//wait time at pulling position
			try {
				Thread.sleep(2000);
				} catch (InterruptedException e) {
			}
			
			//rotation of the motor's arm from pulling position to ready to launch position
			centerMotor.rotate(128,true);
			
			
			//wait time at ready to launch position
			try {
				Thread.sleep(3000);
				} catch (InterruptedException e) {
			}
			
			//Launches the ball : rotation of the motor's arm from ready to launch to initial position
			centerMotor.rotate(77,true);
			
			
	}
}