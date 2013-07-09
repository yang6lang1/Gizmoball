
    // Note the very indirect way control flow works during an animation:
    //
    // (1) We set up an eventListener with a reference to the animationWindow.
    // (2) We set up a timer with a reference to the eventListener.
    // (3) We call timer.start().
    // (4) Every 20 milliseconds the timer calls eventListener.actionPerformed()
    // (5) eventListener.actionPerformed() modifies the logical
    //     datastructure (e.g. changes the coordinates of the ball).
    // (6) eventListener.actionPerformed() calls myWindow.repaint.
    // (7) Swing schedules, at some point in the future, a call to 
    //      myWindow.paint()
    // (8) myWindow.paint() tells various objects to paint
    //     themselves on the provided Graphics context.
    //
    // This may seem very complicated, but it makes the coordination of
    // all the various different kinds of user input much easier.  For
    // example here is how control flow works when the user presses the
    // mouse button:
    //
    // (1) We set up an eventListener (actually we just use the same
    //     eventListener that is being used by the timer.)
    // (2) We register the eventListener with the window using the
    //     addMouseListener() method.
    // (3) Every time the mouse button is pressed inside the window the
    //     window calls eventListener.mouseClicked().
    // (4) eventListener.mouseClicked() modifies the logical
    //     datastructures.  (In this example it calls ball.randomBump(), but
    //     in other programs it might do something else, including request a
    //     repaint operation).
    //
	// During building mode, Gizmos should "snap" to a 1 L by 1 L grid


/*Building Mode:
 * Add any of the available types of gizmos to the playing area.
An attempt to place a gizmo in such a way that it overlaps a previously placed gizmo or the boundary of the playing area should be rejected (i.e., it should have no effect).
Move a gizmo from one place to another on the playing area.
An attempt to place a gizmo in such a way that it overlaps a previously placed gizmo or the boundary of the playing area should be rejected (i.e., it should have no effect).
Apply a 90 degree clockwise rotation to any gizmo.
Rotation has no effect on gizmos with rotational symmetry. For example, circular bumpers look and act the same, no matter how many times they have been rotated by 90 degrees.
Connect a particular gizmo's trigger to a particular gizmo's action.
The standard gizmos produce a trigger when hit by the ball, and exhibit at most one action (for example, moving a flipper, shooting the ball out of an absorber, or changing the color of a bumper). The trigger that a gizmo produces can be connected to the actions of many gizmos. Likewise, a gizmo's action can be activated by many triggers. The required triggers and actions for the basic gizmos are described below.
Note that triggers do not "chain". That is, when A is connected to B and B is connected to C, a ball hitting A should only cause the action of B to be triggered.
Connect a key-press trigger to the action of a gizmo.
Each keyboard key generates a unique trigger when pressed. As with gizmo-generated triggers, key-press triggers can also be connected to the actions of many gizmos.
Delete a gizmo from the playing area.
Add a ball to the playing area.
The user should be able to specify a position and velocity.
An attempt to place the ball in such a way that it overlaps a previously placed gizmo or the boundary of the playing area should be rejected (i.e., it should have no effect). There is one exception in the standard gizmo set: a stationary ball may be placed inside an absorber.
Save to a file named by the user.
You must be able to save to a file in the standard format given in Appendix 2. You may, if you wish, define an extension to the standard format that handles special features of your implementation. If you do so, the user must have the choice of saving in the standard format or in your special format.
The saved file must include information about all the gizmos currently in the playing area, all of the connections between triggers and actions, and the current position and velocity of the ball.
Load from a file named by the user. You must be able to load a game saved in the standard format.
Switch to running mode.
Quit the application.
 * */

/*Game Mode:
 * Press keys, thereby generating triggers that may be connected to the actions of gizmos.
Switch to building mode at any time.
If the user requests to switch to building mode while a flipper is in motion, it is acceptable to delay switching until the flipper has reached the end of its trajectory.
Similar short delays in order to finish transitional states of gizmos you create are also acceptable.
Quit the application.
In running mode, Gizmoball should:

Provide visually smooth animation of the motion of the ball.
The ball by default must have a diameter of approximately 0.5L.
Ball velocities must range at least from 0.01 L/sec to 200 L/sec and can cover a larger range if you wish. 0 L/sec (stationary) must also be supported.
An acceptable frame rate should be used to generate a smooth animation. We have found that 20 frames per second tends to work well across a reasonably wide range of platforms.
Provide intuitively reasonable interactions between the ball and the gizmos in the playing area. That is, the ball should bounce in the direction and with the resulting velocity that you would expect it to bounce in a physical pinball game.
Continually modify the velocity of the ball to account for the effects of gravity.
You should support the standard gravity value of 25 L/sec2, which resembles a pinball game with a slightly tilted playing surface.
Continually modify the velocity of the ball to account for the effects of friction.
You should model friction by scaling the velocity of the ball using the frictional constants mu and mu2. For sufficiently small delta_t's you can model friction as Vnew = Vold * (1 - mu * delta_t - mu2 * |Vold| * delta_t).
The default value of mu should be 0.025 per second.
The default value of mu2 should be 0.025 per L.

 * */
package app;

import interfaces.gizmosInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Timer;

import system.Constants;

import components.BouncingBall;
import components.circularBumper;
import components.gridElement;
import components.gridPanel;
import components.squareBumper;
import components.triangularBumper;

/*AnimationWindow contains all the oprations for game mode
 * */
public class AnimationWindow extends JComponent {
    private static final long serialVersionUID = 3257281448464364082L;

    // Controls how often we redraw
    private int FRAMES_PER_SECOND = Constants.FRAMES_PER_SECOND;

    private AnimationEventListener eventListener;
    private int number_of_grids_per_dimension =Constants.number_of_grids_per_dimension;
    private int gridSize = Constants.WIDTH*Constants.SCALE/number_of_grids_per_dimension;
    private gridPanel panel = new gridPanel();
    private BouncingBall ball;
    private Timer timer;
    private boolean mode;
    private gizmosInterface gizmos[]; //gizmos is a collection of all the Gizmos on the screen
    private gridElement element[][];  //element is the collection of all the grid elements (20*20)
    private int gizmoCount=0;//It counts how many Gizmos are currently on the window(ball is not included)
    	//TODO: Reduce gizmoCount when delete, increase when adding
        //Node: this variable can be used in update()

    //Constructor:
    public AnimationWindow(){
    	gizmos =new 
        		gizmosInterface[Constants.number_of_grids_per_dimension * Constants.number_of_grids_per_dimension];
    	ball = new BouncingBall(this);
    	panel.setBounds(0, 0, Constants.WIDTH*Constants.SCALE, Constants.HEIGHT*Constants.SCALE);
    	panel.setOpaque(false);
    	add(panel);
    	element = new gridElement[Constants.number_of_grids_per_dimension][Constants.number_of_grids_per_dimension];
    	for(int row=0;row<Constants.number_of_grids_per_dimension;row++){
    		for(int col=0;col<Constants.number_of_grids_per_dimension;col++){
    			element[col][row] = new gridElement(col*gridSize,row*gridSize,null);
    			System.out.println(element[col][row].toString());
    		}
    	}
 
        squareBumper square;
        triangularBumper triangle;
        circularBumper circle;
        circle = new circularBumper(8*Constants.L,0); this.addGizmos(circle);
        circle = new circularBumper(9*Constants.L,0); this.addGizmos(circle);
        circle = new circularBumper(10*Constants.L,0); this.addGizmos(circle);
        circle = new circularBumper(11*Constants.L,0); this.addGizmos(circle);

        triangle = new triangularBumper(12*Constants.L,0,0); this.addGizmos(triangle);
        triangle = new triangularBumper(13*Constants.L,0,90); this.addGizmos(triangle);
        triangle = new triangularBumper(14*Constants.L,0,180); this.addGizmos(triangle);
        triangle = new triangularBumper(15*Constants.L,0,270); this.addGizmos(triangle);

    	square = new squareBumper(16*Constants.L,0); this.addGizmos(square);
    	square = new squareBumper(17*Constants.L,0); this.addGizmos(square);
    	square = new squareBumper(18*Constants.L,0); this.addGizmos(square);
    	square = new squareBumper(19*Constants.L,0); this.addGizmos(square);
    	square = new squareBumper(19*Constants.L,19*Constants.L); this.addGizmos(square);
       
        // this only initializes the timer, we actually start and stop the
        // timer in the setMode() method
        eventListener = new AnimationEventListener();

        // The first parameter is how often (in milliseconds) the timer
        // should call us back. Ps: 1000ms
        timer = new Timer(1000 / FRAMES_PER_SECOND, eventListener);
        mode = false;
       
        this.setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    /**
     * @modifies g
     * @effects Repaints the Graphics area g.  Swing will then send the newly painted g to the screen.
     * @param g Graphics context received by either system or app calling repaint()
     */
    @Override public void paintComponent(Graphics g) {
        // first repaint the proper background color (controlled by
        // the windowing system)
        super.paintComponent(g);
        ball.paint(g);
       // square.paintSquare(g);
        for(int i=0; i <this.gizmoCount;i++){
        	gizmos[i].paintComponents(g);
        }
    }

    /**
     * This method is called when the Timer goes off and we
     * need to move and repaint the ball.
     * @modifies both the ball and the window that this listener owns 
     * @effects causes the ball to move and the window to be updated
     * to show the new position of the ball. 
     */
    private void update() {
        Rectangle oldPos = ball.boundingBox();

        ball.move(); // make changes to the logical animation state

        Rectangle repaintArea = oldPos.union(ball.boundingBox());

        // Have Swing tell the AnimationWindow to run its paint()
        // method.  One could also call repaint(), but this would
        // repaint the entire window as opposed to only the portion that
        // has changed.
        repaint(repaintArea.x, repaintArea.y, repaintArea.width,
                        repaintArea.height);
        
    }

    /**
     * @modifies this
     * @effects Turns the animation on/off. 
     * @param m Boolean indicating if animation is on/off
     */
    public void setMode(boolean m) {

        if (mode == m) {
            // Nothing to do.
            return;
        }

        if (mode == true) {
            // we're about to change mode: turn off all the old listeners
            removeMouseListener(eventListener);
            removeMouseMotionListener(eventListener);
            removeKeyListener(eventListener);
        }

        mode = m;

        if (mode == true) {
            // the mode is true: turn on the listeners
            addMouseListener(eventListener);
            addMouseMotionListener(eventListener);
            addKeyListener(eventListener);
            requestFocus(); // make sure keyboard is directed to us
            timer.start();
        }
        else {
            timer.stop();
        }
    }

    public void setGridInvisible(){
    	this.panel.setVisible(false);
    }

    public void setGridVisible(){
    	this.panel.setVisible(true);
    }

    public void setFPS(int fps){
    	this.FRAMES_PER_SECOND = fps;
    }
    //getters:
    public boolean getMode(){
    	return mode;
    }
    
    public int getNumberOfGizmos(){
    	return this.gizmoCount;
    }
    
    public gizmosInterface[] getGizmos(){
    	return gizmos;
    }
    
    public int getFPS(){
    	return this.FRAMES_PER_SECOND;
    }

    public void addGizmos(gizmosInterface gizmo){//TODO: in Gizmoball.java I need to check the gizmoCount
    									//if no places to place the gizmos I need to show some message
    	gizmos[this.gizmoCount++] =gizmo;
    }
    /**
     * Overview: AnimationEventListener is an inner class that 
     * responds to all sorts of external events, and provides the
     * required semantic operations for our particular program.  It 
     * owns, and sends semantic actions to the ball and window of the
     * outer class
     */
    class AnimationEventListener extends MouseAdapter implements
                    MouseMotionListener, KeyListener, ActionListener {

        // MouseAdapter gives us empty methods for the MouseListener
        // interface: mouseClicked, mouseEntered, mouseExited, mousePressed,
        // and mouseReleased.

        /**
         * For this example we only need to override mouseClicked
         * @modifes the ball that this listener owns
         * @effects causes the ball to be bumped in a random direction
         * @param e Detected MouseEvent
         */
        @Override public void mouseClicked(MouseEvent e) {
            ball.randomBump();
        }

        /**
         * MouseMotionListener interface
         * Override this method to act on mouse drag events. 
         * @param e Detected MouseEvent
         */ 
        public void mouseDragged(MouseEvent e) {
        }

        /**
         * MouseMotionListener interface
         * Override this method to act on mouse move events. 
         * @param e Detected MouseEvent
         */ 
        public void mouseMoved(MouseEvent e) {
        }

        /**
         * We implement the KeyListener interface so that we can bump the ball in a 
         * random direction if keys A-J is presse.
         * @modifies the ball that this listener owns
         * @effects causes the ball to be bumped in a random direction but 
         * only if one of the keys A-J is pressed.
         * @param e Detected Key Press Event
         */
        public void keyPressed(KeyEvent e) {
            // 
            int keynum = e.getKeyCode();

            if ((keynum >= 65) && (keynum <= 74)) {
                System.out.println("keypress " + e.getKeyCode());
                ball.randomBump();
            }
        }
        
        /**
         * Do nothing.
         * @param e Detected Key Released Event
         */
        public void keyReleased(KeyEvent e) {
        }

        /**
         * Do nothing.
         * @param e Detected Key Typed Event
         */
        public void keyTyped(KeyEvent e) {
        }

        /**
         * This is the callback for the timer
         * @param e ActionEvent generated by timer
         */
        public void actionPerformed(ActionEvent e) {
            update();
        }
    }
}


