package tests;

import navigation.Launcher;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.USLocalizer;
import utilities.OdoLCD;

public class Corr3LocShoot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S4);
		LightLocalizer ls1 = new LightLocalizer(patBot, lsLeft);
		new Launcher();
		//UltrasonicSensor usLeft = new UltrasonicSensor(SensorPort.S3);		
		USLocalizer us1 = new USLocalizer(patBot, usRight);
		Button.waitForAnyPress();
		us1.doLocalization();
		patBot.turnTo(0);
		ls1.doLocalization();
		new OdoLCD(odo);
		OdometryCorrection odoCorrect = new OdometryCorrection(patBot, lsLeft, lsRight);
		odoCorrect.startCorrectionTimer();
		patBot.travelTo(0, 60);
		patBot.travelTo(30, 30);
		patBot.turnTo(90);
		Launcher.drive(Motor.A, Motor.B, Motor.C);

		
//		patBot.travelTo(30, 30);
//		patBot.travelTo(0, 30);
//		patBot.travelTo(0, 60);
//		patBot.travelTo(60, 30);
//		patBot.travelTo(60, 60);
//		patBot.travelTo(0, 60);
//		patBot.travelTo(60, 0);
//		patBot.travelTo(0,0);
		odoCorrect.stopCorrectionTimer();
//		patBot.turnTo(0);
		Button.waitForAnyPress();
	}

}
