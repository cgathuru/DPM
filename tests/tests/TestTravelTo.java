package tests;

import display.LCDInfo;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;

public class TestTravelTo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odo = new Odometer(true);
		TwoWheeledRobot robot = new TwoWheeledRobot(odo,Motor.A, Motor.B);
		new LCDInfo(odo);
		Button.waitForAnyPress();
		robot.travelTo(0, 30);
		robot.travelTo(30, 30);
		robot.travelTo(30, 0);
		robot.travelTo(0, 0);
		Button.waitForAnyPress();
		
	}

}
