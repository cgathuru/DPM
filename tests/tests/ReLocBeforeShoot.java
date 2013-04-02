package tests;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.LightSampler;
import utilities.OdoLCD;

public class ReLocBeforeShoot {
	
	public static void main (String[] args){
		
	
	
	
	Odometer odo = new Odometer(true);
	TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
	LightSensor lsLeft = new LightSensor(SensorPort.S1);
	LightSensor lsRight = new LightSensor(SensorPort.S2);
	LightSampler leftLight = new LightSampler(lsLeft);
	LightSampler rightLight = new LightSampler(lsRight);
	LightLocalizer reLoc = new LightLocalizer(patBot, lsLeft);
	new OdoLCD(odo);
	OdometryCorrection odoCorrect = new OdometryCorrection(patBot, leftLight, rightLight);
	Button.waitForAnyPress();
	odoCorrect.startCorrectionTimer();
	patBot.travelTo(60,0);
	patBot.travelTo(60,60);
	odoCorrect.stopCorrectionTimer();
	reLoc.doLocalization();
	///odoCorrect.startCorrectionTimer();
	patBot.travelTo(60,60);
	
	
	
	}
	
	
}
