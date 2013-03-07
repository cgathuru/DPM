package tests;

import navigaion.Navigation;
import odometer.Odometer;
import odometer.OdometryCorrection;
import odometer.TwoWheeledRobot;
import sensors.USLocalizer;
import sensors.USLocalizer.LocalizationType;
import utilities.OdoLCD;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class OdoCorrectFloat {

	/**
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
		new OdoLCD(odo, lsLeft, lsRight);
		OdometryCorrection odoCorrect = new OdometryCorrection(odo, lsLeft, lsRight);
		do {
			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		odoCorrect.startCorrectionTimer();
		Motor.A.flt();
		Motor.B.flt();
		
		Button.waitForAnyPress();
		System.exit(0);

	}

}
