package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import system.Constants;

public class squareBumper extends JComponent implements gizmosInterface{
	private static final long serialVersionUID =1L;
	private static final char TYPE = 'S';
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;

    private static final int edgeLength = 1* Constants.L;
    private int x;
    private int y;
    private double SquareCOR = Constants.SquareCOR;//coefficient of reflect
    private boolean trigger = false;
    private char type;
    private int resetTime = 10;//ms
    private Color color;
    
    public squareBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y);
    }
    
    public squareBumper(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.type = TYPE;
    	this.color = Constants.colorOfSquareBumper;
    }
    public int getEdge(){
    	return edgeLength;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    
    public double getSquareCOR(){
    	return SquareCOR;
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
    
    public void setColor(Color color){
    	this.color = color;
    }
    
    public void setSquareCOR(double SquareCOR){
    	this.SquareCOR = SquareCOR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    }
    
    public void paintComponents(Graphics g){
    	
        g.setColor(this.color);
        g.fillRect(x,y,edgeLength,edgeLength);
        g.setColor(this.color);
        g.drawRect(x,y,edgeLength,edgeLength);  
    }


	public void select() {
		if(this.color == Constants.colorOfSquareBumper){
			this.setColor(Color.white);
		}else{
			this.setColor(Constants.colorOfSquareBumper);
		}

	}
}
