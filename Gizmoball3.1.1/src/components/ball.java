package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;

import physics.Angle;
import physics.Circle;
import physics.Vect;
import system.Configuration;
import app.AnimationWindow;
import dataCollection.Quadtree;

public class ball extends JComponent implements gizmosInterface{

	private static final long serialVersionUID = 6L;
	private static final char TYPE = 'B';
    private static final int DEFAULT_RADIUS = Configuration.RADIUS;
	private double delta_t = Configuration.delta_t/1000;					//screen refresh rate
	
	//for testing purposes
	private int x;															//x position of the ball
	private int y;														 	//Y position of the ball
    private int original_x;
    private int original_y;
    private int radius;														//radius of the ball
	private int vx;															//x-axis velocity of the ball
	private int vy;															//y-axis velocity of the ball
	private int original_vx;
	private int original_vy;
	private Circle ball=new Circle(x,y,radius);								//the Circle object that represents this ball
    private Vect velocity= new Vect(vx,vy);  								//the Vect object that represents the ball's velocity
	private char type;														//type of the ball
    private String name;													//name of the ball
    private Quadtree<gizmosInterface> gizmos;								//temp buffer to store all the gizmos passed in my the window
    private ArrayList<gizmosInterface> allGizmos;							//temp buffer to store all the gizmos passed in my the window
    private Color color;													//color of the ball
    private double vy_old,vy_new;											//2 variables used in gravity calculation
    private double GRAVITY_VALUE=Configuration.DEFAULT_GRAVITY_VALUE;		//gravity constant(might be changed by the new game configuration)
    private double mu=Configuration.DEFAULT_mu;								//friction constant
    private double mu2=Configuration.DEFAULT_mu2;							//friction constant 2
    private AnimationWindow window;										    // Keep track of the animation window that will be drawing this ball.

    
    //Constructor:
    public ball(AnimationWindow win) {
    	this(win,new Configuration());
    }
    
    public ball(AnimationWindow win,Configuration newGame){

    	x = newGame.getBallX();
    	y = newGame.getBallY();
    	original_x = newGame.getBallX();
    	original_y = newGame.getBallY();
    	radius =DEFAULT_RADIUS;
        velocity = new Vect(newGame.getBallAngle(),newGame.getBallSpeed());
    	vx =(int)(velocity.x());
    	vy = (int)(velocity.y());
    	original_vx =(int)(velocity.x());
    	original_vy =(int)(velocity.y());
    	type = TYPE;
    	name = newGame.getBallName();
    	color = Configuration.colorOfBall;
    	vy_old=vy_new = vy;
    	GRAVITY_VALUE = newGame.getGravity();
    	mu = newGame.getMU();
    	mu2 = newGame.getMU2();
        this.window = win;
    	gizmos = this.window.getGizmos();

    }
    
    public String getName(){
    	return name;
    }
    

    public void setName(String name){
    	this.name = name;
    }
    

    public void setLocation(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
    public void setSpeed(double speed){
    	this.velocity = new Vect(velocity.angle(),speed);
    }
    
    public void setAngle(Angle angle){
    	this.velocity = new Vect(angle,velocity.length());
    }
    
    public void setColor(Color color){
    	this.color = color;
    }
    public double getSpeed(){
    	return this.velocity.length();
    }
    
    public Angle getAngle(){
    	return this.velocity.angle();
    }
    
    public Color getColor(){
    	return this.color;
    }
    
    public int getOriginalX(){
    	return this.original_x;
    }
    
    public int getOriginalY(){
    	return this.original_y;
    }
    
    public int getOriginalVX(){
    	return this.original_vx;
    }
    
    public int getOriginalVY(){
    	return this.original_vy;
    }
    /**
     * @modifies the Graphics object <g>.
     * @effects paints a circle on <g> reflecting the current position 
     * of the ball.
     * @param g Graphics context to be used for drawing.
     */
    public void paint(Graphics g) {

        // the "clip rectangle" is the area of the screen that needs to be
        // modified
        Rectangle clipRect = g.getClipBounds();

        // For this tiny program, testing whether we need to redraw is
        // kind of silly.  But when there are lots of objects all over the
        // screen this is a very important performance optimization
        if (clipRect.intersects(this.boundingBox())) {
            g.setColor(color);
            g.fillOval(x - radius, y - radius, radius + radius, radius
            		+ radius);       
            //g.setColor(Color.white);	//draw the border
            //g.drawOval(x - radius, y - radius, radius + radius, radius
            	//	+ radius);    
        }
    }
    
    public Rectangle boundingBox() {

        // a Rectangle is the x,y for the upper left corner and then the
        // width and height
        return new Rectangle(x - radius - 1, y - radius - 1, radius + radius + 2,
                        radius + radius + 2);
    }
    
    
    /**
     * @modifies this
     * @effects Moves the ball according to its velocity.  Reflections off 
     * walls cause the ball to change direction.
     */
    public void move(double delta_t) {
    	
    	//Friction:(angle not changed)
    	//equation:Vnew = Vold * (1 - mu * delta_t - mu2 * |Vold| * delta_t).
    	vx =(int)(velocity.x());
        vy = (int)(velocity.y());
    	
        //Update the horizontal position
    	x += vx*delta_t;

        //Gravity:(Only the vertical velocity is affected)
        //equation:vy_new = vy_old + gt  
        int delta_y = 0;
        
        //Update the vertical position
        //change in vertical distance due to gravity:
        //equation: delta_y = 1/2 * delta_t * (vy_old+vy)
        delta_y =(int)(0.5 * delta_t *(vy_old +vy));
        y += delta_y;
        if (x <= radius) {
            x = radius;
            vx = -vx;
        }
        if (x >= window.getWidth() - radius) {
            x = window.getWidth() - radius;
            vx = -vx;
        }
        
        if (y <= radius) {
        	//touch up
            y = radius;
            vy = -vy;
           // System.out.println(vy);
        }
        if (y >= window.getHeight() - radius) {
        	//touch down
            y = window.getHeight() - radius;
            vy = -vy;
        }
        velocity = new Vect(vx,vy);
    }

    public void updateVelocity(double delta_t){
       	
    	//Friction:(angle not changed)
    	//equation:Vnew = Vold * (1 - mu * delta_t - mu2 * |Vold| * delta_t).
    	vx =(int)(velocity.x());
    	vx =(int)(vx *(1 - mu*delta_t - mu2*Math.abs(vx)*delta_t));
    	
        vy = (int)(velocity.y());
    	vy = (int)(vy *(1 - mu*delta_t - mu2*Math.abs(vy)*delta_t));
    	
        //Gravity:(Only the vertical velocity is affected)
        //equation:vy_new = vy_old + gt  
        vy_old = vy;
        vy_new =vy_old + GRAVITY_VALUE * delta_t;
        vy = (int)vy_new;
            
        //Update velocity
        velocity = new Vect(vx,vy);

    }
    
    public double[] timeUntilNextCollision(Quadtree<gizmosInterface> gizmos){
    	ball=new Circle(x,y,radius);

        double[] outPut={Configuration.delta_t+1,0,0};
        double[] nextCollision={Configuration.delta_t+1,0,0};
        
        ArrayList<gizmosInterface> temp = new ArrayList<gizmosInterface>();
        temp = gizmos.retrieve(temp, this.boundingBox());
        for(int i = 0;i<temp.size();i++){     	 			
        	outPut = temp.get(i).timeUntilNextCollision(ball, velocity);
        	outPut[1] = i;//outPut[1] -> collisionGizmo
        		
       		if(outPut[0]<=nextCollision[0]){
       			nextCollision = outPut;
       		}

        }
        //gizmos.remove(gizmos, this.boundingBox());
    	return nextCollision;      
    }
    
    public void handleCollision(int collisionGizmo, int collisionSide){
    	ball=new Circle(x,y,radius);
        ArrayList<gizmosInterface> temp = new ArrayList<gizmosInterface>();
        temp = gizmos.retrieve(temp, this.boundingBox());
        
    	//handle the collision to update the velocity
    	velocity = temp.get(collisionGizmo).handleCollision(ball, velocity, collisionSide);

    	if(temp.get(collisionGizmo).getType()=='A'){
    		this.radius=0;
    	}
    }
    
	@Override
	public void paintComponents(Graphics g) {
		g.setColor(color);
        g.fillOval(x - radius, y - radius, radius + radius, radius
        		+ radius);
        g.setColor(color);
        g.drawOval(x - radius, y - radius, radius + radius, radius
        		+ radius);
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public char getType() {
		return this.type;
	}

	@Override
	public void select() {
		if(this.color == Configuration.colorOfBall){
			this.setColor(Color.white);
		}else{
			this.setColor(Configuration.colorOfBall);
		}
	}

	@Override
	public void rotate() {
		//do nothing
	}

	@Override
	public boolean isTriggered() {
		//do nothing
		return false;
	}

	@Override
	public void originalState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrigger(boolean trigger) {
		//do nothing
		
	}
	
	public void update(){
		//1. update the speed
		//2. time until
		//3. update position
		//4. reflect
		double[] output= new double[3];
		output[0]=Configuration.delta_t+1;
		this.updateVelocity(delta_t); 
		if(gizmos!=null){
			output = this.timeUntilNextCollision(gizmos); //output[0]-minTime; output[1]-index of the collided gizmo; output[2]-side of the collision
		}
			if(output[0] <= delta_t){
				//System.out.println("Output: "+output[0]+", "+output[1]+", "+output[2]+", ");
				this.move(output[0]);
				this.handleCollision((int)(output[1]),(int)(output[2]));
			}else{
				this.move(delta_t);
			}

		this.window.repaint();
	}


	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide) {
		//do nothing
		return null;
	}

	@Override
	public double[] timeUntilNextCollision(Circle ball, Vect velocity) {//TODO
	       double minTime = Configuration.delta_t+1; //Initially
	        double collisionGizmo = 0;
	        double collisionSide = -1; //0-top, 1-bottom, 2-left, 3-right, 4-topLeft, 5-topRight, 6-botLeft, 7-botRight

        double[] outPut = {minTime,collisionGizmo,collisionSide};
        return outPut;
	}

	@Override
	public int getOrientation() {
	
		return 0;
	}
	

}
