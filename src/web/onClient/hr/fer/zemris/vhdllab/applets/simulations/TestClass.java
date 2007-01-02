package hr.fer.zemris.vhdllab.applets.simulations;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.vhdl.simulations.VcdParser;

import java.awt.Container;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;



class Console 
{
  // Create a title string from the class name:
  public static String title(Object o) 
  {
    String t = o.getClass().toString();
    // Remove the word "class":
    if(t.indexOf("class") != -1)
    {
      t = t.substring(6);
    }
    return t;
  }
  public static void
  run(JFrame frame, int width, int height) 
  {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(width, height);
    frame.setVisible(true);
  }
  public static void
  run(JApplet applet, int width, int height) 
  {
    JFrame frame = new JFrame(title(applet));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(applet);
    frame.setSize(width, height);
    applet.init();
    applet.start();
    frame.setVisible(true);
  }
  public static void
  run(JPanel panel, int width, int height) 
  {
    JFrame frame = new JFrame(title(panel));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(panel);
    frame.setSize(width, height);
    frame.setVisible(true);
  }
} ///:~


public class TestClass extends JApplet
{

	     
  /**
	 * 
	 */
	private static final long serialVersionUID = 7708659930688856035L;

public void init() 
  {
	
	  VcdParser parser = new VcdParser("./src/web/onClient/hr/fer/zemris/vhdllab/applets/simulations/proba.vcd");
	  parser.parse();

	  FileContent fileContent = new FileContent("proba", "nana", parser.getResultInString());
	  WaveApplet panel = new WaveApplet();
	  panel.init();
	  panel.setFileContent(fileContent);

	  Container cp = getContentPane();
	  cp.add(panel);
  }

public static void main(String[] args) 
  {
      Console.run(new TestClass(), 1024, 1000);
  }
} 
