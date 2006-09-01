package hr.fer.zemris.vhdllab.simulations;

import java.awt.Graphics;

interface Shape
{
	void draw(Graphics g, int x1, int y1, int x2);
	void putLabel(Graphics g, String s, int x1, int y1, int x2);
}


/**
 * Predstavlja cistu nulu.  Moze biti pocetna vrijednost ili dolazi iza nule 
 *
 * @author Boris Ozegovic
 */
class Shape1 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1 + 20, x2, y1 + 20); 
	}
	
	
	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}


/**
 * Predstavlja nulu s vertikalnom crtom prema gore na pocetku.  Dolazi iza jedinice
 *
 * @author Boris Ozegovic
 */
class Shape2 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1 + 20, x2, y1 + 20);  
		g.drawLine(x1, y1, x1, y1 + 20);
	}


	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}


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


/**
 * Predstavlja cistu jedinicu.  Dolazi iza jedinice
 *
 * @author Boris Ozegovic
 */
class Shape4 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
		g.drawLine(x1, y1, x2, y1);  
	}


	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
		;
	}
}


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


/**
 * Predstavlja cisti heksagon.  Dolazi na pocetku ili iza heksagona
 *
 * @author Boris Ozegovic
 */
class Shape7 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
        if (x2 - 3 > x1 + 3)
        {
            g.drawLine(x1, y1 + 10, x1 + 3, y1);
            g.drawLine(x1, y1 + 10, x1 + 3, y1 + 20);
            g.drawLine(x2, y1 + 10, x2 - 3, y1);
            g.drawLine(x2, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x1 + 3, y1, x2 - 3, y1);
            g.drawLine(x1 + 3, y1 + 20, x2 - 3, y1 + 20);
        }

        /* 
         * kada je trajanje heksagona premalo, i postaje sve manje kako se
         * srolla, heksagon ostaje spojen
         */
        else
        {
            g.drawLine(x1, y1 + 10, x2 - 3, y1);
            g.drawLine(x1, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x2, y1 + 10, x2 - 3, y1);
            g.drawLine(x2, y1 + 10, x2 - 3, y1 + 20);
        }
            
    }


	public void putLabel (Graphics g, String string, int x1, int y1, int x2)
	{
		int length = x2 - x1;
		int middle = length / 2;
		int startPoint = x1 + (middle - (string.length() * 7) / 2);

        /* ako je trajanje premaleno, ne crtaj string unutar heksagona */
		if (length > string.length() * 8 && length > 10)
        {
            g.drawString(string, startPoint, y1 + 14);
        }
	}
}


/**
 * Predstavlja heksagon s poluvertilkalnom crtom prema gore na pocetku.  Dolazi
 * iza jedinice.
 *
 * @author Boris Ozegovic
 */
class Shape8 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
        if (x2 - 3 > x1 + 3)
        {
            g.drawLine(x1, y1 + 10, x1 + 3, y1);
            g.drawLine(x1, y1 + 10, x1 + 3, y1 + 20);
            g.drawLine(x2, y1 + 10, x2 - 3, y1);
            g.drawLine(x2, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x1 + 3, y1, x2 - 3, y1);
            g.drawLine(x1 + 3, y1 + 20, x2 - 3, y1 + 20);
            g.drawLine(x1, y1, x1, y1 + 10);
        }

        /* 
         * kada je trajanje heksagona premalo, i postaje sve manje kako se
         * srolla, heksagon ostaje spojen
         */
        else
        {
            g.drawLine(x1, y1 + 10, x2 - 3, y1);
            g.drawLine(x1, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x2, y1 + 10, x2 - 3, y1);
            g.drawLine(x2, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x1, y1, x1, y1 + 10);
        }
            
	}


	public void putLabel (Graphics g, String string, int x1, int y1, int x2)
	{
		int length = x2 - x1;
		int middle = length / 2;
		int startPoint = x1 + (middle - (string.length() * 7) / 2);

        /* ako je trajanje heksagona premalo, ne crtaj String */
		if (length > string.length() * 8 && length > 10)
        {
            g.drawString(string, startPoint, y1 + 14);
        }
	}
}


/**
 * Predstavlja heksagon s poluvertikalnom crtom prema dolje na pocetku.  Dolazi
 * iza nule
 *
 * @author Boris Ozegovic
 */
class Shape9 implements Shape
{
	public void draw (Graphics g, int x1, int y1, int x2)
	{
        if (x2 - 3 > x1 + 3)
        {
            g.drawLine(x1, y1 + 10, x1 + 3, y1);
            g.drawLine(x1, y1 + 10, x1 + 3, y1 + 20);
            g.drawLine(x2, y1 + 10, x2 - 3, y1);
            g.drawLine(x2, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x1 + 3, y1 + 20, x2 - 3, y1 + 20);
            g.drawLine(x1 + 3, y1, x2 - 3, y1);
            g.drawLine(x1, y1 + 10, x1, y1 + 20);
        }

        /* 
         * kada je trajanje heksagona premalo, i postaje sve manje kako se
         * srolla, heksagon ostaje spojen
         */
        else
        {
            g.drawLine(x1, y1 + 10, x2 - 3, y1);
            g.drawLine(x1, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x2, y1 + 10, x2 - 3, y1);
            g.drawLine(x2, y1 + 10, x2 - 3, y1 + 20);
            g.drawLine(x1, y1 + 10, x1, y1 + 20);
        }
            
	}


	public void putLabel (Graphics g, String string, int x1, int y1, int x2)
	{
		int length = x2 - x1;
		int middle = length / 2;
		int startPoint = x1 + (middle - (string.length() * 7) / 2);

        
        /* ako je trajanje heksagona premalo, ne crtaj String */
		if (length > string.length() * 8 && length > 10)
        {
            g.drawString(string, startPoint, y1 + 14);
        }
	}
}

