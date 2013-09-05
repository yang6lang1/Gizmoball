
package app;

/** 
 * AnimationWindow.java represents the game board when the game is in Game Mode. 
 * It contains many gizmos(bumpers and flippers and absorbers)
 * and one bouncing ball(this might be changed later depends on the game design)
 * The game board is updated at the rate of (1s/FPS) by a Timer thread.
 * The AnimationEventListener class handles to all the actions on keyboard and mouse when the
 * Timer thread is running.
 * */

import interfaces.gizmosInterface;
import interfaces.flippersInterface;

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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.Timer;

import system.Configuration;
import system.XMLReader;
import system.XMLWriter;

import components.ball;
import dataCollection.Quadtree;

public class AnimationWindow extends JComponent {
    private static final long serialVersionUID = 3257281448464364082L;
    
    // Controls how often we redraw
    private int FRAMES_PER_SECOND = Configuration.FRAMES_PER_SECOND;						//FPS
    private AnimationEventListener eventListener;
    private int screenSize = Configuration.WIDTH*Configuration.SCALE;
    private gridPanel panel;																//the build mode grid
    private ball ball;																		//bouncing ball
    private Timer timer;																	//major task scheduler/main thread
    private boolean mode;																	//running or stop
    private Gizmoball game;																	//game control panel
    private Quadtree<gizmosInterface> gizmos;												//collection of all the Gizmos on the screen(except the ball)
    private ArrayList<gizmosInterface> allGizmos;
    private Configuration gameConfig = new Configuration();									//all the game configuration data read in from XML file
    																						//Source file: Configuration.loadedConfig

    //Constructor:
    public AnimationWindow(Gizmoball game){
    	gizmos = gameConfig.getGizmos();
    	this.game = game;
    	ArrayList<gizmosInterface> allGizmos = new ArrayList<gizmosInterface>();
		allGizmos = gizmos.returnAll(allGizmos);
        
    	panel = new gridPanel(this.game);
    	panel.setBounds(0, 0, screenSize, screenSize);
    	panel.setBackground(Color.black);
    	//panel.setGizmos(gameConfig.getGizmos());
    	add(panel); 

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
     * Set up the game board
     * This method is used in the actionListener of "New" and "Open" menu item.
     * */
    public void newGameConfiguration(Configuration newGame){
    	if(newGame == null){
    		this.gizmos = new Quadtree<gizmosInterface>(new Rectangle(0,0,screenSize,screenSize));
	    	this.panel.setGizmos(gizmos);
	    	gameConfig = new Configuration();
	    	Configuration.loadedConfig=null;
	    	this.ball = null;
			allGizmos = gizmos.returnAll(allGizmos);

    	}else{
    		gameConfig = newGame;
    		this.gizmos = newGame.getGizmos();
    		this.panel.setGizmos(newGame.getGizmos());
    		this.ball = new ball(this,newGame);
		//	allGizmos = gizmos.returnAll(allGizmos);
    	}
    	this.panel.setIsDirty(false);
    	this.repaint();
    
    }
    
    public Configuration getLoadedGameConfiguration(){
    	return this.gameConfig;
    }
    
    public void setLoadedGameConfiguration(Configuration newGame){
    	this.gameConfig = newGame;
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
        
        if(ball!= null){
            ball.paint(g);
        }
        allGizmos=new ArrayList<gizmosInterface>();
        allGizmos = gizmos.returnAll(allGizmos);
        //paint the all the gizmos
		for(int i=0;i<allGizmos.size();i++){
			allGizmos.get(i).paintComponents(g.create());
		}

    }

    /**
     * This method is called when the Timer goes off and we
     * need torepaint the ball and the flippers.
     * @modifies both the ball and the window that this listener owns 
     * @effects causes the ball to move and the window to be updated
     * to show the new position of the ball and flippers. 
     */
    private void update() {    	
        Rectangle oldPos = ball.boundingBox();
       
        // Have Swing tell the AnimationWindow to run its paint()
        // method.  One could also call repaint(), but this would
        // repaint the entire window as opposed to only the portion that
        // has changed.
        ball.update();
        Rectangle repaintArea = oldPos.union(ball.boundingBox());
        repaint(repaintArea.x, repaintArea.y, repaintArea.width,
                        repaintArea.height);
    
        //paint the gridElements
		for(int i=0;i<allGizmos.size();i++){
        	if(allGizmos.get(i).getType()=='L'||allGizmos.get(i).getType()=='R'){

        		flippersInterface flipper =(flippersInterface) allGizmos.get(i);
        		oldPos = allGizmos.get(i).boundingBox();
        		if(flipper.isTriggered()&&!flipper.isKeyPressed()){
        			
	        		flipper.move(); //update the angle
        		}else if(flipper.isKeyPressed()){
        			flipper.moveUp();
        		}else{
	        		
        			allGizmos.get(i).originalState();
        		}
        		
                repaintArea = oldPos.union(allGizmos.get(i).boundingBox());
                repaint(repaintArea.x, repaintArea.y, repaintArea.width,repaintArea.height);
        	}//end of leftFlipper and rightFlipper case
		}

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
            
            /*for(int i= 0; i < gizmos.size();i++){//TODO:
            	if(gizmos.get(i).getType()=='L'||gizmos.get(i).getType()=='R'){
            		gizmos.get(i).setTrigger(true);
             	}
            }*/
            timer.start();
        }
        else {
        	 /* for(int i= 0; i < gizmos.size();i++){//TODO:
            	if(gizmos.get(i).getType()=='L'||gizmos.get(i).getType()=='R'){
            		gizmos.get(i).setTrigger(false);
             	}
        	  }
        	/*new java.util.Timer().schedule( 
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here
                        	 timer.stop();
                        }
                    }, 
                    500
                    );*/
            timer.stop();
        }
    }

    //this is used in Gizmoball.java when entering the Game Mode
    public void setGridInvisible(){
    	this.panel.setVisible(false);
    }

    //this is used in Gizmoball.java when entering the Build Mode
    public void setGridVisible(){
    	this.panel.setVisible(true);
    }

    public void setFPS(int fps){
    	this.FRAMES_PER_SECOND = fps;
    }

    public void setGizmos(Quadtree<gizmosInterface> gizmos){
    	this.gizmos = gizmos;
    }
    
    public boolean getMode(){
    	return mode;
    }
    
    public ball getBall(){
    	return this.ball;
    }
    
    public int getNumberOfGizmos(){
    	return this.allGizmos.size();
    }
    
    public gridPanel getGridPanel(){
    	return this.panel;
    }
    
    public Quadtree<gizmosInterface> getGizmos(){
    	return this.gizmos;
    }
    
    public int getFPS(){
    	return this.FRAMES_PER_SECOND;
    }
    
    public void restartGame(){//TODO:this method might not be necessary later, or should be modified
    	this.ball = new ball(this,this.gameConfig);        

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
            //ball.randomBump();
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
            
            // When keyboard 'Z' is pressed, all the leftFlippers should move up
            if(keynum == 90){
                for(int i= 0; i < allGizmos.size();i++){
                	if(allGizmos.get(i).getType()=='L'){
                		//flippersInterface flipper =(flippersInterface) gizmos.get(i);
                		((flippersInterface) allGizmos.get(i)).setKeyPressed(true);
                	}//end of leftFlipper and rightFlipper case
                }  
            }
            
            // When keyboard '/' is pressed, all the rightFlippers should move up
            if(keynum == 47){// keyboard '/'
                for(int i= 0; i < allGizmos.size();i++){
                	if(allGizmos.get(i).getType()=='R'){
                		//flippersInterface flipper =(flippersInterface) gizmos.get(i);
                		((flippersInterface) allGizmos.get(i)).setKeyPressed(true);
                	}//end of leftFlipper and rightFlipper case
                }  
            }
            
            /*if ((keynum >= 30) && (keynum <= 100)) {
                System.out.println("keypress " + e.getKeyCode());
               // ball.randomBump();
            }*/
        }
        
        /**
         * Do nothing.
         * @param e Detected Key Released Event
         */
        public void keyReleased(KeyEvent e) {
        	 
            int keynum = e.getKeyCode();
            
            // When keyboard 'Z' is pressed, all the leftFlippers should move back to original
            if(keynum == 90){
            	//System.out.println("keyrelease " + e.getKeyCode());
                for(int i= 0; i < allGizmos.size();i++){
                	if(allGizmos.get(i).getType()=='L'){
                		//flippersInterface flipper =(flippersInterface) gizmos.get(i);
                		((flippersInterface) allGizmos.get(i)).setKeyPressed(false);
                	}//end of leftFlipper and rightFlipper case
                }  
            }
            
            // When keyboard '/' is pressed, all the rightFlippers should move back to original
            if(keynum == 47){
            	//System.out.println("keypress " + e.getKeyCode());
                for(int i= 0; i < allGizmos.size();i++){
                	if(allGizmos.get(i).getType()=='R'){
                		//flippersInterface flipper =(flippersInterface) gizmos.get(i);
                		((flippersInterface) allGizmos.get(i)).setKeyPressed(false);
                	}//end of leftFlipper and rightFlipper case
                }  
            }
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


