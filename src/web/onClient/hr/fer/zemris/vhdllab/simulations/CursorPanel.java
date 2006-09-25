package hr.fer.zemris.vhdllab.simulations;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Dimension;


/**
 * Predstavlja panel po kojem se pomice kursor 
 *
 * @author Boris Ozegovic
 */
class CursorPanel extends JPanel
{
    /** Zavrsna tocka panela  */
    private int panelEndPoint;
     
    /** Pocetna tocka kursora u pikselima */
    private int cursorStartPoint = 100;

    /** Offset horizntalnog scrollbara */
    private int offset;

    /** Vrijednost kursora u stringu */
    private String string;

    /** Vrijednost kursora */
    private double value = 100;;

    /** Mjerna jedinica */
    private String measureUnitName;

    /** Boje */
    private ThemeColor themeColor;

    /** SerialVersionUID */ 
    private static final long serialVersionUID = 5;



    /**
     * Constructor
     *
     * @param panelEndPoint duzina panela u pikselima
     * @param offset trenutni pomak
     * @param measureUnitName mjerna jedinica
     */
    public CursorPanel (int panelEndPoint, int offset, String measureUnitName, ThemeColor themeColor)
    {
        this.panelEndPoint = panelEndPoint;
        this.offset = offset;
        this.measureUnitName = measureUnitName;
        this.themeColor = themeColor;
        string = "100.0" + this.measureUnitName;
    }


    /**
     * Vraca preferiranu velicinu
     */
    public Dimension getPreferredSize ()
    {
        return new Dimension(panelEndPoint, 20);
    }


    /**
     * Postavlja novu vrijednost pocetka kursora
     *
     * @param cursorStartPoint nova vrijednost
     */
    public void setCursorStartPoint (int cursorStartPoint)
    {
        this.cursorStartPoint = cursorStartPoint;
    }


    /**
     * Vraca trenutni polozaj kursora
     */
    public int getCursorStartPoint ()
    {
        return cursorStartPoint;
    }



    /**
     * Postavlja trenutni offset
     *
     * @param offset novi offset
     */
    public void setOffset (int offset)
    {
        this.offset = offset;
    }


    /**
     * Vrijednost kursora u stringu
     *
     * @param string vrijednost
     */
    public void setString (String string)
    {
        this.string = string;
    }


    /** 
     * Vrijednost kursora
     *
     * @param value vrijednost
     */
    public void setValue (double value)
    {
        this.value = value;
    }


    /**
     * Vrijednost kursora
     */
    public double getValue ()
    {
        return value;
    }
    
    
    public void paintComponent (Graphics g) 
	{
        super.paintComponent(g);
        setBackground(themeColor.getCursorPanel());
        g.setColor(themeColor.getLetters());

        /* crtanje kursora */
        if (cursorStartPoint < 0)
        {
            cursorStartPoint = 0;
        }
        g.drawString(string, cursorStartPoint - offset - (string.length() * 6) / 2, 10);
        g.setColor(themeColor.getApplet());
        g.fillRect(cursorStartPoint - offset - 4, 11, 9, 9);
    }
}
