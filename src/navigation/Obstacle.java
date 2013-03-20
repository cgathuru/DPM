package navigation;

/*
 * Author: Madeleine Incutiyayezu
 * March 15,2013
 */

import robot.Odometer;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

/**
 * 
 * @author Madeleine Incutiyayezu
 *
 */
public class Obstacle {
	private static int leftUsDist;
	private static int rightUsDist;
	private static int filterControl;
//	public static double obstacleDistance = 40;
	
	private static UltrasonicSensor usLeft, usRight;
	public static final int bandCenter=30, bandwith=2, motorLow=5, motorLow2 = 140,motorHigh=100,motorHigh2 =200,motorStraight=200,filterOut=20;

	public Obstacle(Odometer odo, UltrasonicSensor usLeft, UltrasonicSensor usRight){
		
		this.usLeft = usLeft;
		this.usRight = usRight;
		this.filterControl =0;
			
	}
		
	public void obManager(){
		//long start = System.currentTimeMillis(),end = System.currentTimeMillis(); //record start time
				
		while(filteredLeftDist()<bandCenter+2 || filteredRightDist()<bandCenter+2){ //true whenever either of us distances < bandCenter+5
																														//loop continues until robot is clear of obstacle
			
			while(filteredLeftDist()<25||filteredRightDist()<25){ //back up when robot is too close to obstacle
				Motor.A.setSpeed(200); //
				Motor.B.setSpeed(200); // 
				Motor.A.backward();
				Motor.B.backward();
	
			}	
			
		if(filteredLeftDist()<filteredRightDist()){ //obstacle detected by left us, robot has to turn right around the obstacle				
				Sound.beep();				
			
			long start1 = System.currentTimeMillis(),end1 = System.currentTimeMillis(); //store start and end times
			
			while(filteredLeftDist()<80||end1-start1<1000){  //turn until the path is clear, or for at least one second
				Motor.A.setSpeed(0); //
				Motor.B.setSpeed(200); // 
				Motor.A.backward();
				Motor.B.backward();
				
				end1 = System.currentTimeMillis(); //update end time				
			}	
			
			long start2 = System.currentTimeMillis(),end2 = System.currentTimeMillis(); //store start and end times
			while(end2-start2<2.5*(end1-start1)){ //robot makes a wide turn, takes three times the time it took to turn away from obstacle
				Motor.A.setSpeed(160); 
				Motor.B.setSpeed(200); 
				Motor.A.forward();
				Motor.B.forward();
				
				if(filteredLeftDist()<25||filteredRightDist()<25){ //exit if new obstacles are detected
					break;
				}				
				end2 = System.currentTimeMillis(); //update end time 
			}
			
		
		}else{ //obstacle detected by right us, robot has to turn left around the obstacle 
	
			Sound.buzz();
	
			long start1 = System.currentTimeMillis(),end1 = System.currentTimeMillis(); //store start and end times
		
			while(filteredRightDist()<80||end1-start1<1000 ){ //turn until path is clear
				Motor.B.setSpeed(0); //
				Motor.A.setSpeed(200); // 
				Motor.B.backward();
				Motor.A.backward();
				end1 = System.currentTimeMillis(); //update end time			
			}
			
			long start2 = System.currentTimeMillis(),end2 = System.currentTimeMillis(); //store start and end times
			
			while(end2-start2<2.5*(end1-start1)){ //robot makes a wide turn
				
				Motor.A.setSpeed(200); 
				Motor.B.setSpeed(160); 
				Motor.A.forward();
				Motor.B.forward();
				
				if(filteredLeftDist()<25||filteredRightDist()<25){ //exit if new obstacles detected
					break;
				}
				
				end2 = System.currentTimeMillis(); //store the second end time
			}
			
			}
			
		}
		
	}	

	
	public static int filteredLeftDist(){	//filter code slows down detection, so commented out
		//int i =0;
			// do a ping
		usLeft.ping();	
		
		leftUsDist = usLeft.getDistance();
		
//		while (leftUsDist==255 && i<20){ //filter out spurious 255 values
//			i++;
//			leftUsDist = usLeft.getDistance();
//		}
	
		
		return leftUsDist;
	}
	
	public static int filteredRightDist(){	
		//int i =0;
			// do a ping
		usRight.ping();		
		
		rightUsDist = usRight.getDistance();
		
//		while (rightUsDist == 255 && i<20){ //filter out spurious 255 values
//			i++;
//			rightUsDist = usRight.getDistance();
//		}

		return rightUsDist;
	}	
	
		
}
