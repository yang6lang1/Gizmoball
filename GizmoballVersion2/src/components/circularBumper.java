package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import system.Constants;

public class circularBumper extends JComponent implements gizmosInterface{
	private static final long serialVersionUID =3L;
	private static final char TYPE = 'C';
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;
    private static final int radius =(int)( 0.5* 1* Constants.L);
    
    private int x;
    private int y;
    private double CircleCOR = Constants.CircleCOR;//coefficient of reflect
    private boolean trigger = false;
    private char type;
    private int resetTime = 10;//ms
    
    public circularBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y);
    }
    
    public circularBumper(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.type = TYPE;
    }
    public int getRadius(){
    	return radius;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    
    public int[] getCenter(){
    	int[] center = new int[2];
    	center[0] =x+(int)(0.5*radius);
    	center[1] =y+(int)(0.5*radius);
    	return center;
    }
    
    public double getCircleCOR(){
    	return CircleCOR;
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
    
    public void setCircleCOR(double CircleCOR){
    	this.CircleCOR = CircleCOR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    }
    
    public void paintComponents(Graphics g){
    	
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, radius+radius, radius+radius);
        g.setColor(Color.ORANGE);
        g.drawOval(x, y, radius+radius, radius+radius);  
//        g.setColor(Color.green);
//        g.fillRoundRect(x, y+2*Constants.L, radius, 2*Constants.L,(int)(radius),(int)(radius));
 
    }

}
