package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import system.Constants;

public class triangularBumper  extends JComponent implements gizmosInterface{
	private static final long serialVersionUID =2L;
	private static final char TYPE = 'T';
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
    
    public triangularBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y,DEFAULT_ORIENTATION);
    }
    
    public triangularBumper(int x, int y,int orientation){
    	this.x = x;
    	this.y = y;
    	this.orientation =orientation;
    	this.type = TYPE;
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
    	
        g.setColor(Constants.colorOfTriangularBumper);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Constants.colorOfTriangularBumper);
        g.drawPolygon(xPoints, yPoints, 3);  
    }

}
