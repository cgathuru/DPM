package tests;

import display.LCDInfo;
import navigation.Navigation;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import robot.Odometer;
import robot.TwoWheeledRobot;
import sensors.USLocalizer;

public class TestLocalization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		USLocalizer usl = new USLocalizer(patBot, us);
		Button.waitForAnyPress();
		new LCDInfo(odo);
		usl.doLocalization();
		patBot.turnTo(90);
		Sound.beepSequenceUp();

	}

}
