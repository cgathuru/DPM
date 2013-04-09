package tests;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;

public class BackwardsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odometer = new Odometer(false);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odometer, Motor.A, Motor.B);
		Button.waitForAnyPress();
		patBot.moveForwardBy(30);
		patBot.moveForwardBy(-30, true);
		Button.waitForAnyPress();

	}

}
