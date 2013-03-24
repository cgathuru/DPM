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
	public void DecodeTranmission(){
		goalX = Constants.GOALX * Constants.TILE_DISTANCE;
		goalY = Constants.GOALY * Constants.TILE_DISTANCE;
		dispenserX = transmission.bx * Constants.TILE_DISTANCE;
		dispenserY = transmission.by * Constants.TILE_DISTANCE;
		startCorner = transmission.startingCorner;
		
	}
}
