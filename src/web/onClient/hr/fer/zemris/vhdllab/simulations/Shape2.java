import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;


/**
 * Predstavlja nulu s vertikalnom crtom prema gore na pocetku.  Dolazi iza jedinice
 *
 * @author Boris Ozegovic
 */
class Shape2 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1 + 20, x2, y1 + 20);  
		g.drawLine(x1, y1, x1, y1 + 20);
	}


	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}
