package app;

/** 
 * Gizmoball.java contains the code for the front-end interface, including
 * buttons, panels, animationWindow(the game board), menu bar,etc...
 * The important methods in this .java file are:
 * 1. the constructor: public Gizmoball(){...}
 * 2. all the ActionListeners for buttons
 * 3. all the ActionListeners for menuItems(New, Open, Save, Save As)
 *    the rest of them are not done
 * 4. setMode(): this is the method used to switch between build mode and game mode
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import system.Configuration;
import system.XMLReader;
import system.XMLWriter;

import components.absorber;
import components.circleBumper;
import components.leftFlipper;
import components.rightFlipper;
import components.squareBumper;
import components.triangleBumper;

public class Gizmoball extends JFrame{	
    private static final long serialVersionUID = 3257563992905298229L;
	private static final int width = Configuration.WIDTH;									//screen width
	private static final int height = Configuration.HEIGHT;									//screen height
	private static final int scale = Configuration.SCALE;									//scale factor for the window size
	private static final boolean GAME_MODE = false;
	private static final boolean BUILD_MODE = true;
	private boolean mode = GAME_MODE; 														//0 - Game mode, 1 - Build modes
	private boolean moveMode = false;														//whether the "move" button is pressed											
    protected AnimationWindow animationWindow;												//the instance of the game board
    private JButton squareBumperButton,triangularBumperButton,circularBumperButton,
    leftFlipperButton,rightFlipperButton,absorberButton,moveButton,rotateButton,deleteButton,
    connectButton,slowDownButton,playPauseButton,fastForwardButton,gameButton,buildButton;
    private XMLReader read = new XMLReader();												//Used by "Open" menu item
    JFileChooser fc;																		//file chooser

    /**
     * The default constructor:
     * It constructs a game window in the following steps:
     * 1. miscellaneous settings: the title of the frame, how the program exits, etc..
     * 2. Set up the content pane: content pane is the very bottom panel inside of the frame
     * 	  all the other JComponents are layed on top of that
     * 3. Create an instance of the game board and put it on top of the content pane
     * 4. Create menu and toolbar that contains all the buttons
     * 5. Set the mode as Game Mode(by default)
     */
    public Gizmoball() {
        // Title bar
        super("Gizmaball");
        
        // respond to the window system asking us to quit
        // when the user clicks 'x'(close button) on the top right of the window,
        // the program stops
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
       
        //Create a file chooser
        fc = new JFileChooser();

        //Lay out the content pane.
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setBackground(Color.BLACK);
        contentPane.setBounds(0, 0, (width/4)*scale*2+width*scale, height*scale);
        contentPane.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPane.setLayout(null);
 
        Configuration gameConfig= read.readConfig(Configuration.DEFAULT_GAME_CONFIG,Configuration.DEFAULT_GAME_SCHEMA);	
        //Create the animation area used for output.
        animationWindow = new AnimationWindow(this);
        animationWindow.setBounds((width/4)*scale, 0, width*scale, height*scale);
        animationWindow.newGameConfiguration(gameConfig);

        //animationWindow.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        contentPane.add(animationWindow); 
    
        JToolBar gameToolBar = new JToolBar();
        gameToolBar=this.setGameToolBar();
        contentPane.add(gameToolBar);
        
        JToolBar buildToolBar = new JToolBar();
        buildToolBar=this.setBuildToolBar();
        contentPane.add(buildToolBar);
        
        setMode(GAME_MODE);
    }

    /*
     * Utility functions:
     * 1. setGameToolBar()
     * 2. setMenu()
     * 3. setBuildToolBar()
     * */
    
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
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Create a new game configuration");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(animationWindow.getMode()==true){
            		//stop the game if it is running
                	animationWindow.setMode(false);
            	}
            	
            	//1. check if the gridPanel is dirty
                if(animationWindow.getGridPanel().isDirty()){
                	//Yes-ask if the user wants to save the game configuration
                    int n = JOptionPane.showConfirmDialog(
                            animationWindow, "The game configuration has been changed. Do you want to save it?",
                            "Unsaved changes",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                    	System.out.println("The user wants to save it");
                    	saveConfig();
                    } else if (n == JOptionPane.NO_OPTION) {
                    	System.out.println("The user Doesn't want to save it");
                    	
                    } else {
                    	System.out.println("The user cancelled the operation");
                    	return;
                    }  	
                }

            	animationWindow.newGameConfiguration(null);
        		animationWindow.getGridPanel().requestFocusInWindow();
            }
        });
        fileMenu.add(menuItem);

        icon = new ImageIcon("res/Open-Icon.png");
        image=icon.getImage().getScaledInstance(18, 18, 0);
        icon.setImage(image);
        menuItem = new JMenuItem(" Open           ",icon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_T, ActionEvent.META_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Load an existing game configuration");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(animationWindow.getMode()==true){
            		//stop the game if it is running
                	animationWindow.setMode(false);
            	}
            	
            	//1. set default directory
                fc.setCurrentDirectory(new java.io.File("./Save"));		
                
                //2. set the fileFilter to .xml file only
                FileFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                fc.setFileFilter(xmlfilter);

                //3. check if the game configuration on the game board has been modified
                //	yes - ask if the user wants to save it
                //  no - go to step 4
                if(animationWindow.getGridPanel().isDirty()){
                
                    int n = JOptionPane.showConfirmDialog(
                            animationWindow, "The game configuration has been changed. Do you want to save it?",
                            "Unsaved changes",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                    	System.out.println("The user wants to save it");
                    	saveConfig();

                    } else if (n == JOptionPane.NO_OPTION) {
                    	System.out.println("The user Doesn't want to save it");
                    } else {
                    	System.out.println("The user cancelled the operation");
                    	return;
                    }  	
                }
                    //4. select the file and update the Configuration.loadedConfig           	
                    int returnVal = fc.showOpenDialog(Gizmoball.this);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {//TODO: error handler?
                        System.out.println("getSelectedFile() : " +  fc.getSelectedFile());
                    	//Configuration.loadedConfig = fc.getSelectedFile().toString();
                        Configuration gameConfig= read.readConfig(fc.getSelectedFile().toString(),Configuration.DEFAULT_GAME_SCHEMA);	
                        Configuration.loadedConfig = fc.getSelectedFile().toString();
                        animationWindow.newGameConfiguration(gameConfig);
                    } else {
                    	System.out.println("Open command cancelled by user.");
                    }
                
                
                if(mode != GAME_MODE){// in the build mode
                	animationWindow.getGridPanel().requestFocusInWindow();
                }
            }
        });
        fileMenu.add(menuItem);        
        fileMenu.addSeparator();
   
        icon = new ImageIcon("res/Save-Icon.png");
        image=icon.getImage().getScaledInstance(18, 18, 0);
        icon.setImage(image);
        menuItem = new JMenuItem(" Save           ",icon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Save the game configuration");
      	
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	if(animationWindow.getMode()==true){
            		//stop the game if it is running
                	animationWindow.setMode(false);
            	}
		        saveConfig();
            }
        });
        
        
        fileMenu.add(menuItem);    
        
        icon = new ImageIcon("res/Save-As-Icon.png");
        image=icon.getImage().getScaledInstance(18, 18, 0);
        icon.setImage(image);
        menuItem = new JMenuItem(" Save As         ",icon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"Save the game configuration");
      	
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	if(animationWindow.getMode()==true){
            		//stop the game if it is running
                	animationWindow.setMode(false);
            	}
            	Configuration.loadedConfig = null;
		        saveConfig();
            }
        });
        
        
        fileMenu.add(menuItem);     
        fileMenu.addSeparator();
  
        menuItem = new JMenuItem(" Quit           ");//TODO
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
        ImageIcon icon = null;
        Image image=null;
           
        title = new JLabel("  Mode Control: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelOne.add(title);

        icon = new ImageIcon("res/Game-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/8), (int)(height*scale*3/32), 0);
        icon.setImage(image);
        //button = makeButtonWithImage("Game-Icon","Enter the Game Mode","Game");
        gameButton=new JButton("Play",icon);
        gameButton.setHorizontalTextPosition(JButton.CENTER);
        gameButton.setVerticalTextPosition(JButton.TOP);
        gameButton.setToolTipText("Game");
        gameButton.setBounds((int)(width*scale*1/16), (int)(width*scale*1/32), 
        		(int)(width*scale*1/8), (int)(width*scale*1/8));
        // when this button is pushed it enters game mode
        gameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
		    	if(animationWindow.getGridPanel().isSelected()){
		    		animationWindow.getGridPanel().getBuffer().select();
		    		animationWindow.getGridPanel().setSelected(false);
		    		animationWindow.getGridPanel().selectGizmo(null);
		    	}
	                setMode(GAME_MODE);
	               ///] System.out.println("Enter mode: "+mode);    
            }
        });
        panelOne.add(gameButton);
        
        icon = new ImageIcon("res/Build-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/8), (int)(width*scale*3/32), 0);
        icon.setImage(image);
        buildButton=new JButton("Build",icon);
        buildButton.setHorizontalTextPosition(JButton.CENTER);
        buildButton.setVerticalTextPosition(JButton.TOP);
        buildButton.setToolTipText("Build mode");
        buildButton.setBounds((int)(width*scale*1/16), (int)(width*scale*3/16), 
        		(int)(width*scale*1/8), (int)(width*scale*1/8));
        // when this button is pushed it enters game mode
        buildButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMode(BUILD_MODE);  
            }
        });
        panelOne.add(buildButton);
        
        title = new JLabel("  Game Control: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelTwo.add(title);

        icon = new ImageIcon("res/Rewind-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16), (int)(width*scale*1/16), 0);
        icon.setImage(image);
        slowDownButton=new JButton(icon);
        slowDownButton.setToolTipText("Slow down");
        slowDownButton.setBounds((int)(width*scale*1/64), (int)(width*scale*1/32), 
        		(int)(width*scale*1/16), (int)(width*scale*1/16));
        slowDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
        panelTwo.add(slowDownButton);

        icon = new ImageIcon("res/Play-Pause-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16), (int)(width*scale*1/16), 0);
        icon.setImage(image);
        playPauseButton=new JButton(icon);
        playPauseButton.setToolTipText("Start running");
        playPauseButton.setBounds((int)(width*scale*3/32), (int)(width*scale*1/32),
        		(int)(width*scale*1/16), (int)(width*scale*1/16));
        // when this button is pushed it enters game mode
        playPauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(animationWindow.getMode()==false){
                	animationWindow.setMode(true);
            	}else{
            		animationWindow.setMode(false);
            	}
            }
        });
        panelTwo.add(playPauseButton);

        icon = new ImageIcon("res/Fastforward-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16), (int)(width*scale*1/16), 0);
        icon.setImage(image);
        fastForwardButton=new JButton(icon);
        fastForwardButton.setToolTipText("Slow down");
        fastForwardButton.setBounds((int)(width*scale*11/64), (int)(width*scale*1/32), 
        		(int)(width*scale*1/16),(int)(width*scale*1/16));
        // when this button is pushed it enters game mode
        fastForwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO
            }
        });
        panelTwo.add(fastForwardButton);

    } 
    
    protected void addButtonsToBuildToolBar(JToolBar toolBar,JPanel panelOne,JPanel panelTwo){
 
    	JLabel title = null;
        ImageIcon icon = null;
        Image image=null;
        
        title = new JLabel("  Gizmos: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelOne.add(title);
        
        icon = new ImageIcon("res/Square-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);
        squareBumperButton=new JButton(icon);
        squareBumperButton.setToolTipText("Add a square bumper");
        squareBumperButton.setBounds((int)(width*scale*1/32),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        squareBumperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	animationWindow.getGridPanel().addGizmos(new squareBumper(0,0));
            	animationWindow.getGridPanel().repaint();
            	animationWindow.getGridPanel().requestFocusInWindow();
            	animationWindow.getGridPanel().setIsDirty(true);
            }
        });
        panelOne.add(squareBumperButton);

        icon = new ImageIcon("res/Triangle-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        triangularBumperButton=new JButton(icon);
        triangularBumperButton.setToolTipText("Add a triangular bumper");
        triangularBumperButton.setBounds((int)(width*scale*9/64),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        triangularBumperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	animationWindow.getGridPanel().addGizmos(new triangleBumper());
            	animationWindow.getGridPanel().repaint();  
            	animationWindow.getGridPanel().requestFocusInWindow();
            	animationWindow.getGridPanel().setIsDirty(true);
            }
        });
        panelOne.add(triangularBumperButton);
        
        icon = new ImageIcon("res/Circle-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        circularBumperButton=new JButton(icon);
        circularBumperButton.setToolTipText("Add a circular bumper");
        circularBumperButton.setBounds((int)(width*scale*1/32),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        circularBumperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	animationWindow.getGridPanel().addGizmos(new circleBumper());
            	animationWindow.getGridPanel().repaint();    
            	animationWindow.getGridPanel().requestFocusInWindow();
            	animationWindow.getGridPanel().setIsDirty(true);
            }
        });
        panelOne.add(circularBumperButton);

        icon = new ImageIcon("res/Absorber-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        absorberButton=new JButton(icon);
        absorberButton.setToolTipText("Add an absorber");
        absorberButton.setBounds((int)(width*scale*9/64),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        absorberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	/*JTextField firstName = new JTextField();
            	JTextField lastName = new JTextField();
            	JPasswordField password = new JPasswordField();
            	final JComponent[] inputs = new JComponent[] {
            			new JLabel("First"),
            			firstName,
            			new JLabel("Last"),
            			lastName,
            			new JLabel("Password"),
            			password
            	};
            	
            	JOptionPane.showMessageDialog(null, inputs, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
            	System.out.println("You entered " +
            			firstName.getText() + ", " +
            			lastName.getText() + ", " +
            			password.getPassword()); */
            	animationWindow.getGridPanel().addGizmos(new absorber());
            	animationWindow.getGridPanel().repaint();    
            	animationWindow.getGridPanel().requestFocusInWindow();
            	animationWindow.getGridPanel().setIsDirty(true);

            }
        });
        panelOne.add(absorberButton);
  
        icon = new ImageIcon("res/Left-Flipper-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        leftFlipperButton=new JButton(icon);
        leftFlipperButton.setToolTipText("Add a left flipper");
        leftFlipperButton.setBounds((int)(width*scale*1/32),(int)(width*scale*1/4),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        leftFlipperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	leftFlipper newFlipper =new leftFlipper();
            	//newFlipper.setTrigger(true);//TODO: testing purpose
            	animationWindow.getGridPanel().addGizmos(newFlipper);
            	animationWindow.getGridPanel().repaint();   
            	animationWindow.getGridPanel().requestFocusInWindow();
            	animationWindow.getGridPanel().setIsDirty(true);
            }
        });
        panelOne.add(leftFlipperButton);

        icon = new ImageIcon("res/Right-Flipper-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        rightFlipperButton=new JButton(icon);
        rightFlipperButton.setToolTipText("Add a right flipper");
        rightFlipperButton.setBounds((int)(width*scale*9/64),(int)(width*scale*1/4),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        rightFlipperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	rightFlipper newFlipper =new rightFlipper();
            	//newFlipper.setTrigger(true);
            	animationWindow.getGridPanel().addGizmos(newFlipper);
            	animationWindow.getGridPanel().repaint();   
            	animationWindow.getGridPanel().requestFocusInWindow();
            	animationWindow.getGridPanel().setIsDirty(true);
            }
        });
        panelOne.add(rightFlipperButton);

        title = new JLabel("  Gizmo Control: ");
        title.setBounds(0,0,(width/4)*scale,(int)(width*scale*1/32));
        panelTwo.add(title);
 
        icon = new ImageIcon("res/Move-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);
        moveButton=new JButton(icon);
        moveButton.setToolTipText("<html>Move selected gizmo with keyboard.<br/>Quit move mode by pressing Esc.</html>");
        moveButton.setBounds((int)(width*scale*1/32),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, the gridPanel should enter move mode
        moveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//1. check if the destination is available
            	//2. delete the gizmo in the original place
            	//3. add a new gizmo in the destination
            	if(!animationWindow.getGridPanel().isSelected()){
            		System.out.println("Please select a ball");
            	}else{
            		setMoveMode(true); //enable move mode	
            		animationWindow.getGridPanel().requestFocusInWindow();
            	}
            	
            }
        });
        panelTwo.add(moveButton);

        icon = new ImageIcon("res/Rotate-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        rotateButton=new JButton(icon);
        rotateButton.setToolTipText("Add a triangular bumper");
        rotateButton.setBounds((int)(width*scale*9/64),(int)(width*scale*1/32),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, the selected gizmo is rotated 90 clockwise
        rotateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(animationWindow.getGridPanel().getBuffer()!= null){

            		Rectangle rotateArea = animationWindow.getGridPanel().getBuffer().boundingBox();
	            	animationWindow.getGridPanel().getBuffer().rotate();
	            	animationWindow.getGridPanel().repaint(rotateArea.x,rotateArea.y,rotateArea.width,rotateArea.height);
	            	animationWindow.getGridPanel().requestFocusInWindow();
	            	animationWindow.getGridPanel().setIsDirty(true);
            	}
            }
        });
        panelTwo.add(rotateButton);
        
        icon = new ImageIcon("res/Delete-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        deleteButton=new JButton(icon);
        deleteButton.setToolTipText("Add a circular bumper");
        deleteButton.setBounds((int)(width*scale*1/32),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a selected gizmo is deleted
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(animationWindow.getGridPanel().getBuffer()!= null){
            		animationWindow.getGridPanel().deleteGizmos(animationWindow.getGridPanel().getBuffer());
            		animationWindow.getGridPanel().repaint();
            		animationWindow.getGridPanel().requestFocusInWindow();
                	animationWindow.getGridPanel().setIsDirty(true);
            	}
            	  
            }
        });
        panelTwo.add(deleteButton);

        icon = new ImageIcon("res/Link-Icon.png");
        image=icon.getImage().getScaledInstance((int)(width*scale*1/16),(int)(width*scale*1/16), 0);
        icon.setImage(image);     
        connectButton=new JButton(icon);
        connectButton.setToolTipText("Add a bouncing ball");
        connectButton.setBounds((int)(width*scale*9/64),(int)(width*scale*9/64),
        		(int)(width*scale*5/64),(int)(width*scale*5/64));
        // when this button is pressed, a square bumper is added to the game board
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO: add a gizmo
        		animationWindow.getGridPanel().requestFocusInWindow();
            	animationWindow.getGridPanel().setIsDirty(true);
            	System.out.println("A bouncing ball is added");    
            }
        });
        panelTwo.add(connectButton);

    }
  
    protected void setMode(boolean mode){
    	this.mode=mode;
    	if(mode == GAME_MODE){
    		//animationWindow.restartGame();
    			this.moveMode=false;
            	animationWindow.setMode(false);
        		animationWindow.restartGame();
	    		animationWindow.setGridInvisible();
	    	   // animationWindow.requestFocusInWindow();
	    		animationWindow.setGizmos(animationWindow.getGridPanel().getGizmos());
	    		squareBumperButton.setEnabled(false);
	    		triangularBumperButton.setEnabled(false);
	    		circularBumperButton.setEnabled(false);
	    		absorberButton.setEnabled(false);
	    		leftFlipperButton.setEnabled(false);
	    		rightFlipperButton.setEnabled(false);
	    		moveButton.setEnabled(false);
	    		rotateButton.setEnabled(false);
	    		deleteButton.setEnabled(false);
	    		connectButton.setEnabled(false);
	    		gameButton.setEnabled(false);
	    		buildButton.setEnabled(true);
	    		slowDownButton.setEnabled(true);
	    		playPauseButton.setEnabled(true);
	    		fastForwardButton.setEnabled(true);
    	}else{   
            /*new java.util.Timer().schedule( 

                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here

                        }
                    }, 
                    400
            );*/
            
        	animationWindow.setMode(false);
    		animationWindow.restartGame();
    		animationWindow.setGridVisible();
    	    animationWindow.getGridPanel().requestFocusInWindow();
    	       
    		squareBumperButton.setEnabled(true);
    		triangularBumperButton.setEnabled(true);
    		circularBumperButton.setEnabled(true);
    		absorberButton.setEnabled(true);
    		leftFlipperButton.setEnabled(true);
    		rightFlipperButton.setEnabled(true);
    		moveButton.setEnabled(true);
    		rotateButton.setEnabled(true);
    		deleteButton.setEnabled(true);
    		connectButton.setEnabled(true);
    		gameButton.setEnabled(true);
    		buildButton.setEnabled(false);
    		slowDownButton.setEnabled(false);
    		playPauseButton.setEnabled(false);
    		fastForwardButton.setEnabled(false);

    		}
    }
    
    private void saveConfig(){
    	
    	if(Configuration.loadedConfig == null){
           	//1. set default directory
            fc.setCurrentDirectory(new java.io.File("./Save"));		
            
            //2. set the fileFilter to .xml file only
            FileFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
            fc.setFileFilter(xmlfilter);
            
            //3. show Save window
    		int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {//TODO: error handler?
                System.out.println("getSelectedFile() : " +  fc.getSelectedFile());
                String filename = fc.getSelectedFile().toString().trim();
                String filenameExtension = filename.substring(filename.length()-4,filename.length());
                if(!filenameExtension.equalsIgnoreCase(".xml")){
                	Configuration.loadedConfig = filename.concat(".xml");;
                }else{
                	Configuration.loadedConfig = filename;
                }
            	
            } else {
            	System.out.println("Open command cancelled by user.");
            	return;
            }
    	}
    	
        XMLWriter configFile = new XMLWriter();
        System.out.println("save as filename : "+Configuration.loadedConfig);
		configFile.setFile(Configuration.loadedConfig);
		try {
		    configFile.saveConfig(animationWindow);
	        JOptionPane.showMessageDialog(animationWindow, "The game configuration has been saved successfully."
        			,"Configuration saved",JOptionPane.INFORMATION_MESSAGE);
	        animationWindow.getGridPanel().setIsDirty(false);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
        if(mode != GAME_MODE){// in the build mode
        	animationWindow.getGridPanel().requestFocusInWindow();
        }
    }
    
    public boolean getMode(){
    	return this.mode;
    }
    
    public void setMoveMode(boolean mode){
    	this.moveMode = mode;
    }
    
    public boolean getMoveMode(){
    	return this.moveMode;
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
