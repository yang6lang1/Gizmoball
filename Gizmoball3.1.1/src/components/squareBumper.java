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

public class squareBumper extends JComponent implements gizmosInterface{
	private static final long serialVersionUID =1L;
	private static final char TYPE = 'S';
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;

    private static final int edgeLength = 1* Configuration.L;
    private int x;
    private int y;
    private double SquareCOR = Configuration.SquareCOR;//coefficient of reflect
    private boolean trigger = false;
    private char type;
    private int resetTime =0;
    private Color color;
    private String name;
    private LineSegment top,bottom,left,right;
    private Circle topLeft,topRight,botLeft,botRight;
    
    public squareBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y);
    }
    
    public squareBumper(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.type = TYPE;
    	this.color = Configuration.colorOfSquareBumper;
    	this.name = new String("S"+x/Configuration.L+"_"+y/Configuration.L);
		//top:(x1,y1,  x1+E,y1)
		top =    new LineSegment(x,y,x+this.boundingBox().width,y);
		//bottom:(x1,y1+E,  x1+E,y1+E)
		bottom = new LineSegment(x, y+this.boundingBox().height, x+this.boundingBox().width, y+this.boundingBox().height);
		//left:(x1,y1,  x1,y1+E)
		left =   new LineSegment(x, y, x, y+this.boundingBox().height);
		//right:(x1+E,y1,  x1+E,y1+E)
		right =  new LineSegment(x+this.boundingBox().width, y, x+this.boundingBox().width, y+this.boundingBox().height);
		topLeft = new Circle(x,y,0);
		topRight = new Circle (x+this.boundingBox().width,y,0);
		botLeft = new Circle(x,y+this.boundingBox().height,0);
		botRight = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
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
    
    public String getName(){
    	return this.name;
    }
    
    public double getSquareCOR(){
    	return SquareCOR;
    }
 
    public char getType(){
    	return this.type;
    }
    
    public boolean isTriggered(){
    	return trigger;
    }
    
    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.name = new String("S"+x/Configuration.L+"_"+y/Configuration.L);
    }
    
    public void setName(String name){
    	this.name=name;
    }
    
    public void setColor(Color color){
    	this.color = color;
    }
    
    public void setSquareCOR(double SquareCOR){
    	this.SquareCOR = SquareCOR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    	if(this.trigger){
        	this.resetTime = (int)( 2000/Configuration.delta_t); //update the resetTime
    	}
    }
    
    public void paintComponents(Graphics g){
    	if(!this.trigger){
	        g.setColor(this.color);
	        g.fillRect(x,y,edgeLength,edgeLength);
	        g.setColor(this.color);
	        g.drawRect(x,y,edgeLength,edgeLength);  
    	}else{
	        g.setColor(Configuration.colorOfTriggeredGizmo);
	        g.fillRect(x,y,edgeLength,edgeLength);
	        g.setColor(Configuration.colorOfTriggeredGizmo);
	        g.drawRect(x,y,edgeLength,edgeLength);  
	        resetTime--;
    	}
    	
        if(resetTime <=0){
        	this.setTrigger(false);
        }
    }
    

	public void select() {
		if(this.color == Configuration.colorOfSquareBumper){
			this.setColor(Color.white);
		}else{
			this.setColor(Configuration.colorOfSquareBumper);
		}
	}

	@Override
	public void rotate() {
		//do nothing
		
	}

	@Override
	public Rectangle boundingBox() {
		//return super.getBounds();
	    return new Rectangle(x, y, this.getEdge(), this.getEdge());
	}

	@Override
	public void originalState() {
		// TODO Auto-generated method stubs
	}

	@Override
	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide) {
		
		switch(collisionSide){
			case 0:
				velocity = Geometry.reflectWall(top, velocity);
				//System.out.println("Bounce with top: "+ top.toString());
				break;
			case 1:
				velocity = Geometry.reflectWall(bottom, velocity);	
				//System.out.println("Bounce with bot: "+ bottom.toString());
				break;
			case 2:    	
				velocity = Geometry.reflectWall(left, velocity);
				//System.out.println("Bounce with left: "+ left.toString());
				break;
			case 3:
				velocity = Geometry.reflectWall(right, velocity);
				//.out.println("Bounce with right: "+ right.toString());
				break;
			case 4:
				velocity = Geometry.reflectCircle(topLeft.getCenter(), ball.getCenter(), velocity);
				//System.out.println("Bounce with topLeft: "+ topLeft.toString());
				break;
			case 5:
				velocity = Geometry.reflectCircle(topRight.getCenter(), ball.getCenter(), velocity);
				//System.out.println("Bounce with topRight: "+ topRight.toString());
				break;
			case 6:
				velocity = Geometry.reflectCircle(botLeft.getCenter(), ball.getCenter(), velocity);
				//System.out.println("Bounce with botLeft: "+ botLeft.toString());
				break;
			case 7:
				velocity = Geometry.reflectCircle(botRight.getCenter(), ball.getCenter(), velocity);
				//System.out.println("Bounce with botRight: "+ botRight.toString());
				break;
			
			default:
				break;					
			}
			
			this.setTrigger(true);
			
			return velocity;
	}

	@Override
	public double[] timeUntilNextCollision(Circle ball, Vect velocity) {
		//top:(x1,y1,  x1+E,y1)
		top =    new LineSegment(x,y,x+this.boundingBox().width,y);
		//bottom:(x1,y1+E,  x1+E,y1+E)
		bottom = new LineSegment(x, y+this.boundingBox().height, x+this.boundingBox().width, y+this.boundingBox().height);
		//left:(x1,y1,  x1,y1+E)
		left =   new LineSegment(x, y, x, y+this.boundingBox().height);
		//right:(x1+E,y1,  x1+E,y1+E)
		right =  new LineSegment(x+this.boundingBox().width, y, x+this.boundingBox().width, y+this.boundingBox().height);
		//topLeft:(x1,y1)
		topLeft = new Circle(x,y,0);
		topRight = new Circle (x+this.boundingBox().width,y,0);
		botLeft = new Circle(x,y+this.boundingBox().height,0);
		botRight = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
		
        double minTime = Configuration.delta_t+1; //Initially
        double collisionGizmo = 0;
        double collisionSide = -1; //0-top, 1-bottom, 2-left, 3-right, 4-topLeft, 5-topRight, 6-botLeft, 7-botRight

        if(minTime >= Geometry.timeUntilWallCollision(top, ball, velocity)){
        	minTime = Geometry.timeUntilWallCollision(top, ball, velocity);collisionSide=0;
        }
        if(minTime >=Geometry.timeUntilWallCollision(bottom, ball, velocity)){
        	minTime = Geometry.timeUntilWallCollision(bottom, ball, velocity);collisionSide=1;
        }
        if(minTime>=Geometry.timeUntilWallCollision(left, ball, velocity)){
        	minTime = Geometry.timeUntilWallCollision(left, ball, velocity);collisionSide=2;
        }
        if(minTime>= Geometry.timeUntilWallCollision(right, ball, velocity)){
        	minTime = Geometry.timeUntilWallCollision(right, ball, velocity);collisionSide=3;
        }
        if(minTime >= Geometry.timeUntilCircleCollision(topLeft, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(topLeft, ball, velocity);collisionSide=4;
        }
        if(minTime >= Geometry.timeUntilCircleCollision(topRight, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(topRight, ball, velocity);collisionSide=5;
        }
        if(minTime >= Geometry.timeUntilCircleCollision(botLeft, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(botLeft, ball, velocity);collisionSide=6;
        }
        if(minTime >= Geometry.timeUntilCircleCollision(botRight, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(botRight, ball, velocity);collisionSide=7;
        }
      
        double[] outPut = {minTime,collisionGizmo,collisionSide};

    	return outPut;     
	}
	
	public String toString(){
		String output =null;
		output="squareBumper[name: "+this.name
				+", position:("+this.x+","+this.y+")]\n";
		return output;
	}

	@Override
	public int getOrientation() {
	
		return 0;
	}
}
