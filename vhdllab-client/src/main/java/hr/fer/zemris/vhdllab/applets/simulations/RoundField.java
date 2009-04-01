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

