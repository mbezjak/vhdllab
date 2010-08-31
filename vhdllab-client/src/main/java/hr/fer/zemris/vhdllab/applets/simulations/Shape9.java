/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Color;
import java.awt.Graphics;


/**
 * Predstavlja heksagon s poluvertikalnom crtom prema dolje na pocetku.  Dolazi
 * iza nule
 *
 * @author Boris Ozegovic
 */
class Shape9 implements Shape
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


    /**
     * Postavlja labelu unutar valnog oblika
     *
     * @param g Graphics
     * @param string labela koju treba ispisati
     * @param x1 pocetna tocka po X-osi od koje pocinje crtanje
     * @param y1 pocetna tocka po Y-osi od koje pocinje crtanje
     * @param x2 zarsna tocka po X-osi do koje traje crtanje
     */
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
