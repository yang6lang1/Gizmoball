package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import interfaces.flippersInterface;
import interfaces.gizmosInterface;

import javax.swing.JComponent;

import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;
import system.Configuration;

public class leftFlipper extends JComponent implements gizmosInterface,flippersInterface{

	private static final long serialVersionUID =7L;
	private static final char TYPE = 'L';
	private static final int DEFAULT_ORIENTATION =0;
	//private static final Angle DEFAULT_ANGLE =Angle.DEG_180;
	private static final Angle DEFAULT_ANGLE =Angle.DEG_90;
	private static final double DEFAULT_ANGULAR_SPEED = Configuration.angularSpeed;
	private static final int ORIGINAL_X = 0;
	private static final int ORIGINAL_Y = 0;
    private static final int edgeLength = 2* Configuration.L;
    private static final int thickness = (int)(0.5*Configuration.L);
	private static final double delta_t = Configuration.delta_t/1000;
	private static final int L = Configuration.L;
    
    private int x;
    private int y;
	private int orientation;
	private Angle angle=DEFAULT_ANGLE;
    private double LF_COR = Configuration.LF_COR;//coefficient of reflection
    private boolean trigger = false; 		//this determines if the flipper is triggered by other gizmos
    private boolean keyPressed = false;     //this determines if the flipper bound key is pressed
    private char type;
    private int delay =  1;//500ms
    private Color color;
    private String name;
    private double angularSpeed;//this angularspeed has no direction    												
    private LineSegment left,right;
    private Circle topLeft,topRight,botLeft,botRight;
    private Circle start,end;    	
	Vect rotatingCenter = new Vect(x+L/4,y+L/4);
    
    public leftFlipper(){
    	this(ORIGINAL_X,ORIGINAL_Y,DEFAULT_ORIENTATION);
    }
    
    public leftFlipper(int x, int y,int orientation){
    	this.x = x;
    	this.y = y;
    	this.name = new String("L"+x/Configuration.L+"_"+y/Configuration.L);
    	this.orientation =orientation;
    	this.type = TYPE;
    	this.trigger = false;//TODO: set to be true for testing purpose
    	this.color = Configuration.colorOfRightFlipper;

    	this.angularSpeed =0;
    	//System.out.println("Name: "+this.name+"("+this.x+","+this.y+")");
        switch(this.orientation){
        case 0:
        	left = new LineSegment(x+L/4-angle.sin()*(L/4),		y+L/4+angle.cos()*(L/4),
        						   x+L/4+angle.cos()*(3*L/2)-angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)+angle.cos()*(L/4));
        	right = new LineSegment(x+L/4+angle.sin()*(L/4),		y+L/4-angle.cos()*(L/4),
					   x+L/4+angle.cos()*(3*L/2)+angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)-angle.cos()*(L/4));
        	start = new Circle(x+L/4,		y+L/4,		L/4);
        	end = new Circle(x+L/4+angle.cos()*(3*L/2),		y+L/4+angle.sin()*(3*L/2),		L/4);
        	topLeft = new Circle(x+L/4-angle.sin()*(L/4),		y+L/4+angle.cos()*(L/4),		0);
        	topRight = new Circle(x+L/4+angle.sin()*(L/4),		y+L/4-angle.cos()*(L/4),		0);
        	botLeft =  new Circle(x+L/4+angle.cos()*(3*L/2)-angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)+angle.cos()*(L/4),	0);
        	botRight = new Circle(x+L/4+angle.cos()*(3*L/2)+angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)-angle.cos()*(L/4), 0);

        	rotatingCenter = new Vect(x+L/4,y+L/4);
        	
        	break;
        default:
        	
        	break;
        }
    	
     }
    
    public int getEdge(){
    	return edgeLength;
    }
    
    public int getThickness(){
    	return thickness;
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
    
    public double getLFCOR(){
    	return this.LF_COR;
    }
 
    public char getType(){
    	return this.type;
    }
    
    public boolean isTriggered(){
    	return trigger;
    }
    
    public Angle getAngle(){
    	return this.angle;
    }
    
    public double getAngularSpeed(){
    	return this.angularSpeed;
    }
    
    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    	this.name = new String("L"+x/Configuration.L+"_"+y/Configuration.L);
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
    
    public void setLFCOR(double LF_COR){
    	this.LF_COR = LF_COR;
    }
    
    public void setTrigger(boolean trigger){
    	this.trigger = trigger;
    }
    
    public void setAngle(Angle angle){
    	this.angle = angle;
    }
    
    /*Note: angularSpeed should be in radians
     * */
    public void setAngularSpeed(double angularSpeed){
    	this.angularSpeed = angularSpeed;
    }
    
    public void move(){// this function is called when

		if(delay <=0){
			this.angle=new Angle(this.angle.radians()+angularSpeed*delta_t);

//			if(this.angle.compareTo(Angle.DEG_90)<=0){
			if(this.angle.compareTo(Angle.ZERO.plus(new Angle(this.angle.radians()+angularSpeed*delta_t)))<=0){
				this.angle = Angle.ZERO;
				this.angularSpeed = 0;
				delay = (int)(250/Configuration.delta_t);
			}

			if(this.angle.compareTo(Angle.DEG_90)>=0){
				this.angle = Angle.DEG_90;
				this.angularSpeed =0;
				delay = (int)(250/Configuration.delta_t);
			}
		}else{

			//if(this.angle.compareTo(Angle.DEG_90)<=0){
			if(this.angle.compareTo(Angle.ZERO.plus(new Angle(this.angle.radians()+angularSpeed*delta_t)))<=0){
				this.angle = Angle.ZERO;
				this.angularSpeed = DEFAULT_ANGULAR_SPEED;
				delay--;
			}
			
			if(this.angle.compareTo(Angle.DEG_90)>=0){
				this.angle = Angle.DEG_90;
				this.angularSpeed =-DEFAULT_ANGULAR_SPEED;
				delay--;
			}		
		}


    }

    public void repaint(){
    	this.repaint(x, y, this.getEdge(), this.getEdge());
    }
    
    
    public void paintComponents(Graphics g){
    	
    	int xPoints=0,yPoints=0;
		Graphics2D g2d = (Graphics2D)g;

		switch (this.orientation){
		case 0:
			xPoints=x									; 
			yPoints=y  									;         						  
			g2d.rotate(angle.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;
		case 90:
			xPoints=x+this.getEdge()-this.getThickness();									 
			yPoints=y									;	   						 
			g2d.rotate(angle.radians()+Angle.DEG_90.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;
		case 180:
			xPoints=x+this.getEdge()-this.getThickness();
			yPoints=y+this.getEdge()-this.getThickness();
			g2d.rotate(angle.radians()+Angle.DEG_180.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;	
		case 270:
			xPoints=x									;									 
			yPoints=y+this.getEdge()-this.getThickness();
			g2d.rotate(angle.radians()+Angle.DEG_270.radians(), xPoints+this.getThickness()/2, yPoints+this.getThickness()/2);
			break;
		default:
			break;
		}
	    	
        g2d.setColor(this.color);
        g2d.fillRoundRect(xPoints, yPoints, this.getEdge(), this.getThickness(), Configuration.L/2, Configuration.L/2);
        g2d.setColor(this.color);
        g2d.fillRoundRect(xPoints, yPoints, this.getEdge(), this.getThickness(), Configuration.L/2, Configuration.L/2);

    }

	@Override
	public void select() {
		if(this.color == Configuration.colorOfRightFlipper){
			this.setColor(Color.white);
		}else{
			this.setColor(Configuration.colorOfRightFlipper);
		}
	}

	private void setColor(Color color) {
		this.color = color;
		
	}
	
    public Rectangle boundingBox() {

        // a Rectangle is the x,y for the upper left corner and then the
        // width and height
        return new Rectangle(x, y, this.getEdge(),this.getEdge());
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
	public void originalState() {
		
		if(this.angle.compareTo(Angle.DEG_90)!=0){
			this.angularSpeed = DEFAULT_ANGULAR_SPEED;
			this.angle=new Angle(this.angle.radians()+angularSpeed*delta_t);
		
			if(this.angle.compareTo(Angle.ZERO)<=0){
			//if(this.angle.compareTo(Angle.ZERO.plus(new Angle(this.angle.radians()+angularSpeed*delta_t)))<=0){	
				this.angle = Angle.ZERO;
			}
			
			if(this.angle.compareTo(Angle.DEG_90)>=0){
				this.angle = Angle.DEG_90;
				this.angularSpeed=0;
			}
		}
		
	}

	@Override
	public void moveUp() {
		
		if(this.angle.compareTo(Angle.ZERO)!=0){
		
			this.angularSpeed = -DEFAULT_ANGULAR_SPEED;
			this.angle=new Angle(this.angle.radians()+angularSpeed*delta_t);

			if(this.angle.compareTo(Angle.ZERO.plus(new Angle(this.angle.radians()+angularSpeed*delta_t)))<=0){
				this.angle = Angle.ZERO;
				this.angularSpeed = 0;
			}
			
			if(this.angle.compareTo(Angle.DEG_90)>=0){
				this.angle = Angle.DEG_90;
			}
		}
	}
	
	@Override
	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide) {
		//System.out.println(this.angularSpeed);
		LineSegment l_2 = null;			//the line between p1 and the center of the ball
		double l_1 =0.0;				//the length from p1 to the point there they collide
		double linearSpeed = 0.0;		//<0: x+,y-; >0: x-,y+;
		double vx=0.0,vy=0.0;
		Vect linearVelocity,vectForFlipper;
		
		//TODO: Add the linear velocity into it returned velocity. I can't seem to be able to figure out what caused leftFlipper so buggy...
			switch(collisionSide){
			case 0:
				//determine the linear speed of the position where the ball and the flipper collide
				//velocity = Geometry.reflectRotatingWall(left, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				l_2 = new LineSegment(left.p1(),ball.getCenter());
				l_1 = Math.sqrt(l_2.length()*l_2.length()-ball.getRadius()*ball.getRadius());
				linearSpeed = l_1*Math.abs(this.angularSpeed);		
				
				velocity = Geometry.reflectRotatingWall(left, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				vx=velocity.x();
				vy=velocity.y();
				/*
				switch(this.orientation){
				case 0:
					vectForFlipper = new Vect(Angle.DEG_180.plus(angle),2*L);
					if(angularSpeed<0){
						linearVelocity = new Vect(Angle.ZERO.minus(Angle.DEG_90.minus(angle)),linearSpeed);	
						vx= vx+linearSpeed*Math.sin(angle.radians());
						vy= vy-linearSpeed*Math.cos(angle.radians());
						velocity = new Vect(vx,vy);
						
						if(velocity.projectOn(linearVelocity).length()<linearVelocity.length()){
							velocity = linearVelocity.plus(velocity.projectOn(vectForFlipper));
						}
					}
					if(angularSpeed>0){
						linearVelocity = new Vect(Angle.DEG_90.plus(angle),linearSpeed);	
						vx= vx-linearSpeed*Math.sin(angle.radians());
						vy= vy+linearSpeed*Math.cos(angle.radians());
						velocity = new Vect(vx,vy);
						
						if(velocity.projectOn(linearVelocity).length()<linearVelocity.length()){
							velocity = linearVelocity.plus(velocity.projectOn(vectForFlipper));
						}
					}

					break;
				case 90:

					break;
				default:
					break;				
				}
				*/
			/*	if(angularSpeed<0){
					switch(this.orientation){
					case 0:
						velocity = new Vect(velocity.x()+Math.abs(linearSpeed)*Math.sin(angle.radians())
								, velocity.y()-Math.abs(linearSpeed)*Math.cos(angle.radians()));
						break;
					case 90:
						velocity = new Vect(velocity.x()+Math.abs(linearSpeed)*Math.sin(angle.radians())
								, velocity.y()+Math.abs(linearSpeed)*Math.cos(angle.radians()));
						break;
					default:
						break;				
					}
					
				}else{
					switch(this.orientation){
					case 0:
						velocity = new Vect(velocity.x()-Math.abs(linearSpeed)*Math.sin(angle.radians())
								, velocity.y()+Math.abs(linearSpeed)*Math.cos(angle.radians()));
						break;
					case 90:
						velocity = new Vect(velocity.x()-Math.abs(linearSpeed)*Math.sin(angle.radians())
								, velocity.y()-Math.abs(linearSpeed)*Math.cos(angle.radians()));
						break;
					default:
						break;		
					}	
				}*/
				
				/*System.out.println(this.name+ "--Bounce with left: "+ left.toString());
				System.out.println("x_velocity: "+ velocity.x());
				System.out.println("y_velocity: "+ velocity.y());
				System.out.println("angularSpeed: "+ Math.abs(angularSpeed));
				System.out.println();*/
				break;
			case 1:
		
				l_2 = new LineSegment(right.p1(),ball.getCenter());
				l_1 = Math.sqrt(l_2.length()*l_2.length()-ball.getRadius()*ball.getRadius());
				linearSpeed = l_1*Math.abs(this.angularSpeed);		
				
				velocity = Geometry.reflectRotatingWall(right, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				vx=velocity.x();
				vy=velocity.y();
				
			/*	switch(this.orientation){
				case 0:
					
					vectForFlipper = new Vect(Angle.DEG_180.plus(angle),2*L);
					if(angularSpeed<0){
						linearVelocity = new Vect(Angle.ZERO.minus(Angle.DEG_90.minus(angle)),linearSpeed);	
						vx= vx+linearSpeed*Math.sin(angle.radians());
						vy= vy-linearSpeed*Math.cos(angle.radians());
						velocity = new Vect(vx,vy);
						
						if(velocity.projectOn(linearVelocity).length()<linearVelocity.length()){
							velocity = linearVelocity.plus(velocity.projectOn(vectForFlipper));
						}
					}
					if(angularSpeed>0){
						linearVelocity = new Vect(Angle.DEG_90.plus(angle),linearSpeed);	
						vx= vx-linearSpeed*Math.sin(angle.radians());
						vy= vy+linearSpeed*Math.cos(angle.radians());
						velocity = new Vect(vx,vy);
						
						if(velocity.projectOn(linearVelocity).length()<linearVelocity.length()){
							velocity = linearVelocity.plus(velocity.projectOn(vectForFlipper));
						}
					}

					break;
				case 90:

					break;
				default:
					break;				
				}*/

				/*
				l_2 = new LineSegment(right.p1(),ball.getCenter());
				l_1 = Math.sqrt(l_2.length()*l_2.length()-ball.getRadius()*ball.getRadius());
				linearSpeed = l_1*this.angularSpeed;			
				
				velocity = Geometry.reflectRotatingWall(right, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				
				if(angularSpeed<0){
					velocity = new Vect(velocity.x()+Math.abs(linearSpeed)*Math.sin(angle.radians())
							, velocity.y()-Math.abs(linearSpeed)*Math.cos(angle.radians()));
				}else{
					velocity = new Vect(velocity.x()-Math.abs(linearSpeed)*Math.sin(angle.radians())
							, velocity.y()+Math.abs(linearSpeed)*Math.cos(angle.radians()));
				}*/
				
				//velocity = Geometry.reflectRotatingWall(right, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);

				//System.out.println(this.name+ "--Bounce with right: "+ right.toString());
				break;
			case 2:    	
				velocity = Geometry.reflectCircle(start.getCenter(), ball.getCenter(), velocity,this.LF_COR);
				//System.out.println("Bounce with start: "+ start.toString());
				break;
			case 3:
				velocity = Geometry.reflectRotatingCircle(end, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				//System.out.println("Bounce with end: "+ end.toString());
				break;
			case 4:
				velocity = Geometry.reflectRotatingCircle(topLeft, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				//System.out.println("Bounce with topLeft: "+ topLeft.toString());
				break;
			case 5:
				velocity = Geometry.reflectRotatingCircle(topRight, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				//System.out.println("Bounce with topRight: "+ topRight.toString());
				break;
			case 6:
				velocity = Geometry.reflectRotatingCircle(botLeft, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				//System.out.println("Bounce with botLeft: "+ botLeft.toString());
				break;
			case 7:
				velocity = Geometry.reflectRotatingCircle(botRight, rotatingCenter, angularSpeed, ball, velocity, this.LF_COR);
				//System.out.println("Bounce with botRight: "+ botRight.toString());
				break;
			default:
				break;					
			}
		return velocity;

	}

	@Override
	public double[] timeUntilNextCollision(Circle ball, Vect velocity) {
	
        double minTime = Configuration.delta_t+1; //Initially
        double collisionGizmo = 0;
        double collisionSide = -1; //0-left, 1-right, 2-start, 3-end,4-topLeft,5-topRight,6-botLeft,7-botRight
        
    	//rotatingCenter = new Vect(x+L/4,y+L/4);
        switch(this.orientation){
        case 0:
        	left = new LineSegment(x+L/4-angle.sin()*(L/4),		y+L/4+angle.cos()*(L/4),
        						   x+L/4+angle.cos()*(3*L/2)-angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)+angle.cos()*(L/4));
        	right = new LineSegment(x+L/4+angle.sin()*(L/4),		y+L/4-angle.cos()*(L/4),
					   x+L/4+angle.cos()*(3*L/2)+angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)-angle.cos()*(L/4));
        	start = new Circle(x+L/4,		y+L/4,		L/4);
        	end = new Circle(x+L/4+angle.cos()*(3*L/2),		y+L/4+angle.sin()*(3*L/2),		L/4);
        	topLeft = new Circle(x+L/4-angle.sin()*(L/4),		y+L/4+angle.cos()*(L/4),		0);
        	topRight = new Circle(x+L/4+angle.sin()*(L/4),		y+L/4-angle.cos()*(L/4),		0);
        	botLeft =  new Circle(x+L/4+angle.cos()*(3*L/2)-angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)+angle.cos()*(L/4),	0);
        	botRight = new Circle(x+L/4+angle.cos()*(3*L/2)+angle.sin()*(L/4),	y+L/4+angle.sin()*(3*L/2)-angle.cos()*(L/4), 0);

        	rotatingCenter = new Vect(x+L/4,y+L/4);
        	
        	break;
        case 90:
        	left = new LineSegment(x+7*L/4-angle.cos()*(L/4),		y+L/4-angle.sin()*(L/4),
					   x+7*L/4-angle.sin()*(3*L/2)-angle.cos()*(L/4),	y+L/4+angle.cos()*(3*L/2)-angle.sin()*(L/4));
        	right = new LineSegment(x+7*L/4+angle.cos()*(L/4),		y+L/4+angle.sin()*(L/4),
					   x+7*L/4-angle.sin()*(3*L/2)+angle.cos()*(L/4),	y+L/4+angle.cos()*(3*L/2)+angle.sin()*(L/4));
        	start = new Circle(x+7*L/4,		y+L/4,		L/4);
        	end = new Circle(x+7*L/4-angle.sin()*(3*L/2),		y+L/4+angle.cos()*(3*L/2),		L/4);
        	topLeft = new Circle(x+7*L/4-angle.cos()*(L/4),		y+L/4-angle.sin()*(L/4),		0);
        	topRight = new Circle(x+7*L/4+angle.cos()*(L/4),		y+L/4+angle.sin()*(L/4),		0);
        	botLeft =  new Circle(x+7*L/4-angle.sin()*(3*L/2)-angle.cos()*(L/4),	y+L/4+angle.cos()*(3*L/2)-angle.sin()*(L/4),	0);
        	botRight = new Circle(x+7*L/4-angle.sin()*(3*L/2)+angle.cos()*(L/4),	y+L/4+angle.cos()*(3*L/2)+angle.sin()*(L/4),	0);

        	rotatingCenter = new Vect(x+7*L/4,y+L/4);
        	break;
        default:

        	break;
        }

        /* System.out.println("angle:\t"+this.angle.toString());
     	System.out.println("orientation:\t"+this.orientation);
     	System.out.println("left:\t\t"+left.toString());
     	System.out.println("rotatingCenter:\t"+rotatingCenter.toString());
     	System.out.println("angularSpeed:\t"+angularSpeed);
     	System.out.println("ball:\t\t"+ball.toString());
     	System.out.println("velocity:\t"+velocity.toString());*/
       
	        if(minTime >= Geometry.timeUntilRotatingWallCollision(left, rotatingCenter, angularSpeed, ball, velocity)){
	        	minTime = Geometry.timeUntilRotatingWallCollision(left, rotatingCenter, angularSpeed, ball, velocity);collisionSide=0;
	        	//System.out.println("Going to colide with left LineSegment: " + minTime);
	        }
	        
	        if(minTime >= Geometry.timeUntilRotatingWallCollision(right, rotatingCenter, angularSpeed, ball, velocity)){
	        	minTime = Geometry.timeUntilRotatingWallCollision(right, rotatingCenter, angularSpeed, ball, velocity);collisionSide=1;
	        }
	        
	        if(minTime >= Geometry.timeUntilCircleCollision(start, ball, velocity)){
	        	//Circle start is static
	        	minTime = Geometry.timeUntilCircleCollision(start, ball, velocity);collisionSide=2;
	        }
	        if(minTime >= Geometry.timeUntilRotatingCircleCollision(end, rotatingCenter, angularSpeed, ball, velocity)){
	        	//Circle end is moving
	        	minTime = Geometry.timeUntilRotatingCircleCollision(end, rotatingCenter, angularSpeed, ball, velocity);collisionSide=3;
	        } 
	       if(minTime >= Geometry.timeUntilRotatingCircleCollision(topLeft, rotatingCenter, angularSpeed, ball, velocity)){
	        	minTime = Geometry.timeUntilRotatingCircleCollision(topLeft, rotatingCenter, angularSpeed, ball, velocity);collisionSide=4;
	        } 
	        if(minTime >= Geometry.timeUntilRotatingCircleCollision(topRight, rotatingCenter, angularSpeed, ball, velocity)){
	        	minTime = Geometry.timeUntilRotatingCircleCollision(topRight, rotatingCenter, angularSpeed, ball, velocity);collisionSide=5;
	        } 
	        if(minTime >= Geometry.timeUntilRotatingCircleCollision(botLeft, rotatingCenter, angularSpeed, ball, velocity)){
	        	minTime = Geometry.timeUntilRotatingCircleCollision(botLeft, rotatingCenter, angularSpeed, ball, velocity);collisionSide=6;	       
	        } 
	        if(minTime >= Geometry.timeUntilRotatingCircleCollision(botRight, rotatingCenter, angularSpeed, ball, velocity)){
	        	minTime = Geometry.timeUntilRotatingCircleCollision(botRight, rotatingCenter, angularSpeed, ball, velocity);collisionSide=7;
	        } 
      
	        //System.out.println("angular speed: "+this.angularSpeed);
	        //System.out.println("alpha: "+this.alpha.toString());
	       // System.out.println(left.toString());

 
        double[] outPut = {minTime,collisionGizmo,collisionSide};

    	return outPut;     

	}

	@Override
	public boolean isKeyPressed() {

		return this.keyPressed;
	}

	@Override
	public void setKeyPressed(boolean keyPressed) {
		this.keyPressed = keyPressed;
		
	}

	public String toString(){
		String output =null;
		output="leftFlipper[name: "+this.name
				+", position:("+this.x+","+this.y
				+"), orientation: "+this.orientation+"]\n";
		return output;
	}

}
