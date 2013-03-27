package tests;

import navigation.Launcher;
import navigation.Navigation;
import navigation.Obstacle;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.USLocalizer;
import utilities.StopControl;

import communication.BluetoothConnection;
import communication.Transmission;

import display.LCDInfo;

public class BetaTestDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NXTRegulatedMotor leftMotor = Motor.A;
		NXTRegulatedMotor rightMotor = Motor.B;
		Odometer odo = new Odometer(false);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		//Bluetooth connection with Mufasa (our robot)
		UltrasonicSensor usLeft = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S4);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		USLocalizer usl = new USLocalizer(patBot, usRight);
		LightLocalizer lLocalizer = new LightLocalizer(patBot, lsLeft);
		Obstacle obstacle = new Obstacle(usLeft, usRight, odo);
		OdometryCorrection correction = new OdometryCorrection(patBot, lsLeft, lsRight);
		Navigation nav = new Navigation(patBot, obstacle, correction);
		Button.waitForAnyPress();
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		Transmission trans = connection.getTransmission();
		new StopControl().run();
		int goalX = trans.w1;
		int goalY = trans.w2;
		int goalTargetX = goalX*30;;
		int goalTargetY = determineYTarget(goalX,goalY)*30;
		new LCDInfo(odo);
		usl.doLocalization();
		lLocalizer.doLocalization();
		nav.startCorrectionTimer();
		nav.travelTo(0, goalTargetY);
		nav.travelTo(goalTargetX, goalTargetY);
		patBot.turnToFace(goalTargetX, goalTargetY);
		Launcher.drive(leftMotor, rightMotor, Motor.C);
		nav.travelTo(0, 0);
		patBot.turnTo(0);
		Button.waitForAnyPress();
	}
	
	public static int determineYTarget(int goalX, int goalY){
		boolean [][] playingField = new boolean[8][12];
		playingField[goalX-1][goalY-1] = true;
		try{
			playingField[goalX-1][goalY -9] = true;
			return goalY - 9;
		}
		catch(ArrayIndexOutOfBoundsException ex){
			return  goalY + 7;
		}
	}

}
