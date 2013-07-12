package app;

import interfaces.gizmosInterface;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import system.Constants;

import components.gridElement;

public class gridPanel extends JPanel  implements MouseListener {
	 private static final long serialVersionUID = 10L;

	 private gridElement element[][];  //element is the collection of all the grid elements (20*20)
    private int number_of_grids_per_dimension =Constants.number_of_grids_per_dimension;
    private int gridSize = Constants.WIDTH*Constants.SCALE/number_of_grids_per_dimension;
    private boolean isSelected;
    private gridElement tempBuffer=null;
    private Gizmoball game;
    
    public gridPanel(Gizmoball game){
    	element = new gridElement[Constants.number_of_grids_per_dimension][Constants.number_of_grids_per_dimension];
    	for(int row=0;row<Constants.number_of_grids_per_dimension;row++){
    		for(int col=0;col<Constants.number_of_grids_per_dimension;col++){
    			element[col][row] = new gridElement(col*gridSize,row*gridSize,null);
    			//System.out.println(element[col][row].toString());
    		}
    	}
    	isSelected = false;
    	this.game = game;
    	this.addMouseListener(this);
    }
    
    public gridElement[][] getElement(){
    	return element;
    }
    
    public gridElement getBuffer(){
    	return this.tempBuffer;
    }
    public boolean isSelected(){
    	return this.isSelected;
    }
    
    public void setElement(gridElement[][] element){
    	this.element = element;
    }
    
    public void setSelectionType(boolean selectionType){
    	this.isSelected = selectionType;
    }
    
    public void setBuffer(gridElement buffer){
    	this.tempBuffer =buffer;
    }
    
    public void addGizmos(gizmosInterface gizmo){//TODO: in Gizmoball.java I need to check the gizmoCount
				//if no places to place the gizmos I need to show some message
		int row=0,col = 0;
		boolean hasElement = element[col][row].hasElement();// try to insert into (0,0)
		
		for(row = 0;row<Constants.number_of_grids_per_dimension&& hasElement == true;row++){	
			for(col = 0;col<Constants.number_of_grids_per_dimension&& hasElement == true;col++){
				//col++;
				hasElement = element[col][row].hasElement();	
				if(!hasElement){
					break;
				}
			}
			if(!hasElement){
				break;
			}
		}
		
		gizmo.setLocation(col*gridSize, row*gridSize);
		
		try{
			element[col][row].setElement(gizmo);
		}catch(ArrayIndexOutOfBoundsException e){
		//TODO: Screen is full! Show an alert!
		}
    }
    
    public void deleteGizmos(gridElement gizmoElement){
    	//TODO: need to check if the gizmo exist on the board
    	
    	/*int col = gizmoElement.getElement().getX();
    	int row = gizmoElement.getElement().getY();
    	col = col/gridSize;
    	row = row/gridSize;*/
    	gizmoElement.setElement(null);
    	gizmoElement = null;
    	this.isSelected = false;
    	
    }

    public boolean moveGizmos(int destCol, int destRow){
    	boolean success = false;
    	
    	if( element[destCol][destRow].hasElement()){
    		success = false;
        	
        	System.out.println("Gizmo moved fail");
    	}else{
    		gizmosInterface newGizmo =tempBuffer.getElement();
    		this.deleteGizmos(tempBuffer);
    		//TODO:once a gizmo is moved, it cant be selected
    		//need to fix this bugs
    		newGizmo.select();
    		newGizmo.setLocation(destCol*gridSize, destRow*gridSize);
			element[destCol][destRow].setElement(newGizmo);
			
			tempBuffer = null;
			this.isSelected = false;
			this.game.setMoveMode(false);
    		this.repaint();
    		
    	}

    	return success;
    }

    @Override public void paintComponent(Graphics g) {
        // first repaint the proper background color (controlled by
        // the windowing system)
    	super.paintComponent(g);
    	
    	//paint the grid
       	for (int row = 0; row <= number_of_grids_per_dimension; row++) {
    	        g.setColor(Constants.colorOfGridLine);
    	        g.drawLine(0,row*gridSize , Constants.WIDTH*Constants.SCALE, row*gridSize);
       	}
        g.setColor(Constants.colorOfGridLine);
        g.drawLine(0,20*gridSize-1 , Constants.WIDTH*Constants.SCALE, 20*gridSize-1);

       	for (int col = 0; col <= number_of_grids_per_dimension; col++) {
            g.setColor(Constants.colorOfGridLine);
	        g.drawLine(col*gridSize,0 ,col*gridSize, Constants.HEIGHT*Constants.SCALE );
       	}
        g.setColor(Constants.colorOfGridLine);
        g.drawLine(20*gridSize-1,0 ,20*gridSize-1, Constants.HEIGHT*Constants.SCALE );

        //paint the gridElements
    	for(int row=0;row<Constants.number_of_grids_per_dimension;row++){
    		for(int col=0;col<Constants.number_of_grids_per_dimension;col++){
    			if(element[col][row].getElement() !=null){
    				element[col][row].getElement().paintComponents(g);
    			}
    		}
    	}
    	
    	//what about the bouncing ball? the ball should be painted at its original position

    }

	@Override
	public void mouseClicked(MouseEvent e) {
		//detect the position where the mouse is clicked
		//use the position to calculate which Gizmo is selected
		int col = (int)(e.getX()/gridSize);
		int row = (int)(e.getY()/gridSize);
		
		if(element[col][row].hasElement()){
			if(!(this.game.getMoveMode())){
			if(!isSelected)
			{
					tempBuffer = element[col][row];
					tempBuffer.getElement().select();
					isSelected = true;
			}else{
				//System.out.println("Move mode is: "+this.game.getMoveMode());
				//if(!(this.game.getMoveMode())){
				//if it is not in move mode, select other gizmos
					if(tempBuffer == element[col][row]){
							tempBuffer.getElement().select();
							isSelected = false;
							tempBuffer = null;
					}else{
						if(tempBuffer.getElement()!=null){
							tempBuffer.getElement().select(); //change the selected item into its original color	
							//tempBuffer = null;
						}
							tempBuffer = element[col][row];
							tempBuffer.getElement().select();			
					}
				//}else{
				//if it is in move mode, move to location
				//	this.moveGizmos(col,row);
				//}
					
			}}else{
				//this handles the situation when the move mode is on
				//System.out.println("testing");
				//TODO: display a tooltip at the cursor when mouse clicked
			}			
		}else{
			if(!(this.game.getMoveMode())){
				if(isSelected){
					tempBuffer.getElement().select();
					isSelected = false;
					tempBuffer = null;
				}else{
					//if nothing is selected, nothing happens
					//System.out.println("nothing happens");
				}
			}else{
				this.moveGizmos(col,row);
			}
		}
		
/*		if(tempBuffer!=null){
		if(col ==tempBuffer.getElement().getX() && row == tempBuffer.getElement().getY()){
			tempBuffer.getElement().select();
				isSelected =false;
				this.game.setMoveMode(false);
				tempBuffer = null;

		}}*/
		
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

}
