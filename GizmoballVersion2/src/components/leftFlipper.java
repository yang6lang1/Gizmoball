package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import physics.Angle;
import system.Constants;

public class leftFlipper extends JComponent implements gizmosInterface {

		private static final long serialVersionUID =4L;
		private static final char TYPE = 'L';
		private static final int DEFAULT_ORIENTATION =0;
		private static final Angle DEFAULT_ANGLE =Angle.ZERO;
		private static final int ORIGINAL_X = 0;
		private static final int ORIGINAL_Y = 0;
	    private static final int edgeLength = 2* Constants.L;
	    private static final int thickness = (int)(0.5*Constants.L);
	    
	    private int x;
	    private int y;
		private int orientation;
		private Angle angle;
	    private double LF_COR = Constants.LF_COR;//coefficient of reflection
	    private boolean trigger = false; 		//this determins if the flipper is moving or not
	    private char type;
	    private int resetTime = 10;//ms
	    private Color color;
	    private String name;
	    private double angularSpeed;//this angularspeed has no direction
	    												//the direction if angularVelocity is counterclockwise
	    
	    
	    public leftFlipper(){
	    	this(ORIGINAL_X,ORIGINAL_Y,DEFAULT_ORIENTATION,DEFAULT_ANGLE);
	    }
	    
	    public leftFlipper(int x, int y,int orientation,Angle angle){
	    	this.x = x;
	    	this.y = y;
	    	this.name = new String("L"+x/Constants.L+"_"+y/Constants.L);
	    	this.orientation =orientation;
	    	this.type = TYPE;
	    	this.trigger = true;//TODO: set to be true for testing purpose
	    	this.color = Constants.colorOfLeftFlipper;
	    	this.angle = angle;
	    	this.angularSpeed =Constants.angularSpeed;
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
	    
	    public double getLFCOR(){
	    	return this.LF_COR;
	    }
	 
	    public char getType(){
	    	return this.type;
	    }
	    
	    public boolean isTouched(){
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
	    	this.name = new String("L"+x/Constants.L+"_"+y/Constants.L);
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
	    
	    public void setLFCOR(double LF_COR){
	    	this.LF_COR = LF_COR;
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
	    
	    public void paintComponents(Graphics g){//TODO
	    	int[] xPoints = new int[2],yPoints=new int[2];
			switch (this.orientation){
			case 0:
				xPoints[0]=x; xPoints[1]=x;				   xPoints[2]=x+this.getEdge();
				yPoints[0]=y; yPoints[1]=y+this.getEdge(); yPoints[2]=y+this.getEdge();
				break;
			case 90:
				xPoints[0]=x+this.getEdge(); xPoints[1]=x; xPoints[2]=x;
				yPoints[0]=y;			     yPoints[1]=y; yPoints[2]=y+this.getEdge();
				break;
			case 180:
				xPoints[0]=x+this.getEdge(); xPoints[1]=x+this.getEdge(); xPoints[2]=x;
				yPoints[0]=y+this.getEdge(); yPoints[1]=y				; yPoints[2]=y;
				break;	
			case 270:
				xPoints[0]=x;				 xPoints[1]=x+this.getEdge(); xPoints[2]=x+this.getEdge();
				yPoints[0]=y+this.getEdge(); yPoints[1]=y+this.getEdge(); yPoints[2]=y;
				break;
			default:
				break;
			}
	    	
	        g.setColor(this.color);
	        g.fillPolygon(xPoints, yPoints, 3);
	        g.setColor(this.color);
	        g.drawRoundRect(x, type, width, height, arcWidth, arcHeight)

	    }

		@Override
		public void select() {
			if(this.color == Constants.colorOfTriangularBumper){
				this.setColor(Color.white);
			}else{
				this.setColor(Constants.colorOfTriangularBumper);
			}

			
		}

		private void setColor(Color color) {
			this.color = color;
			
		}

		@Override
		public void rotate() {
			if(this.orientation==0||this.orientation ==90||this.orientation==180){
				this.setOrientation(this.getOrientation() +90);
			}else{
				this.setOrientation(0);
			}
			
		}

}

