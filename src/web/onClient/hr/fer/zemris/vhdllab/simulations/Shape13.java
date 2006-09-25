package hr.fer.zemris.vhdllab.simulations;

import java.awt.Graphics;
import java.awt.Color;


/**
 * Predstavlja nepoznatu vrijednost: X
 *
 * @author Boris Ozegovic
 */
class Shape13 implements Shape
{
    private Color color = new Color(51, 51, 51);


    /**
     * Crta valni oblik
     *
     * @param g Graphics
     * @param x1 pocetna tocka po X-osi od koje pocinje crtanje
     * @param y1 pocetna tocka po Y-osi od koje pocinje crtanje
     * @param x2 zarsna tocka po X-osi do koje traje crtanje
     */
    public void draw (Graphics g, int x1, int y1, int x2)
    {
        g.setColor(color);
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


    /**
     * Za buducu uporabu
     */
    public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}


    /**
     * Postavlja boju valnog oblika
     *
     * @param color zeljena boja
     */
    public void setColor (Color color)
    {
        this.color = color;
    }


    /**
     * Vraca trenutnu boju valnog oblika
     */
    public Color getColor ()
    {
        return color;
    }
}
