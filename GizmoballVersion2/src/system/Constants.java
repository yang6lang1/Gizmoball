package system;

import java.awt.Color;

import physics.Angle;

public class Constants {
	//General
	public static final int FRAMES_PER_SECOND = 40;
	public static final int FRAME_REDRAW= 1000/FRAMES_PER_SECOND;//ms

	//gridPanel:
	public static final Color colorOfGridLine = Color.orange;
	public enum SelectionType {
	    NONE, SELECTED, MOVESELETED, CONNECTSELETED	    
	}
	
	//Application window
	//The playing area must be at least 20 L wide by 20 L high
	public static final int WIDTH = 160;
	public static final int HEIGHT = 160;
	public static final int SCALE = 2;//scale factor for the window size
									// we can set based on our own need
	public static final int number_of_grids_per_dimension = 20;
	//The upper left corner is (0,0) and the lower right corner is (20,20)
	//Note: the Gizmos can be only placed between (0,0) and (19,19) :unit L
	public static final int L = WIDTH*SCALE/number_of_grids_per_dimension; //basic distance unit
	//Continually modify the velocity of the ball to account for the effects of gravity.
	public static final double GRAVITY_VALUE=25*L;
	//Continually modify the velocity of the ball to account for the effects of friction
	public static final double delta_t = FRAME_REDRAW  ; //s
	public static final double mu = 0.025; //unit 1/sec
	public static final double mu2 = 0.025/L;
	
	//Bouncing ball
	//Ball velocities must range at least from 0.01 L/sec to 200 L/sec
	//0 L/sec (stationary) must also be supported.***special case***
	public static final double SPEED = 20.0*L; //default speed
	public static final Angle ANGLE = new Angle(-1,-20); //default direction: upwards
	//public static final Angle ANGLE = new Angle(1,-20); //default direction: upwards
	public static final int RADIUS = L/4;//according to the requirement
	
	//Square bumper
	//coefficient of reflection
	public static final double SquareCOR =1.0;
	public static final Color colorOfSquareBumper = Color.blue;
	
	//triangular bumper
	public static final double TriangleCOR =1.0;
	public static final Color colorOfTriangularBumper = Color.green;
	
	//circular bumper
	public static final double CircleCOR =1.0;
	public static final Color colorOfCircularBumper = Color.yellow;
}
