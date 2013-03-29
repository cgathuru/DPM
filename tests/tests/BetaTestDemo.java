package tests;

import oldCommunication.oldBluetoothConnection;
import oldCommunication.oldTransmission;
import main.Constants;
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
import sensors.LightSampler;
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
		LightSampler leftLight = new LightSampler(lsLeft);
		LightSampler rightLight = new LightSampler(lsRight);
		//new LCDInfo(odo);
		Button.waitForAnyPress();
		
		oldBluetoothConnection connection = new oldBluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		//Transmission trans = connection.getTransmission();
		
		leftLight.startCorrectionTimer();
		rightLight.startCorrectionTimer();
		odo.startTimer();
		//new StopControl().run();
		//int goalX = trans.w1;
		//int goalY = trans.w2;
		int goalX = oldTransmission.goalX;
		int goalY = oldTransmission.goalY;
		int goalTargetX = goalX*30;;
		int goalTargetY = determineYTarget(goalX,goalY)*30;
		new LCDInfo(odo);
		boolean loacalized = false;
		//if facing towards the wall
		if(usLeft.getDistance() < 60){
			boolean wrongDirection = true;
			patBot.setRotationSpeed(Constants.ROTATE_SPEED);
			while(wrongDirection){
				if(usLeft.getDistance() > Constants.WALL_DIST){
					patBot.stopMotors();
					wrongDirection = false;
				}
			}
		}
		//if facing away from the wall
		if(usLeft.getDistance() > 60){
			while(!loacalized){
				patBot.setRotationSpeed(Constants.ROTATE_SPEED);
				//rotate clockwise till you see a wall
				if(usLeft.getDistance() < Constants.WALL_DIST){
					patBot.turnToImmediate(-55);
					patBot.stopMotors();
					patBot.moveForward(); //move forawrd until you hit a line
					boolean light = false;
					while(!light){
						if(rightLight.isDarkLine()){
							Motor.B.stop(true);
							Motor.A.stop();
							Motor.A.setSpeed(-Constants.ROTATE_SPEED);
							Motor.A.backward();
							boolean light2 = false;
							while(!light2){
								if(leftLight.isDarkLine()){
									Motor.A.stop();
									odo.setTheta(0);
									double odoX = odo.getX();
									int xValue = (int)(odoX/30) *30;
									odo.setX(xValue);
									light2 = true;
									light = true;
									break;
								}
							}
						}
						/*else{
							Motor.A.stop(true);
							Motor.B.stop();
							Motor.B.setSpeed(Constants.ROTATE_SPEED);
							Motor.B.forward();
							boolean light2 = false;
							while(!light2){
								if(leftLight.isDarkLine()){
									Motor.B.stop();
									odo.setTheta(90);
									double odoX = odo.getX();
									int xValue = (int)(odoX/30) *30;
									odo.setY(xValue);
									light2 = true;
									light = true;
									break;
								}
							}
						}*/
						
					}
					patBot.turnTo(-90);
					patBot.moveForward();
					boolean there = false;
					while(!there){
						if(leftLight.isDarkLine()){
							patBot.stopMotors();
							boolean last = false;
							Motor.B.setSpeed(-Constants.ROTATE_SPEED);
							Motor.B.backward();
							while(!last){
								if(rightLight.isDarkLine()){
									Motor.B.stop();
									last = true;
									break;
								}
							}
							double odoY = odo.getY();
							int yValue = (int)(odoY/30) *30;
							odo.setY(yValue);
							//odo.setY(0);
							there = true;
							odo.setTheta(0);
							patBot.turnTo(0);
							odo.setX(0);
							loacalized = true;
							break;
						}
					}
					
					
				}
				
			}
		}
		//usl.doLocalization();
		//patBot.turnTo(0);
		//lLocalizer.doLocalization();
		nav.startCorrectionTimer();
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
		/*patBot.travelTo(goalTargetX, goalTargetY);
		patBot.turnToFace(goalTargetX, goalTargetY);
		Launcher.drive(leftMotor, rightMotor, Motor.C);
		nav.travelTo(0, 0);
		patBot.turnTo(0);*/
		Button.waitForAnyPress();
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
