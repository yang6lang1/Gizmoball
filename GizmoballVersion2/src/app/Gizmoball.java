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
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;


public class Gizmoball extends JFrame{	
    private static final long serialVersionUID = 3257563992905298229L;
	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH;
	public static final int SCALE = 4;//scale factor for the window size
									// we can set based on our own need
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
        contentPane.setBounds(0, 0, (WIDTH/4)*SCALE*2+WIDTH*SCALE, WIDTH*SCALE);
        contentPane.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPane.setLayout(null);
 
        //Create the animation area used for output.
        animationWindow = new AnimationWindow();
        animationWindow.setBounds(160, 0, 640, 640);
        animationWindow.setOpaque(false);
        
        JPanel gridPanel = new JPanel();
        gridPanel.setBounds(0, 0, 640, 640);
        gridPanel.setOpaque(false);
        animationWindow.add(gridPanel);
        GridBagLayout gbl_gridPanel = new GridBagLayout();
        gbl_gridPanel.columnWidths = new int[]{0};
        gbl_gridPanel.rowHeights = new int[]{0};
        gbl_gridPanel.columnWeights = new double[]{Double.MIN_VALUE};
        gbl_gridPanel.rowWeights = new double[]{Double.MIN_VALUE};
        gridPanel.setLayout(gbl_gridPanel);
        contentPane.add(animationWindow); 
    
        JToolBar gameToolBar = new JToolBar();
        gameToolBar=this.setGameToolBar();
        contentPane.add(gameToolBar);
        
        JToolBar buildToolBar = new JToolBar();
        buildToolBar.setLayout(null);
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
        gameToolBar.setBounds(0, 0, 160, 640);
        gameToolBar.setForeground(Color.GRAY);        
        gameToolBar.setOrientation(SwingConstants.VERTICAL);
        gameToolBar.setFloatable(false);
        
        JPanel modePanel = new JPanel();
        modePanel.setLayout(null);
        modePanel.setBounds(0,0,160,220);
        modePanel.setForeground(Color.GRAY);
        modePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        modePanel.setOpaque(true);
        gameToolBar.add(modePanel);
        
        JPanel gameControlPanel = new JPanel();
        gameControlPanel.setLayout(null);
        gameControlPanel.setBounds(0,220,160,420);
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
        buildToolBar.setBounds(800, 0, 160, 640);
        buildToolBar.setForeground(Color.GRAY);        
        buildToolBar.setOrientation(SwingConstants.VERTICAL);
        addButtonsToBuildToolBar(buildToolBar);
        buildToolBar.setFloatable(false);
//        buildToolBar.setBorder(BorderFactory.createLineBorder(Color.black));
        
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

        menuBar.setPreferredSize(new Dimension((WIDTH/4)*SCALE*2+WIDTH*SCALE, 22));
        menuBar.setBackground(Color.GRAY);
        
        return menuBar;
    }
    /**
     * @modifies toolBar 
     * @effects adds Save, Load, Run, Stop and Quit buttons to toolBar
     * @param toolbar to add buttons to.
     */
    protected JButton makeButtonWithImage(String imageName,String toolTipText, String buttonName){
        //Look for the image.
        String imgLocation ="res/"+imageName
                             + ".png";
        
        //Create and initialize the button.
        JButton button = new JButton();
        //button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        //button.addActionListener(this);

        ImageIcon icon= new ImageIcon(imgLocation);
        button.setIcon(icon);
        return button;
    }

    protected void addButtonsToGameToolBar(JToolBar toolBar,JPanel panelOne,JPanel panelTwo){
    		  
    	JButton button = null;
        ImageIcon icon = null;
        Image image=null;
       
        icon = new ImageIcon("res/Game-Icon.png");
        image=icon.getImage().getScaledInstance(80, 60, 0);
        icon.setImage(image);
        //button = makeButtonWithImage("Game-Icon","Enter the Game Mode","Game");
        button=new JButton("Game Mode",icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.TOP);
        button.setToolTipText("Game mode");
        button.setBounds(40, 20, 80, 80);
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMode(GAME_MODE);
                System.out.println("Enter mode: "+mode);    
            }
        });
        panelOne.add(button);
        
        icon = new ImageIcon("res/Build-Icon.png");
        image=icon.getImage().getScaledInstance(80, 60, 0);
        icon.setImage(image);
        button=new JButton("Build Mode",icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.TOP);
        button.setToolTipText("Build mode");
        button.setBounds(40, 120, 80, 80);
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMode(BUILD_MODE);
                System.out.println("Enter mode: "+mode);    
            }
        });
        panelOne.add(button);

        icon = new ImageIcon("res/Rewind-Icon.png");
        image=icon.getImage().getScaledInstance(40, 40, 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("Slow down");
        button.setBounds(10, 20, 40, 40);
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO
            }
        });
        panelTwo.add(button);

        icon = new ImageIcon("res/Play-Pause-Icon.png");
        image=icon.getImage().getScaledInstance(40, 40, 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("Start running");
        button.setBounds(60, 20, 40, 40);
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
        image=icon.getImage().getScaledInstance(40, 40, 0);
        icon.setImage(image);
        button=new JButton(icon);
        button.setToolTipText("Slow down");
        button.setBounds(110, 20, 40, 40);
        // when this button is pushed it enters game mode
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//TODO
            }
        });
        panelTwo.add(button);

    } 
    
    protected void addButtonsToBuildToolBar(JToolBar toolBar){
    }
  
    protected void setMode(boolean mode){
    	this.mode=mode;
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
		frame.setBounds(0, 0, WIDTH*SCALE+(WIDTH/4)*SCALE*2, WIDTH*SCALE+22*2);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
