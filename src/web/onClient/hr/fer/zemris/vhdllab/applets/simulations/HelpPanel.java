package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


/**
 * Help panel u kojem se nalaze informacije o izgledu signala i u kojem su
 * navedene sve tipkonicke kratice
 *
 * @author Boris Ozegovic
 */
class HelpPanel extends JPanel
{
    /* Valni oblici */
    private Shape[] shapes;

    
    /** SerialVersionUID */ 
    private static final long serialVersionUID = 6;


	/**
	 * Postavlja vrijednosti potrebne za iscrtavanje panela
	 *
	 * @param shapes oblici
	 */
	public void setContent(Shape[] shapes)
	{
		this.shapes = shapes;
	}


    /**
     * Crta HelpPanel
     *
     * @param g Graphics objekt
     */
    public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

        /* predstavlja valni oblik koji predstavlja jedinicu */
        g.setColor(shapes[3].getColor());
		g.drawLine(5, 30, 105, 30);
		g.drawLine(105, 30, 105, 5);
        g.drawLine(105, 5, 205, 5);
        g.drawString("1", 220, 25);

        /* predstavlja valni oblik koji predstavlja nulu */
        g.setColor(shapes[0].getColor());
		g.drawLine(5, 40, 105, 40);
		g.drawLine(105, 40, 105, 65);
        g.drawLine(105, 65, 205, 65);
        g.drawString("0", 220, 60);

        /* crta valni oblik koji predstavlja visoku impedanciju */
        g.setColor(shapes[9].getColor());
        g.drawLine(5, 88, 105, 88);
        g.drawLine(5, 89, 105, 89);
        g.drawLine(5, 90, 105, 90);
        g.drawLine(5, 91, 105, 91);   
        g.drawString("High impedance 'Z'", 220, 95);

        /* crta valni oblik koji predstavlja u, H, L i W signale */
        g.setColor(shapes[13].getColor());
        g.drawLine(5, 108, 105, 108);
        g.drawLine(5, 109, 105, 109);
        g.drawLine(5, 110, 105, 110);
        g.drawLine(5, 111, 105, 111);   
        g.setColor(new Color(51, 51, 51));
        g.drawString("U or H or L or W.  Has a tag above waveform", 220, 115);

        /* crta valni oblik koji predstavlja unknown value */
        int x1 = 5;
        int y1 = 128;
        g.setColor(shapes[12].getColor());
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

        /* crta valni oblik koji predstavlja bit-vektor */
        g.setColor(shapes[6].getColor());
        g.drawLine(5, 166 + 10, 5 + 3, 166);
        g.drawLine(5, 166 + 10, 5 + 3, 166 + 20);
        g.drawLine(105, 166 + 10, 105 - 3, 166);
        g.drawLine(105, 166 + 10, 105 - 3, 166 + 20);
        g.drawLine(5 + 3, 166, 105 - 3, 166);
        g.drawLine(5 + 3, 166 + 20, 105 - 3, 166 + 20);
        g.drawString("bit-vector", 220, 180);

        /* crta tipkovnicke kratice */
        g.setColor(new Color(51, 51, 51));
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
        g.drawString("f == fit to window", 5, 570);
        g.drawString("u == default zoom", 5, 590);
    }
}
