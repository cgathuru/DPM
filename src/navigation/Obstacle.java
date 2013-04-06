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
 
 public Obstacle(UltrasonicSensor usRight, UltrasonicSensor usLeft, Odometer odometer, TwoWheeledRobot robot){  
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
  

  
//  if(avoidanceRoutine() == 1){
//   moveToTheRight(); //avoid by moving to the right
//   while(loop){
//    loop = false;
//    while(newObstacleDetected/*||isObstacleStillThereR()*/){
//     moveToTheRight();
//     if (isHeadedToWall()){//if robot is close to wall stop avoiding 
//      moveParallelToWall(targetX,targetY);
//      return;
//     }
//    }
//    moveForwardBy(Constants.FORWARD_DISTANCE);
//    if (isHeadedToWall()){//if robot is close to wall stop avoiding 
//     moveParallelToWall(targetX,targetY);
//     return;
//    }
//    
//    if(newObstacleDetected) loop = true;
//    newObstacleDetected = false;
//   }
//  }else if(avoidanceRoutine() == 2){
//   moveToTheLeft(); //avoid by moving to the left
//   while(loop){
//    loop = false;
//    while(newObstacleDetected/*||isObstacleStillThereL()*/){
//     moveToTheLeft();
//     if (isHeadedToWall()){//if robot is close to wall stop avoiding 
//      moveParallelToWall(targetX,targetY);
//      return;
//     }
//    }
//    moveForwardBy(Constants.FORWARD_DISTANCE);
//    if (isHeadedToWall()){//if robot is close to wall stop avoiding 
//     moveParallelToWall(targetX,targetY);
//     return;
//    }
//    if(newObstacleDetected) loop = true;
//    newObstacleDetected = false;
//   }
//  }    
// }
  
  if(avoidanceRoutine() == 1){
   newObstacleDetected = true;
   
   while(newObstacleDetected) {
    moveToTheRight(Constants.SIDE_DISTANCE);
    
    while(!newObstacleDetected && isObstacleStillThereR()){
     moveToTheRight(15);     
    }
    
    moveForwardBy(Constants.FORWARD_DISTANCE);
    if (isHeadedToWall()){//if robot is close to wall stop avoiding 
     moveParallelToWall(targetX,targetY);
     return;
    }
   }
   
   
  }else if(avoidanceRoutine() == 2){
   newObstacleDetected = true;
   
   while(newObstacleDetected) {
    moveToTheLeft(Constants.SIDE_DISTANCE); 
    
    while(!newObstacleDetected && isObstacleStillThereL()){
     moveToTheLeft(15);     
    }
    
    moveForwardBy(Constants.FORWARD_DISTANCE);
    if (isHeadedToWall()){//if robot is close to wall stop avoiding 
     moveParallelToWall(targetX,targetY);
     return;
    }
   }
  } 
      
 }
 
 private boolean isObstacleStillThereR(){ //robot is to the right of the obstacle  
  robot.turnToImmediate(-Constants.US_ANGLE);
  leftSensor = leftUsDist();
  robot.turnToImmediate(Constants.US_ANGLE);
  
  if(leftSensor<Constants.OBSTACLE_NOT_THERE_DISTANCE){
   return true;
  }
  return false;
 }
 
 private boolean isObstacleStillThereL(){ // robot is to the left of the obstacle 
  robot.turnToImmediate(Constants.US_ANGLE);
  rightSensor = rightUsDist();
  robot.turnToImmediate(-Constants.US_ANGLE);  
  
  if(rightSensor<Constants.OBSTACLE_NOT_THERE_DISTANCE){
   return true;
  }
  return false;
 }
 
 private void moveToTheRight(int distance ){//avoid by moving to the right
  robot.turnToImmediate(90);
  moveForwardBy(distance);
  if(newObstacleDetected) return;
  robot.turnToImmediate(-90); 
 } 
 
 private void moveToTheLeft(int distance){//avoid by moving to the left
  robot.turnToImmediate(-90);
  moveForwardBy(distance);
  if(newObstacleDetected) return;
  robot.turnToImmediate(90);  
 }
 
 public void moveForwardBy(double distance){ 
  newObstacleDetected = false;
  robot.moveForwardBy(distance,true);
  NXTRegulatedMotor rightMotor = robot.getRightMotor();
  
  while(rightMotor.isMoving()){ //code to stop the motors when an obstacle is detected
   
    if(leftUsDist()<Constants.OBSTACLE_DISTANCE_WHILE_AVOIDING||rightUsDist()<Constants.OBSTACLE_DISTANCE_WHILE_AVOIDING){
     newObstacleDetected  = true;
      return;
    }
  }  
 } 
 
 private double headingTo(int x, int y){//used to calculate heading to corner
  double dx = x-odometer.getX();
  double dy = y-odometer.getY();
  double headingTo = 90- Math.toDegrees(Math.atan2(dy, dx));//
  return fixDegAngle(headingTo); //angle between 0 and 360 degrees
 } 
 
 //1 for avoiding by turning to the right and 2 for the left
 private int avoidanceRoutine(){
  
  //turning right
  double headingToTheRight = odometer.getTheta()+90;
  if(headingToTheRight>360) headingToTheRight-=360;
  
  //turning left
  double headingToTheLeft = odometer.getTheta()-90;
  if(headingToTheLeft<0) headingToTheLeft+=360;
  
  //wall the robot is headed to by turning right
  int wallR = wallpointedAt(headingToTheRight);
  
  //wall the robot is headed to by turning left
  int wallL = wallpointedAt(headingToTheLeft);
  
  //return the routine that moves the robot away from the closest wall
  if(distanceToWall(wallR)>distanceToWall(wallL))
   return 1;
  else 
   return 2;
    
 }
 
 //wall the robot is moving towards by turning left or right
 //bottom wall is 1, left wall is 2, top wall is 3, right wall is 4
 private int wallpointedAt(double heading){
  //headings to corners
  double bottomLeftAng = headingTo(Constants.BOTTOM_LEFT_X,Constants.BOTTOM_LEFT_Y);
  double bottomRightAng = headingTo(Constants.BOTTOM_RIGHT_X,Constants.BOTTOM_RIGHT_Y);
  double topLeftAng = headingTo(Constants.TOP_LEFT_X,Constants.TOP_LEFT_Y);
  double topRightAng = headingTo(Constants.TOP_RIGHT_X,Constants.TOP_RIGHT_Y);
  
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
  
  return 0;//if wall number is not valid  
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
 
 //returns true if the robot is close to a wall
 private boolean isCloseToWall(){
  if((odometer.getY()<0)||(odometer.getX()<0)||(odometer.getY()>300)||(odometer.getX()>300))
   return true;
  return false;   
 }
 
 //returns true if the robot is headed to a wall
 private boolean isHeadedToWall(){
  if (closeToWhichWall()==1 && (odometer.getTheta()>90 && odometer.getTheta()<270)) //headed to bottom wall
   return true;
  else if(closeToWhichWall()==2 && (odometer.getTheta()>180 && odometer.getTheta()<360)) //headed to left wall
   return true;
  else if(closeToWhichWall()==3 && ( (odometer.getTheta()>270 && odometer.getTheta()<=360) || (odometer.getTheta()>=0 && odometer.getTheta()<90))) //headed to top wall
    return true;
  else if(closeToWhichWall()==4 && (odometer.getTheta()>0 && odometer.getTheta()<180))
   return true;
  
  return false;  
 } 
 
 private int isHeadedToWhichWall(){
  if (closeToWhichWall()==1 && (odometer.getTheta()>90 && odometer.getTheta()<270)) //headed to bottom wall
   return 1;
  else if(closeToWhichWall()==2 && (odometer.getTheta()>180 && odometer.getTheta()<360)) //headed to left wall
   return 2;
  else if(closeToWhichWall()==3 && ( (odometer.getTheta()>270 && odometer.getTheta()<=360) || (odometer.getTheta()>=0 && odometer.getTheta()<90))) //headed to top wall
    return 3;
  else if(closeToWhichWall()==4 && (odometer.getTheta()>0 && odometer.getTheta()<180))
   return 4;
  
  return 0; //not headed to wall  
 }
 
 //moves the robot parallel to the wall
 private void moveParallelToWall(int xTarget,int yTarget){
  
  if(isHeadedToWhichWall() == 1){
   if(odometer.getX()>xTarget){ //move to the left
   robot.turnToFace(0,0);
  
   }else{
    robot.turnToFace(300, 0);  
   }
  }else if(isHeadedToWhichWall() == 2){
   if(odometer.getY()>yTarget){ //
   robot.turnToFace(0,0);
   
   }else{
    robot.turnToFace(0, 300);  
   }
  }
  else if(isHeadedToWhichWall() == 3){
   if(odometer.getX()>xTarget){
    robot.turnToFace(0, 300);
 
   }else{
    robot.turnToFace(300, 300); 
   }
   
  }else if(isHeadedToWhichWall() == 4){
   if(odometer.getY()>yTarget){
    robot.turnToFace(300, 0);
 
   }else{
    robot.turnToFace(300, 300);  
   }
   
  }
  moveForwardBy(30);
  
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
 
}