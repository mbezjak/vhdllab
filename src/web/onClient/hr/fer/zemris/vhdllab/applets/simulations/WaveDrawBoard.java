package hr.fer.zemris.vhdllab.applets.simulations;


import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Klasa koja predstavlja panel po kojem se crtaju valni oblici
 *
 * @author Boris Ozegovic
 */
class WaveDrawBoard extends JPanel 
{
    /** Prvi valni oblik pocinje po y-osi od 20. piksela */
    private final int FIRST_WAVE_YAXIS_START = 20;

    /** Svaki spring (elasticni pretinac) vala iznosi 45 piksela */
    private final int waveSpringSize;
    
    /** Vrijednosti signala */
    private List<String[]> signalValues;

	/** Sadrzi rezultate simulacije */
    private GhdlResults results;
    
    /** Trajanje u pikselima svake od vrijednosti u signalValues */
    private int[] durationsInPixels;

    /** Svaki valni oblik pocinje od yAxis tocke */
    private int yAxis;
    
    /** Offset po X-osi */
    private int offsetXAxis;
    
    /** Offset po Y-osi */
    private int offsetYAxis;

    /** Kraj vala u pikselima */
    private int waveEndPointInPixels;

    /** Panel se sastoji od valnih oblika */
    private WaveForm waveForm;
    
    /** Skala */
    private Scale scale;

   /** 
     * Lista trenutnih indeksa vektora u listi sa signalima
     */
    private List<Integer> currentVectorIndex = new ArrayList<Integer>();

    /** Varijabla koja sadrzi informaciju je li trenutni signal oznacen misem */
    private boolean isClicked = false;

    /** Sadrzi indeks trenutno oznacenog signala misem */
    private int index = -1;

    /** Pocetna tocka prvog kursora u pikselima */
    private int firstCursorStartPoint;

    /** Pocetna tocka drugog kursora u pikselima */
    private int secondCursorStartPoint;

    /** Aktivni kursor */
    private byte activeCursor = 1;

    /** Boje */
    private ThemeColor themeColor;
    
    /** Svi valni oblici */
    private Shape[] shapes = new Shape[] {new Shape1(), new Shape2(), new Shape3(),
                                          new Shape4(), new Shape5(), new Shape6(), 
                                          new Shape7(), new Shape8(), new Shape9(),
                                          new Shape10(), new Shape11(), new Shape12(),
                                          new Shape13(), new Shape14(), new Shape15(),
                                          new Shape16()};
    /** SerialVersionUID */ 
    private static final long serialVersionUID = 3;



    /**
     * Constructor
     *
     * @param scale skala
	 * @param waveSpringSize velicina springa
     */
    public WaveDrawBoard (ThemeColor themeColor, int waveSpringSize)
    {
        super();
        this.themeColor = themeColor;
		this.waveSpringSize = waveSpringSize;
    }


	/**
	 * Postavljanje vrijednosti za pokretanje panela
	 *
	 * @param resultsrezultati koje je parsirao GhdlResults
	 * @param scale rezultati dobiveni od skale
	 */
	public void setContent(GhdlResults results, Scale scale, 
			int firstCursorStartPoint, int secondCursorStartPoint)
	{
		this.results = results;
        this.signalValues = results.getSignalValues();
		this.firstCursorStartPoint = firstCursorStartPoint;
		this.secondCursorStartPoint = secondCursorStartPoint;
        /* skala proracunava sve parametre i panel koristi gotove parametre */
        this.durationsInPixels = scale.getDurationInPixels();
        this.waveEndPointInPixels = scale.getScaleEndPointInPixels();
        this.scale = scale;
        this.currentVectorIndex = results.getCurrentVectorIndex();
	}


    /**
     * Getter koji vraca prefereiranu dimenziju.
     */

    public Dimension getPreferredSize() 
    { 
        return new Dimension(waveEndPointInPixels, signalValues.size() * waveSpringSize);
    } 

	
    /**
     * Postavlja horizontalni offset
     *
     * @param offset Nova vrijednost
     */
    public void setHorizontalOffset (int offset) 
    {
        this.offsetXAxis = offset;
    }


    /**
     * Vraca horizontalni offset
     */
    public int getHorizontalOffset ()
    {
        return offsetXAxis;
    }
    

    /**
     * Postavlja vertiklani offset
     *
     * @param offset Nova vrijednost
     */
    public void setVerticalOffset (int offset) 
    {
        this.offsetYAxis = offset;
    }


    /**
     * Postavlja nove vrijednosti trajanja 
     *
     * @param durationsInPixels Trajanje po vrijednostima signala
     */
    public void setDurationsInPixels (int[] durationsInPixels)
    {
        this.durationsInPixels = durationsInPixels;
    }


    /**
     * Postavlja krajnju tocka valnih oblika
     *
     * @param waveEndPointInPixels Nova vrijednost
     */
    public void setWaveEndPointInPixels (int waveEndPointInPixels)
    {
        this.waveEndPointInPixels = waveEndPointInPixels;
    }


    /**
     * Metoda koja postavlja novi set vrijednosti signala 
     *
     * @param signalValues Nove vrijednosti po signalima
     */
    public void setSignalValues (List<String[]> signalValues)
    {
        this.signalValues = signalValues;
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
     */
    public boolean getIsClicked ()
    {
        return isClicked;
    }


    /**
     * Vraca indeks trenutno oznacenog signala
     */
    public int getIndex ()
    {
        return index;
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
     * Vraca trenutnu vrijednost pocetka prvog kursora u pikselima
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
     * Vraca trenutnu vrijednost pocetka kursora u pikselima
     */
    public int getSecondCursorStartPoint ()
    {
        return secondCursorStartPoint;
    }


    /**
     * Vraca trenutnu duljinu panela
     */
    public int getPanelWidth()
    {
        return getWidth();
    }

    
    /**
     * Metoda vraca polje valnih oblika
     */
    public Shape[] getShapes ()
    {
        return shapes;
    }


    /**
     * Postavlja aktivni kursor
     */
    public void setActiveCursor (byte activeCursor)
    {
        this.activeCursor = activeCursor;
    }


	/**
	 * Ekspandira bit-vektor
	 *
	 * @param index Indeks bit-vektora koji se ekspandira
	 */
	public void expand (int index)
	{

		String[] expandedValues = new String[signalValues.get(index).length];
		int j = 0;
		for (; j < signalValues.get(index + j)[0].length(); j++)
		{
			expandedValues = new String[signalValues.get(index).length];
			for (int k = 0; k < signalValues.get(index).length; k++)
			{
				expandedValues[k] = String.valueOf(signalValues.get(index + j)[k].charAt(j));
			}

			signalValues.add(index + j, expandedValues);
		}
		signalValues.remove(index + j);
	}


	/**
	 * Kolapsira bit-vektor
	 * 
	 * @param index Indeks bit-vektora koji se kolapsira
	 */
	public void collapse (int index)
	{
		Integer defaultIndex = currentVectorIndex.get(index);
		int vectorSize = results.getDefaultSignalValues()[defaultIndex][0].length();
		for (int i = 0; i < vectorSize; i++)
		{
			signalValues.remove(index);
		}
		signalValues.add(index, results.getDefaultSignalValues()[defaultIndex]);
	}

    
    /**
     * Crtanje valnih oblika 
     *
     * @param g Graphics objekt
     */
	public void paintComponent (Graphics g) 
	{
		super.paintComponent(g);

        setBackground(themeColor.getWaves());

        waveEndPointInPixels = scale.getScaleEndPointInPixels();
		yAxis = FIRST_WAVE_YAXIS_START - offsetYAxis;

        /* crtanje isprekidanih crta */
        g.setColor(themeColor.getWavesNet());
        int x = 100 - offsetXAxis % 100;
        int y = 0;
        while (x < this.getWidth() + 100)
        {
            y = 0;
            while (y < this.getHeight() + 100)
            {
                g.drawLine(x, y, x, y + 8);
                y += 12;
            }
            x += 100;
        }
        
		/* ako treba oznaciti neki od signala */
        if (isClicked)
        {
            g.setColor(themeColor.getApplet());
            g.fillRect(0, index * waveSpringSize + 15 - offsetYAxis,
                    getPreferredSize().width, waveSpringSize / 2 + 5);
        }
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
        if (activeCursor == (byte)1)
        {
            g.setColor(themeColor.getActiveCursor());
            g.drawLine(firstCursorStartPoint - offsetXAxis, 0, 
					firstCursorStartPoint - offsetXAxis, this.getHeight());
            g.setColor(themeColor.getPasiveCursor());
            g.drawLine(secondCursorStartPoint - offsetXAxis, 0, 
					secondCursorStartPoint - offsetXAxis, this.getHeight());
        }
        else
        {
            g.setColor(themeColor.getActiveCursor());
            g.drawLine(secondCursorStartPoint - offsetXAxis, 0, 
					secondCursorStartPoint - offsetXAxis, this.getHeight());
            g.setColor(themeColor.getPasiveCursor());
            g.drawLine(firstCursorStartPoint - offsetXAxis, 0, 
					firstCursorStartPoint - offsetXAxis, this.getHeight());
        }

        /* crtanje valova */
        g.setColor(themeColor.getLetters());


		for (String[] array : signalValues)
		{
			waveForm =  new WaveForm(array, durationsInPixels, shapes);
			waveForm.drawWave(g, this.getWidth(), yAxis, offsetXAxis, 
					durationsInPixels, waveEndPointInPixels);
			yAxis += waveSpringSize;
            
		}
	}
}

