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
 * @author Madeleine Incutiyayezu, Zachary Zoldan
 * April 5, 2013
 *
 */

public class ZachObstacle {
 private static int leftUsDist;
 private static int rightUsDist;  
 private static UltrasonicSensor usLeft, usRight; 
 private Odometer odometer;   
 private TwoWheeledRobot robot;
 private boolean newObstacleDetected;//
 private int rightSensor,leftSensor;
 private int rightSideDist, leftSideDist;
 private boolean loop;
 
 public ZachObstacle(UltrasonicSensor usRight, UltrasonicSensor usLeft, Odometer odometer, TwoWheeledRobot robot){  
  this.usLeft = usLeft;
  this.usRight = usRight; 
  this.odometer = odometer;
  this.robot = robot;
 } 
 
 public void avoid(int targetX, int targetY){ 
  newObstacleDetected = false;
  loop = true;
  
  while(leftUsDist()<Constants.OBSTACLE_DIST2||rightUsDist()<Constants.OBSTACLE_DIST2){ //back up when the robot is too close to an obstacle
   Motor.A.setSpeed(Constants.FORWARD_SPEED); //
   Motor.B.setSpeed(Constants.FORWARD_SPEED); // 
   Motor.A.backward();
   Motor.B.backward();
  }
  while(loop){
	  if(wallpointedAt(odometer.getTheta()) >0){
		  loop=false;
	  }
	  if(isObstacleThereR() || isObstacleThereL()){			  
			  robot.turnToImmediate(-90);
			  robot.moveForwardBy(15);
			  if(leftUsDist()<Constants.OBSTACLE_DIST || rightUsDist() < Constants.OBSTACLE_DIST){
				  robot.turnToImmediate(180);
				  robot.moveForwardBy(60);
			  }
		  
		  else{
			  robot.moveForwardBy(15);
			  robot.turnTo(90);
			  robot.moveForwardBy(30);
		  }
	  }
	  loop=false;
  }
  
  
//  robot.turnToImmediate(65);//look for free path
//  rightSideDist = leftUsDist();
//  robot.turnToImmediate(-135);
//  leftSideDist = rightUsDist();
//  robot.turnToImmediate(65);
  
// if(rightSideDist>leftSideDist){ 
  
//  if(avoidanceRoutine() == 1){
//   moveToTheRight(); //avoid by moving to the right
//   while(loop){
//    loop = false;
//    while(newObstacleDetected||isObstacleStillThereR()) moveToTheRight();
//    moveForwardBy(60);
//    if(newObstacleDetected) loop = true;
//    newObstacleDetected = false;
//   }
//  }else if(avoidanceRoutine() == 2){
//   moveToTheLeft(); //avoid by moving to the left
//   while(loop){
//    loop = false;
//    while(newObstacleDetected||isObstacleStillThereL()) moveToTheLeft();
//    moveForwardBy(60);
//    if(newObstacleDetected) loop = true;
//    newObstacleDetected = false;
//   }
//  }    
 
 
 
 
 }
 
 private boolean isObstacleThereR(){//robot is to the right of the obstacle  
  leftSensor = leftUsDist();  
  if(leftSensor<Constants.OBSTACLE_DIST){
   return true;
  }
  return false;
 }
 
 private boolean isObstacleThereL(){// robot is to the left of the obstacle 
  rightSensor = rightUsDist();
  
  if(rightSensor<Constants.OBSTACLE_DIST){
   return true;
  }
  return false;
 }
 
 private double headingTo(int x, int y){//used to calculate theta to corner
	  double dx = x-odometer.getX();
	  double dy = y-odometer.getY();
	  double headingTo = 90- Math.toDegrees(Math.atan2(dy, dx));//
	  return fixDegAngle(headingTo); //angle between 0 and 360 degrees
	 } 
	 
	 //1 for avoiding by turning to the right and 2 for the left
	 private int avoidanceRoutine(){
	  
	  //turning to the right
	  double headingToTheRight = odometer.getTheta()+90;
	  if(headingToTheRight>360) headingToTheRight-=360;
	  
	  //turning to the left
	  double headingToTheLeft = odometer.getTheta()-90;
	  if(headingToTheLeft<0) headingToTheLeft+=360;
	  
	  //wall the robot is headed to by turning to the right
	  int wallR = wallpointedAt(headingToTheRight);
	  
	  //wall the robot is headed to by turning to the left
	  int wallL = wallpointedAt(headingToTheLeft);
	  
	  //
	  if(distanceToWall(wallR)>distanceToWall(wallL))
	   return 1;
	  else 
	   return 2;
	    
	 }
	 
	 //wall the robot is moving towards by turning to the left or right
	 //bottom wall is 1, left wall is 2, top wall is 3, right wall is 4
	 private int wallpointedAt(double heading){
	  //headings to corners
	  double bottomLeftAng = headingTo(-30,-30);
	  double bottomRightAng = headingTo(330,-30);
	  double topLeftAng = headingTo(-30,330);
	  double topRightAng = headingTo(330,330);
	  
	  //if the heading is between the two bottom corners, return wall 1 i.e. bottom wall
	  if((greaterThan(heading,bottomRightAng)||equalTo(heading,bottomRightAng)) && (lessThan(heading,bottomLeftAng)||equalTo(heading,bottomLeftAng))) 
	   return 1;
	  else if((greaterThan(heading,bottomLeftAng)||equalTo(heading,bottomLeftAng)) && (lessThan(heading,topLeftAng)||equalTo(heading,topLeftAng)))
	   return 2;
	  else if((greaterThan(heading,topLeftAng)||equalTo(heading,topLeftAng)) && (lessThan(heading,topRightAng)||equalTo(heading,topRightAng)))
	   return 3;
	  else if((greaterThan(heading,topRightAng)||equalTo(heading,topRightAng)) && (lessThan(heading,bottomRightAng)||equalTo(heading,bottomRightAng)))
	   return 4;
	  
	  return 5;//doesn't do anything
	 }
	 
	 private double distanceToWall(int wallNumber){
	  if (wallNumber == 1)
	   return odometer.getY()+30;
	  else if(wallNumber == 2)
	   return odometer.getX()+30;
	  else if(wallNumber == 3)
	   return 330 - odometer.getY();
	  else if(wallNumber == 4)
	   return 330 - odometer.getX();
	  
	  return 0;//not used for anything  
	 }
	 
	 //bottom wall is 1, left wall is 2, top wall is 3, right wall is 4
	 private int closeToWhichWall(){
	  if(odometer.getY()<0)
	   return 1;
	  else if(odometer.getX()<0)
	   return 2;
	  else if(odometer.getY()>300)
	   return 3;
	  else if(odometer.getX()>300)
	   return 4;
	  
	  return 0; //robot not close to any wall  
	 }
	 
	 private void moveAwayFromWall(int x, int y){//takes in right or left corner coordinates
	  robot.turnToFace(x, y);
	  robot.moveForwardBy(30);  
	 }
	   
	 //returns true if angle1 is to the right of angle2 in a half circle
	 private boolean greaterThan(double angle1, double angle2){
	  if(differenceBetweenTwoHeadings(angle1,angle2)>0) 
	   return true;
	  return false;  
	 }
	 
	 //returns true if angle1 is to the left of angle2
	 private boolean lessThan(double angle1, double angle2){
	  if(differenceBetweenTwoHeadings(angle1,angle2)<0) return true;
	  return false;
	  
	 }
	 
	 //returns true if angle1 = angle2
	 private boolean equalTo(double angle1,double angle2){
	  if(differenceBetweenTwoHeadings(angle1,angle2)==0) return true;
	  return false;
	 }
	 
	 private double differenceBetweenTwoHeadings(double angle1,double angle2){ //angle1-angle2 ->positive if angle1 is to the right of angle2
	  double dH = angle1 - angle2;  
	  if(Math.abs(dH) <= 180){
	   return dH;
	  }
	  else if(dH < -180){
	   dH += 360;
	  }
	  else if(dH > 180){
	   dH -=360;
	  }  
	  return dH;
	 }
	 
	 public static double fixDegAngle(double angle) {
	  if (angle < 0.0)
	   angle = 360.0 + (angle % 360.0);

	  return angle % 360.0;
	 }
	 
	 public static int leftUsDist(){ //filter code left out because it slows down detection
	  
	  // do a ping
	 usLeft.ping();   
	 leftUsDist = usLeft.getDistance();  
	 return leftUsDist;
	 }

	 public static int rightUsDist(){ 
	 
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
	 
	}