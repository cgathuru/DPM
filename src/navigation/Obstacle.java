package navigation;

import robot.Odometer;
import robot.TwoWheeledRobot;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import main.Constants;

/**
 * 
 * @author Madeleine Incutiyayezu
 * April 2, 2013
 *
 */

public class Obstacle {
 private static int leftUsDist;
 private static int rightUsDist;  
 private static UltrasonicSensor usLeft, usRight; 
 private Odometer odometer;   
 private TwoWheeledRobot robot;
 private boolean newObstacleDetected;//
 private int rightSensor,leftSensor; 
 private boolean loop;
 private static int usDistance = 0;
 private int leftDistance =0, rightDistance = 0;
 
	 public Obstacle(UltrasonicSensor usLeft, UltrasonicSensor usRight, Odometer odometer, TwoWheeledRobot robot){  
		  this.usLeft = usLeft;
		  this.usRight = usRight; 
		  this.odometer = odometer;
		  this.robot = robot;
	 } 
 
 
	 public void travelToNextBestLocation(int xTarget, int yTarget){
		 
	 }

	 

	 

 
	 public static  boolean isObstacleAhead(){
		 if (getLeftUsDistance() < 25){
			 leftUsDist = usDistance;
			 return true;
		 }
		 if(getRightUsDistance() < 25){
			 rightUsDist = usDistance;
			 return true;
		 }
		 else{
			 return false;
		 }
	 }

	 public static int getLeftUsDistance(){ //filter code left out because it slows down detection
		 // do a ping
		 usLeft.ping();   
		 leftUsDist = usLeft.getDistance();  
		 return leftUsDist;
	 }

	 public static int getRightUsDistance(){ 
		 // do a ping
		 usRight.ping();   
		 rightUsDist = usRight.getDistance();  
		 return rightUsDist;
		 
	 }
 
 
	 public static UltrasonicSensor getLeftUs(){
		 return usLeft;
	 }
 
	 public static UltrasonicSensor getRightUs(){
		 return usRight;
	 }
	 
	 public static int getUsDistance(){
		 return usDistance;
	 }
}