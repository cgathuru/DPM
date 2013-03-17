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
		int buttonChoice = Button.waitForAnyPress();
		if(buttonChoice== Button.ID_LEFT){
		patBot.moveForwardBy(30);
		patBot.turnToImmediate(90);
		patBot.moveForwardBy(30);
		patBot.turnToImmediate(90);
		patBot.moveForwardBy(30);
		patBot.turnToImmediate(90);
		patBot.moveForwardBy(30);
		patBot.turnToImmediate(90);
		Button.waitForAnyPress();
	}
		else{
			patBot.motorFloat();
		}
	}
}
