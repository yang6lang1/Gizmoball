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
    
    public squareBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y);
    }
    
    public squareBumper(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.type = TYPE;
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
    
    public void setX(int x){
    	this.x = x;
    }
    
    public void setY(int y){
    	this.y = y;
    }
    
    public void setSquareCOR(double SquareCOR){
    	this.SquareCOR = SquareCOR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    }
    
    public void paintComponents(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(x,y,edgeLength,edgeLength);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,edgeLength,edgeLength);  
    }
}
