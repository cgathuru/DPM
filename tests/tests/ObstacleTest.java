package tests;

import navigation.Obstacle;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import navigation.Navigation;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import sensors.USLocalizer;

import display.LCDInfo;

public class ObstacleTest {

 /**
  * @param args
  */
 public static void main(String[] args) {
  Button.waitForAnyPress();
  
  // setup the odometer, display, and ultrasonic and light sensors  
  Odometer odo = new Odometer(true);
  TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
  UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);// 
  UltrasonicSensor us2 = new UltrasonicSensor(SensorPort.S3);// 
  
  LightSensor lsLeft = new LightSensor(SensorPort.S1);
  LightSensor lsRight = new LightSensor(SensorPort.S2);
  LightSampler leftLight = new LightSampler(lsLeft);
  LightSampler rightLight = new LightSampler(lsRight); 
  Obstacle ob = new Obstacle(us,us2,odo,patBot);  
  OdometryCorrection odometryCorrection = new OdometryCorrection(patBot, leftLight, rightLight);
  Navigation nav = new Navigation(patBot,ob, odometryCorrection);

  
  new LCDInfo(odo);
  odometryCorrection.startCorrectionTimer();
 
  //patBot.moveForwardBy(60);
//   patBot.travelTo(0,180);
//   patBot.travelTo(0,0);
//  while(true){
  	nav.travelTo(0,150);   
 nav.travelTo(150, 0);
 nav.travelTo(150, 150);
 nav.travelTo(0,0);
// //patBot.turnTo(0);
// nav.travelTo2(180, 0);
// nav.travelTo2(0, 0);
//   nav.travelTo(0, 60);
//   nav.travelTo(0, 0);
   
    
   Button.waitForAnyPress();
  
 }}

