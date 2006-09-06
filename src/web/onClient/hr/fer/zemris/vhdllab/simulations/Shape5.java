package hr.fer.zemris.vhdllab.simulations;

import java.awt.Graphics;
    

/**
 * Predstavlja jedinicu s vertikalnom crtom prema dolje na pocetku.  Dolazi kao
 * pocetna vrijednost ili iza nule
 *
 * @author Boris Ozegovic
 */
class Shape5 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1, x2, y1);  // jedinica s vertikalnom crtom prema dolje
		g.drawLine(x1, y1, x1, y1 + 20);
	}


	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}

