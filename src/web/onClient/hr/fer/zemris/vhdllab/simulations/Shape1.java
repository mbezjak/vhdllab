import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;


/**
 * Predstavlja cistu nulu.  Moze biti pocetna vrijednost ili dolazi iza nule 
 *
 * @author Boris Ozegovic
 */
class Shape1 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1 + 20, x2, y1 + 20); 
	}
	
	
	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}
