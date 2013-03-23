package tests;

import display.LCDInfo;
import navigation.Launcher;
import navigation.Navigation;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import robot.Odometer;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.USLocalizer;
import utilities.OdoLCD;

public class TestLocalization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		LightSensor ls = new LightSensor(SensorPort.S1);
		USLocalizer usl = new USLocalizer(patBot, us);
		LightLocalizer lightLoc = new LightLocalizer(patBot, ls);
		Button.waitForAnyPress();
		new OdoLCD(odo);
		usl.doLocalization();
		//patBot.travelTo(30, 30);
		//patBot.turnTo(90);
		
		lightLoc.doLocalization();
		patBot.turnTo(0);
	//	new Launcher();
	//	Launcher.drive(Motor.A, Motor.B);
		patBot.motorFloat();
		patBot.travelTo(30, 30);
		//patBot.travelTo(0, 0);
		patBot.turnTo(0);
		Button.waitForAnyPress();
	}

}
