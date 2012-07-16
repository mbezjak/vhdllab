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
 * Predstavlja jedinicu s vertikalnom crtom prema dolje na pocetku.  Dolazi kao
 * pocetna vrijednost ili iza nule
 *
 * @author Boris Ozegovic
 */
class Shape5 implements Shape
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
		g.drawLine(x1, y1, x2, y1);  // jedinica s vertikalnom crtom prema dolje
		g.drawLine(x1, y1, x1, y1 + 20);
	}


    /**
     * Za buducu uporabu
     */
	public void putLabel(Graphics g, String s, int x1, int y1, int x2)
	{
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
