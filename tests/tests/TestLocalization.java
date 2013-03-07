package tests;

import display.LCDInfo;
import navigaion.Navigation;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
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
		Navigation navigator = odo.getNavigation();
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		USLocalizer usl = new USLocalizer(odo, us);
		Button.waitForAnyPress();
		new LCDInfo(odo);
		usl.doLocalization();
		navigator.turnTo(90);
		Sound.beepSequenceUp();

	}

}
