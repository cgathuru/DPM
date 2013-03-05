package tests;

import display.LCDInfo;
import navigaion.Navigation;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import odometer.Odometer;
import odometer.OdometryCorrection;
import odometer.TwoWheeledRobot;
import sensors.USLocalizer;
import sensors.USLocalizer.LocalizationType;

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
		int buttonChoice;
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		Navigation nav = odo.getNavigation();
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		USLocalizer usl = new USLocalizer(odo,us, LocalizationType.FALLING_EDGE);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		new LCDInfo(odo, usl);
		OdometryCorrection odoCorrect = new OdometryCorrection(odo, lsLeft, lsRight);
		do {
			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
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

		//}
	}

}
