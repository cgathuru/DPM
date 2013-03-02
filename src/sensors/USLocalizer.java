package sensors;

import navigaion.Navigation;
import odometer.Odometer;
import odometer.TwoWheeledRobot;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	

	private final double DIST_TOL= 70;
	
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 40;

	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	private Navigation nav;
	public double angleA, angle1 = 0;
	public double angleB, angle2 =0;
	
	public USLocalizer(Odometer odo, UltrasonicSensor us, LocalizationType locType) {
		
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.nav = odo.getNavigation();
		this.us = us;
		this.locType = locType;
		us.off();
	}
	
	public void doLocalization() {

		this.angleA = 0;
		this.angleB = 0;
		double delta = 0;
		int distance;
		boolean polling = true;
		
		if (locType == LocalizationType.FALLING_EDGE) {
			
			// rotate the robot until it sees no wall
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4);
				robot.setRotationSpeed(ROTATION_SPEED);
				if (distance > DIST_TOL){
					polling = false;
				}
			}
			Sound.beep();
			try { Thread.sleep(500); } catch (InterruptedException e) {}
					
			// keep rotating until the robot sees a wall, then latch the angle
			polling = true;
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4); 
				robot.setRotationSpeed(ROTATION_SPEED);
				if (distance < DIST_TOL){
					robot.setRotationSpeed(0);
					this.angleB = odo.getTheta();
					angle2 = angleB;
					polling = false;
				}	
			}
			
			Sound.beep();			
			try { Thread.sleep(500); } catch (InterruptedException e) {}
		
			
			// switch direction and wait until it sees no wall
			polling = true;
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4);
				robot.setRotationSpeed(-ROTATION_SPEED);
				if (distance > DIST_TOL){
					polling = false;
				}
			}
			Sound.beep();
			try { Thread.sleep(500); } catch (InterruptedException e) {}
		
			
			// keep rotating until the robot sees a wall, then latch the angle
			polling = true;
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4);
				robot.setRotationSpeed(-ROTATION_SPEED);
				if (distance < DIST_TOL){
					robot.setRotationSpeed(0);
					this.angleA = odo.getTheta();
					polling = false;
				}	
			}
			
			
			if(angleA<angleB){
				
				delta = 45 - (angleA + angleB)/2;  //tweak the 45
				
			}
			else{
				
				delta = 225 - (angleA + angleB)/2;  //tweak the 225
			}
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, odo.getTheta()+delta}, new boolean [] {true, true, true});
			
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			nav.turnTo(0);
			
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			//turn until it sees a wall
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4);
				robot.setRotationSpeed(ROTATION_SPEED);
				if (distance < DIST_TOL){
					polling = false;
				}
			}
			
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			Sound.beep();
			
			polling = true;
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4);
				robot.setRotationSpeed(ROTATION_SPEED);
				if (distance > DIST_TOL){
					this.angleA = odo.getTheta();
					angle1 = angleA;
					robot.setRotationSpeed(0);
					polling = false;
				}
			}
			
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			Sound.beep();
			
			
			polling = true;
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4);
				robot.setRotationSpeed(-ROTATION_SPEED);
				if (distance < DIST_TOL){
					polling = false;
				}
			}
			
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			Sound.beep();
			
			polling = true;
			while (polling){
				distance = getFilteredData();
				LCD.drawInt(distance, 0, 4);
				robot.setRotationSpeed(-ROTATION_SPEED);
				if (distance > DIST_TOL){
					angleB = odo.getTheta();
					angle2 = angleB;
					robot.setRotationSpeed(0);
					polling = false;
				}
			}
				
			
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			Sound.beep();
		
			
			if(angleA < angleB){
				
				delta = 45 - (angleA + angleB)/2;  //tweak the 45
				
			}
			else{
				
				delta = 225 - (angleA + angleB)/2;  //tweak the 225
			}
			
			
				// angleA is clockwise from angleB, so assume the average of the
				// angles to the right of angleB is 45 degrees past 'north'
				// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, odo.getTheta()+delta}, new boolean [] {true, true, true});
			
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			
			nav.turnTo(0);
									
		}
		
	}
	
	private int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		
		// there will be a delay here
		distance = us.getDistance();
		
	    if (distance > 80){             
			distance = 80;
		}
							
		return distance;
	}
	
	public int getAngle1(){
		return (int)angleA;
	}
	
	public int getAngle2(){
		return (int)angleB;
	}
}
