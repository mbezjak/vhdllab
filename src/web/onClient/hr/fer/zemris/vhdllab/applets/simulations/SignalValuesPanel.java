package hr.fer.zemris.vhdllab.applets.simulations;


import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


/**
 * Panel sadrzi trenutne vrijednosti signala, ovisno o polozaju kursora
 *
 * @author Boris Ozegovic
 */
class SignalValuesPanel extends JPanel
{
	/** Prva vrijednosti pocinje od 30-tog piksela */
    private final int YAXIS_START_POINT = 30;

    /** Svaka se vrijednost nalazi u springu (elasticnom pretincu) koji je visine 45 piksela */
	private final int SIGNAL_NAME_SPRING_HEIGHT = 45;

    /** Maksimalna duljina koju panel moze poprimiti iznosi 150 piksela */
	private final int PANEL_MAX_WIDTH = 650;

	/** Trenutni indeks na kojem je kursor, vrijednost na tom indeksu crta se u panelu */
    private int valueIndex;

	/** Sadrzi rezultate simulacije */
    private GhdlResults results; 
	
	/** Polozaj trenutnog springa */
    private int yAxis;
    
    /** Trenutni offset po Y-osi */
    private int offsetYAxis;

    /** Trenutni offset po X-osi */
    private int offsetXAxis;	

	/** Sirina panela s imenima signala */
    private int panelWidth;

	/** Makimalna duljina vektora, ukoliko postoje vektori */
    private int maximumVectorSize;

	/** Varijabla koja sadrzi informaciju je li trenutni signal oznacen misem */
    private boolean isClicked = false;

    /** Sadrzi indeks trenutno oznacenog signala misem */
    private int index = -1;

    /** Boje */
    private ThemeColor themeColor;

    /** SerialVersionUID */ 
    private static final long serialVersionUID = 9;


	 /**
     * Constructor
     *
     * @param results rezultati dobiveni HTTP-om parsirani od GhdlResults klase 
	 * @param themeColor Sve raspolozive boje
     */
    public SignalValuesPanel (GhdlResults results, ThemeColor themeColor)
    {
		super();
		this.results = results;
		this.maximumVectorSize = results.getMaximumVectorSize();
		this.themeColor = themeColor;
		panelWidth = maximumVectorSize * 6;
	}


	/**
     * Getter koji vraca preferirane dimenzije
     */
    public Dimension getPreferredSize() 
    { 
        return new Dimension(panelWidth + 4, 
				results.getSignalNames().size() * SIGNAL_NAME_SPRING_HEIGHT); 
    } 


    /**
     * Getter koji vraca preferirane dimenzije ako je ime najvece duljine manje
     * od 650 piksela, inace vraca 650 piksela
     */
    public Dimension getMaximumSize()
    {
        if (panelWidth < PANEL_MAX_WIDTH)
        {
            return new Dimension(panelWidth, 
					results.getSignalNames().size() * SIGNAL_NAME_SPRING_HEIGHT); 
        }
        else
        {
            return new Dimension(PANEL_MAX_WIDTH, results.getSignalNames().size() * SIGNAL_NAME_SPRING_HEIGHT);
        }
    }

	/**
     * Setter koji postavlja vertikalni offset
     *
     * @param offset Zeljeni novi offset
     */
	public void setVerticalOffset (int offset) 
    {
        this.offsetYAxis = offset;
    }


    /**
     * Vraca trenutni vertikalni offset
     */
    public int getVerticalOffset ()
    {
        return offsetYAxis;
    }


    /**
     * Setter koji postavlja horizontalni offset
     *
     *@param offset Zeljeni novi offset
     */
    public void setHorizontalOffset (int offset)
    {
        this.offsetXAxis = offset;
    }


	/**
     * Postavlja informaciju treba li oznaciti signal
     *
     * @param isClicked true or false
     */
    public void setIsClicked (boolean isClicked)
    {
        this.isClicked = isClicked;
    }


    /**
     * Postavlja trenutni indeks oznacenog signala 
     *
     * @param index indeks oznacenog signala
     */
    public void setIndex (int index)
    {
        this.index = index;
    }


    /**
     * Vraca informaciju je li signal oznacen
     *
     * @return true ako je kliknut, inace false
     */
    public boolean getIsClicked ()
    {
        return isClicked;
    }


    /**
     * Vraca indeks trenutno oznacenog signala
     *
     * @return Index kliknutog signala, inace vraca -1
     */
    public int getIndex ()
    {
        return index;
    }


    /**
     * Vraca visinu springa u kojoj je smjesteno ime signala 
     */
    public int getSignalNameSpringHeight ()
    {
        return SIGNAL_NAME_SPRING_HEIGHT;
    }


	/**
	 * Postavlja indeks vrijednosti na kojoj se trenutno nalazi kursor 
	 */
	public void setValueIndex (int valueIndex)
	{
		this.valueIndex = valueIndex;
	}


	/**
     * Postavlja novu vrijednosti sirine panela
     *
     * @param maximumSignalNameLength nova vrijednost
     */
    public void setPanelWidth (int panelWidth)
    {
        this.panelWidth = panelWidth;
    }


    /**
     * Vraca sirinu panela
     */
    public int getPanelWidth ()
    {
        return panelWidth;
    }
 


	 /**
     * Crta komponentu
     *
     * @param g Graphics objekt
     */
    public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

        setBackground(themeColor.getSignalNames());
		/* ako treba oznaciti neki od signala */
        if (isClicked)
        {
            g.setColor(themeColor.getApplet());
            g.fillRect(0, index * SIGNAL_NAME_SPRING_HEIGHT + 15 - offsetYAxis, 
                    getMaximumSize().width, SIGNAL_NAME_SPRING_HEIGHT / 2 + 5);
        }
        g.setColor(themeColor.getLetters());
		yAxis = YAXIS_START_POINT - offsetYAxis;
		for (int i = 0; i < results.getSignalValues().size(); i++)
		{
			g.drawString(results.getSignalValues().get(i)[valueIndex], 5 - offsetXAxis, yAxis);
			yAxis += SIGNAL_NAME_SPRING_HEIGHT;
		}
	}
}
