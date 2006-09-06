import javax.swing.*;
import java.awt.*;


/**
 * Predstavlja nepoznatu vrijednost: X
 *
 * @author Boris Ozegovic
 */
class Shape13 implements Shape
{
    public void draw (Graphics g, int x1, int y1, int x2)
    {
        while (x1 + 6 <= x2)
        {
            g.drawLine(x1, y1 + 20, x1 + 6, y1 + 16);
            g.drawLine(x1, y1 + 16, x1 + 6, y1 + 12);
            g.drawLine(x1, y1 + 12, x1 + 6, y1 + 8);
            g.drawLine(x1, y1 + 8, x1 + 6, y1 + 4);
            g.drawLine(x1, y1 + 4, x1 + 6, y1);   

            g.drawLine(x1, y1, x1 + 6, y1 + 4);
            g.drawLine(x1, y1 + 4, x1 + 6, y1 + 8);
            g.drawLine(x1, y1 + 8, x1 + 6, y1 + 12);
            g.drawLine(x1, y1 + 12, x1 + 6, y1 + 16);
            g.drawLine(x1, y1 + 16, x1 + 6, y1 + 20);
            x1 += 6;
        }

            g.drawLine(x1, y1 + 20, x2, y1 + 16);
            g.drawLine(x1, y1 + 16, x2, y1 + 12);
            g.drawLine(x1, y1 + 12, x2, y1 + 8);
            g.drawLine(x1, y1 + 8, x2, y1 + 4);
            g.drawLine(x1, y1 + 4, x2, y1);
            
            g.drawLine(x1, y1, x2, y1 + 4);
            g.drawLine(x1, y1 + 4, x2, y1 + 8);
            g.drawLine(x1, y1 + 8, x2, y1 + 12);
            g.drawLine(x1, y1 + 12, x2, y1 + 16);
            g.drawLine(x1, y1 + 16, x2, y1 + 20);
    }


    public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}
