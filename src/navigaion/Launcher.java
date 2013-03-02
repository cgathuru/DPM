package navigaion;

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
	public static void drive(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		// reset the motors
		
		
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor, rightMotor}) {
			motor.stop();
			//set the acceleration of the three motors
			motor.setAcceleration(300);
		}
		rightMotor.rotate(-120,true);
		leftMotor.rotate(-120,true);
		
		// wait 5 seconds
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the launcher will be interrupted by another thread
		}

			rightMotor.setAcceleration(3000);
			leftMotor.setAcceleration(3000);
			//rotation made by the three motors to have a good launch angle
			rightMotor.rotate(125,true);
			leftMotor.rotate(125,true);
	}
}