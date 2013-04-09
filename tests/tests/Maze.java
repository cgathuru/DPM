package tests;

import main.Constants;
import robot.Odometer;

public class Maze {
	
	private boolean[][] maze;
	private Odometer odometer;

	public Maze(Odometer odometer){
		this.maze = new boolean[11][11];
		this.odometer= odometer;

	}
	
	public void removeIntersection(int distance){
		int xObstacle = translatePositionToIntersection(odometer.getX(), distance);
		int yObstacle = translatePositionToIntersection(odometer.getY(), distance);
		maze[xObstacle][yObstacle] = false;
	}

	public int translatePositionToIntersection(double ordinate, int distance){
		return (int)(ordinate/Constants.TILE_DISTANCE) + distance;
	}
	
	public boolean isDestinationTravellable(int xTarget, int yTarget){
		return maze[xTarget][yTarget];
	}
}
