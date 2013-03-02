package navigaion;

import odometer.Odometer;
import sensors.LightDetection;
import sensors.USDetection;

public class Localization {
	
	public enum Corner{TOP_LEFT,TOP_RIGHT,BOTTOM_LEFT,BOTTOM_RIGHT};
	public enum Heading{NORTH,SOUTH,EAST,WEST};
	
	public Heading heading = Heading.EAST;
	private Odometer odometer;
	private LightDetection lightDetection;
	private USDetection usDetection;
	
	public Localization(Odometer odometer, LightDetection lightDetection, USDetection usDetection){
		this.odometer = odometer;
		this.lightDetection = lightDetection;
		this.usDetection = usDetection;
	}
	
	public void localize(){
		new Thread(usDetection).start();
		lightDetection.doLocalization();
		
	}
	
	public void setHeading(){
		switch(heading){
		case NORTH:
			odometer.setPosition(new double[]{0, 0,0}, new boolean[]{false,false,true});
			break;
		case SOUTH:
			odometer.setPosition(new double[]{0, 0,180}, new boolean[]{false,false,true});
			break;
		case EAST:
			odometer.setPosition(new double[]{0, 0,90}, new boolean[]{false,false,true});
			break;
		case WEST:
			odometer.setPosition(new double[]{0, 0,270}, new boolean[]{false,false,true});
			break;
		}
	}

}
