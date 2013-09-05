package Practice;

import interfaces.gizmosInterface;

import java.awt.Rectangle;

import components.absorber;
import components.circleBumper;
import components.squareBumper;

import dataCollection.Quadtree;

public class QuadTreeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Quadtree<gizmosInterface> gizmos = new Quadtree<gizmosInterface>(new Rectangle(0,0,640,640));

		//TestCase #1: default constructor
		System.out.println("TestCase #1: default constructor");
		System.out.println(gizmos.toString());

		//TestCase #2: parameterized constructor
		gizmos = new Quadtree<gizmosInterface>(new Rectangle(0,0,320,320),2);
		System.out.println("TestCase #2: parameterized constructor");
		System.out.println(gizmos.toString());

		//TestCase #3: split function
		gizmos = new Quadtree<gizmosInterface>(new Rectangle(0,0,320,320),2);
		System.out.println("TestCase #3: split function");
		gizmos.split();
		System.out.println(gizmos.toString());

		//TestCase #4: getIndex function
		System.out.println("TestCase #4: getIndex function");
		Rectangle newRect = new Rectangle(0,0,80,80);//in topLeft
		int index = gizmos.getIndex(newRect);
		if(index ==1){
			System.out.println("rect(0, 0, 80, 80) is in topLeft");
		}else{
			System.out.println("getIndex function is wrong");
		}

		newRect = new Rectangle(160,0,80,80);//in parent
		index = gizmos.getIndex(newRect);
		if(index ==0){
			System.out.println("rect(160, 0, 80, 80) is in topRight");
		}else if(index ==-1){System.out.println("rect(160, 0, 80, 80) is in parent");}
		else{
			System.out.println("getIndex function is wrong");
			System.out.println(index);
		}

		newRect = new Rectangle(161,0,80,80);//in topRight
		index = gizmos.getIndex(newRect);
		if(index ==0){
			System.out.println("rect(161, 0, 80, 80) is in topRight");
		}else if(index ==-1){System.out.println("rect(161, 0, 80, 80) is in parent");}
		else{
			System.out.println("getIndex function is wrong");
			System.out.println(index);
		}

		newRect = new Rectangle(0,0,159,160);//in parent
		index = gizmos.getIndex(newRect);
		if(index ==1){
			System.out.println("rect(0,0,159,160) is in topLeft");
		}else if(index ==-1){System.out.println("rect(0,0,159,160) is in parent");}
		else{
			System.out.println("getIndex function is wrong");
			System.out.println(index);
		}

		newRect = new Rectangle(0,0,159,159);//in topLeft
		index = gizmos.getIndex(newRect);
		if(index ==1){
			System.out.println("rect(0,0,159,159) is in topLeft");
		}else if(index ==-1){System.out.println("rect(0,0,159,159) is in parent");}
		else{
			System.out.println("getIndex function is wrong");
			System.out.println(index);
		}

		newRect = new Rectangle(80,80,160,160);//in parent
		index = gizmos.getIndex(newRect);
		if(index ==1){
			System.out.println("rect(80,80,160,160)) is in topLeft");
		}else if(index ==-1){System.out.println("rect(80,80,160,160) is in parent");}
		else{
			System.out.println("getIndex function is wrong");
			System.out.println(index);
		}
		System.out.println();

		//TestCase #5: insert function,getIndex function, retrieve function
		gizmos = new Quadtree<gizmosInterface>(new Rectangle(0,0,320,320),1);
		System.out.println("TestCase #5: insert function");

		squareBumper square1 = new squareBumper(0,0);
		gizmos.insert(square1);
		
		squareBumper square2 = new squareBumper(160,0);
		gizmos.insert(square2);
		
		squareBumper square3 = new squareBumper(161,0);
		gizmos.insert(square3);
		
		circleBumper circle1 = new circleBumper(167,0);
		gizmos.insert(circle1);
		
		circleBumper circle2 = new circleBumper(100,0);
		gizmos.insert(circle2);
		
		absorber theAbsorber = new absorber(0,0);
		gizmos.insert(theAbsorber);
		
		System.out.println(gizmos.toString());

		//TestCase #6: remove function
		System.out.println("TestCase #6: remove function");
		//gizmos = gizmos.remove(gizmos, theAbsorber.boundingBox());
		//gizmos = gizmos.remove(gizmos, circle1.boundingBox());
		//gizmos = gizmos.remove(gizmos, square1.boundingBox());
		gizmos = gizmos.remove(gizmos, circle2.boundingBox());
		System.out.println(gizmos.toString());
		

	}

}