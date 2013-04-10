package tests;

import display.LCDInfo;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;

public class squaretest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		new LCDInfo(odo);
		Button.waitForAnyPress();
		patBot.moveForwardBy(300);
		Button.waitForAnyPress();
	}
		
}
