package tests;

import navigaion.Navigation;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import odometer.Odometer;
import odometer.OdometryCorrection;
import odometer.TwoWheeledRobot;
import utilities.OdoLCD;
import display.LCDInfo;

public class TestNavigation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		Navigation nav = odo.getNavigation();
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		new OdoLCD(odo, lsLeft, lsRight);
		OdometryCorrection odoCorrect = new OdometryCorrection(odo, lsLeft, lsRight);
		Button.waitForAnyPress();
		odoCorrect.startCorrectionTimer();
		//int repeatNumber =4;
		//for(int i =0; i < repeatNumber; i++){
			patBot.moveForwardBy(60);
			nav.turnTo(90);
			patBot.moveForwardBy(30);
			nav.turnTo(180);
			patBot.moveForwardBy(30);
			nav.turnTo(270);;
			patBot.moveForwardBy(30);
			nav.turnTo(180);
			patBot.moveForwardBy(60);
			nav.turnTo(180);
			patBot.moveForwardBy(30);
			nav.turnTo(270);
			patBot.moveForwardBy(60);
			nav.turnTo(0);
			patBot.turnToImmediate(-90);
			
			odoCorrect.stopCorrectionTimer();
			//nav.turnTo(0);

			Button.waitForAnyPress();
			System.exit(0);
	}

}
