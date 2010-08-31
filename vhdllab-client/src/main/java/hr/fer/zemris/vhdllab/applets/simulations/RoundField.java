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

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/**
 * Crta zaobljeni JTextField
 *
 * @author sun
 */
public class RoundField extends JTextField 
{
    /**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -8155311461982032920L;

	public RoundField (int cols) 
    {
        super(cols);

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    }

    @Override
    protected void paintComponent (Graphics g) 
    {
        int width = getWidth();
        int height = getHeight();

        g.setColor(new Color(254, 217, 182));
        g.fillRoundRect(0, 0, width, height, height, height);

        super.paintComponent(g);
    }
}

