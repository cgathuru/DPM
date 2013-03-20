package tests;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;
import display.LCDInfo;

public class TravelToLevel2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stubOdometer odo = new Odometer(true);
		Odometer odo = new Odometer(true);
		TwoWheeledRobot robot = new TwoWheeledRobot(odo,Motor.A, Motor.B);
		new LCDInfo(odo);
		Button.waitForAnyPress();
		robot.travelTo(0, 60);
		robot.travelTo(30, 30);
		robot.travelTo(60, 60);
		robot.travelTo(60, 0);
		robot.travelTo(0, 0);
		Button.waitForAnyPress();
		

	}

}
