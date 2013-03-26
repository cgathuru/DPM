package navigation;

/*
 * Author: Madeleine Incutiyayezu
 * March 15, 2013
 */

import robot.Odometer;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import main.Constants;


/**
 * 
 * @author Madeleine Incutiyayezu
 *
 */


public class Obstacle {
 private static int leftUsDist;
 private static int rightUsDist;  
 private static UltrasonicSensor usLeft, usRight; 
 private Odometer odometer; 
 private boolean travelingTowardsTarget;
 private boolean check;
 private boolean isPathClear;

 

 public Obstacle(UltrasonicSensor usLeft, UltrasonicSensor usRight, Odometer odometer){  
  this.usLeft = usLeft;
  this.usRight = usRight; 
  this.odometer = odometer;
 }
  
 
 public void obManager(int targetX, int targetY){
  
  isPathClear = true;
  travelingTowardsTarget = true;
  check = false; //to check if an obstacle was detected while avoiding
  
  while((filteredLeftDist()<Constants.OBSTACLE_DIST+2 || filteredRightDist()<Constants.OBSTACLE_DIST+2)&&!check){ 
   //true whenever either of us distances < obstacleDist+2, loop continues until robot is clear of obstacle
   
   while(filteredLeftDist()<Constants.OBSTACLE_DIST||filteredRightDist()<Constants.OBSTACLE_DIST){ //back up when the robot is too close to an obstacle
    Motor.A.setSpeed(Constants.FORWARD_SPEED); //
    Motor.B.setSpeed(Constants.FORWARD_SPEED); // 
    Motor.A.backward();
    Motor.B.backward();
   }
   
   Motor.A.setSpeed(0); //stop the motors
   Motor.B.setSpeed(0); // 
   
   if(travelingTowardsTarget){
    Sound.beepSequence();
   }
   if(!travelingTowardsTarget){
    Sound.twoBeeps();
   }

   
   if(System.currentTimeMillis()-Navigation.endRight<Constants.AVOIDANCE_ROUTINE_EXPIRATION){
    if(travelingTowardsTarget){
     processUsRight();
    }
    else{
     processUsLeft();
    }
    
    if(!isPathClear){ //exit if no clear path was found
     Navigation.endRight=System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;
     Navigation.endLeft = System.currentTimeMillis();
     return;
    }
    
    Navigation.endRight=System.currentTimeMillis();    
   }
   else if(System.currentTimeMillis()-Navigation.endLeft<Constants.AVOIDANCE_ROUTINE_EXPIRATION){
    if(travelingTowardsTarget){
     processUsLeft();
    }
    else{
     processUsRight();
    }
    
    if(!isPathClear){ //exit if no clear path was found
     Navigation.endLeft = System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;
     Navigation.endRight=System.currentTimeMillis(); 
     return;
    }    
    Navigation.endLeft = System.currentTimeMillis();
   }
   else{
    if(filteredLeftDist()<filteredRightDist()){
     processUsLeft();
     
     if(!isPathClear){ //exit if no clear path was found
      Navigation.endLeft = System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;
      Navigation.endRight=System.currentTimeMillis(); 
      return;
     }
     
     Navigation.endLeft = System.currentTimeMillis();
    }
    else{
     processUsRight();
     
     if(!isPathClear){ //exit if no clear path was found
      Navigation.endRight = System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;
      Navigation.endLeft = System.currentTimeMillis();
      return;
     }
     
     Navigation.endRight = System.currentTimeMillis();
    }
   }  
  } 
 } 

 //code executes when left us sensor detects an obstacle 
 public void processUsLeft(){  
  
  long start1 = System.currentTimeMillis(),end1 = System.currentTimeMillis(); //store start and end times  
  
  while((filteredLeftDist()<Constants.CLEARPATH_DIST)&&(end1-start1<=Constants.MAX_TURN_TIME)){  //turn until the path is clear
   
   Motor.A.setSpeed(0); //
   Motor.B.setSpeed(Constants.FORWARD_SPEED); // 
   Motor.A.backward();
   Motor.B.backward();   
   end1 = System.currentTimeMillis(); //update end time    
  } 
  
  if(filteredLeftDist()<Constants.NO_PATH_DIST){//if the path is not clear, exit routine
   isPathClear=false;
   return;
  }
    
  while(end1-start1<Constants.MIN_TURN_TIME){ //when done turning, turn a little bit more   
   Motor.A.setSpeed(0); //
   Motor.B.setSpeed(Constants.FORWARD_SPEED); // 
   Motor.B.backward();
   Motor.A.backward();
   end1 = System.currentTimeMillis(); //update end time    
  }
  
  
  long start2 = System.currentTimeMillis(),end2 = System.currentTimeMillis(); //store start and end times
  //specify minimum forward time
  while(end2-start2<2.5*(end1-start1) && end2-start2<Constants.MAX_FORWARD_TIME){ //robot drives straight, time is proportional to the time it took to turn away from obstacle
   Motor.A.setSpeed(Constants.FORWARD_SPEED); 
   Motor.B.setSpeed(Constants.FORWARD_SPEED); 
   Motor.A.forward();
   Motor.B.forward();
   
   if(filteredLeftDist()<Constants.OBSTACLE_DIST2||filteredRightDist()<Constants.OBSTACLE_DIST2){ //exit if new obstacles are detected
    travelingTowardsTarget = false;    
    check =  true;
    break;    
   }    
   end2 = System.currentTimeMillis(); //update end time 
  }
  travelingTowardsTarget = false;  
 }
 
 //code executes when right us sensor detects an obstacle
 public void processUsRight(){
  
  long start1 = System.currentTimeMillis(),end1 = System.currentTimeMillis(); //store start and end times  
  
  while((filteredRightDist()<Constants.CLEARPATH_DIST)&&(end1-start1<=Constants.MAX_TURN_TIME)){ //turn until path is clear
  
   Motor.B.setSpeed(0); //
   Motor.A.setSpeed(Constants.FORWARD_SPEED); // 
   Motor.B.backward();
   Motor.A.backward();
   end1 = System.currentTimeMillis(); //update end time    
  }
  
  if(filteredRightDist()<Constants.NO_PATH_DIST){//if the path is not clear, exit routine
   isPathClear=false;
   return;
  }
  
  while(end1-start1<Constants.MIN_TURN_TIME){ //when done turning, turn a little bit more   
   Motor.B.setSpeed(0); //
   Motor.A.setSpeed(Constants.FORWARD_SPEED); // 
   Motor.B.backward();
   Motor.A.backward();
   end1 = System.currentTimeMillis(); //update end time    
  }
  
  //if path not clear, exit and call the other routine
  
  long start2 = System.currentTimeMillis(),end2 = System.currentTimeMillis(); //store start and end times
  //specify minimum forward time
  while(end2-start2<2.5*(end1-start1) && end2-start2<Constants.MAX_FORWARD_TIME){ //robot drives straight
   
   Motor.A.setSpeed(Constants.FORWARD_SPEED); 
   Motor.B.setSpeed(Constants.FORWARD_SPEED); 
   Motor.A.forward();
   Motor.B.forward();
   
   if(filteredLeftDist()<Constants.OBSTACLE_DIST2||filteredRightDist()<Constants.OBSTACLE_DIST2){ //exit if new obstacles detected
    travelingTowardsTarget = false;      
    check = true;
    break;
   }
   
   end2 = System.currentTimeMillis(); //store the second end time
  }
  travelingTowardsTarget = false;
 }
 
 public static int filteredLeftDist(){ //filter code slows down detection, so commented out
  
   // do a ping
  usLeft.ping();   
  leftUsDist = usLeft.getDistance();  
  return leftUsDist;
 }
 
 public static int filteredRightDist(){ 
  
   // do a ping
  usRight.ping();   
  rightUsDist = usRight.getDistance();  
  return rightUsDist;
 } 
 
  
}
