package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import system.Constants;
import app.AnimationWindow;

public class BouncingBall {

	private static double VELOCITY_STEP =Constants.VELOCITY_STEP; //TODO: this constant needs to be sued in later version
													//eg: velocity & angle
	
/*    private int x = (int) ((Math.random() * 100.0) + 100.0); // TODO: the ball is not randomly 
															// generated on the screen
    private int y = (int) ((Math.random() * 100.0) + 100.0);

    //the horizontal velocity
    private int vx = (int) ((Math.random() * VELOCITY_STEP) + VELOCITY_STEP);
    //the vertical velocity
    private int vy = (int) ((Math.random() * VELOCITY_STEP) + VELOCITY_STEP);*/
	
	//for testing purposes
	private int x = 600;
	private int y = 600;
	private int vx = -20;
	private int vy = -40;
	public static int counter=0;

    private int radius = Constants.RADIUS;//radius of the ball
    private Color color = new Color(0,0,205);

    // Keep track of the animation window that will be drawing this ball.
    private AnimationWindow window;

    //Constructor:
    public BouncingBall(AnimationWindow win) {
        this.window = win;
    }

    /**
     * @modifies this
     * @effects Moves the ball according to its velocity.  Reflections off 
     * walls cause the ball to change direction.
     */
    public void move() {
    	//friction: applied every 500 ms
    	if(counter++ ==50){
    		if(vx==0){
    			//do nothing
    		}else if(vx<0){
    			vx+=1;
    		}else{
    			vx-=1;
    		}
    		counter=1;
    	}

    	System.out.println(counter);

    	x += vx;
        if (x <= radius) {
            x = radius;
            vx = -vx;
        }
        if (x >= window.getWidth() - radius) {
            x = window.getWidth() - radius;
            vx = -vx;
        }

        //change in vertical velocity
        vy = (int)(vy + Constants.GRAVITATIONAL_CONSTANT);
        
        //change in vertical distance   
        y += vy;
        if (y <= radius) {
        	//touch up
            y = radius;
            vy = -vy-1;
        }
        if (y >= window.getHeight() - radius) {
        	//touch down
            y = window.getHeight() - radius;
            vy = -vy+1;
        }
    }
    
    /**
     * @modifies this
     * @effects Changes the velocity of the ball by a random amount
     */
    public void randomBump() {//TODO: I don't really understand this method yet
        vx += (int) ((Math.random() * VELOCITY_STEP) - (VELOCITY_STEP/2));
        vx = -vx;
        vy += (int) ((Math.random() * VELOCITY_STEP) - (VELOCITY_STEP/2));
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


	
}
