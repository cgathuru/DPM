package tests;

import display.LCDInfo;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import odometer.Odometer;
import odometer.TwoWheeledRobot;

public class squaretest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
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
