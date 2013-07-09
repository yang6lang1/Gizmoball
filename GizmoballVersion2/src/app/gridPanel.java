package app;

import interfaces.gizmosInterface;

import java.awt.Graphics;

import javax.swing.JPanel;

import components.gridElement;

import system.Constants;

public class gridPanel extends JPanel{
	 private static final long serialVersionUID = 10L;

	 private gridElement element[][];  //element is the collection of all the grid elements (20*20)
    private int number_of_grids_per_dimension =Constants.number_of_grids_per_dimension;
    private int gridSize = Constants.WIDTH*Constants.SCALE/number_of_grids_per_dimension;
    private Constants.SelectionType selectionType;
    
    public gridPanel(){
    	element = new gridElement[Constants.number_of_grids_per_dimension][Constants.number_of_grids_per_dimension];
    	for(int row=0;row<Constants.number_of_grids_per_dimension;row++){
    		for(int col=0;col<Constants.number_of_grids_per_dimension;col++){
    			element[col][row] = new gridElement(col*gridSize,row*gridSize,null);
    			//System.out.println(element[col][row].toString());
    		}
    	}
    	selectionType = Constants.SelectionType.NONE;
    }
    
    public gridElement[][] getElement(){
    	return element;
    }
    
    public Constants.SelectionType getSelectionType(){
    	return this.selectionType;
    }
    
    public void setElement(gridElement[][] element){
    	this.element = element;
    }
    
    public void setSelectionType(Constants.SelectionType selectionType){
    	this.selectionType = selectionType;
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

}
