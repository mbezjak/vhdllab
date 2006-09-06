package hr.fer.zemris.vhdllab.simulations;

import java.awt.Graphics;


/**
 * Predstavlja visoku impedanciju s poluvertikalnom crtom prema dolje. Dolazi
 * iza nule
 *
 * @author Boris Ozegovic
 */
class Shape11 implements Shape
{
    public void draw (Graphics g, int x1, int y1, int x2)
    {
        g.drawLine(x1, y1 + 8, x2, y1 + 8);
        g.drawLine(x1, y1 + 9, x2, y1 + 9);
        g.drawLine(x1, y1 + 10, x2, y1 + 10);
        g.drawLine(x1, y1 + 11, x2, y1 + 11);
        g.drawLine(x1, y1 + 10, x1, y1 + 20);    
    }


    public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}
