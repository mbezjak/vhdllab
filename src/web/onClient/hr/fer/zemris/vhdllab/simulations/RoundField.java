import javax.swing.*;
import java.awt.*;

/**
 * Crta zaobljeni JTextField
 *
 * @author sun
 */
public class RoundField extends JTextField 
{
    public RoundField (int cols) 
    {
        super(cols);

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    }

    protected void paintComponent (Graphics g) 
    {
        int width = getWidth();
        int height = getHeight();

        g.setColor(new Color(254, 217, 182));
        g.fillRoundRect(0, 0, width, height, height, height);

        super.paintComponent(g);
    }
}

