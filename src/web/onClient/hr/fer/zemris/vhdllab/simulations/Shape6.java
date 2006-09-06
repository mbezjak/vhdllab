import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;


/**
 * Predstavlja jedinicu s poluvertikalnom crtom prema dolje na pocetku.  Dolazi
 * iza vektora
 *
 * @author Boris Ozegovic
 */
class Shape6 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1, x2, y1);  // jedinica s poluvertikalnom crtom prema dolje
		g.drawLine(x1, y1, x1, y1 + 10);
	}


	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}
