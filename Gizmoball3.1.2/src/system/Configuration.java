package system;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.util.ArrayList;

import physics.Angle;

/*This class stores all the game configuration information
 * */
public class Configuration {
	//System constants:(default values)
	public final static String DEFAULT_GAME_SCHEMA ="src/system/gb_level.xsd";		
	public final static String DEFAULT_GAME_CONFIG ="Save/leftFlipperTest.xml";
	public final static int FRAMES_PER_SECOND =60;											//fps
	public final static int FRAME_REDRAW= (1000/FRAMES_PER_SECOND);				//ms
	//gridPanel:
	public final static Color colorOfGridLine = Color.orange; 					//grid line color
	//Application window
	//The playing area must be at least 20 L wide by 20 L high
	public final static int WIDTH = 160;																//game board width
	public final static int HEIGHT = 160;																//game board height
	public final static int SCALE = 2;																	//window size scale factor
	public final static int number_of_grids_per_dimension = 20;
	public final static int L = WIDTH*SCALE/number_of_grids_per_dimension;		//basic distance unit
	public final static double delta_t = FRAME_REDRAW  ; 								//ms
	public final static int RADIUS = L/4;																//radius of the bouncing ball
	//coefficient of reflection
	public final static double SquareCOR =1.0;
	public final static double TriangleCOR =1.0;
	public final static double CircleCOR =1.0;
	public final static double LF_COR = 0.95;
	public final static double RF_COR = 0.95;
	//colors
	public final static Color colorOfTriggeredGizmo = Color.white;
	public final static Color colorOfSquareBumper = Color.blue;
	public final static Color colorOfBall =Color.cyan;
	public final static Color colorOfTriangularBumper = Color.green;
	public final static Color colorOfCircularBumper = Color.yellow;
	public final static Color colorOfLeftFlipper = Color.red;
	public final static Color colorOfRightFlipper = Color.red;
	public final static Color colorOfAbsorber = new Color(153,51,255);	//RGB: 153,51,255 ££9933ff
	public final static double angularSpeed = 3*Math.PI;								//angular velocity of left and right flippers
	public final static double DEFAULT_GRAVITY_VALUE=25*L;									
	public final static double DEFAULT_mu = 0.025; 											//unit 1/sec								
	public final static double DEFAULT_mu2 = 0.025/L;
	
	
	//Mutable system constants:(input values)
	public static String loadedConfig = DEFAULT_GAME_CONFIG;

	//The gizmos to be put onto the game board 
	private ArrayList<gizmosInterface> gizmos;
	
	//Constructor
	public Configuration(){
		gizmos =new ArrayList<gizmosInterface>(number_of_grids_per_dimension*number_of_grids_per_dimension);
	}
	
	public void setGizmos(ArrayList<gizmosInterface> gizmos){
		this.gizmos = gizmos;
	}
	
	public ArrayList<gizmosInterface> getGizmos(){
		return this.gizmos;
	}
	
	public void addGizmos(gizmosInterface gizmo){
		try{
			gizmos.add(gizmo);
		}catch(ArrayIndexOutOfBoundsException e){
		//TODO: What if screen is full
		}
	}

	private double GRAVITY_VALUE=DEFAULT_GRAVITY_VALUE;								//gravity constant
	private double mu = DEFAULT_mu; //unit 1/sec											//friction constants
	private double mu2 = DEFAULT_mu2;
	
	public double getGravity(){
		return GRAVITY_VALUE;
	}
	public void setGravity(double gravity){
		GRAVITY_VALUE = gravity;
	}
	
	public double getMU(){
		return mu;
	}
	public void setMU(double newMU){
		mu = newMU;
	}
	
	public double getMU2(){
		return mu2;
	}
	public void setMU2(double newMU2){
		mu2 = newMU2;
	}
	
	//ball
	//Ball velocities must range at least from 0.01 L/sec to 200 L/sec
	//0 L/sec (stationary) must also be supported.***special case***
	private int originalX = 18*L;														//original x position of the ball
	private int originalY = 18*L;														//original y position of the ball
	private double SPEED =100.0*L; 													//initial speed of the ball
	private Angle ANGLE = Angle.DEG_270; 										//initial direction of the velocity
	private String nameOfBall = new String("B"+originalX+"_"+originalY);
	//public static final Angle ANGLE = new Angle(1,-20);		//initial direction of the velocity
	
	public int getBallX(){
		return this.originalX;
	}
	public void setBallX(int newX){
		this.originalX = newX;
	}
	
	public int getBallY(){
		return this.originalY;
	}
	public void setBallY(int newY){
		this.originalY = newY;
	}
	
	public double getBallSpeed(){
		return this.SPEED;
	}
	public void setBallSpeed(int speed){
		this.SPEED = speed;
	}
	
	public Angle getBallAngle(){														//get the ball's original angle of velocity
		return this.ANGLE;
	}
	public void setBallAngle(Angle angle){
		this.ANGLE = angle;
	}
	
	public String getBallName(){
		return this.nameOfBall;
	}
	public void setBallName(String name){
		this.nameOfBall = name;
	}
	
	public String toString(){
		String output = null;
		output="nameOfBall: " +this.nameOfBall 
				+", position: (" +this.originalX+","+this.originalY+")"
				+", speed: " +this.SPEED
				+", angle: " +this.ANGLE.toString()+"\n";
		for(int i=0;i<gizmos.size();i++){
			output+=gizmos.get(i).toString();
		}
	
		//output+= gizmos.size();
		
		return output;
	}

}
