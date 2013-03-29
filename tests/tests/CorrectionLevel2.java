package tests;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import utilities.OdoLCD;

public class CorrectionLevel2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		LightSampler leftLight = new LightSampler(lsLeft);
		LightSampler rightLight = new LightSampler(lsRight);
		new OdoLCD(odo);
		OdometryCorrection odoCorrect = new OdometryCorrection(patBot, leftLight, rightLight);
		Button.waitForAnyPress();
		odoCorrect.startCorrectionTimer();
		patBot.travelTo(0, 60);
		patBot.travelTo(30, 30);
		patBot.travelTo(60, 60);
		patBot.travelTo(60, 0);
		patBot.travelTo(0,0);
		odoCorrect.stopCorrectionTimer();
		patBot.turnTo(0);
		Button.waitForAnyPress();
	}

}
