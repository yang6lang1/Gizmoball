package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import system.Constants;

public class leftFlipper extends JComponent implements gizmosInterface {

		private static final long serialVersionUID =4L;
		private static final char TYPE = 'L';
		private static final int DEFAULT_ORIENTATION =0;
		private static final int ORIGINAL_X = 0;
		private static final int ORIGINAL_Y = 0;
	    private static final int edgeLength = 1* Constants.L;
	    private static final double hypotenuse = Math.sqrt(2)*Constants.L;
	    
	    private int x;
	    private int y;
		private int orientation = 0;
	    private double TriangleCOR = Constants.TriangleCOR;//coefficient of reflect
	    private boolean trigger = false;
	    private char type;
	    private int resetTime = 10;//ms
	    private Color color;
	    private String name;
	    
	    public leftFlipper(){
	    	this(ORIGINAL_X,ORIGINAL_Y,DEFAULT_ORIENTATION);
	    }
	    
	    public triangleBumper(int x, int y,int orientation){
	    	this.x = x;
	    	this.y = y;
	    	this.orientation =orientation;
	    	this.type = TYPE;
	    	this.color = Constants.colorOfTriangularBumper;
	    	this.name = new String("T"+x/Constants.L+"_"+y/Constants.L);
	    }
	    
	    public int getEdge(){
	    	return edgeLength;
	    }
	    
	    public double getHypo(){
	    	return hypotenuse;
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
	    
	    public double getTriangleCOR(){
	    	return TriangleCOR;
	    }
	 
	    public char getType(){
	    	return this.type;
	    }
	    
	    public boolean isTouched(){
	    	return trigger;
	    }
	    
	    public void setLocation(int x, int y){
	    	this.x = x;
	    	this.y = y;
	    	this.name = new String("T"+x/Constants.L+"_"+y/Constants.L);
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
	    
	    public void setTriangleCOR(double TriangleCOR){
	    	this.TriangleCOR = TriangleCOR;
	    }
	    
	    public void setTrigger(boolean trigger){
	    	this.trigger = trigger;
	    }
	    
	    public void paintComponents(Graphics g){
	    	int[] xPoints = new int[3],yPoints=new int[3];
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
	        g.drawPolygon(xPoints, yPoints, 3);  
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

}
