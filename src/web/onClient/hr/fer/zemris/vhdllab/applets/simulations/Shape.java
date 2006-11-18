package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Color;
import java.awt.Graphics;


/**
 * Interface za valne oblike
 *
 * @author Boris Ozegovic
 */
interface Shape
{
    /**
     * Crta valni oblik
     *
     * @param g Graphics
     * @param x1 pocetna tocka po X-osi od koje pocinje crtanje
     * @param y1 pocetna tocka po Y-osi od koje pocinje crtanje
     * @param x2 zarsna tocka po X-osi do koje traje crtanje
     */
	public void draw(Graphics g, int x1, int y1, int x2);
  
   
    /**
     * Postavlja labelu iznad/unutar valnog oblika
     *
     * @param g Graphics
     * @param s labela koju treba ispisati
     * @param x1 pocetna tocka po X-osi od koje pocinje crtanje
     * @param y1 pocetna tocka po Y-osi od koje pocinje crtanje
     * @param x2 zarsna tocka po X-osi do koje traje crtanje
     */
	public void putLabel(Graphics g, String s, int x1, int y1, int x2);
   
    
    /**
     * Postavlja boju valnog oblika
     *
     * @param color zeljena boja
     */
    public void setColor(Color color); 

    
    /**
     * Vraca trenutnu boju valnog oblika
     */
    public Color getColor(); 
}
