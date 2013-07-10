package components;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;
import system.Constants;
import app.AnimationWindow;

public class BouncingBall {

	private static final double delta_t = Constants.delta_t/1000;
	
/*    private int x = (int) ((Math.random() * 100.0) + 100.0); // TODO: the ball is not randomly 
															// generated on the screen
    private int y = (int) ((Math.random() * 100.0) + 100.0);

    //the horizontal velocity
    private int vx = (int) ((Math.random() * speed) + speed);
    //the vertical velocity
    private int vy = (int) ((Math.random() * speed) + speed);*/
	
	//for testing purposes
	private int x = 632;
	private int y = 300;
	private double speed =Constants.SPEED; //TODO: this constant needs to be sued in later version
	private Angle angle = Constants.ANGLE;			  //eg: velocity & angle

	private int vx =(int)(speed*angle.cos());
	private int vy = (int)(speed*angle.sin());
	private int counter=0;
	private int counter2=0;
	private double[] timeForCollision;	


    private int radius = Constants.RADIUS;//radius of the ball
    private Color color = new Color(0,0,205);

    // Keep track of the animation window that will be drawing this ball.
    private AnimationWindow window;

    //Constructor:
    public BouncingBall(AnimationWindow win) {
        this.window = win;
        timeForCollision = new double[Constants.number_of_grids_per_dimension * Constants.number_of_grids_per_dimension];
        counter = 0;
        counter2 = 0;
    }

    public void setSpeed(double speed){
    	this.speed = speed;
    }
    
    public void setAngle(Angle angle){
    	this.angle = angle;
    }
    
    public double getSpeed(){
    	return this.speed;
    }
    
    public Angle getAngle(){
    	return this.angle;
    }
    
    /**
     * @modifies this
     * @effects Moves the ball according to its velocity.  Reflections off 
     * walls cause the ball to change direction.
     */
    public void move() {

    	//Friction:(angle not changed)
    	//Vnew = Vold * (1 - mu * delta_t - mu2 * |Vold| * delta_t).
        speed = speed *(1 - Constants.mu*delta_t - Constants.mu2*Math.abs(speed)*delta_t);
       	vy = (int)(speed*angle.sin());
    	//vx =(int)(vx *(1 - Constants.mu*delta_t - Constants.mu2*Math.abs(vx)*delta_t));
    	//vy = (int)(vy *(1 - Constants.mu*delta_t - Constants.mu2*Math.abs(vy)*delta_t));
        if(Math.abs(vx)>=20){
        	vx =(int)(speed*angle.cos());
        }else{
        	if(counter++ ==Constants.FRAME_REDRAW){
        		if(vx==0){
        			//do nothing
        		}else if(vx<0){
        			vx+=1;
        		}else{
        			vx-=1;
        		}
        		counter=1;
        	}

        }
  
    	x += vx;
        if (x <= radius) {
            x = radius;
            vx = -vx;
        }
        if (x >= window.getWidth() - radius) {
            x = window.getWidth() - radius;
            vx = -vx;
        }

        int delta_y = 0;
        double vy_old,vy_new;
        //change in vertical velocity due to gravity:
        //equation:vy_new = vy_old + gt
        vy_old = vy;
        vy_new =vy_old + Constants.GRAVITY_VALUE * delta_t;
        vy = (int)vy_new;
        
        //change in vertical distance due to gravity:
        //equation: delta_y = 1/2 * delta_t * (vy_old+vy)
        delta_y =(int)(0.5 * delta_t *(vy_old +vy));
        y += delta_y;
        
        if (y <= radius) {
        	//touch up
            y = radius;
            //vy = -vy-1;
            vy = -vy;
        }
        if (y >= window.getHeight() - radius) {
        	//touch down
            y = window.getHeight() - radius;
            //vy = -vy+1;
            vy = -vy;
        }
        
        //collide with Gizmos:
        Circle ball=new Circle(x,y,radius);
        Vect velocity= new Vect(vx,vy);
        gizmosInterface[] gizmos = this.window.getGizmos();        

        
       /* for(int i = 0; i<this.window.getNumberOfGizmos();i++){
        	switch (gizmos[i].getType()){
        	case 'S'://if it is a squareBumper: 4 sides
        		timeForCollision[i]=findMinTimeSquare(i,ball,velocity);
        		System.out.println("Time for collision with ball "+i+" is: "+timeForCollision);
        		break;
        	default:
        		break;
        	
        	}//end of switch
        	
        	//update the speed
        	speed = velocity.length();
        	angle = velocity.angle();
           	vx = (int)(speed*angle.cos());
           	vy = (int)(speed*angle.sin());

            	
        }*/
		

        
        
        //update speed and angle.
        if(vx==0 && vy ==0){
        	speed = 0;	
        	//give the speed a random angle
        	angle =new Angle(0.0, 1.0);
        }else if(vx ==0){//travelling vertically
        	if(vy>0){
        		angle = new Angle(0.0, 1.0);
        		//angle = angle.DEG_90;//travelling down
        	}else{
        		angle =new Angle(0.0, -1.0);
        		//angle = angle.DEG_270;//travelling up
        	}
    		speed = Math.abs(vy);
        }else if(vy == 0){//travelling horizontally
        	if(vx>0){
        		angle= new Angle(1.0, 0.0);
        		//angle = angle.ZERO;//travelling right
        	}else{
        		angle =new Angle(-1.0, 0.0);
        		//angle = angle.DEG_180;//travelling up
        	}
    		speed = Math.abs(vx);
        }else{
        	//if its not travelling horizontally or vertically
        	speed = Math.sqrt(vx*vx+vy*vy);
        	angle = new Angle(vx,vy);
        }

    }
    
    /**
     * @modifies this
     * @effects Changes the velocity of the ball by a random amount
     */
    public void randomBump() {//TODO: I don't really understand this method yet
        vx += (int) ((Math.random() * speed) - (speed/2));
        vx = -vx;
        vy += (int) ((Math.random() * speed) - (speed/2));
        vy = -vy;
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

    /**
     * @return the smallest rectangle that completely covers the current 
     * position of the ball.
     */
    public Rectangle boundingBox() {

        // a Rectangle is the x,y for the upper left corner and then the
        // width and height
        return new Rectangle(x - radius - 1, y - radius - 1, radius + radius + 2,
                        radius + radius + 2);
    }


    private double findMinTimeSquare(int i,Circle ball,Vect velocity){
        //collide with Gizmos:
        LineSegment top,bottom,left,right;
        gizmosInterface[] gizmos = this.window.getGizmos(); 
        double minTime;

	gizmos[i]=(squareBumper)gizmos[i];
	//top:(x1,y1,  x1+E,y1)
	top = new LineSegment(((squareBumper)gizmos[i]).getX(),
						   ((squareBumper)gizmos[i]).getY(),
						   ((squareBumper)gizmos[i]).getX()+((squareBumper)gizmos[i]).getEdge(),
						   ((squareBumper)gizmos[i]).getX());
	//bottom:(x1,y1+E,  x1+E,y1+E)
	bottom = new LineSegment(((squareBumper)gizmos[i]).getX(),
			   ((squareBumper)gizmos[i]).getY()+((squareBumper)gizmos[i]).getEdge(),
			   ((squareBumper)gizmos[i]).getX()+((squareBumper)gizmos[i]).getEdge(),
			   ((squareBumper)gizmos[i]).getX()+((squareBumper)gizmos[i]).getEdge());
	//left:(x1,y1,  x1,y1+E)
	left = new LineSegment(((squareBumper)gizmos[i]).getX(),
			   ((squareBumper)gizmos[i]).getY(),
			   ((squareBumper)gizmos[i]).getX(),
			   ((squareBumper)gizmos[i]).getX()+((squareBumper)gizmos[i]).getEdge());
	//left:(x1+E,y1,  x1+E,y1+E)
	right = new LineSegment(((squareBumper)gizmos[i]).getX()+((squareBumper)gizmos[i]).getEdge(),
			   ((squareBumper)gizmos[i]).getY(),
			   ((squareBumper)gizmos[i]).getX()+((squareBumper)gizmos[i]).getEdge(),
			   ((squareBumper)gizmos[i]).getX()+((squareBumper)gizmos[i]).getEdge());
	//ball = new Circle(x,y,radius);
	//velocity = new Vect(vx,vy);

	minTime = Geometry.timeUntilWallCollision(top, ball, velocity);
	if(minTime >=Geometry.timeUntilWallCollision(bottom, ball, velocity)){
		minTime = Geometry.timeUntilWallCollision(bottom, ball, velocity);
	}
	if(minTime>=Geometry.timeUntilWallCollision(left, ball, velocity)){
		minTime = Geometry.timeUntilWallCollision(left, ball, velocity);
	}
	if(minTime>= Geometry.timeUntilWallCollision(right, ball, velocity)){
		minTime = Geometry.timeUntilWallCollision(right, ball, velocity);
	}
	return minTime;
    }
	
}
