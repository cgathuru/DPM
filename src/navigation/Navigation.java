package navigation;


import communication.Decoder;

import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import tests.Maze;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import main.Constants;

/**
 * Controls the flow of events that determines the robots movements
 * @author charles
 *
 */
public class Navigation {
 //private static double width = 23.4;                                                                               
 private Odometer odometer;
 private TwoWheeledRobot robot;
 private Obstacle obstacle;
 private OdometryCorrection odoCorrection;
  public static long endLeft, endRight;//used in obstacle avoidance 
 private boolean avoidance;
 private LightSampler leftLight, rightLight;
 private Maze maze;
 private boolean avoidLeftX = true, avoidLeftY = true;
 private boolean headingY = true;
 
 /**
  * Initializes all the variables contained within the class
  * @param robot The {@link TwoWheeledRobot}
  * @param obstacle An {@link Obstacle} responsible for obstacle avoidance
  * @param odoCorrection The {@link OdometryCorrection}
  */
 public Navigation(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection) {
	  this.odometer = robot.getOdometer();
	  this.robot = robot;
	  this.obstacle = obstacle;
	  this.odoCorrection = odoCorrection;
	  avoidance = true;
	  leftLight = odoCorrection.getLeftLightSampler();
	  rightLight = odoCorrection.getRightLightsampler();
	  maze = new Maze(odometer);
 }
    
 /**
  * Travels to a target destination. This version of travelTo includes obstacle avoidance and odometry correction
  * @param xTarget The x position of the target destination
  * @param yTarget The y position of the target destination
  */ 
  public void travelTo(int xTarget, int yTarget){
	  while((Math.abs(odometer.getX() - xTarget) > Constants.ALLOWABLE_ERROR) || (Math.abs(odometer.getY() - yTarget) > Constants.ALLOWABLE_ERROR)){
		  headingY = true;
		  moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
		  headingY = false;
		  moveInGivenHeading(xTarget, yTarget, headingY);
	  }
	  
	  //robot.turnToFace(xTarget, yTarget);
  }
  
  public void determineToDeactivateAvoidanceForBallCollection(){
	  if(((Decoder.dispenserX - 45) < odometer.getX() && (Decoder.dispenserX + 45) > odometer.getY()) && ((Decoder.dispenserY - 45) < odometer.getY() && (Decoder.dispenserY + 45) > odometer.getY())){
		  
		turnOffObstacleAvoidance();  	
	  }
	  else{
		  turnOnObstacleAvoidance();
	  }
	  
  }
 

public void moveInGivenHeading(int xTarget, int yTarget, boolean headingY) {
	determineToDeactivateAvoidanceForBallCollection();
	robot.turnToFace(xTarget, yTarget);
	  this.headingY = headingY;
	  if(headingY){
		  refreshAvoidYStatus();
	  }
	  else{
		  refreshAvoidXStatus();
	  }
	  double distance = robot.calculatedCorrectedDistance(xTarget, yTarget);
	  //divider(distance, xTarget, yTarget, true);
	  robot.moveForwardBy(distance, true);
	  while(hasNotReachedDestination(xTarget, yTarget)){
		  determineToDeactivateAvoidanceForBallCollection();
		  if(Obstacle.isObstacleAhead()){
			 // maze.removeIntersection(Obstacle.getUsDistance());
			  determineToDeactivateAvoidanceForBallCollection();
			  if(avoidance){
				  avoidObstacle(xTarget, yTarget);
			  }
			  else{
				  //do nothing because obstacle avoidance is off
			  }
			  //obstacleAvoidance(xTarget, yTarget);
		  }
		  else{
			  determineToDeactivateAvoidanceForBallCollection();
			  robot.moveForwardBy(robot.calculatedCorrectedDistance(xTarget, yTarget), true);
		  }
	  }
}
  
  
  		public void refreshAvoidXStatus(){
  			if(odometer.getY() > 160){
	  			  avoidLeftX = false;
	  			  if(odometer.getTheta() > 260 && odometer.getTheta() < 280){
	  				  avoidLeftX = true;
	  			  }
	  		  }
	  		  else if(odometer.getY() < 150){
	  			  avoidLeftX = true;
	  			if(odometer.getTheta() > 260 && odometer.getTheta() < 280){
	  				  avoidLeftX = false;
	  			  }
	  		  }
	  		  else{
	  			  avoidLeftX = false;
	  		  }
  		}
  		
  		public void refreshAvoidYStatus(){
	  		if(odometer.getX() > 160){
	  			  avoidLeftX = true;
	  			  if(odometer.getTheta() > 170 && odometer.getTheta() < 190){
	  				  avoidLeftX = false;
	  			  }
	  		  }
	  		  else if(odometer.getX() < 150){
	  			  avoidLeftX = false;
	  			if(odometer.getTheta() > 170 && odometer.getTheta() < 190){
	  				  avoidLeftX = true;
	  			  }
	  		  }
	  		  else{
	  			  avoidLeftX = true;
	  		  }
  		}
  		
  		public void obstacleAvoidance(int xTarget, int yTarget){
  			//back up
  			robot.moveForwardBy(-15);
  			refreshAvoidXStatus();
  			if(avoidLeftX){
  				robot.turnToImmediate(-90);
  			}
  			else{
  				robot.turnToImmediate(90);
  			}
  			robot.moveForwardBy(15);
  			if(odometer.getTheta() % 180 < 10){
  				moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
  				moveInGivenHeading(xTarget, yTarget, headingY);
  			}
  			else{
  				moveInGivenHeading(xTarget, (int)odometer.getY(), headingY);
  				moveInGivenHeading(xTarget, yTarget, headingY);
  			}
  		}
  
	  public void avoidObstacle(int xTarget, int yTarget){
		  boolean interrupt = false;
		  NXTRegulatedMotor leftMotor = robot.getLeftMotor(), rightMotor = robot.getRightMotor();
		  //stop correction but keep the sensors on
			/*robot.stopMotors();
			leftMotor.forward();
			rightMotor.forward();
			leftMotor.setSpeed(Constants.FORWARD_SPEED);
			rightMotor.setSpeed(Constants.FORWARD_SPEED);
			robot.moveForwardBy(-Constants.TILE_DISTANCE, true);
			boolean there = false, aligned = false;
			while(!there){
				//if left sensor first
				 if(leftLight.isDarkLine()){
					 leftMotor.stop();
					 while(!aligned){
						 if(rightLight.isDarkLine()){
							 rightMotor.stop();
							 there = true;
							 aligned = true;
						 }
					 }
				 }
				 //if right sensor first
				 if(rightLight.isDarkLine()){
					 rightMotor.stop();
					 while(!aligned){
						 if(leftLight.isDarkLine()){
							 leftMotor.stop();
							 there = true;
							 aligned = true;
						 }
					 }
				 }// end right is dark line
			 }//end while
*/			
		  	if((headingY && (odometer.getY() > yTarget)) && (odometer.getTheta() < 10 && odometer.getTheta() > 350)){
		  		Sound.beepSequence();
		  		moveInGivenHeading(xTarget, (int)odometer.getY(), false);
		  		moveInGivenHeading(xTarget, yTarget, true);
		  		interrupt = true;
		  	}
		  	else if((!headingY &&  (odometer.getY() >= yTarget)) && (odometer.getTheta() < 10 && odometer.getTheta() > 350)){
		  		Sound.buzz();
		  		moveInGivenHeading(xTarget, (int)odometer.getY(), false);
		  		moveInGivenHeading(xTarget, yTarget, true);
		  		interrupt = true;
		  	}
	  	
		  	if(avoidLeftX){
			  	robot.turnToImmediate(-90);
			  	if(Obstacle.isObstacleAhead()){
			  		robot.turnToImmediate(90);
			  		turningRight(xTarget, yTarget, interrupt);
			  	}
			  	else{
			  		robot.turnToImmediate(90);
			  		turningLeft(xTarget, yTarget, interrupt);
			  	}
		  		
		  	}
		  	else{
		  		robot.turnToImmediate(90);
		  		if(Obstacle.isObstacleAhead()){
		  			robot.turnToImmediate(-90);
		  			turningLeft(xTarget, yTarget, interrupt);
		  		}
		  		else{
		  			robot.turnToImmediate(-90);
		  			turningRight(xTarget, yTarget, interrupt);
		  		}
		  		
		  	}
			
		 }

	public void turningRight(int xTarget, int yTarget, boolean interrupt) {
		robot.turnToImmediate(90);
		//if moving along x direction
		if(isMovingAlongX() && !interrupt ){
			refreshAvoidXStatus();
			if(Obstacle.isObstacleAhead()){
				Sound.beep();
				robot.turnToImmediate(180);
				robot.moveForwardBy(Constants.TILE_DISTANCE);
				robot.turnToImmediate(90);
				moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
				moveInGivenHeading(xTarget, yTarget, headingY);
			}
			else{
				Sound.beepSequence();
				robot.moveForwardBy(Constants.TILE_DISTANCE);
				robot.turnToImmediate(-90);
				if(headingY){
					moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
				}
				else{
					moveInGivenHeading(xTarget, (int)odometer.getY(), headingY);
				}
				
				//travelTo(xTarget, yTarget, headingY);
				
			}
			
		}
		//if moving along the y direction
		else{
			refreshAvoidYStatus();
			if(Obstacle.isObstacleAhead()){
				Sound.buzz();
				robot.turnToImmediate(180);
				robot.moveForwardBy(Constants.TILE_DISTANCE);
				robot.turnToImmediate(90);
				moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
				//travelTo(xTarget, yTarget, headingY);
			}
			else{
				Sound.beepSequenceUp();
				robot.moveForwardBy(Constants.TILE_DISTANCE);
				robot.turnToImmediate(-90);
				moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
				//travelTo(xTarget, yTarget, headingY);
			}
		}
	}
	  
	
	public void turningLeft(int xTarget, int yTarget, boolean interrupt){
		robot.turnToImmediate(-90);
		//if moving along x direction
				if(isMovingAlongX() && !interrupt ){
					refreshAvoidXStatus();
					if(Obstacle.isObstacleAhead()){
						Sound.beep();
						robot.turnToImmediate(180);
						robot.moveForwardBy(Constants.TILE_DISTANCE);
						robot.turnToImmediate(-90);
						moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
						moveInGivenHeading(xTarget, yTarget, headingY);
					}
					else{
						Sound.beepSequence();
						robot.moveForwardBy(Constants.TILE_DISTANCE);
						robot.turnToImmediate(90);
						if(headingY){
							moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
						}
						else{
							moveInGivenHeading(xTarget, (int)odometer.getY(), headingY);
						}
						
						//travelTo(xTarget, yTarget, headingY);
						
					}
					
				}
				//if moving along the y direction
				else{
					refreshAvoidYStatus();
					if(Obstacle.isObstacleAhead()){
						Sound.buzz();
						robot.turnToImmediate(180);
						robot.moveForwardBy(Constants.TILE_DISTANCE);
						robot.turnToImmediate(-90);
						moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
						//travelTo(xTarget, yTarget, headingY);
					}
					else{
						Sound.beepSequenceUp();
						robot.moveForwardBy(Constants.TILE_DISTANCE);
						robot.turnToImmediate(90);
						moveInGivenHeading((int)odometer.getX(), yTarget, headingY);
						//travelTo(xTarget, yTarget, headingY);
					}
				}
	}
	  
	  
	public boolean isMovingAlongX() {
		return !(odometer.getTheta() % 180 < 10) || !(odometer.getTheta() % 180 > 170);
	}
	 
	  public void moveInXTo(int xTarget){
		  while((Math.abs(odometer.getX() - xTarget) > Constants.ALLOWABLE_ERROR)){
			  break;
		  }
	  }
	  
	  public void moveInYTo(int yTarget){
		  
	  }
  
	  public boolean hasNotReachedDestination(int xTarget, int yTarget) {
		  if(headingY){
			  return (Math.abs(odometer.getY() - yTarget) > Constants.ALLOWABLE_ERROR);
		  }
		  return (Math.abs(odometer.getX() - xTarget) > Constants.ALLOWABLE_ERROR);
			//return (Math.abs(odometer.getX() - xTarget) > Constants.ALLOWABLE_ERROR) || (Math.abs(odometer.getY() - yTarget) > Constants.ALLOWABLE_ERROR);
	  }
	  
	 
	  
	  public void divider(double distance, int xTarget, int yTarget, boolean immediateReturn){
		  if(distance <= 60){
			    robot.moveForwardBy(distance, immediateReturn);
			   }
			   else if(distance > 60 && distance < 90){
			    double multiplier = distance/Constants.TILE_DISTANCE;
			    for(int i = 1; i < multiplier-1; i++){
			     //Sound.beepSequenceUp();
			     robot.turnToFace(xTarget, yTarget);
			     robot.moveForwardBy(Constants.TILE_DISTANCE, immediateReturn);
			    }
			    robot.turnToFace(xTarget,yTarget);
			    robot.moveForwardBy(calculateDistance(xTarget,yTarget), immediateReturn);
			   }
			   else{
			    double multiplier = distance/Constants.TILE_DISTANCE;
			    for(int i = 1; i < multiplier-1; i++){
			    // Sound.beepSequenceUp();
			     robot.turnToFace(xTarget, yTarget);
			     robot.moveForwardBy(Constants.TILE_DISTANCE, immediateReturn);
			    }
			    robot.turnToFace(xTarget,yTarget);
			    robot.moveForwardBy(calculateDistance(xTarget,yTarget), immediateReturn);
			   }
	  }
	
	  
	 /**
	  * Calculates the distance between the robot and is intended target destination
	  * @param xTarget The x ordinate of the targets location
	  * @param yTarget The y ordinate of the targets location
	  * @return
	  */
	  public double calculateDistance(int xTarget, int yTarget){
	   return robot.calculatedCorrectedDistance(xTarget, yTarget);
	  }
	 
	 /**
	  * Gets the x position of the robot
	  * @return The x-ordinate of the robot
	  */
	 public double getX(){
	  return this.odometer.getY();
	 }
	 
	 /**
	  * Gets the y position of the robot
	  * @return The y-ordinate of the robot
	  */
	 public double getY(){
	  return this.odometer.getX();
	 }
	 
	 /**
	  * Gets the heading of the robot
	  * @return The current heading of the robot
	  */
	 public double getTheta(){
	  return this.odometer.getTheta();
	 }
	 
	 /**
	  * Turns on {@link OdometryCorrection}
	  */
	 public void startCorrectionTimer(){
	  odoCorrection.startCorrectionTimer();
	 }
	 
	 /**
	  * Turns off {@link OdometryCorrection}
	  */
	 public void stopCorrectionTimer(){
	  odoCorrection.stopCorrectionTimer();
	 }
	 
	 /**
	  * Turns off obstacle avoidance
	  */
	 public void turnOffObstacleAvoidance(){
	  this.avoidance = false;
	 }
	 
	 /**
	  * Turns off obstacle avoidance
	  */
	 public void turnOnObstacleAvoidance(){
	  this.avoidance = true;
	 }
	 
	 /**
	  * Checks if obstacle avoidance is on or off
	  * @return True if the obstacle avoidance is on
	  */
	 public boolean getAvoidanceStatus(){
	  return this.avoidance;
	 }
	 
	 /**
	  * Moves the robot by a given distance
	  * @param distance The distance the robot should move forward by
	  */
	 public void moveForwardBy(double distance){
	  robot.moveForwardBy(distance);
	 }
}
