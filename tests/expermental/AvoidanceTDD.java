package expermental;

import display.LCDInfo;
import navigation.Navigation;
import navigation.Obstacle;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import main.Constants;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import utilities.OdoLCD;

public class AvoidanceTDD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odo = new Odometer(false);
		NXTRegulatedMotor leftMotor = Motor.A;
		NXTRegulatedMotor rightMotor = Motor.B;
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, leftMotor, rightMotor);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		LightSampler leftLight = new LightSampler(lsLeft);
		LightSampler rightLight = new LightSampler(lsRight);
		new LCDInfo(odo);
		OdometryCorrection odoCorrect = new OdometryCorrection(patBot, leftLight, rightLight);
		UltrasonicSensor leftUs = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor righttUs = new UltrasonicSensor(SensorPort.S3);
		Obstacle obstacle = new Obstacle(leftUs, righttUs, odo, patBot);
		Navigation nav = new Navigation(patBot, obstacle, odoCorrect);
		Button.waitForAnyPress();
		odo.startTimer();
		odoCorrect.startCorrectionTimer();
		/*Move the robot in a square path*/
		nav.travelTo(0, 120, true);
		nav.travelTo(0, 120, false);
		//nav.travelTo(60, 0, true);
		//nav.travelTo(0, 0, false);
		Button.waitForAnyPress();
		

	}
	
	
}

	
