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

public class CorrActualLevel3 {

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
		patBot.travleToActual(0, 60);
		patBot.travleToActual(30, 60);
		patBot.travleToActual(30, 30);
		patBot.travleToActual(0, 30);
		patBot.travleToActual(0, 60);
		patBot.travleToActual(60, 30);
		patBot.travleToActual(60, 60);
		patBot.travleToActual(0, 60);
		patBot.travleToActual(60, 0);
		patBot.travleToActual(0,0);
		odoCorrect.stopCorrectionTimer();
		patBot.turnTo(0);
		Button.waitForAnyPress();
	}

}
