package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import system.Constants;

public class circleBumper extends JComponent implements gizmosInterface{
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
    private Color color;
    private String name;
    
    public circleBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y);
    }
    
    public circleBumper(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.type = TYPE;
    	this.color = Constants.colorOfCircularBumper;
    	this.name = new String("S"+x/Constants.L+"_"+y/Constants.L);
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
    
    public Color getColor(){
    	return this.color;
    }
    
    public boolean isTouched(){
    	return trigger;
    }
    
    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.name = new String("C"+x/Constants.L+"_"+y/Constants.L);
    }
    
    public void setCircleCOR(double CircleCOR){
    	this.CircleCOR = CircleCOR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    }
    
    public void setColor(Color color){
    	this.color = color;
    }
    public void paintComponents(Graphics g){
    	
        g.setColor(this.color);
        g.fillOval(x, y, radius+radius, radius+radius);
        g.setColor(this.color);
        g.drawOval(x, y, radius+radius, radius+radius);  
//        g.setColor(Color.green);
//        g.fillRoundRect(x, y+2*Constants.L, radius, 2*Constants.L,(int)(radius),(int)(radius));
 
    }


	public void select() {
		if(this.color == Constants.colorOfCircularBumper){
			this.setColor(Color.white);
		}else{
			this.setColor(Constants.colorOfCircularBumper);
		}
		
	}

	@Override
	public void rotate() {
		//do nothing;		
	}

}
