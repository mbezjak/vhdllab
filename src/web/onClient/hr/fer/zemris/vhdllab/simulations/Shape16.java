package hr.fer.zemris.vhdllab.simulations;

import javax.swing.*;
import java.awt.*;


/**
 * Predstavlja visoku impedanciju s poluvertikalnom crtom prema gore. Dolazi
 * iza jedinice
 *
 * @author Boris Ozegovic
 */
class Shape16 implements Shape
{
    public void draw (Graphics g, int x1, int y1, int x2)
    {
        g.setColor(new Color(119, 119, 192));
        g.drawLine(x1, y1 + 8, x2, y1 + 8);
        g.drawLine(x1, y1 + 9, x2, y1 + 9);
        g.drawLine(x1, y1 + 10, x2, y1 + 10);
        g.drawLine(x1, y1 + 11, x2, y1 + 11);
        g.drawLine(x1, y1, x1, y1 + 10);    
        g.setColor(new Color(51, 51, 51));
    }


    public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		if (x2 - x1 >= 10)
        {
             g.drawString(s, x1, y1 + 5);
        }
	}
}

