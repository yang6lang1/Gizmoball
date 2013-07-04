package system;

public class Constants {
	//General
	public static final int FRAMES_PER_SECOND = 100;

	//Application window
	public static final int WIDTH = 160;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;//scale factor for the window size
									// we can set based on our own need
	
	public static final int L = WIDTH*SCALE/20; //basic distance unit
	
	//Bouncing ball
	public static final double VELOCITY_STEP = 2.0; //2(pixel/10ms)
	public static final double GRAVITATIONAL_CONSTANT=1; //1 (pixel/10ms)
	public static final int RADIUS = L/4;//according to the requirement
}
