package navigation;

/**
 * The strategy to be implemented by robot after localization. 
 * @author charles
 *
 */
public interface Strategy {

	/**
	 * The default method called to start the robot's strategy.
	 */
	public void start();
}
