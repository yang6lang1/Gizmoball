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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import system.Configuration;

public class gridPanel extends JPanel  implements MouseListener,KeyListener {
	private static final long serialVersionUID = 10L;		
	private int number_of_grids_per_dimension =Configuration.number_of_grids_per_dimension;
    private int gridSize = Configuration.WIDTH*Configuration.SCALE/number_of_grids_per_dimension; //unit size of each grid element
    private ArrayList<gizmosInterface> gizmos;				//gizmos is the collection of all the gizmos currently on the screen (max 20*20)
    private boolean isSelected;
    private gizmosInterface selectedGizmo;
    private Gizmoball game;
    private boolean isDirty = false;						//true- the game configuration is modified
															//false- the game configuration is not modified

    public gridPanel(Gizmoball game){
    	gizmos =new ArrayList<gizmosInterface>(number_of_grids_per_dimension*number_of_grids_per_dimension);
    	this.game = game;
    	isSelected = false;
    	selectedGizmo = null;
    	isDirty = false;
    	
    	this.addMouseListener(this);
    	//if(game.getMode()){//if it is in build mode
    		this.addKeyListener(this);
    	//}
    }
    
    public ArrayList<gizmosInterface> getGizmos(){
    	return gizmos;
    }
    
    public gizmosInterface getBuffer(){
    	return this.selectedGizmo;
    }
    
    public int getNumberOfGizmos(){
    	return this.gizmos.size();
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
    
    public void setGizmos(ArrayList<gizmosInterface> gizmos){
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

		if(gizmo.getType()=='B'){
			//TODO
			// * 1. add a Customized Dialog to bouncingBallButton to set the speed and angle\
			// * 2. bind the ball to an absorber
			// * 3. need to decide how many balls can be added to the screen		
		}else{
			for(row = 0;row< number_of_grids_per_dimension-((gizmo.boundingBox().height/Configuration.L)-1);row++){	
				for(col = 0;col<number_of_grids_per_dimension-((gizmo.boundingBox().width/Configuration.L)-1);col++){
								//go through the gizmos[] one by one to check if this location is occupied
					for(int i = 0; i < gizmos.size();i++){	
						if(gizmos.get(i).boundingBox().intersects(col*gridSize, row*gridSize,
								gizmo.boundingBox().width, gizmo.boundingBox().height)){
							isEmpty = false;
						}else{
							isEmpty = true;
						}
						if(!isEmpty){
							break;
						}
					}
					if(isEmpty){
						break;
					}
				}
				if(isEmpty){
					break;
				}

			}

		}
		gizmo.setLocation(col*gridSize, row*gridSize);
		try{
			gizmos.add(gizmo);
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
    	gizmos.remove(gizmo);
    	selectedGizmo = null;
    	this.isSelected = false; 	
    }

    public boolean moveGizmos(int destX, int destY){
    	boolean success = false;
    	
		boolean hasElement = false;
		int index;		
		
		if(selectedGizmo.getType()=='B'){
			//TODO:
			
		}else{
			//all gizmos except bouncing ball
			for(index = 0; index < gizmos.size();index++){
				if(gizmos.get(index).boundingBox().intersects(destX,destY,
						selectedGizmo.boundingBox().width, selectedGizmo.boundingBox().height)
						||(destX+selectedGizmo.boundingBox().width>Configuration.WIDTH*Configuration.SCALE)
						||(destY+selectedGizmo.boundingBox().height>Configuration.HEIGHT*Configuration.SCALE)){
					hasElement = true;
				}else{
					hasElement = false;
				}
				if(hasElement){
					break;
				}
			}//this for loop updates the index

		}
		
    	if( hasElement){
    		success = false;
    		System.out.println("Trying to insert into: "+destX+","+destY);
        	System.out.println("Gizmo moved fail. Target position is occupied or is not big engough");
    	}else{
    		selectedGizmo.setLocation(destX, destY);

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
        for(int i=0; i< this.gizmos.size();i++){
        	gizmos.get(i).paintComponents(g.create());	
        }
       
    	//TODO:what about the bouncing ball? the ball should be painted at its original position

    }

	@Override
	public void mouseClicked(MouseEvent e) {
		//detect the position where the mouse is clicked
		//use the position to determine which Gizmo is selected
		boolean hasElement = false;
		int index;
		
		for(index = 0; index < gizmos.size();index++){
			if(gizmos.get(index).boundingBox().contains(e.getX(),e.getY())){
				hasElement = true;
			}else{
				hasElement = false;
			}
			if(hasElement){
				break;
			}
		}//this for loop updates the index
		
		int destX = ((int)(e.getX()/gridSize))*gridSize;
		int destY = ((int)(e.getY()/gridSize))*gridSize;
		
		if(hasElement){
			if(!(this.game.getMoveMode())){
			if(!isSelected)
			{

					selectedGizmo = gizmos.get(index);
					selectedGizmo.select();
					isSelected = true;
				
			}else{
					if(selectedGizmo ==  gizmos.get(index)){
							selectedGizmo.select();
							isSelected = false;
							selectedGizmo = null;
					}else{
						if(selectedGizmo!=null){
							selectedGizmo.select(); //change the selected item into its original color	
							//tempBuffer = null;
						}
							selectedGizmo =  gizmos.get(index);
							selectedGizmo.select();			
					}
			}}else{
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
