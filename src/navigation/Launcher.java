package navigation;
import lejos.nxt.*;

/**
 * Controls the robots catapult arm, responsible for shooting of the balls at the target destination
 * @author anthony
 *
 */
public class Launcher {
	
	/**
	 * Starts the ball launching sequence.
	 * @param leftMotor The robots left wheel
	 * @param rightMotor The robots right wheel
	 * @param centerMotor The robots catapult arm
	 */
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
	/**
	 * Launches a ball at the target and corrects the robots wheels to counter the effects felt by recoil of the the catapults arm
	 * @param leftMotor The robots left wheel
	 * @param rightMotor The robots right wheel
	 * @param centerMotor The robots catapult arm
	 */
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
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rightMotor.rotate(10,true);
			leftMotor.rotate(15, true);
			
	}
}