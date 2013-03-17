package tests;

import display.LCDInfo;
import navigaion.Navigation;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;

public class turntoTest {
	public static void main (String[] args){
		
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		new LCDInfo(odo);
		Button.waitForAnyPress();
		patBot.turnTo(90);
		patBot.turnTo(45);
		patBot.turnTo(180);
		patBot.turnTo(270);
		patBot.turnTo(355);
		patBot.turnTo(0);
		
		Button.waitForAnyPress();
		
		
		
		
		
	}
}
