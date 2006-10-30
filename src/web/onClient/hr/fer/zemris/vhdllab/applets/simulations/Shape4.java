package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Graphics;
import java.awt.Color;


/**
 * Predstavlja cistu jedinicu.  Dolazi iza jedinice
 *
 * @author Boris Ozegovic
 */
class Shape4 implements Shape
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
		g.drawLine(x1, y1, x2, y1);  
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
