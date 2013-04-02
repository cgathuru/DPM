package tests;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import navigation.Obstacle;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import sensors.Localiser;
import utilities.OdoLCD;

import communication.Decoder;
import communication.StartCorner;
import communication.Transmission;

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
		//USLocalizer usl = new USLocalizer(patBot, usRight);
		//UsSampler leftUs = new UsSampler(usLeft);
		
		//LightLocalizer lLocalizer = new LightLocalizer(patBot, lsLeft);
		Obstacle obstacle = new Obstacle(usLeft, usRight, odo);
		//Navigation nav = new Navigation(patBot, obstacle, correction);
		LightSampler leftLight = new LightSampler(lsLeft);
		LightSampler rightLight = new LightSampler(lsRight);
		OdometryCorrection correction = new OdometryCorrection(patBot, leftLight, rightLight);
		Decoder decoder = new Decoder(new Transmission());
		decoder.startCorner = StartCorner.BOTTOM_LEFT;
		Localiser localizer= new Localiser(patBot,usLeft, leftLight, rightLight, decoder);
		new OdoLCD(odo);
		Button.waitForAnyPress();
		
		//oldBluetoothConnection connection = new oldBluetoothConnection();
		//LCD.clear();
		//connection.printTransmission();
		//Transmission trans = connection.getTransmission();
		
		leftLight.startCorrectionTimer();
		rightLight.startCorrectionTimer();
		odo.startTimer();
		//new StopControl().run();
		//int goalX = trans.w1;
		//int goalY = trans.w2;
		int goalX = 2;//oldTransmission.goalX;
		int goalY = 10;// oldTransmission.goalY;
		int goalTargetX = goalX*30;;
		int goalTargetY = determineYTarget(goalX,goalY)*30;
		localizer.dolocalise();
		//usl.doLocalization();
		//patBot.turnTo(0);
		//lLocalizer.doLocalization();
		/*nav.startCorrectionTimer();
		//odo.setTheta(270);
		nav.travelTo(0, goalTargetY);
		nav.travelTo(goalTargetX, goalTargetY);
		patBot.turnToFace(goalTargetX, goalTargetY);
		//patBot.turnTo(180);
		Launcher.drive(leftMotor, rightMotor, Motor.C);
		nav.travelTo(0, goalTargetY);
		nav.travelTo(0, 0);
		//nav.travelTo(0, goalTargetY);
		//patBot.turnTo(90);
		patBot.travelTo(goalTargetX, goalTargetY);
		patBot.turnToFace(goalTargetX, goalTargetY);
		Launcher.drive(leftMotor, rightMotor, Motor.C);
		nav.travelTo(0, 0);
		patBot.turnTo(0);
		Button.waitForAnyPress();*/
	}
	
	public static int determineYTarget(int goalX, int goalY){
		boolean [][] playingField = new boolean[8][12];
		playingField[goalX-1][goalY-1] = true;
		try{
			playingField[goalX-1][goalY -9] = true;
			return goalY - 7;
		}
		catch(ArrayIndexOutOfBoundsException ex){
			if((goalY + 7) > 11){
				return 11;
			}
			return  goalY + 7;
		}
	}

}
