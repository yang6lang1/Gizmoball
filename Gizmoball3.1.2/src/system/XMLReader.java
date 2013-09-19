package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import physics.Vect;

import components.absorber;
import components.circleBumper;
import components.leftFlipper;
import components.rightFlipper;
import components.squareBumper;
import components.triangleBumper;

public class XMLReader {
	private final static int L = Configuration.L;
	static final String BOARD = "board";
	static final String BALL = "ball";
	static final String SQUAREBUMPER = "squareBumper";
	static final String CIRCLEBUMPER = "circleBumper";
	static final String TRIANGLEBUMPER = "triangleBumper";
	static final String LEFTFLIPPER = "leftFlipper";
	static final String RIGHTFLIPPER = "rightFlipper";
	static final String ABSORBER = "absorber";

  @SuppressWarnings({ "unchecked", "null" })
  public Configuration readConfig(String configFile,String schemaFile) {
      Configuration newGame = new Configuration();
    try {
    	if(schemaFile != null){
	    	//the first part checks the input XML against the XML Schema
	        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(configFile));
	
	        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new File(schemaFile));
	
	        Validator validator = schema.newValidator();
	        validator.validate(new StAXSource(reader));
	
	        //no exception thrown, so valid
	        System.out.println("Document is valid");
    	}
    	
      // First create a new XMLInputFactory
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      // Setup a new eventReader
      InputStream in = new FileInputStream(configFile);
      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
      // Read the XML document

      
      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();
        if (event.isStartElement()) { 
          StartElement startElement = event.asStartElement();
          // If we have a ball element we create a new ball
          if (startElement.getName().getLocalPart() == (BALL)) {
            // We read the attributes from this tag and add all the attributes to our object
            int vx=0, vy=0;
            Iterator<Attribute> attributes = startElement
                .getAttributes();
            while (attributes.hasNext()) {
              Attribute attribute = attributes.next();
              if (attribute.getName().toString().equals("name")) {
            	  newGame.setBallName(attribute.getValue());
              }
              
              if(attribute.getName().toString().equals("x")){
            	  //eg: x = "1.0" -> 1.0*L
            	  newGame.setBallX((int)(Double.parseDouble(attribute.getValue())*L));
              }
              
              if(attribute.getName().toString().equals("y")){
            	  //eg: y = "11.0" -> 11.0*L
            	  newGame.setBallY((int)(Double.parseDouble(attribute.getValue())*L));
              }

              if(attribute.getName().toString().equals("xVelocity")){
            	  vx = ((int)(Double.parseDouble(attribute.getValue())*L));
            
              }
              
              if(attribute.getName().toString().equals("yVelocity")){
            	  vy = ((int)(Double.parseDouble(attribute.getValue())*L));
              }
              
            }//end of attribute reading
            Vect velocity = new Vect(vx,vy);
            newGame.setBallSpeed((int)(velocity.length()));
            newGame.setBallAngle(velocity.angle());
            
          }// end of Ball tag

          if (startElement.getName().getLocalPart() == (SQUAREBUMPER)) {
              // We read the attributes from this tag and add all the attributes to our object
              int x=0,y=0;
              String name = null;
              Iterator<Attribute> attributes = startElement
                  .getAttributes();
              while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals("name")) {
                	name = attribute.getValue();
                }
                
                if(attribute.getName().toString().equals("x")){
                	x = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("y")){
                	y = Integer.parseInt(attribute.getValue())*L;
                }
               
              }//end of attribute reading
              squareBumper square = new squareBumper(x, y);
              square.setName(name);
              newGame.addGizmos(square);              
            }// end of squareBumper tag

          if (startElement.getName().getLocalPart() == (CIRCLEBUMPER)) {
              // We read the attributes from this tag and add all the attributes to our object

              int x=0,y=0;
              String name = null;
              Iterator<Attribute> attributes = startElement
                  .getAttributes();
              while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals("name")) {
                	name = attribute.getValue();
                }
                
                if(attribute.getName().toString().equals("x")){
                	x = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("y")){
                	y = Integer.parseInt(attribute.getValue())*L;
                }
               
              }//end of attribute reading
              circleBumper circle = new circleBumper(x, y);
              circle.setName(name);
              newGame.addGizmos(circle);              
            }// end of circleBumper tag 
          
          if (startElement.getName().getLocalPart() == (TRIANGLEBUMPER)) {
              // We read the attributes from this tag and add all the attributes to our object
              int x=0,y=0,orientation=0;
              String name = null;
              Iterator<Attribute> attributes = startElement
                  .getAttributes();
              while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals("name")) {
                	name = attribute.getValue();
                }
                
                if(attribute.getName().toString().equals("x")){
                	x = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("y")){
                	y = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("orientation")){
                	orientation = Integer.parseInt(attribute.getValue());
                }
               
              }//end of attribute reading
        	  triangleBumper triangle = new triangleBumper(x, y,orientation);
              triangle.setName(name);
              newGame.addGizmos(triangle);              
            }// end of triangleBumper tag 
          
          if (startElement.getName().getLocalPart() == (LEFTFLIPPER)) {
              // We read the attributes from this tag and add all the attributes to our object
              int x=0,y=0,orientation=0;
              String name = null;
              Iterator<Attribute> attributes = startElement
                  .getAttributes();
              while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals("name")) {
                	name = attribute.getValue();
                }
                
                if(attribute.getName().toString().equals("x")){
                	x = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("y")){
                	y = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("orientation")){
                	orientation = Integer.parseInt(attribute.getValue());
                }
               
              }//end of attribute reading
        	  leftFlipper left = new leftFlipper(x, y,orientation);
        	  left.setName(name);
              newGame.addGizmos(left);              
            }// end of leftFlipper tag 
          
          if (startElement.getName().getLocalPart() == (RIGHTFLIPPER)) {
              // We read the attributes from this tag and add all the attributes to our object
              int x=0,y=0,orientation=0;
              String name = null;
              Iterator<Attribute> attributes = startElement
                  .getAttributes();
              while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals("name")) {
                	name = attribute.getValue();
                }
                
                if(attribute.getName().toString().equals("x")){
                	x = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("y")){
                	y = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("orientation")){
                	orientation = Integer.parseInt(attribute.getValue());
                }
               
              }//end of attribute reading
        	  rightFlipper right = new rightFlipper(x, y,orientation);
        	  right.setName(name);
              newGame.addGizmos(right);              
            }// end of rightFlipper tag 
          
          if (startElement.getName().getLocalPart() == (ABSORBER)) {
              // We read the attributes from this tag and add all the attributes to our object
              int x=0,y=0,width=0,height=0;
              String name = null;
              Iterator<Attribute> attributes = startElement
                  .getAttributes();
              while (attributes.hasNext()) {
                Attribute attribute = attributes.next();
                if (attribute.getName().toString().equals("name")) {
                	name = attribute.getValue();
                }
                
                if(attribute.getName().toString().equals("x")){
                	x = Integer.parseInt(attribute.getValue())*L;
                }
                
                if(attribute.getName().toString().equals("y")){
                	y = Integer.parseInt(attribute.getValue())*L;
                }

                if(attribute.getName().toString().equals("width")){
                	width = Integer.parseInt(attribute.getValue())*L;
                }    
                
                if(attribute.getName().toString().equals("height")){
                	height = Integer.parseInt(attribute.getValue())*L;
                }
              }//end of attribute reading
        	  absorber theAbsorber = new absorber(x, y,width,height);
        	  theAbsorber.setName(name);
              newGame.addGizmos(theAbsorber);              
            }// end of absorber tag 
          
          //TODO:
          //1. Connections between gizmos
          //2. keyBindings
          
        }

      }
    } catch (FileNotFoundException e) {//TODO: use JDialog to handle exceptions
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
	}
    return newGame;
  }

} 