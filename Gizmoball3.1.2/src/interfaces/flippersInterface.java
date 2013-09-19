package interfaces;

import physics.Angle;

public interface flippersInterface extends gizmosInterface {
	public Angle getAngle();
	
	public boolean isKeyPressed();
	
	public void setKeyPressed(boolean keyPressed);
	
	public void move();
	
	public void moveUp();
	
	public void moveUpByDeltaT(double delta_t);
	
	public void moveDownByDeltaT(double delta_t);
}
