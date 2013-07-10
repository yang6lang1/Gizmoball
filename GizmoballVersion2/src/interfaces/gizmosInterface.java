package interfaces;

import java.awt.Graphics;

public interface gizmosInterface {

	//paint method
	public void paintComponents(Graphics g);
	
	public void setLocation(int x, int y);
	//S-square,T-triangle,C-circle,L-leftFlipper,R-rightFlipper

	public int getX();
	
	public int getY();
	
	public char getType();
	
	public void select();
	
	public void repaint();
	
	public void rotate();
}
