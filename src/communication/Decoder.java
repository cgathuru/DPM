package communication;

import main.Constants;

/**
 * Parses the Transmission object into information the {@link Navigation} class can understand.
 * @author charles
 *
 */
public class Decoder {
	
	private Transmission transmission;
	/**
	 * The x-ordinate of the goals location
	 */
	public static int goalX;
	/**
	 * The y-ordinate of the goals location
	 */
	public static int goalY;
	
	/**
	 * The x-ordinate of the shooting target location
	 */
	public static int shootX;
	
	/**
	 * The y-ordinate of the shooting target location
	 */
	public static int shootY;
	
	/**
	 * The x-ordinate of the defence target location
	 */
	public static int defenceX;
	
	/**
	 * The y-ordinate of the defence target location
	 */
	public static int defenceY;
	
	/**
	 * The x-ordinate of the ball dispensers location
	 */
	public static int dispenserX;
	
	/**
	 * The y-ordinate of the ball dispensers location
	 */
	public static int dispenserY;
	
	/**
	 * The starting corner of the robot
	 */
	public static StartCorner startCorner;
	
	/**
	 * Initializes the {@code Decoder} for parsing the  {@code Transmission}
	 * @param transmission The {@link Transmission} object for decoding
	 */
	public Decoder(Transmission transmission){
		this.transmission = transmission;
	}

	/**
	 * Translates the data in the {@link Transmission} into usable information
	 */
	public void decodeTranmission(){
		goalX = Constants.GOALX * Constants.TILE_DISTANCE;
		goalY = Constants.GOALY * Constants.TILE_DISTANCE;
		processAttack();
		processDefence();
		dispenserX = transmission.bx * Constants.TILE_DISTANCE;
		dispenserY = transmission.by * Constants.TILE_DISTANCE;
		startCorner = transmission.startingCorner;
		
	}

	/**
	 * Sets the x and y coordinates for the offensive position. This method should only be called after the {@code decodeTransmission}
	 * method has been called
	 */
	public void processAttack() {
		shootX = goalX;
		shootY = Constants.TILE_DISTANCE*determineYTarget(goalX/Constants.TILE_DISTANCE, goalY/Constants.TILE_DISTANCE);
	}
	
	/**
	 * Sets the x and y coordinates for the defensive position. This method should only be called after the {@code decodeTransmission}
	 * method has been called
	 */
	public void processDefence(){
		defenceX = goalX;
		defenceY = goalY - 2*Constants.TILE_DISTANCE;
	}
	
	/**
	 * Determines the y ordinate of the robots shooting location
	 * @param goalX The x-ordinate of the goal
	 * @param goalY The y-ordinate of the goal
	 * @return The y-ordinate need in order to shoot accurately
	 */
	public static int determineYTarget(int goalX, int goalY){
		boolean [][] playingField = Constants.PLAYING_FEILD;
		playingField[goalX-1][goalY-1] = true;
		try{
			playingField[goalX-1][goalY -9] = true;
			return goalY - 7;
		}
		catch(ArrayIndexOutOfBoundsException ex){
			if((goalY + 7) > 11){
				return 11;
			}
			return  goalY + 7;
		}
	}
}
