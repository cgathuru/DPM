package sensors;

import robot.Odometer;
import robot.TwoWheeledRobot;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import main.Constants;

public class Localizer {
	private LightSampler leftLight, rightLight;
	private UltrasonicSensor usLeft;
	private TwoWheeledRobot robot;
	private Odometer odometer;
	
	public Localizer(TwoWheeledRobot robot, UltrasonicSensor usLeft, LightSampler leftLight, LightSampler rightLight){
		this.robot = robot;
		this.odometer = robot.getOdometer();
		this.usLeft = usLeft;
		this.leftLight = leftLight;
		this.rightLight = rightLight;
	}
	
	public void delocalize(){
		//new LCDInfo(odo);
				boolean loacalized = false;
				//if facing towards the wall
				if(usLeft.getDistance() < Constants.WALL_DIST){
					boolean wrongDirection = true;
					robot.setRotationSpeed(Constants.ROTATE_SPEED);
					while(wrongDirection){
						if(usLeft.getDistance() > Constants.WALL_DIST){
							robot.stopMotors();
							wrongDirection = false;
						}
					}
				}
				//if facing away from the wall
				if(usLeft.getDistance()  > Constants.WALL_DIST){
					while(!loacalized){
						robot.setRotationSpeed(Constants.ROTATE_SPEED);
						//rotate clockwise till you see a wall
						if(usLeft.getDistance()  < 60){
							//patBot.turnToImmediate(-55);
							robot.stopMotors();
							robot.moveForward(); //move forawrd until you hit a line
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
											odometer.setTheta(0);
											double odoX = odometer.getX();
											int xValue = (int)(odoX/30) *30;
											odometer.setX(xValue);
											light2 = true;
											light = true;
											break;
										}
									}
								}
								
							}
							robot.turnTo(-90);
							robot.moveForward();
							boolean there = false;
							while(!there){
								if(leftLight.isDarkLine()){
									robot.stopMotors();
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
									double odoY = odometer.getY();
									int yValue = (int)(odoY/30) *30;
									odometer.setY(yValue);
									//odo.setY(0);
									there = true;
									odometer.setTheta(0);
									robot.turnTo(0);
									odometer.setX(0);
									loacalized = true;
									break;
								}
							}
							
							
						}
						
					}
				}
	}
}
