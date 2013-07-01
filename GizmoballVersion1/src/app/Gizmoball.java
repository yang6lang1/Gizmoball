package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;


public class Gizmoball extends JFrame{	
    private static final long serialVersionUID = 3257563992905298229L;
    private static final int GAME_MODE= 0;
	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH;
	public static final int SCALE = 4;//scale factor for the window size
									// we can set based on our own need

	private int mode = 0; //0 - Game mode, 1 - Build modes
    protected AnimationWindow animationWindow;

    /**
     * @effects Initializes the application window so that it contains
     * a toolbar and an animation window.
     */
    public Gizmoball() {
        // Title bar
        super("Gizmaball");

        // respond to the window system asking us to quit
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        

        //Create the topToolbar.
        JToolBar topToolbar = new JToolBar();
        topToolbar.setBounds(0, 0, WIDTH*SCALE+100, 22);
        addButtons(topToolbar);
        topToolbar.setFloatable(false);
        topToolbar.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //Create the animation area used for output.
        animationWindow = new AnimationWindow();
        animationWindow.setBounds(100, 22, WIDTH*SCALE,  WIDTH*SCALE);
        animationWindow.setOpaque(false);
         
        //Lay out the content pane.
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setBackground(Color.BLACK);
        contentPane.setBounds(0, 0, WIDTH*SCALE+100, WIDTH*SCALE+22);
        contentPane.add(topToolbar);
        contentPane.add(animationWindow,BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createLineBorder(Color.black));

        //Create the leftToolBar.
        JToolBar leftToolBar = new JToolBar();
        leftToolBar.setBounds(0, 22, 100, WIDTH*SCALE);
        leftToolBar.setForeground(Color.GRAY);
        leftToolBar.setOrientation(SwingConstants.VERTICAL);
        leftToolBar.setFloatable(false);
        contentPane.add(leftToolBar);
        addButtonsToLeftToolBar(leftToolBar);
                
        //Create the bottomToolBar.
        JToolBar bottomToolBar = new JToolBar();
        bottomToolBar.setBounds(0, WIDTH*SCALE+22, WIDTH*SCALE+100, 22);
        bottomToolBar.setFloatable(false);
        contentPane.add(bottomToolBar);
        addButtonsToBottomToolBar(bottomToolBar);
        bottomToolBar.setBorder(BorderFactory.createLineBorder(Color.black));
      
    }

    /**
     * @modifies toolBar 
     * @effects adds Save, Load, Run, Stop and Quit buttons to toolBar
     * @param toolbar to add buttons to.
     */
    protected void addButtons(JToolBar toolBar) {

        JButton button = null;

        button = new JButton("Save");
        button.setToolTipText("Save the game configuration");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);
        
        button = new JButton("Load");
        button.setToolTipText("Load the game configuration");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);
        
        button = new JButton("Run");
        button.setToolTipText("Start the animation");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animationWindow.setMode(true);
            }
        });
        toolBar.add(button);

        button = new JButton("Stop");
        button.setToolTipText("Stop the animation");
        // when this button is pushed it calls animationWindow.setMode(false)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animationWindow.setMode(false);
            }
        });
        toolBar.add(button);

        button = new JButton("Quit");
        button.setToolTipText("Quit the program");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        toolBar.add(button);
    }
    
    protected void addButtonsToLeftToolBar(JToolBar toolBar){
        JButton button = null;

        button = new JButton("Game Mode");
        button.setToolTipText("Game mode");
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setMode(GAME_MODE);
            }
        });
        toolBar.add(button);

        button = new JButton("Build Mode");
        button.setToolTipText("Build your own Gizmoball game");
        // when this button is pushed it enters the build mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               setMode(GAME_MODE+1);
            }
        });
        toolBar.add(button);

        toolBar.addSeparator();
        
        button = new JButton("Square");
        button.setToolTipText("Place a square Gizmo in the playing area");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

        button = new JButton("Triangle");
        button.setToolTipText("Place a triangular Gizmo in the playing area");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

        button = new JButton("Circle");
        button.setToolTipText("Place a circular Gizmo in the playing area");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

        button = new JButton("LeftFilpper");
        button.setToolTipText("Click to flip the left flipper");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);
        
        button = new JButton("RightFlipper");
        button.setToolTipText("Click to flip the right flipper");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

    } 
    
    protected void addButtonsToBottomToolBar(JToolBar toolBar){
        JButton button = null;

        button = new JButton("Move");
        button.setToolTipText("Move the selected Gizmo");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

        button = new JButton("Rotate");
        button.setToolTipText("rotate the selected Gizmo clockwizely");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

        button = new JButton("Delete");
        button.setToolTipText("Delete the selected Gizmo");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

        button = new JButton("Connect");
        button.setToolTipText("Connect the selected action of Gizmo with other Gizmo");
        // when this button is pushed it calls animationWindow.setMode(true)
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               //TODO: implement the save function
            }
        });
        toolBar.add(button);

    }
  
    protected void setMode(int mode){
    	this.mode=mode;
    }
    
    public static void main(String[] args) {
        Gizmoball frame = new Gizmoball();
	    // frame.getContentPane().add(yellowLabel, BorderLayout.CENTER);
		 
		//set the layout
		frame.getContentPane().setLayout(new BorderLayout());
     
        // the following code realizes the top level application window
        frame.pack();
		frame.setBounds(0, 22, WIDTH*SCALE+100, WIDTH*SCALE+22*3);
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
