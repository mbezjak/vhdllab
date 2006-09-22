import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;


/**
 * Help panel u kojem se nalaze informacije o izgledu signala
 *
 * @author Boris Ozegovic
 */
class HelpPanel extends JPanel
{
    public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		g.drawLine(5, 30, 105, 30);
		g.drawLine(105, 30, 105, 5);
        g.drawLine(105, 5, 205, 5);
        g.drawString("1", 220, 25);

		g.drawLine(5, 40, 105, 40);
		g.drawLine(105, 40, 105, 65);
        g.drawLine(105, 65, 205, 65);
        g.drawString("0", 220, 60);

        g.drawLine(5, 88, 105, 88);
        g.drawLine(5, 89, 105, 89);
        g.drawLine(5, 90, 105, 90);
        g.drawLine(5, 91, 105, 91);   
        g.drawString("High impedance 'Z'", 220, 95);

        g.setColor(new Color(119, 119, 192));
        g.drawLine(5, 108, 105, 108);
        g.drawLine(5, 109, 105, 109);
        g.drawLine(5, 110, 105, 110);
        g.drawLine(5, 111, 105, 111);   
        g.setColor(new Color(51, 51, 51));
        g.drawString("U or H or L or W.  Has a tag above waveform", 220, 115);

        int x1 = 5;
        int y1 = 128;
        while (x1 + 6 <= 105)
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

        g.drawLine(x1, y1 + 20, 105, y1 + 16);
        g.drawLine(x1, y1 + 16, 105, y1 + 12);
        g.drawLine(x1, y1 + 12, 105, y1 + 8);
        g.drawLine(x1, y1 + 8, 105, y1 + 4);
        g.drawLine(x1, y1 + 4, 105, y1);
        
        g.drawLine(x1, y1, 105, y1 + 4);
        g.drawLine(x1, y1 + 4, 105, y1 + 8);
        g.drawLine(x1, y1 + 8, 105, y1 + 12);
        g.drawLine(x1, y1 + 12, 105, y1 + 16);
        g.drawLine(x1, y1 + 16, 105, y1 + 20);
        g.drawString("Unknown value 'X'", 220, 140);

        g.drawLine(5, 166 + 10, 5 + 3, 166);
        g.drawLine(5, 166 + 10, 5 + 3, 166 + 20);
        g.drawLine(105, 166 + 10, 105 - 3, 166);
        g.drawLine(105, 166 + 10, 105 - 3, 166 + 20);
        g.drawLine(5 + 3, 166, 105 - 3, 166);
        g.drawLine(5 + 3, 166 + 20, 105 - 3, 166 + 20);
        g.drawString("bit-vector", 220, 180);

        g.drawString("Keyboard shortcuts:", 5, 240);
        g.drawString("+ == zoom in by two", 5, 270);
        g.drawString("- == zoom out by two", 5, 290);
        g.drawString("( == zoom in by ten", 5, 310);
        g.drawString(") == zoom out by ten", 5, 330);
        g.drawString("w or up arrow == scroll up verticalScrollbar", 5, 350);
        g.drawString("s or down arrow == scroll down verticalScrollbar", 5, 370);
        g.drawString("a or left arrow == scroll left horizontalScrollbar", 5, 390);
        g.drawString("d or righ arrow == scroll right horizontalScrollbar", 5, 410);
        g.drawString("lk == move to next positive edge", 5, 430);
        g.drawString("lj == move to next negative edge", 5, 450);
        g.drawString("hk == move to previous positive edge", 5, 470);
        g.drawString("hj == move to previous negative edge", 5, 490);
        g.drawString("pageUp == page up", 5, 510);
        g.drawString("pageDown == page down", 5, 530);
        g.drawString("end and home == end and home", 5, 550);
    }
}
