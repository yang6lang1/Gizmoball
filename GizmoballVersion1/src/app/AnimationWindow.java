package app;

import javax.swing.JComponent;
import javax.swing.Timer;

import Example.AnimationWindow.AnimationEventListener;

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
        super(); // do the standard JPanel setup stuff
        
        ball = new BouncingBall(this);

        // this only initializes the timer, we actually start and stop the
        // timer in the setMode() method
        eventListener = new AnimationEventListener();

        // The first parameter is how often (in milliseconds) the timer
        // should call us back. Ps: 1000ms
        timer = new Timer(1000 / FRAMES_PER_SECOND, eventListener);

        mode = false;

    }
}
