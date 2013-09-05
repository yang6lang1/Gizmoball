package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

import system.Configuration;

public class circleBumper extends JComponent implements gizmosInterface{
	private static final long serialVersionUID =3L;
	private static final char TYPE = 'C';
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;
    private static final int radius =(int)( 0.5* 1* Configuration.L);
    
    private int x;
    private int y;
    private double CircleCOR = Configuration.CircleCOR;//coefficient of reflect
    private boolean trigger = false;
    private char type;
    private int resetTime=0;//ms
    private Color color;
    private String name;
    private Circle circle;
    
    public circleBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y);
    }
    
    public circleBumper(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.type = TYPE;
    	this.color = Configuration.colorOfCircularBumper;
    	this.name = new String("C"+x/Configuration.L+"_"+y/Configuration.L);
    	this.circle= new Circle(x+radius,y+radius,radius);
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
    
    public String getName(){
    	return this.name;
    }
    
    public boolean isTriggered(){
    	return trigger;
    }
    
    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.name = new String("C"+x/Configuration.L+"_"+y/Configuration.L);
    }
    
    public void setCircleCOR(double CircleCOR){
    	this.CircleCOR = CircleCOR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    	if(this.trigger){
    		this.resetTime = (int)( 2000/Configuration.delta_t); //update the resetTime
    	}
    }
    
    public void setColor(Color color){
    	this.color = color;
    }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public void paintComponents(Graphics g){
    	if(!this.trigger){
            g.setColor(this.color);
            g.fillOval(x, y, radius+radius, radius+radius);
            g.setColor(this.color);
            g.drawOval(x, y, radius+radius, radius+radius); 
    	}else{
	        g.setColor(Configuration.colorOfTriggeredGizmo);
	        g.fillOval(x, y, radius+radius, radius+radius);
	        g.setColor(Configuration.colorOfTriggeredGizmo);
	        g.drawOval(x, y, radius+radius, radius+radius); 
	        resetTime--;
    	}
    	 if(resetTime <=0){
         	this.setTrigger(false);
         }
    }


	public void select() {
		if(this.color == Configuration.colorOfCircularBumper){
			this.setColor(Color.white);
		}else{
			this.setColor(Configuration.colorOfCircularBumper);
		}
	}

	@Override
	public void rotate() {
		//do nothing;		
	}

	@Override
	public Rectangle boundingBox() {
	    return new Rectangle(x, y, radius + radius, radius + radius);
	}


	public void originalState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide) {
		velocity = Geometry.reflectCircle(circle.getCenter(), ball.getCenter(), velocity);
		//System.out.println("Bounce with circularBumper: "+ circle.toString());

		this.setTrigger(true);
		return velocity;
	}

	@Override
	public double[] timeUntilNextCollision(Circle ball, Vect velocity) {
		circle= new Circle(x+radius,y+radius,radius);
		
        double minTime = Configuration.delta_t+1; //Initially
        double collisionGizmo = 0;
        //double collisionSide = -1; //circle doesn't need collisionSide

        if(minTime >= Geometry.timeUntilCircleCollision(circle, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(circle, ball, velocity);
        }
      
        double[] outPut = {minTime,collisionGizmo,0};

    	return outPut;     

	}
	
	public String toString(){
		String output =null;
		output="circleBumper[name: "+this.name
				+", position:("+this.x+","+this.y+")]\n";
		return output;
	}

	@Override
	public int getOrientation() {
		return 0;
	}

}
