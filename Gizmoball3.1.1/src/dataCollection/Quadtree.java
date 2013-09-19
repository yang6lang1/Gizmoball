package dataCollection;

import interfaces.gizmosInterface;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Quadtree<E extends gizmosInterface> {

	private final static int MAX_ELEMENTS =5;
	
	private ArrayList<E> elements;
	private int maxElement;
	private Rectangle bounds; // the 2D space that the node occupies
	private Quadtree<E> topLeft;
	private Quadtree<E> topRight;
	private Quadtree<E> botLeft;
	private Quadtree<E> botRight;
	
	public Quadtree(Rectangle pBounds) {
		this.maxElement = MAX_ELEMENTS;
		bounds = pBounds;
		elements = new ArrayList<E>();
	}

	public Rectangle boundingBox(){
		return this.bounds;
	}
	
	public boolean hasChildren(){
		return (topLeft != null);
	}
	
	public boolean hasElement(){
		return (elements.size()!=0);
	}
	/**
	 * The clear method clears the quadtree by recursively clearing all objects
	 * from all nodes.
	 * */
	public void clear() {
		elements.clear();
		if(topLeft !=null){
			topLeft.clear();
			topLeft=null;
		}

		if(topRight !=null){
			topRight.clear();
			topRight=null;
		}

		if(botLeft !=null){
			botLeft.clear();
			botLeft=null;
		}

		if(botRight !=null){
			botRight.clear();
			botRight=null;
		}

	}

	/**
	 * The split method splits the node into four subnodes by dividing the node
	 * into four equal parts and initializing the four subnodes with the new
	 * bounds.
	 * */
	public void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int x = (int) (bounds.getX());
		int y = (int) (bounds.getY());

		topRight = new Quadtree<E>(new Rectangle(x + subWidth, y,subWidth, subHeight));
		topLeft = new Quadtree<E>( new Rectangle(x, y, subWidth,subHeight));
		botLeft = new Quadtree<E>( new Rectangle(x, y + subHeight,subWidth, subHeight));
		botRight = new Quadtree<E>(new Rectangle(x + subWidth, y+ subHeight, subWidth, subHeight));
	}

	public void merge(){
		topLeft.clear();
		topLeft=null;
		topRight.clear();
		topRight=null;
		botLeft.clear();
		botLeft=null;
		botRight.clear();
		botRight=null;
	}
	
	/**
	 * Determine which node the object belongs to. -1 means object cannot
	 * completely fit within a child node and is part of the parent node
	 */
	public int getIndex(Rectangle pRect) {
		int index = -1;				//-1 means object cannot completely fit within a child node
		double verticalMidPoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidPoint = bounds.getY() + (bounds.getHeight() / 2);

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (pRect.getY() <= horizontalMidPoint 
				&& pRect.getY() + pRect.getHeight() <= horizontalMidPoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (pRect.getY() >= horizontalMidPoint);

		// Object can completely fit within the left quadrants
		if (pRect.getX() <= verticalMidPoint
				&& pRect.getX() + pRect.getWidth() <= verticalMidPoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (pRect.getX() >= verticalMidPoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	/**
	 * Insert the object into the quadtree. If the node exceeds the capacity, it
	 * will split and add all objects to their corresponding nodes.
	 */
	public void insert(E theElement) {
		/*
		 * The insert method is where everything comes together. The method
		 * first determines whether the node has any child nodes and tries to
		 * add the object there. If there are no child nodes or the object
		 * doesn't fit in a child node, it adds the object to the parent node.
		 * 
		 * Once the object is added, it determines whether the node needs to
		 * split by checking if the current number of objects exceeds the max
		 * allowed objects. Splitting will cause the node to insert any object
		 * that can fit in a child node to be added to the child node; otherwise
		 * the object will stay in the parent node.
		 */
		if (topLeft != null) {
			int index = getIndex(theElement.boundingBox());
			
			switch(index){
			case 0:  				//topRight
				topRight.insert(theElement);
				return;
			case 1: 				//topLeft
				topLeft.insert(theElement);
				return;
			case 2:					//botLeft
				botLeft.insert(theElement);
				return;
			case 3:					//botRight
				botRight.insert(theElement);
				return;
			default:
				//index = -1; the pRect doesn't fit in the sub-rectangle
				break;
			}
		}

		elements.add(theElement);

		if (elements.size() > maxElement) {
			if (topLeft == null) {
				split();
			}

			int i = 0;
			while (i < elements.size()) {
				int index = getIndex(elements.get(i).boundingBox());
				switch(index){
				case 0:  				//topRight
					topRight.insert(elements.remove(i));
					break;
				case 1: 				//topLeft
					topLeft.insert(elements.remove(i));
					break;
				case 2:					//botLeft
					botLeft.insert(elements.remove(i));
					break;
				case 3:					//botRight
					botRight.insert(elements.remove(i));
					break;
				default:
					//index = -1;
					i++;
					break;
				}
			}
		}
	}

	/**
	 * Return all objects that could collide with the given object
	 */
	public ArrayList<E> retrieve(ArrayList<E> returnObjects, Rectangle pRect){
		int index = getIndex(pRect);
		if (index != -1 && topLeft != null) {
			
			switch(index){
			case 0:  				//topRight
				topRight.retrieve(returnObjects, pRect);
				break;
			case 1: 				//topLeft
				topLeft.retrieve(returnObjects, pRect);
				break;
			case 2:					//botLeft
				botLeft.retrieve(returnObjects, pRect);
				break;
			case 3:					//botRight
				botRight.retrieve(returnObjects, pRect);
				break;
			default:
				//index = -1; the pRect doesn't fit in the sub-rectangle
				break;
			}
		}

		returnObjects.addAll(elements);

		return returnObjects;
	}
	
	/**
	 * Return an ArrayList contains the elements within all the children nodes
	 * */
	public ArrayList<E> returnAll(ArrayList<E> returnObjects){
		if(topLeft!=null){
			topLeft.returnAll(returnObjects);
		}

		if(topRight!=null){
			topRight.returnAll(returnObjects);
		}

		if(botLeft!=null){
			botLeft.returnAll(returnObjects);
		}

		if(botRight!=null){
			botRight.returnAll(returnObjects);
		}
		
		returnObjects.addAll(elements);

		

		return returnObjects;
	}
	/** For removal in a quadtree you'll need to basically do the following:
		
		    Find the object's leaf, then remove it from that list (the node that contains the leaves)
		    Check if the removal of the leaf leaves the node empty, if it does, then remove the node itself.
		    Check if the surrounding nodes are empty as well, and if so, collapse this node into the parent by "unsubdividing" (this can get recursively tricky to do). The trick is to just check if the adjacent nodes have anything in them. If not, you're safe to throw the whole quadrant away and step up one level. Doing this recursively will collapse the tree back up to where an adjacent node with a leaf exists.
		
		After step 1, you're basically done. If you want to save memory and keep the tree efficient then you should do steps 2 and 3.
	 */
	 public Quadtree<E> remove(Quadtree<E> returnObjects, Rectangle pRect){
		 ArrayList<E> temp =new ArrayList<E>();
		 
		 int index = getIndex(pRect);
		 if(index != -1 && hasChildren()){
				switch(index){
				case 0:  				//topRight
					returnObjects=topRight.remove(returnObjects, pRect);
					break;
				case 1: 				//topLeft
					returnObjects=topLeft.remove(returnObjects, pRect);
					break;
				case 2:					//botLeft
					returnObjects=botLeft.remove(returnObjects, pRect);
					break;
				case 3:					//botRight
					returnObjects=botRight.remove(returnObjects, pRect);
					break;
				default:
					//index = -1; the pRect doesn't fit in the sub-rectangle
					break;
				}
		 }
		 
		 if(index == -1 || !hasChildren()){
			 for(E tempElement : elements){
				 if(tempElement.boundingBox().intersects(pRect)){
					// elements.remove(tempElement);
					// break;
				 }else{
					 temp.add(tempElement);
				 }
			 }
			 elements=temp;
		 }
		 
		/* if(index != -1 && hasChildren() 
			&&!topLeft.hasChildren()&&!topRight.hasChildren()
			&&!botLeft.hasChildren()&&!botRight.hasChildren()){

				//optional
				boolean canBeMerged = false;
				if(!topLeft.hasChildren()){
					if (topLeft.hasElement()){
						canBeMerged = false;
					}else{
						canBeMerged = true;
					}
				}
				if(!topRight.hasChildren()){
					if (topRight.hasElement()){
						canBeMerged = false;
					}else{
						canBeMerged = true;
					}
				}
				if(!botLeft.hasChildren()){
					if (botLeft.hasElement()){
						canBeMerged = false;
					}else{
						canBeMerged = true;
					}
				}
				if(!botRight.hasChildren()){
					if (botRight.hasElement()){
						canBeMerged = false;
					}else{
						canBeMerged = true;
					}
				}
				if(canBeMerged){
					this.merge();
				}
		 }*/
		 
		 return returnObjects;
	 }
	 
	 public Quadtree<E> removeNode(Quadtree<E> theNode){
		 theNode.clear();
		 return theNode;
	 }
	
	public String toString(){
		String output = null;
		output = "maxElement:"+this.maxElement;
		output+="\nbounds:"+this.bounds.getX()+","+this.bounds.getY()+","+this.bounds.getWidth()+","+this.bounds.getHeight();
		if(topLeft==null){
			output+="\ntopLeft,topRight,botLeft,botRight are null";
		}else{
			output+="\ntopLeft,topRight,botLeft,botRight exist!";
			output+="\ntopLeft: "+topLeft.boundingBox().getX()+","+topLeft.boundingBox().getY()+","+topLeft.boundingBox().getWidth()+","+topLeft.boundingBox().getHeight();
			output+="\ntopRight: "+topRight.boundingBox().getX()+","+topRight.boundingBox().getY()+","+topRight.boundingBox().getWidth()+","+topRight.boundingBox().getHeight();
			output+="\nbotLeft: "+botLeft.boundingBox().getX()+","+botLeft.boundingBox().getY()+","+botLeft.boundingBox().getWidth()+","+botLeft.boundingBox().getHeight();
			output+="\nbotRight: "+botRight.boundingBox().getX()+","+botRight.boundingBox().getY()+","+botRight.boundingBox().getWidth()+","+botRight.boundingBox().getHeight();
			output+= "\n";
			ArrayList<E> element = new ArrayList<E>();
			element = this.returnAll(element);
			//element=this.retrieve(element, new Rectangle(0,0,81,81));
			//element=this.retrieve(element, topLeft.boundingBox());
			//element=this.retrieve(element, this.topRight.boundingBox());
			//element=this.retrieve(element, this.botLeft.boundingBox());
			//element=this.retrieve(element, this.botRight.boundingBox());
			for(int i=0;i<element.size();i++){
				//output+="\n"+element.size();
				output+= ""+element.get(i).toString();
			}
		}
	
		output+= "\n";
		return output;
	}

}
