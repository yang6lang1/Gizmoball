package interfaces;

import java.awt.Graphics;
import java.awt.Rectangle;

import physics.Circle;
import physics.Vect;

public interface gizmosInterface {

	//paint method
	public void paintComponents(Graphics g);
	
	public void setLocation(int x, int y);
	//S-square,T-triangle,C-circle,L-leftFlipper,R-rightFlipper,B-ball,A-absorber

	public int getX();
	
	public int getY();
	
	public String getName();
	
	public char getType();
	
	public void select();
	
	public void repaint();
	
	public void rotate();
	
	public void originalState();
	
	public boolean isTriggered();
	
	public void setTrigger(boolean trigger);
		
	public Rectangle boundingBox();
	
	public Vect handleCollision(Circle ball, Vect velocity, int collisionSide);
	
	public double[] timeUntilNextCollision(Circle ball, Vect velocity);
	
	public String toString();
	
	public int getOrientation();
}
