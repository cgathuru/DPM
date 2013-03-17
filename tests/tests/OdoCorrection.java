package tests;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import utilities.OdoLCD;

/**
 * This class tests the {@code OdometryCorrection} class to determine how how accurate the correction is.
 * @author charles
 *
 */
public class OdoCorrection {

	/**
	 * Drives in a square to and checks how accurate the 
	 * @param args
	 */
	public static void main(String[] args) {
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		new OdoLCD(odo);
		OdometryCorrection odoCorrect = new OdometryCorrection(patBot, lsLeft, lsRight);
		Button.waitForAnyPress();
		odoCorrect.startCorrectionTimer();
		//int repeatNumber =4;
		//for(int i =0; i < repeatNumber; i++){
			patBot.moveForwardBy(30);
			patBot.turnToImmediate(-90);
			patBot.moveForwardBy(30);
			patBot.turnToImmediate(-90);
			patBot.moveForwardBy(30);
			patBot.turnToImmediate(-90);
			patBot.moveForwardBy(30);
			patBot.turnToImmediate(-90);
			
			odoCorrect.stopCorrectionTimer();
			//nav.turnTo(0);

			Button.waitForAnyPress();
			System.exit(0);
		//}
	}

}
