package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import app.AnimationWindow;

public class BouncingBall {

	private static double VELOCITY_STEP =2.0; //TODO: I want to allow the user to change the speed
	
    private int x = (int) ((Math.random() * 100.0) + 100.0); // TODO: the ball is not randomly 
															// generated on the screen
    private int y = (int) ((Math.random() * 100.0) + 100.0);

    //the horizontal velocity
    private int vx = (int) ((Math.random() * VELOCITY_STEP) + VELOCITY_STEP);
    //the vertical velocity
    private int vy = (int) ((Math.random() * VELOCITY_STEP) + VELOCITY_STEP);

    private int radius = 6;//radius of the ball
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

        x += vx;
        if (x <= radius) {
            x = radius;
            vx = -vx;
        }
        if (x >= window.getWidth() - radius) {
            x = window.getWidth() - radius;
            vx = -vx;
        }

        y += vy;
        if (y <= radius) {
            y = radius;
            vy = -vy;
        }
        if (y >= window.getHeight() - radius) {
            y = window.getHeight() - radius;
            vy = -vy;
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
