package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


/**
 * Predstavlja panel po kojem se pomice kursor 
 *
 * @author Boris Ozegovic
 */
class CursorPanel extends JPanel
{
     /* Aktivan je prvi kursor */
    private final byte FIRST = 1;

    /** Zavrsna tocka panela  */
    private int panelEndPoint;
     
    /** Pocetna tocka prvog kursora u pikselima */
    private int firstCursorStartPoint;

    /** Pocetna tocka drugog kursora u pikselima */
    private int secondCursorStartPoint;

    /** Offset horizntalnog scrollbara */
    private int offset;

    /** Vrijednost prvog kursora u stringu */
    private String firstString;

    /** Vrijednost drugog kursora u stringu */
    private String secondString;

    /** Vrijednost prvog kursora */
    private double firstValue;
    
    /** Vrijednost drugog kursora */
    private double secondValue;

	/** 
	 * Indeks prvog kursora nakon sto se pozicionira na tja kursor, 
	 * tj. indeks vrijednosti 
	 */
	private int firstValueIndex;

	/**
	 * Indeks drugog kursora nakon sto se pozicionira na taj kursor, 
	 * tj. indeks vrijednosti
	 */
	private int secondValueIndex;

    /** Mjerna jedinica */
    private String measureUnitName;

    /** Aktivan kursor; 0 */
    private byte activeCursor = FIRST;

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
    public CursorPanel (ThemeColor themeColor)
    {
        this.themeColor = themeColor;
    }


	/**
	 * Postavlja vrijednosti potrebne za iscrtavanje panela
	 *
	 * @param panelEndPoint zavrsna tocka panela
	 * @param offset pomak
	 * @param measureUnitName jedinica
	 */
	public void setContent(int panelEndPoint, int offset, double firstValue, 
			double secondValue, int firstCursorStartPoint, 
			int secondCursorStartPoint, String measureUnitName)
	{
		this.panelEndPoint = panelEndPoint;
        this.offset = offset;
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.firstCursorStartPoint = firstCursorStartPoint;
		this.secondCursorStartPoint = secondCursorStartPoint;
        this.measureUnitName = measureUnitName;
		firstString = firstValue + this.measureUnitName;
        secondString = secondValue + this.measureUnitName;
	}


	/**
     * Vraca preferiranu velicinu
     */
    public Dimension getPreferredSize ()
    {
        return new Dimension(panelEndPoint, 30);
    }


    /**
     * Postavlja novu vrijednost pocetka prvog kursora
     *
     * @param firstCursorStartPoint nova vrijednost
     */
    public void setFirstCursorStartPoint (int firstCursorStartPoint)
    {
        this.firstCursorStartPoint = firstCursorStartPoint;
    }


    /**
     * Vraca trenutni polozaj prvog kursora
     */
    public int getFirstCursorStartPoint ()
    {
        return firstCursorStartPoint;
    }


    /**
     * Postavlja novu vrijednost pocetka drugog kursora
     *
     * @param secondCursorStartPoint nova vrijednost
     */
    public void setSecondCursorStartPoint (int secondCursorStartPoint)
    {
        this.secondCursorStartPoint = secondCursorStartPoint;
    }


    /**
     * Vraca trenutni polozaj drugog kursora
     */
    public int getSecondCursorStartPoint ()
    {
        return secondCursorStartPoint;
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
     * Vrijednost prvog kursora u stringu
     *
     * @param firstString vrijednost
     */
    public void setFirstString (String firstString)
    {
        this.firstString = firstString;
    }


    /**
     * Vrijednost drugog kursora u stringu
     *
     * @param secondString vrijednost
     */
    public void setSecondString (String secondString)
    {
        this.secondString = secondString;
    }


    /** 
     * Vrijednost prvog kursora
     *
     * @param firstValue vrijednost
     */
    public void setFirstValue (double firstValue)
    {
        this.firstValue = firstValue;
    }


    /**
     * Vrijednost prvog kursora
     */
    public double getFirstValue ()
    {
        return firstValue;
    }


    /** 
     * Vrijednost drugog kursora
     *
     * @param secondValue vrijednost
     */
    public void setSecondValue (double secondValue)
    {
        this.secondValue = secondValue;
    }


    /**
     * Vrijednost drugog kursora
     */
    public double getSecondValue ()
    {
        return secondValue;
    }


    /**
     * Vraca trenutno aktivni kursor 
     */
    public byte getActiveCursor ()
    {
        return activeCursor;
    }


	/**
	 * Postavlja valueIndex prvog kursora
	 */
	public void setFirstCursorIndex(int valueIndex) {
		this.firstValueIndex = valueIndex;
	}


	/**
	 * Postavlja valueIndex drugog kursora
	 */
	public void setSecondCursorIndex(int valueIndex) {
		this.secondValueIndex = valueIndex;
	}


	/** 
	 * Vraca valueIndex prvog kursora
	 */
	public int getFirstCursorIndex() {
		return firstValueIndex;
	}


	/**
	 * Vraca valueIndex drugog kursora
	 */
	public int getSecondCursorIndex() {
		return secondValueIndex;
	}


    /**
     * Postavlja novi aktivni kursor
     *
     * @param activeCursor Novi aktivni kursor
     */
    public void setActiveCursor (byte activeCursor)
    {
        this.activeCursor = activeCursor;
    }
    
    
    public void paintComponent (Graphics g) 
	{
        super.paintComponent(g);
        setBackground(themeColor.getCursorPanel());
        g.setColor(themeColor.getLetters());

        /* crtanje kursora */
        if (firstCursorStartPoint < 0)
        {
            firstCursorStartPoint = 0;
        }
        if (secondCursorStartPoint < 0)
        {
            secondCursorStartPoint = 0;
        }
        g.drawString(firstString, firstCursorStartPoint - offset - (firstString.length() * 6) / 2, 20);
        g.drawString(secondString, secondCursorStartPoint - offset - (secondString.length() * 6) / 2, 10);

        if (activeCursor == FIRST)
        {
            g.setColor(themeColor.getActiveCursor());
            g.fillRect(firstCursorStartPoint - offset - 4, 21, 9, 9);
            g.setColor(themeColor.getPasiveCursor());
            g.fillRect(secondCursorStartPoint - offset - 4, 21, 9, 9);
        }
        else
        {
            g.setColor(themeColor.getPasiveCursor());
            g.fillRect(firstCursorStartPoint - offset - 4, 21, 9, 9);
            g.setColor(themeColor.getActiveCursor());
            g.fillRect(secondCursorStartPoint - offset - 4, 21, 9, 9);
        }
    }
} 
