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
 * This class controls the ultrasonic sesnors by getting data from them to determine if there is an obstacle ahead or not
 * @author charles
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
 
 /**
  * Initialises the parameters in the class.
  * @param usLeft The left Ultrasonic sensor
  * @param usRight The right Ultrasonic sensor
  * @param odometer The {@link Odometer} of the {@link TwoWheeledRobot}
  * @param robot The {@link TwoWheeledRobot}
  */
	 public Obstacle(UltrasonicSensor usLeft, UltrasonicSensor usRight, Odometer odometer, TwoWheeledRobot robot){  
		  this.usLeft = usLeft;
		  this.usRight = usRight; 
		  this.odometer = odometer;
		  this.robot = robot;
	 } 
 
 
	 

	 /**
	  * Checks if there is an obstacle ahead of the robot
	  * @return If there is an obstacle ahead of the robot
	  */
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

	 /**
	  * Gets the distance to an object from the left Ultrasonic sensor
	  * @return The distance to an object from the left Ultrasonic sensor
	  */
	 public static int getLeftUsDistance(){ //filter code left out because it slows down detection
		 // do a ping
		 usLeft.ping();   
		 leftUsDist = usLeft.getDistance();  
		 return leftUsDist;
	 }

	 /**
	  * Gets the distance to an object from the right Ultrasonic sensor
	  * @return The distance to an object from the right Ultrasonic sensor
	  */
	 public static int getRightUsDistance(){ 
		 // do a ping
		 usRight.ping();   
		 rightUsDist = usRight.getDistance();  
		 return rightUsDist;
		 
	 }
 
	 /**
	  * Gets the robots left Ultrasonic sensor
	  * @return The robots left Ultrasonic sensor
	  */
	 public static UltrasonicSensor getLeftUs(){
		 return usLeft;
	 }
 
	 /**
	  * Gets the robots left Ultrasonic sensor
	  * @return The robots left Ultrasonic sensor
	  */
	 public static UltrasonicSensor getRightUs(){
		 return usRight;
	 }
	 
	 /**
	  * Gets the distance between the robot and the obstacle. The distance
	  * between the robot and the first Ultrasonic sensor to detect the obstacle is returned
	  * @return
	  */
	 public static int getUsDistance(){
		 return usDistance;
	 }
}