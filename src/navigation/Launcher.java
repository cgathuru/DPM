package navigation;

import lejos.nxt.*;

/**
 * Shoots the ball from the NXT to the target goal
 * @author charles
 *
 */
public class Launcher {
	//set the rotation speed of the motor to have a good launch speed
	private static final int ROTATE_SPEED = 900;
	
/**
 * Drives the motors back the forward to shoot the ball at the target
 * @param leftMotor Left Motor
 * @param rightMotor Right Motor
 */
	public static void drive(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, NXTRegulatedMotor centerMotor) {
		// reset the motors
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor, rightMotor, centerMotor }) {
			motor.stop();
			//set the acceleration of the three motors
			motor.setAcceleration(200);
			motor.setSpeed(150);
		}
			
			launchFast(leftMotor, rightMotor, centerMotor);
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
		//	rightMotor.rotate(15,true);
		//	leftMotor.rotate(30,true);
			//rotation of the motor's arm from initial position to pulling position
			centerMotor.rotate(-205,true);
			
			//wait time at pulling position
			try {
				Thread.sleep(5000);
				} catch (InterruptedException e) {
			}
			
			//rotation of the motor's arm from pulling position to ready to launch position
			centerMotor.rotate(128,true);
			
			
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
