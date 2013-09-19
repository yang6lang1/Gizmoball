package app;

/** 
 * gridPanel.java represents the game board when the game is in Build Mode
 * In Build Mode, users can:
 * 1. press buttons to add gizmos, this is taken care of by addGizmos() method
 * 2. select gizmos and rotate or delete them: deleteGizmos(), gizmos.rotate()
 * 3. if a gizmo is select, then either if 'M' is pressed or the "Move" button is pressed
 * 	  the game will enter move mode, and the user can move the gizmos into empty locations
 * 	  this is done by:moveGizmos()
 * 4. All the mouse event and keyboard even are taken care of by event listeners
 * 
 * Important: whenever the game board has changed, it will be marked as 'dirty'(which means changes have been applied)
 * 
 * Finally: when entering the Game Mode, gridPanel will be set to invisible and all the gizmos(all the changes)
 * 			will be loaded into AnimationWindow
 * 			when entering the Build Mode, girlPanel will be set to visible and all the gizmos will be loaded into gridPanel
 * */

import interfaces.gizmosInterface;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import system.Configuration;
import dataCollection.Quadtree;

public class gridPanel extends JPanel  implements MouseListener,KeyListener {
	private static final long serialVersionUID = 10L;		
	private int number_of_grids_per_dimension =Configuration.number_of_grids_per_dimension;
    private int screenSize = Configuration.WIDTH*Configuration.SCALE;
    private int gridSize = screenSize/number_of_grids_per_dimension; //unit size of each grid element	
    private Quadtree<gizmosInterface> gizmos;				//gizmos is the collection of all the gizmos currently on the screen (max 20*20)
    private ArrayList<gizmosInterface> allGizmos = new ArrayList<gizmosInterface>();
    private boolean isSelected;
    private gizmosInterface selectedGizmo;
    private Gizmoball game;
    private boolean isDirty = false;						//true- the game configuration is modified
															//false- the game configuration is not modified

    public gridPanel(Gizmoball game){
    	gizmos =new Quadtree<gizmosInterface>(new Rectangle(0,0,screenSize,screenSize));
    	this.game = game;
    	isSelected = false;
    	selectedGizmo = null;
    	isDirty = false;
    	
    	this.addMouseListener(this);
    	//if(game.getMode()){//if it is in build mode
    		this.addKeyListener(this);
    	//}
    }
    
    public Quadtree<gizmosInterface> getGizmos(){
    	return gizmos;
    }
    
    public gizmosInterface getBuffer(){
    	return this.selectedGizmo;
    }
    
    public boolean isDirty(){
    	return this.isDirty;
    }
    
    public boolean isSelected(){
    	return this.isSelected;
    }
    
    public void setIsDirty(boolean isDirty){
    	this.isDirty= isDirty;
    }
    
    public void setGizmos(Quadtree<gizmosInterface> gizmos){
    	this.gizmos = gizmos;
    }
    
    public void setSelected(boolean isSelected){
    	this.isSelected = isSelected;
    }
    
    public void selectGizmo(gizmosInterface buffer){
    	this.selectedGizmo =buffer;
    }
    
    public void addGizmos(gizmosInterface gizmo){
		int row=0,col = 0;
		//int i = 0;
		boolean isEmpty= true;
		ArrayList<gizmosInterface> tempGizmos=new ArrayList<gizmosInterface>();
		if(gizmo.getType()=='B'){
			//TODO
			// * 1. add a Customized Dialog to bouncingBallButton to set the speed and angle\
			// * 2. bind the ball to an absorber
			// * 3. need to decide how many balls can be added to the screen		
		}else{
			tempGizmos = gizmos.returnAll(tempGizmos);
			findLocation:
			for(row = 0;row< number_of_grids_per_dimension-((gizmo.boundingBox().height/Configuration.L)-1);row++){	
				for(col = 0;col<number_of_grids_per_dimension-((gizmo.boundingBox().width/Configuration.L)-1);col++){
				//check if the location is empty. If not then move to the right
					gizmo.setLocation(col*gridSize, row*gridSize);

					if(tempGizmos == null){				//if tempGizmos is empty -> insert the gizmo
						isEmpty = true;
						break findLocation;
					}else{
						for(gizmosInterface aGizmo : tempGizmos){
							if(aGizmo.boundingBox().intersects(gizmo.boundingBox())){
								isEmpty = false;
								break;
								//if the element intersects with the gizmo to be inserted -> check next
							}else{
								//else -> do nothing(check next)
								isEmpty = true;
							}
						}
						if(isEmpty){
							break findLocation;
						}
					}
				}
			}
		}
	
		try{
			gizmos.insert(gizmo);
			//System.out.println("Gizmo is added into: "+ gizmo.getX()+" "+gizmo.getY());
			//System.out.println("New Gizmo: ("+ gizmo.boundingBox().getX()+","+gizmo.boundingBox().getY()+") ("+
			//gizmo.boundingBox().getMaxX()+" "+gizmo.boundingBox().getMaxY()+")");
			//System.out.println("Gizmos collection has "+ gizmos.size()+" gizmos");
			//System.out.println(gizmos.toString());
			//System.out.println("******************");
		}catch(ArrayIndexOutOfBoundsException e){
		//TODO: What if screen is full
		}
		
    }
    
    public void deleteGizmos(gizmosInterface gizmo){
    	gizmos = gizmos.remove(gizmos,gizmo.boundingBox());
    	allGizmos=new ArrayList<gizmosInterface>();
    	selectedGizmo = null;
    	this.isSelected = false; 	
        this.game.setMoveMode(false);
    	this.repaint();
    }

    public boolean moveGizmos(int destX, int destY){
    	boolean success = false;
    	
		ArrayList<gizmosInterface> tempGizmos=new ArrayList<gizmosInterface>();
		boolean isEmpty= true;
		
		if(selectedGizmo.getType()=='B'){
			//TODO:
			
		}else{
			//all gizmos except bouncing ball
			tempGizmos = gizmos.returnAll(tempGizmos);	
			if(tempGizmos == null){				//if tempGizmos is empty -> insert the gizmo
				isEmpty = true;
			}else{
				for(gizmosInterface aGizmo : tempGizmos){
					if(aGizmo.boundingBox().intersects(destX,destY,
							selectedGizmo.boundingBox().width, selectedGizmo.boundingBox().height)
							||(destX+selectedGizmo.boundingBox().width>Configuration.WIDTH*Configuration.SCALE)
							||(destY+selectedGizmo.boundingBox().height>Configuration.HEIGHT*Configuration.SCALE)){
						isEmpty = false;
						break;
						//if the element intersects with the gizmo to be inserted -> check next
					}else{
						//else -> do nothing(check next)
						isEmpty = true;
					}
				}
			}
		}
		
    	if( !isEmpty){
    		success = false;
    		System.out.println("Trying to insert into: "+destX+","+destY);
        	System.out.println("Fail to move gizmo. Target position is occupied or is not big engough");
    	}else{
    		gizmos = gizmos.remove(gizmos, selectedGizmo.boundingBox());
    		selectedGizmo.setLocation(destX, destY);
    		gizmos.insert(selectedGizmo);

    		//selectedGizmo.select();
			//selectedGizmo = null;
			//this.isSelected = false;
			//this.game.setMoveMode(false);
    		this.repaint();
    		success=true;
    	}
    	
    	if(success){
    		this.isDirty = true;
    	}
		return success;
    }
    

    @Override public void paintComponent(Graphics g) {
        // first repaint the proper background color (controlled by
        // the windowing system)
    	super.paintComponent(g);
    	
    	//paint the grid
       	for (int row = 0; row <= number_of_grids_per_dimension; row++) {
    	        g.setColor(Configuration.colorOfGridLine);
    	        g.drawLine(0,row*gridSize , Configuration.WIDTH*Configuration.SCALE, row*gridSize);
       	}
        g.setColor(Configuration.colorOfGridLine);
        g.drawLine(0,20*gridSize-1 , Configuration.WIDTH*Configuration.SCALE, 20*gridSize-1);

       	for (int col = 0; col <= number_of_grids_per_dimension; col++) {
            g.setColor(Configuration.colorOfGridLine);
	        g.drawLine(col*gridSize,0 ,col*gridSize, Configuration.HEIGHT*Configuration.SCALE );
       	}
        g.setColor(Configuration.colorOfGridLine);
        g.drawLine(20*gridSize-1,0 ,20*gridSize-1, Configuration.HEIGHT*Configuration.SCALE );

        //paint the gridElements
		//allGizmos = new ArrayList<gizmosInterface>();
        allGizmos = gizmos.returnAll(allGizmos);
		for(int i=0;i<allGizmos.size();i++){
			allGizmos.get(i).paintComponents(g.create());
		}
       
    	//TODO:what about the bouncing ball? the ball should be painted at its original position

    }

	@Override
	public void mouseClicked(MouseEvent e) {
		//detect the position where the mouse is clicked
		//use the position to determine which Gizmo is selected
		boolean hasElement = false;
		int index;
    	
		ArrayList<gizmosInterface> tempGizmos=new ArrayList<gizmosInterface>();
		gizmosInterface gizmoAtLocation= null;
		
		int destX = ((int)(e.getX()/gridSize))*gridSize;
		int destY = ((int)(e.getY()/gridSize))*gridSize;
		
		
		tempGizmos = gizmos.retrieve(tempGizmos, new Rectangle(destX,destY,gridSize,gridSize));
		
		if(tempGizmos == null){				//if tempGizmos is empty -> insert the gizmo
			hasElement = false;
		}else{
			for(gizmosInterface aGizmo : tempGizmos){
				if(aGizmo.boundingBox().intersects(new Rectangle(destX,destY,gridSize,gridSize))){
					hasElement = true;
					gizmoAtLocation = aGizmo;
					break;
					//if the element intersects with the gizmo to be inserted -> check next
				}else{
					//else -> do nothing(check next)
					hasElement = false;
				}
			}
		}

		if(hasElement){
			if(!(this.game.getMoveMode())){
				if(!isSelected)
				{
	
						selectedGizmo = gizmoAtLocation;
						selectedGizmo.select();
						isSelected = true;
					
				}else{
						if(selectedGizmo ==  gizmoAtLocation){
								selectedGizmo.select();
								isSelected = false;//change the selected item into its original color	
								selectedGizmo = null;
						}else{
							if(selectedGizmo!=null){
								selectedGizmo.select(); 
								//tempBuffer = null;
							}
							selectedGizmo =  gizmoAtLocation;
							selectedGizmo.select();			
						}
				}
			}else{
	    		System.out.println("Trying to insert into: "+destX+","+destY);
	        	System.out.println("Gizmo moved fail. Target position is occupied or is not big engough");
			}			
		}else{
			if(!(this.game.getMoveMode())){
				if(isSelected){
					selectedGizmo.select();
					isSelected = false;
					selectedGizmo = null;
				}else{
					//if nothing is selected, nothing happens
					//System.out.println("nothing happens");
				}
			}else{
				this.moveGizmos(destX,destY);
			}
		}
		
		this.repaint();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {

		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keynum = e.getKeyCode();

        if (keynum==27) {//press Esc: quit mode
            System.out.println("keypress " + e.getKeyCode());
            //it quits all the modes
            if(this.isSelected){
                this.isSelected = false;
                this.selectedGizmo.select();
                this.selectedGizmo=null;
               
            }
            this.game.setMoveMode(false);
    		this.repaint();
        }
        
        if (keynum==77) {//press M: enter move mode
            System.out.println("keypress " + e.getKeyCode());
            if(this.isSelected){
            	this.game.setMoveMode(true);
            }
        }


	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
