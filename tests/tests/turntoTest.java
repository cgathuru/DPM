package tests;

import display.LCDInfo;
import navigaion.Navigation;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;

public class turntoTest {
	public static void main (String[] args){
		
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		Navigation navigator = new Navigation(odo);
		new LCDInfo(odo);
		Button.waitForAnyPress();
		navigator.turnTo(90);
		navigator.turnTo(45);
		navigator.turnTo(180);
		navigator.turnTo(270);
		navigator.turnTo(355);
		navigator.turnTo(0);
		
		Button.waitForAnyPress();
		
		
		
		
		
	}
}
