package components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import system.Constants;

public class gridPanel extends JPanel{
	 private static final long serialVersionUID = 10L;

    private int number_of_grids_per_dimension =Constants.number_of_grids_per_dimension;
    private int gridSize = Constants.WIDTH*Constants.SCALE/number_of_grids_per_dimension;

    @Override public void paintComponent(Graphics g) {
        // first repaint the proper background color (controlled by
        // the windowing system)
        super.paintComponent(g);
       	for (int row = 0; row <= number_of_grids_per_dimension; row++) {
    	        g.setColor(Color.gray);
    	        g.drawLine(0,row*gridSize , Constants.WIDTH*Constants.SCALE, row*gridSize);
       	}
        g.setColor(Color.gray);
        g.drawLine(0,20*gridSize-1 , Constants.WIDTH*Constants.SCALE, 20*gridSize-1);

       	for (int col = 0; col <= number_of_grids_per_dimension; col++) {
	        g.setColor(Color.gray);
	        g.drawLine(col*gridSize,0 ,col*gridSize, Constants.HEIGHT*Constants.SCALE );
       	}
        g.setColor(Color.gray);
        g.drawLine(20*gridSize-1,0 ,20*gridSize-1, Constants.HEIGHT*Constants.SCALE );

    }

}
