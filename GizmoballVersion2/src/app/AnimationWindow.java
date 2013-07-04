
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

package app;

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

import components.BouncingBall;

/*AnimationWindow contains all the oprations for game mode
 * */
public class AnimationWindow extends JComponent {
    private static final long serialVersionUID = 3257281448464364082L;

    // Controls how often we redraw
    private static int FRAMES_PER_SECOND = 100;

    private AnimationEventListener eventListener;
    private BouncingBall ball;
    private Timer timer;
    private boolean mode;

    //Constructor:
    public AnimationWindow(){
        //super(); // do the standard JPanel setup stuff
        
        ball = new BouncingBall(this);

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
        //super.paintComponent(g);
        ball.paint(g);
  
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

    public boolean getMode(){
    	return mode;
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


