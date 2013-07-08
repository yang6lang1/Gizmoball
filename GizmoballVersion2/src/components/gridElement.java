package components;

import interfaces.gizmosInterface;

public class gridElement {

	private int x;
	private int y;
	gizmosInterface element;
	private boolean hasElement = false;
	
	public gridElement(){
		this(0, 0, null);
	}
	
	public gridElement(int x, int y, gizmosInterface element){
		this.x = x;
		this.y = y;
		this.element = element;
		
		if(element != null){
			this.hasElement = true;
		}else{
			this.hasElement = false;
		}
	}
	
	//getters
	public int[] getPosition(){
		int[] position = new int[2];
		position[0]= x; position[1]=y;
		return position;
	}
	
	public gizmosInterface getElement(){
		return this.element;
	}
	
	public boolean hasElement(){
		return this.hasElement;
	}
	
	//setters
	public void setElement(gizmosInterface element){
		this.element = element;
		if(element != null){
			this.hasElement = true;
		}else{
			this.hasElement = false;
		}
	}
	
	public void setStatus(boolean hasElement){
		this.hasElement = hasElement;
	}
	
	public String toString(){
		String out = new String("x position: "+x+ " y position: "+y);
		return out;
	}
	
	
}
