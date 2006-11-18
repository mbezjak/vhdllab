package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Color;
import java.awt.Graphics;


/**
 * Predstavlja visoku impedanciju s poluvertikalnom crtom prema gore. Dolazi
 * iza jedinice
 *
 * @author Boris Ozegovic
 */
class Shape12 implements Shape
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
        g.drawLine(x1, y1 + 8, x2, y1 + 8);
        g.drawLine(x1, y1 + 9, x2, y1 + 9);
        g.drawLine(x1, y1 + 10, x2, y1 + 10);
        g.drawLine(x1, y1 + 11, x2, y1 + 11);
        g.drawLine(x1, y1, x1, y1 + 10);    
    }


    /**
     * Postavlja labelu iznad valnog oblika
     *
     * @param g Graphics
     * @param s labela koju treba ispisati
     * @param x1 pocetna tocka po X-osi od koje pocinje crtanje
     * @param y1 pocetna tocka po Y-osi od koje pocinje crtanje
     * @param x2 zarsna tocka po X-osi do koje traje crtanje
     */
    public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		if (x2 - x1 >= 10)
        {
             g.drawString(s, x1, y1 + 5);
        }
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
