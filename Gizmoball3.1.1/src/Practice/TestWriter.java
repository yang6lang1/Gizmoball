package Practice;

import system.XMLWriter;
import app.AnimationWindow;
import app.Gizmoball;

public class TestWriter {

	  public static void main(String[] args) {
		    XMLWriter configFile = new XMLWriter();
		    configFile.setFile("Save/config2.xml");
		    try {
		      configFile.saveConfig(new AnimationWindow(new Gizmoball()));
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
		}