package navigation;


import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
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
 }
    
 /**
  * Travels to a target destination. This version of travelTo includes obstacle avoidance and odometry correction
  * @param xTarget The x position of the target destination
  * @param yTarget The y position of the target destination
  */ 
  public void travelTo(int xTarget, int yTarget){
      
   while(Math.abs(odometer.getX()-xTarget)>Constants.ALLOWABLE_ERROR || Math.abs(odometer.getY()-yTarget)>Constants.ALLOWABLE_ERROR){  
          
    if((Obstacle.leftUsDist()>Constants.OBSTACLE_DIST && Obstacle.rightUsDist()>Constants.OBSTACLE_DIST)||((Math.cos(Constants.US_ANGLE)*Obstacle.leftUsDist())>=(calculateDistance(xTarget,yTarget)-15)&&(Math.cos(Constants.US_ANGLE)*Obstacle.rightUsDist())>=(calculateDistance(xTarget,yTarget)-15))){
     //drive straight if there is no obstacle, or if obstacle is farther away than target e.g driving towards ball dispenser      
     robot.turnToFace(xTarget, yTarget);
     double distance = calculateDistance(xTarget, yTarget);
     moveForwardBy(distance, xTarget, yTarget);
     divider(xTarget, yTarget, distance); //takes in target coords, used to turn off obstacle avoidance when the robot is traveling towards the ball dispenser
     
     
     //try new navigation, move x direction first, then y direction
    
    //do the x direction
    	
//    robot.turnToFace(xTarget, (int)odometer.getY());
//     double distance2 = calculateDistance(xTarget, (int)odometer.getY());
//     moveForwardBy(distance2, xTarget, (int)odometer.getY());
//     divider((int)distance2, xTarget, odometer.getY());
//     
//     //do the y direction now
//     
//     robot.turnToFace((int)odometer.getX(), yTarget);
//     double distance3 = calculateDistance((int)odometer.getX(),yTarget);
//     moveForwardBy(distance3, (int)odometer.getX(), yTarget);
//     divider((int)distance3, (int) odometer.getX(), yTarget);
   
     

     
    }else if(avoidance){ 
     robot.setForwardSpeed(0);
     robot.turnToFace(xTarget, yTarget);//     
     //odoCorrection.stopCorrectionTimer();
     obstacle.avoid(xTarget,yTarget); //obManager method called in Obstacle class, exited when robot is clear of obstacle     
     robot.turnToFace(xTarget, yTarget); //done avoiding the obstacle, turn towards the target    
   //  odoCorrection.startCorrectionTimer();
    }
    else{
     //do nothing because obstacle avoidance is off
    
    	}  
   	}
  }
  /**
   * Corrects the robots heading if it is off the intended target
   * @param xTarget The x ordinate of the target destination
   * @param yTarget The y ordinate of the target destination
   * @param distance The distance the robot needs to move in order to get to its target
   */
  public void divider(int xTarget, int yTarget, double distance) {
   if(distance <= 60){
    moveForwardBy(distance, xTarget, yTarget);
   }
   else if(distance > 60 && distance < 90){
    double multiplier = distance/Constants.TILE_DISTANCE;
    for(int i = 1; i < multiplier-1; i++){
     Sound.beepSequenceUp();
     robot.turnToFace(xTarget, yTarget);
     moveForwardBy(Constants.TILE_DISTANCE, xTarget, yTarget);
    }
    robot.turnToFace(xTarget,yTarget);
    moveForwardBy(calculateDistance(xTarget,yTarget), xTarget, yTarget);
   }
   else{
    double multiplier = distance/Constants.TILE_DISTANCE;
    for(int i = 1; i < multiplier-1; i++){
     Sound.beepSequenceUp();
     robot.turnToFace(xTarget, yTarget);
     moveForwardBy(Constants.TILE_DISTANCE, xTarget, yTarget);
    }
    robot.turnToFace(xTarget,yTarget);
    moveForwardBy(calculateDistance(xTarget,yTarget), xTarget, yTarget);
   }
   //moveForwardBy(distance);
   //turnToFace(xTarget,yTarget);
   //moveForwardBy(calculateDistance(xTarget,yTarget));
  }
 /**
  * Moves the robot forward  in a straight line by a given distance
  * @param distance The distance to moved in centimeters
  */
 public void moveForwardBy(double distance,int x,int y){
  robot.moveForwardBy(distance,true);
  NXTRegulatedMotor rightMotor = robot.getRightMotor();
  
  while(rightMotor.isMoving()){ //code to stop the motors when an obstacle is detected
   
    if((Obstacle.leftUsDist()<Constants.OBSTACLE_DIST || Obstacle.rightUsDist()<Constants.OBSTACLE_DIST)&&!((Math.cos(Constants.US_ANGLE)*Obstacle.leftUsDist())>=(calculateDistance(x,y)-15)&&(Math.cos(Constants.US_ANGLE)*Obstacle.rightUsDist())>=(calculateDistance(x,y)-15))){
      return;
    }
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
