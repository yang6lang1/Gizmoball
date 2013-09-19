package components;

import interfaces.flippersInterface;
import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import physics.Angle;
import physics.Circle;
import physics.Vect;
import system.Configuration;

public class rightFlipper extends JComponent implements gizmosInterface,flippersInterface{

	private static final long serialVersionUID =7L;
	private static final char TYPE = 'R';
	private static final int DEFAULT_ORIENTATION =0;
	private static final Angle DEFAULT_ANGLE =Angle.DEG_90;
	private static final double DEFAULT_ANGULAR_SPEED = Configuration.angularSpeed;
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;
    private static final int edgeLength = 2* Configuration.L;
    private static final int thickness = (int)(0.5*Configuration.L);
	private static final double delta_t = Configuration.delta_t/1000;
    
    private int x;
    private int y;
	private int orientation;
	private Angle angle= DEFAULT_ANGLE;
    private double RF_COR = Configuration.RF_COR;//coefficient of reflection
    private boolean trigger = false; 		//this determines if the flipper is triggered by other gizmos
    private boolean keyPressed = false;     //this determines if the flipper bound key is pressed
    private char type;
    private int resetTime = 10;//ms
    private Color color;
    private String name;
    private int delay=1;
    private double angularSpeed;//this angularspeed has no direction
    												//the direction if angularVelocity is counterclockwise
    
    public rightFlipper(){
    	this(ORIGINAL_X,ORIGINAL_Y,DEFAULT_ORIENTATION);
    }
    
    public rightFlipper(int x, int y,int orientation){
    	this.x = x;
    	this.y = y;
    	this.name = new String("R"+x/Configuration.L+"_"+y/Configuration.L);
    	this.orientation =orientation;
    	this.type = TYPE;
    	this.trigger = false;//TODO: set to be true for testing purpose
    	this.color = Configuration.colorOfRightFlipper;
    	//this.angle = angle;
    	this.angularSpeed =DEFAULT_ANGULAR_SPEED;
    }
    
    public int getEdge(){
    	return edgeLength;
    }
    
    public int getThickness(){
    	return thickness;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    
    public String getName(){
    	return name;
    }
    
    public int getOrientation(){
    	return orientation;
    }
    
    public double getRFCOR(){
    	return this.RF_COR;
    }
 
    public char getType(){
    	return this.type;
    }
    
    public boolean isTriggered(){
    	return trigger;
    }
    
    public Angle getAngle(){
    	return this.angle;
    }
    
    public double getAngularSpeed(){
    	return this.angularSpeed;
    }
    
    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.name = new String("L"+x/Configuration.L+"_"+y/Configuration.L);
    }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public void setOrientation(int theOrientation){
    	if (theOrientation ==0||theOrientation ==90||
    			theOrientation ==180||theOrientation ==270){
    		this.orientation = theOrientation;
    	}
    	
    }
    
    public void setRFCOR(double RF_COR){
    	this.RF_COR = RF_COR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    }
    
    public void setAngle(Angle angle){
    	this.angle = angle;
    }
    
    /*Note: angularSpeed should be in radians
     * */
    public void setAngularSpeed(double angularSpeed){
    	this.angularSpeed = angularSpeed;
    }
    
    public void move(){
    	//System.out.println(this.delay);   
		if(delay <=0){

			this.angle=new Angle(this.angle.radians()+angularSpeed*delta_t);
			if(this.angle.compareTo(Angle.DEG_90)<=0){
				this.angle = Angle.DEG_90;
				this.angularSpeed = 0;
				delay = (int)(250/Configuration.delta_t);
			}
			
			if(this.angle.compareTo(Angle.DEG_180)>=0){
				this.angle = Angle.DEG_180;
				this.angularSpeed = 0;
				delay = (int)(250/Configuration.delta_t);
			}
		}else{
			if(this.angle.compareTo(Angle.DEG_90)<=0){
				this.angle = Angle.DEG_90;
				this.angularSpeed = DEFAULT_ANGULAR_SPEED;
				delay--;
			}
			
			if(this.angle.compareTo(Angle.DEG_180)>=0){
				this.angle = Angle.DEG_180;
				this.angularSpeed =-DEFAULT_ANGULAR_SPEED;
				delay--;
			}	
		}
    }


    public void repaint(){
    	this.repaint(x, y, this.getEdge(), this.getEdge());
    }

    public void paintComponents(Graphics g){
    	
        int xPoints=0,yPoints=0;
		Graphics2D g2d = (Graphics2D)g;

		switch (this.orientation){
		case 0:
			xPoints=x+this.getEdge()-this.getThickness()-1; 
			yPoints=y  									;         						  
			g2d.rotate(angle.radians()-Angle.DEG_90.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;
		case 90:
			xPoints=x+this.getEdge()-this.getThickness();									 
			yPoints=y+this.getEdge()-this.getThickness();	   						 
			g2d.rotate(angle.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;
		case 180:
			xPoints=x									;
			yPoints=y+this.getEdge()-this.getThickness();
			g2d.rotate(angle.radians()+Angle.DEG_90.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;	
		case 270:
			xPoints=x;									 
			yPoints=y;
			g2d.rotate(angle.radians()+Angle.DEG_180.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;
		default:
			break;
		}
	    	
	        g2d.setColor(this.color);
	        g2d.fillRoundRect(xPoints, yPoints, this.getThickness(), this.getEdge()-1, Configuration.L/2, Configuration.L/2);
	        g2d.setColor(this.color);
	        g2d.drawRoundRect(xPoints, yPoints, this.getThickness(), this.getEdge()-1, Configuration.L/2, Configuration.L/2);
	        
    	}

	@Override
	public void select() {
		if(this.color == Configuration.colorOfRightFlipper){
			this.setColor(Color.white);
		}else{
			this.setColor(Configuration.colorOfRightFlipper);
		}
	}

	private void setColor(Color color) {
		this.color = color;
		
	}
	
    public Rectangle boundingBox() {

        // a Rectangle is the x,y for the upper left corner and then the
        // width and height
        return new Rectangle(x, y, this.getEdge(),this.getEdge());
    }

	@Override
	public void rotate() {
		if(this.orientation==0||this.orientation ==90||this.orientation==180){
			this.setOrientation(this.getOrientation() +90);
		}else{
			this.setOrientation(0);
		}
		
	}

	@Override
	public void originalState() {

		if(this.angle.compareTo(Angle.DEG_90)!=0){
			this.angularSpeed =-DEFAULT_ANGULAR_SPEED;
			this.angle=new Angle(this.angle.radians()+angularSpeed*delta_t);	

			if(this.angle.compareTo(Angle.DEG_90)<=0){
				this.angle = Angle.DEG_90;
				this.angularSpeed=0;
			}
			
			if(this.angle.compareTo(Angle.DEG_180)>=0){
				this.angle = Angle.DEG_180;
			}
		}
	}
	
	@Override
	public void moveUp() {

		if(this.angle.compareTo(Angle.DEG_180)!=0){
			this.angularSpeed =DEFAULT_ANGULAR_SPEED;
			this.angle=new Angle(this.angle.radians()+angularSpeed*delta_t);	

			if(this.angle.compareTo(Angle.DEG_90)<=0){
				this.angle = Angle.DEG_90;
			}
			
			if(this.angle.compareTo(Angle.DEG_180)>=0){
				this.angle = Angle.DEG_180;
				this.angularSpeed =0;
			}
		}

	}

	@Override
	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide) {
		// TODO Auto-generated method stub
		return velocity;
	}

	@Override
	public double[] timeUntilNextCollision(Circle ball, Vect velocity) {
		// TODO Auto-generated method stub
		double[] output = {Configuration.delta_t+1,0,0};
		return output;
	}

	@Override
	public boolean isKeyPressed() {

		return this.keyPressed;
	}

	@Override
	public void setKeyPressed(boolean keyPressed) {
		this.keyPressed = keyPressed;
		
	}

	public String toString(){
		String output =null;
		output="rightFlipper[name: "+this.name
				+", position:("+this.x+","+this.y
				+"), orientation: "+this.orientation+"]\n";
		return output;
	}


}
