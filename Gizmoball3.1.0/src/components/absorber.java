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

public class absorber extends JComponent implements gizmosInterface {

	private static final long serialVersionUID = 8L;
	private static final char TYPE = 'A';
	private static final int DEFAULT_WIDTH=Configuration.WIDTH*Configuration.SCALE;
	private static final int DEFAULT_HEIGHT=Configuration.L;
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;
	
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean trigger = false; 		//this determins if the flipper is moving or not
    private char type;
    private Color color;
    private String name;
    private static int numOfBallsStored;
    private LineSegment top,bottom,left,right;
    private Circle topLeft,topRight,botLeft,botRight;

    public absorber (){
    	this(ORIGINAL_X,ORIGINAL_Y,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
    
    public absorber (int x, int y){
    	this(x,y,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
  
    public absorber(int x, int y, int width, int height){
    	this.type = TYPE;
    	this.x = x;
    	this.y = y;
    	this.width= width;
    	this.height= height;
    	this.color = Configuration.colorOfAbsorber;
    	this.name = new String("A"+x/Configuration.L+"_"+y/Configuration.L);
    	numOfBallsStored = 1;
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
    
    public String getName(){
    	return this.name;
    }
    
    public void setName(String name){
    	this.name=name;
    }
    
    public int getX(){
    	return this.x;
    }
    
    public int getY(){
    	return this.y;
    }
    
    public Color getColor(){
    	return this.color;
    }
    
    public int getWidth(){
    	return this.width;
    }
    
    public int getHeight(){
    	return this.height;
    }
    
    public void setWidth(int width){
    	this.width = width;
    }
    
    public void setHeight(int height){
    	this.height = height;
    }
    
    public void setColor(Color color){
    	this.color = color;
    }

    public char getType() {
		return this.type;
	}

	public void select() {
		if(this.color == Configuration.colorOfAbsorber){
			this.setColor(Color.white);
		}else{
			this.setColor(Configuration.colorOfAbsorber);
		}
	}
	
	public void setLocation(int x, int y){
	    	this.x = x;
	    	this.y = y;
	    	this.name = new String("A"+x/Configuration.L+"_"+y/Configuration.L);
	}
	    
	  

	@Override
	public void rotate() {
		//do nothing
		
	}

	@Override
	public void originalState() {
		//TODO
		
	}

	@Override
	public boolean isTriggered() {
		return this.trigger;
	}

	@Override
	public void setTrigger(boolean trigger) {
		this.trigger = trigger;
		//TODO: when a key is pressed, a new ball is released if the numOfBallsStored >0
		
	}

	@Override
	public Rectangle boundingBox() {
		   return new Rectangle(x, y, this.getWidth(),this.getHeight());
	}
	
    public void paintComponents(Graphics g){
    	
        g.setColor(this.color);
        g.fillRect(x,y,this.getWidth(),this.getHeight());
        g.setColor(this.color);
        g.drawRect(x,y,this.getWidth(),this.getHeight());  
    }

	@Override
	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide) {
		numOfBallsStored++;
		return velocity = new Vect(0,0);
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
		output="absorber[name: "+this.name
				+", position:("+this.x+","+this.y
				+"), width: "+this.width
				+", height: "+this.height+"]\n";
		return output;
	}

	@Override
	public int getOrientation() {
		return 0;
	}

}
