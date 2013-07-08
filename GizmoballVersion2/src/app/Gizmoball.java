package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import system.Constants;

import components.circularBumper;
import components.squareBumper;
import components.triangularBumper;


public class Gizmoball extends JFrame{	
    private static final long serialVersionUID = 3257563992905298229L;
	private static final int width = Constants.WIDTH;
	private static final int height = Constants.HEIGHT;
	private static final int scale = Constants.SCALE;//scale factor for the window size
									// we can set based on our own need
	private static final int screenSize = width*scale;
	private static final boolean GAME_MODE = false;
	private static final boolean BUILD_MODE = true;
	private boolean mode = GAME_MODE; //0 - Game mode, 1 - Build modes
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

        //Lay out the content pane.
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setBackground(Color.BLACK);
        contentPane.setBounds(0, 0, (width/4)*scale*2+width*scale, height*scale);
        contentPane.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPane.setLayout(null);
 
        //Create the animation area used for output.
        animationWindow = new AnimationWindow();
        animationWindow.setBounds((width/4)*scale, 0, width*scale, height*scale);
        animationWindow.setGridInvisible();
        contentPane.add(animationWindow); 
    
        JToolBar gameToolBar = new JToolBar();
        gameToolBar=this.setGameToolBar();
        contentPane.add(gameToolBar);
        
        JToolBar buildToolBar = new JToolBar();
        buildToolBar=this.setBuildToolBar();
        contentPane.add(buildToolBar);
        }

    
    //Utility functions:
    //1. setGameToolBar()
    //2. setMenu()
    //3. setBuildToolBar()
    public JToolBar setGameToolBar(){
        //Create the gameToolBar.(the one on the left)
        JToolBar gameToolBar = new JToolBar();
        gameToolBar.setLayout(null);
        gameToolBar.setBounds(0, 0, (width/4)*scale, height*scale);
        gameToolBar.setForeground(Color.GRAY);        
        gameToolBar.setOrientation(SwingConstants.VERTICAL);
        gameToolBar.setFloatable(false);
        
        JPanel modePanel = new JPanel();
        modePanel.setLayout(null);
        modePanel.setBounds(0,0,(width/4)*scale,(int)(height*scale*11/32));
        modePanel.setForeground(Color.GRAY);
        modePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        modePanel.setOpaque(true);
        gameToolBar.add(modePanel);
        
        JPanel gameControlPanel = new JPanel();
        gameControlPanel.setLayout(null);
        gameControlPanel.setBounds(0,(int)(width*scale*11/32)+1,
        		(width/4)*scale,width*scale-(int)(height*scale*11/32)-1);
        gameControlPanel.setForeground(Color.GRAY);
        gameControlPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        gameControlPanel.setOpaque(true);
        gameToolBar.add(gameControlPanel);
        
        addButtonsToGameToolBar(gameToolBar,modePanel,gameControlPanel);
        return gameToolBar;
    }

    public JToolBar setBuildToolBar(){
        //Create the gameToolBar.(the one on the left)
        JToolBar buildToolBar = new JToolBar();
        buildToolBar.setLayout(null);
        buildToolBar.setBounds(width*scale+(width/4)*scale, 0, (width/4)*scale, width*scale);
        buildToolBar.setForeground(Color.GRAY);        
        buildToolBar.setOrientation(SwingConstants.VERTICAL);
        buildToolBar.setFloatable(false);
        
        JPanel gizmoPanel = new JPanel();
        gizmoPanel.setLayout(null);
        gizmoPanel.setBounds(0,0,(width/4)*scale,(int)(height*scale*23/64));
        gizmoPanel.setForeground(Color.GRAY);
        gizmoPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        gizmoPanel.setOpaque(true);
        buildToolBar.add(gizmoPanel);

        JPanel gizmoControlPanel = new JPanel();
        gizmoControlPanel.setLayout(null);
        gizmoControlPanel.setBounds(0,(int)(width*scale*23/64)+1,
        		(width/4)*scale,width*scale-(int)(height*scale*23/64)-1);
        gizmoControlPanel.setForeground(Color.GRAY);
        gizmoControlPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        gizmoControlPanel.setOpaque(true);
        buildToolBar.add(gizmoControlPanel);

        addButtonsToBuildToolBar(buildToolBar,gizmoPanel,gizmoControlPanel);

        return buildToolBar;
    }

    public JMenuBar setMenu(){
        //Where the GUI is created:
        JMenuBar menuBar;
        JMenu fileMenu,settingsMenu,helpMenu;
        JMenuItem menuItem;
        
        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_A);//TODO: What is this?
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "File options");
        fileMenu.setBackground(Color.GRAY);
        menuBar.add(fileMenu);
        fileMenu.setToolTipText("File options");
        
        //a group of JMenuItems
        ImageIcon icon = null;
        Image image = null;
        icon = new ImageIcon("res/New-Icon.png");
        image=icon.getImage().getScaledInstance(18, 18, 0);
        icon.setImage(image);
        menuItem = new JMenuItem(" New             ",
        		icon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.META_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Create a new game configuration");
        fileMenu.add(menuItem);

        icon = new ImageIcon("res/Open-Icon.png");
        image=icon.getImage().getScaledInstance(18, 18, 0);
        icon.setImage(image);
        menuItem = new JMenuItem(" Open           ",icon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_T, ActionEvent.META_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Load an existing game configuration");
        fileMenu.add(menuItem);        
        fileMenu.addSeparator();
   
        icon = new ImageIcon("res/Save-Icon.png");
        image=icon.getImage().getScaledInstance(18, 18, 0);
        icon.setImage(image);
        menuItem = new JMenuItem(" Save           ",icon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_S, ActionEvent.META_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Save the game configuration");
        fileMenu.add(menuItem);     
        fileMenu.addSeparator();
  
        menuItem = new JMenuItem(" Quit           ");//TODO: What is this?
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_Q, ActionEvent.META_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Quit Gizmoball");
        fileMenu.add(menuItem);  
      
        //Build second menu in the menu bar.
        settingsMenu = new JMenu("Settings");
        settingsMenu.setBackground(Color.GRAY);
        settingsMenu.setMnemonic(KeyEvent.VK_0);//TODO
        settingsMenu.getAccessibleContext().setAccessibleDescription(
                "Settings");
        menuBar.add(settingsMenu);

        menuItem = new JMenuItem("Advanced             ");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.META_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
            "Advanced settings of the current loaded game configuration");
        settingsMenu.add(menuItem);

        //Build third menu in the menu bar.
        helpMenu = new JMenu("Help");
        helpMenu.setBackground(Color.GRAY);
        helpMenu.setMnemonic(KeyEvent.VK_9);//TODO
        helpMenu.getAccessibleContext().setAccessibleDescription(
                "Help");
        menuBar.add(helpMenu);

        menuItem = new JMenuItem("About Gizmoball             ");
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//            KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
            "User manual for Gizmoball game");
        helpMenu.add(menuItem);

        menuItem = new JMenuItem("Credit             ");
//      menuItem.setAccelerator(KeyStroke.getKeyStroke(
//          KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
          "Credits");
        helpMenu.add(menuItem);

        menuBar.setPreferredSize(new Dimension((width/4)*scale*2+width*scale, 22));
        menuBar.setBackground(Color.GRAY);
        
        return menuBar;
    }

    protected void addButtonsToGameToolBar(JToolBar toolBar,JPanel panelOne,JPanel panelTwo){
    	JLabel title = null;
    	JButton button = null;
        ImageIcon icon = null;
        Image image=null;
   
        title = new JLabel("  Mode Control: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelOne.add(title);

        icon = new ImageIcon("res/Game-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/8), (int)(height*scale*3/32), 0);
        icon.setImage(image);
        //button = makeButtonWithImage("Game-Icon","Enter the Game Mode","Game");
        button=new JButton("Game Mode",icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.TOP);
        button.setToolTipText("Game mode");
        button.setBounds((int)(width*scale*1/16), (int)(width*scale*1/32), 
        		(int)(width*scale*1/8), (int)(width*scale*1/8));
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMode(GAME_MODE);
                System.out.println("Enter mode: "+mode);    
            }
        });
        panelOne.add(button);
        
        icon = new ImageIcon("res/Build-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/8), (int)(width*scale*3/32), 0);
        icon.setImage(image);
        button=new JButton("Build Mode",icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.TOP);
        button.setToolTipText("Build mode");
        button.setBounds((int)(width*scale*1/16), (int)(width*scale*3/16), 
        		(int)(width*scale*1/8), (int)(width*scale*1/8));
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMode(BUILD_MODE);
                System.out.println("Enter mode: "+mode);    
            }
        });
        panelOne.add(button);

        title = new JLabel("  Game Control: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelTwo.add(title);

        icon = new ImageIcon("res/Rewind-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16), (int)(width*scale*1/16), 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("Slow down");
        button.setBounds((int)(width*scale*1/64), (int)(width*scale*1/32), 
        		(int)(width*scale*1/16), (int)(width*scale*1/16));
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
        panelTwo.add(button);

        icon = new ImageIcon("res/Play-Pause-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16), (int)(width*scale*1/16), 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("Start running");
        button.setBounds((int)(width*scale*3/32), (int)(width*scale*1/32),
        		(int)(width*scale*1/16), (int)(width*scale*1/16));
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(animationWindow.getMode()==false){
                	animationWindow.setMode(true);
            	}else{
            		animationWindow.setMode(false);
            	}
            }
        });
        panelTwo.add(button);

        icon = new ImageIcon("res/Fastforward-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16), (int)(width*scale*1/16), 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("Slow down");
        button.setBounds((int)(width*scale*11/64), (int)(width*scale*1/32), 
        		(int)(width*scale*1/16),(int)(width*scale*1/16));
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO
            }
        });
        panelTwo.add(button);

    } 
    
    protected void addButtonsToBuildToolBar(JToolBar toolBar,JPanel panelOne,JPanel panelTwo){
 
    	JLabel title = null;
    	JButton button = null;
        ImageIcon icon = null;
        Image image=null;
       
        title = new JLabel("  Gizmos: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelOne.add(title);
        
        icon = new ImageIcon("res/Square-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("Add a square bumper");
        button.setBounds((int)(width*scale*1/32),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	animationWindow.addGizmos(new squareBumper());
            	animationWindow.repaint();    
            }
        });
        panelOne.add(button);

        icon = new ImageIcon("res/Triangle-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a triangular bumper");
        button.setBounds((int)(width*scale*9/64),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	animationWindow.addGizmos(new triangularBumper());
            	animationWindow.repaint();    
            }
        });
        panelOne.add(button);
        
        icon = new ImageIcon("res/Circle-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a circular bumper");
        button.setBounds((int)(width*scale*1/32),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	animationWindow.addGizmos(new circularBumper());
            	animationWindow.repaint();    
            }
        });
        panelOne.add(button);

        icon = new ImageIcon("res/Bouncing-Ball-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a bouncing ball");
        button.setBounds((int)(width*scale*9/64),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: call a menu: ask the user where to put the bouncing ball
            	System.out.println("A bouncing ball is added");    
            }
        });
        panelOne.add(button);
  
        icon = new ImageIcon("res/Left-Flipper-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a left flipper");
        button.setBounds((int)(width*scale*1/32),(int)(width*scale*1/4),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: add a gizmo
            	System.out.println("A left flipper is added");    
            }
        });
        panelOne.add(button);

        icon = new ImageIcon("res/Right-Flipper-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a right flipper");
        button.setBounds((int)(width*scale*9/64),(int)(width*scale*1/4),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: add a gizmo
            	System.out.println("A right flipper is added");    
            }
        });
        panelOne.add(button);

        title = new JLabel("  Gizmo Control: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelTwo.add(title);
 
        icon = new ImageIcon("res/Move-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("<html>Move selected gizmo with keyboard.<br/>Eg: up,down,left,right</html>");
        button.setBounds((int)(width*scale*1/32),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: move a gizmo
            	    
            }
        });
        panelTwo.add(button);

        icon = new ImageIcon("res/Rotate-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a triangular bumper");
        button.setBounds((int)(width*scale*9/64),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: add a gizmo
            	    
            }
        });
        panelTwo.add(button);
        
        icon = new ImageIcon("res/Delete-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a circular bumper");
        button.setBounds((int)(width*scale*1/32),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: add a gizmo
            	System.out.println("A circular bumper is added");    
            }
        });
        panelTwo.add(button);

        icon = new ImageIcon("res/Link-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        button=new JButton(icon);
        button.setToolTipText("Add a bouncing ball");
        button.setBounds((int)(width*scale*9/64),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: add a gizmo
            	System.out.println("A bouncing ball is added");    
            }
        });
        panelTwo.add(button);

    }
  
    protected void setMode(boolean mode){
    	this.mode=mode;
    	if(mode == GAME_MODE){
    		animationWindow.setGridInvisible();
    	}else{
    		animationWindow.setGridVisible();
    	}
    }
    
    public static void main(String[] args) {
    	
        Gizmoball frame = new Gizmoball();
	    // frame.getContentPane().add(yellowLabel, BorderLayout.CENTER);
		 
		//set the layout
		frame.getContentPane().setLayout(new BorderLayout());
     
		JMenuBar menubar = new JMenuBar();
		menubar = frame.setMenu();
	    frame.setJMenuBar(menubar);
        // the following code realizes the top level application window
       // frame.pack();
		frame.setBounds(0, 0, width*scale+(width/4)*scale*2, width*scale+22*2);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
