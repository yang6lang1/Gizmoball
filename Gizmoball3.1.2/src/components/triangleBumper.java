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

public class triangleBumper  extends JComponent implements gizmosInterface{
	private static final long serialVersionUID =2L;
	private static final char TYPE = 'T';
	private static final int DEFAULT_ORIENTATION =0;
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;
    private static final int edgeLength = 1* Configuration.L;
    
    private int x;
    private int y;
	private int orientation = 0;
    private double TriangleCOR = Configuration.TriangleCOR;//coefficient of reflect
    private boolean trigger = false;
    private char type;
    private int resetTime =0;//ms
    private Color color;
    private String name;
    private LineSegment bottom,left,hypotenuse;
    private Circle topLeft,botLeft,botRight;
    
    public triangleBumper(){
    	this(ORIGINAL_X,ORIGINAL_Y,DEFAULT_ORIENTATION);
    }
    
    public triangleBumper(int x, int y,int orientation){
    	this.x = x;
    	this.y = y;
    	this.orientation =orientation;
    	this.type = TYPE;
    	this.color = Configuration.colorOfTriangularBumper;
    	this.name = new String("T"+x/Configuration.L+"_"+y/Configuration.L);
    	
    	switch(this.orientation){
    	case 0:
    		//x,y+E		x+E,y+E
    		bottom = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y		x,y+E
    		left = new LineSegment(x,y,x,y+this.boundingBox().height);
    		//x,y		x+E,y+E
    		hypotenuse = new LineSegment(x,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		topLeft = new Circle(x,y,0);
    		botLeft = new Circle(x,y+this.boundingBox().height,0);
    		botRight = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
    		break;
    	case 90:
    		//x,y		x,y+E
    		bottom = new LineSegment(x,y,x,y+this.boundingBox().height);
    		//x,y		x+E,y
    		left = new LineSegment(x,y,x,y+this.boundingBox().height);
    		//x,y+E		x+E,y
    		hypotenuse = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y);
    		topLeft = new Circle(x+this.boundingBox().width,y,0);
    		botLeft = new Circle(x,y,0);
    		botRight = new Circle(x,y+this.boundingBox().height,0);
    		break;
    	case 180:
    		//x,y		x+E,y
    		bottom = new LineSegment(x,y,x+this.boundingBox().width,y);
    		//x+E,y		x+E,y+E
    		left = new LineSegment(x+this.boundingBox().width,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y		x+E,y+E
    		hypotenuse = new LineSegment(x,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		topLeft = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
    		botLeft = new Circle(x+this.boundingBox().width,y,0);
    		botRight = new Circle(x,y,0);
    		break;
    	case 270:
    		//x+E,y		x+E,y+E
    		bottom = new LineSegment(x+this.boundingBox().width,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y+E		x+E,y+E
    		left = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y+E		x+E,y
    		hypotenuse = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y);
    		topLeft = new Circle(x,y+this.boundingBox().height,0);
    		botLeft = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
    		botRight = new Circle(x+this.boundingBox().width,y,0);
    		break;
    	}
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
    
    public boolean isTriggered(){
    	return trigger;
    }
    
    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.name = new String("T"+x/Configuration.L+"_"+y/Configuration.L);
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
    	if(this.trigger){
    		this.resetTime = (int)( 2000/Configuration.delta_t); //update the resetTime
    	}
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
        
    	if(!this.trigger){
            g.setColor(this.color);
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(this.color);
            g.drawPolygon(xPoints, yPoints, 3);  
    	}else{
	        g.setColor(Configuration.colorOfTriggeredGizmo);
            g.fillPolygon(xPoints, yPoints, 3);
	        g.setColor(Configuration.colorOfTriggeredGizmo);
            g.drawPolygon(xPoints, yPoints, 3);  
	        resetTime--;
    	}
    	
        if(resetTime <=0){
        	this.setTrigger(false);
        }

    }

	@Override
	public void select() {
		if(this.color == Configuration.colorOfTriangularBumper){
			this.setColor(Color.white);
		}else{
			this.setColor(Configuration.colorOfTriangularBumper);
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

	@Override
	public Rectangle boundingBox() {
	    return new Rectangle(x, y, this.getEdge(), this.getEdge());
	}

	@Override
	public void originalState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide) {
		switch(collisionSide){//0-bottom, 1-left, 2-hypotenuse, 3-topLeft, 4-botLeft, 5-botRight
		case 0:
			velocity = Geometry.reflectWall(bottom, velocity);
			//System.out.println("Bounce with bottom: "+ bottom.toString());
			break;
		case 1:
			velocity = Geometry.reflectWall(left, velocity);	
			//System.out.println("Bounce with left: "+ left.toString());
			break;
		case 2:    	
			velocity = Geometry.reflectWall(hypotenuse, velocity);
			//System.out.println("Bounce with hypotenuse: "+ hypotenuse.toString());
			break;
		case 3:
			velocity = Geometry.reflectCircle(topLeft.getCenter(), ball.getCenter(), velocity);
			//System.out.println("Bounce with topLeft: "+ topLeft.toString());
			break;
		case 4:
			velocity = Geometry.reflectCircle(botLeft.getCenter(), ball.getCenter(), velocity);
			//System.out.println("Bounce with botLeft: "+ botLeft.toString());
			break;
		case 5:
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
	   	switch(this.orientation){
    	case 0:
    		//x,y+E		x+E,y+E
    		bottom = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y		x,y+E
    		left = new LineSegment(x,y,x,y+this.boundingBox().height);
    		//x,y		x+E,y+E
    		hypotenuse = new LineSegment(x,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		topLeft = new Circle(x,y,0);
    		botLeft = new Circle(x,y+this.boundingBox().height,0);
    		botRight = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
    		break;
    	case 90:
    		//x,y		x,y+E
    		bottom = new LineSegment(x,y,x,y+this.boundingBox().height);
    		//x,y		x+E,y
    		left = new LineSegment(x,y,x,y+this.boundingBox().height);
    		//x,y+E		x+E,y
    		hypotenuse = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y);
    		topLeft = new Circle(x+this.boundingBox().width,y,0);
    		botLeft = new Circle(x,y,0);
    		botRight = new Circle(x,y+this.boundingBox().height,0);
    		break;
    	case 180:
    		//x,y		x+E,y
    		bottom = new LineSegment(x,y,x+this.boundingBox().width,y);
    		//x+E,y		x+E,y+E
    		left = new LineSegment(x+this.boundingBox().width,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y		x+E,y+E
    		hypotenuse = new LineSegment(x,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		topLeft = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
    		botLeft = new Circle(x+this.boundingBox().width,y,0);
    		botRight = new Circle(x,y,0);
    		break;
    	case 270:
    		//x+E,y		x+E,y+E
    		bottom = new LineSegment(x+this.boundingBox().width,y,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y+E		x+E,y+E
    		left = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y+this.boundingBox().height);
    		//x,y+E		x+E,y
    		hypotenuse = new LineSegment(x,y+this.boundingBox().height,x+this.boundingBox().width,y);
    		topLeft = new Circle(x,y+this.boundingBox().height,0);
    		botLeft = new Circle(x+this.boundingBox().width,y+this.boundingBox().height,0);
    		botRight = new Circle(x+this.boundingBox().width,y,0);
    		break;
    	}
		
        double minTime = Configuration.delta_t+1; //Initially
        double collisionGizmo = 0;
        double collisionSide = -1; //0-bottom, 1-left, 2-hypotenuse, 3-topLeft, 4-botLeft, 5-botRight

        if(minTime >= Geometry.timeUntilWallCollision(bottom, ball, velocity)){
        	minTime = Geometry.timeUntilWallCollision(bottom, ball, velocity);collisionSide=0;
        }
        if(minTime >=Geometry.timeUntilWallCollision(left, ball, velocity)){
        	minTime = Geometry.timeUntilWallCollision(left, ball, velocity);collisionSide=1;
        }
        if(minTime>=Geometry.timeUntilWallCollision(hypotenuse, ball, velocity)){
        	minTime = Geometry.timeUntilWallCollision(hypotenuse, ball, velocity);collisionSide=2;
        }
        if(minTime >= Geometry.timeUntilCircleCollision(topLeft, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(topLeft, ball, velocity);collisionSide=3;
        }
        if(minTime >= Geometry.timeUntilCircleCollision(botLeft, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(botLeft, ball, velocity);collisionSide=4;
        }
        if(minTime >= Geometry.timeUntilCircleCollision(botRight, ball, velocity)){
        	minTime = Geometry.timeUntilCircleCollision(botRight, ball, velocity);collisionSide=5;
        }
      
        double[] outPut = {minTime,collisionGizmo,collisionSide};

    	return outPut;     
		
	}
	
	public String toString(){
		String output =null;
		output="triangleBumper[name: "+this.name
				+", position:("+this.x+","+this.y
				+"), orientation: "+this.orientation+"]\n";
		return output;
	}

}
