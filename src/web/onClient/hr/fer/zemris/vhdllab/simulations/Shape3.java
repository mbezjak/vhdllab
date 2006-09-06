package hr.fer.zemris.vhdllab.simulations;

import java.awt.Graphics;


/**
 * Predstavlja nulu s poluvertikalnom crtom prema gore na pocetku.  Dolazi iza vektora
 *
 * @author Boris Ozegovic
 */
class Shape3 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1 + 20, x2, y1 + 20);  
		g.drawLine(x1, y1 + 10, x1, y1 + 20);
	}


	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}
