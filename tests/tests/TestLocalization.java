package tests;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import odometer.Odometer;
import odometer.TwoWheeledRobot;
import sensors.USLocalizer;

public class TestLocalization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE);
		usl.doLocalization();

	}

}
