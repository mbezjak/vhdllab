package hr.fer.zemris.vhdllab.simulations;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.Map;

/**
 * Klasa koja predstavlja panel po kojem se crtaju valni oblici
 *
 * @author Boris Ozegovic
 */
class WaveDrawBoard extends JPanel 
{
    /** Prvi valni oblik pocinje po y-osi od 20. piksela */
    private final int FIRST_WAVE_YAXIS_START = 20;

    /** Svaki spring (elasticni pretinac) vala iznosi 40 piksela */
    private final int WAVE_SPRING_SIZE;
    
    /** Vrijednosti signala */
    private String[][] signalValues;
    
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

    /** Panel se sastoji od polja valnih oblika */
    private WaveForm[] waveForm;
    
    /** Skala */
    private Scale scale;

    /** Sadrzi informaciju o trenutno ekspandiranim bit-vektorima */
    private Map<Integer, Boolean> expandedSignalNames;

    /** Sadrzi pocetne tocke springova u pikselima */
    private int[] springStartPoints;
    
    /** Varijabla koja sadrzi informaciju je li trenutni signal oznacen misem */
    private boolean isClicked = false;

    /** Sadrzi indeks trenutno oznacenog signala misem */
    private int index = -1;

    /** Pocetna tocka kursora u pikselima */
    private int cursorStartPoint = 100;

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
     * @param results rezultati simulacije, parsirani od GhdlResults klase
     * @param scale skala
     */
    public WaveDrawBoard (GhdlResults results, Scale scale, int WAVE_SPRING_SIZE, int[] springStartPoints, ThemeColor themeColor)
    {
        super();
        this.signalValues = results.getSignalValues();

        /* skala proracunava sve parametre i panel koristi gotove parametre */
        this.durationsInPixels = scale.getDurationInPixels();
        this.waveEndPointInPixels = scale.getScaleEndPointInPixels();
        this.scale = scale;
        this.expandedSignalNames = results.getExpandedSignalNames();
        this.WAVE_SPRING_SIZE = WAVE_SPRING_SIZE;
        this.springStartPoints = springStartPoints;
        this.themeColor = themeColor;
        waveForm = new WaveForm[signalValues.length];
        int i = 0;

        /* 
         * inicijalizacija pojedinih valnih oblika.  Svakom se valnom obliku
         * odmah dodaje lista vrijednosti (1, 0, U, Z...)
         */
        for (String[] polje : signalValues)
        {
            waveForm[i++] = new WaveForm(polje, durationsInPixels, shapes);
        }
    }


    /**
     * Getter koji vraca prefereiranu dimenziju.
     */

    public Dimension getPreferredSize() 
    { 
        return new Dimension(waveEndPointInPixels, signalValues.length * WAVE_SPRING_SIZE);
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
    public void setSignalValues (String[][] signalValues)
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
     * Postavlja novu vrijednost pocetka kursora
     *
     * @param cursorStartPoint nova vrijednost
     */
    public void setCursorStartPoint (int cursorStartPoint)
    {
        this.cursorStartPoint = cursorStartPoint;
    }


    /**
     * Vraca trenutnu vrijednost pocetka kursora u pikselima
     */
    public int getCursorStartPoint ()
    {
        return cursorStartPoint;
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
            g.fillRect(0, (springStartPoints[index]) + 15 - offsetYAxis,
                    getPreferredSize().width, WAVE_SPRING_SIZE / 2 + 5);
        }
        g.setColor(themeColor.getLetters());
        
        /* crtanje kursora */
        if (cursorStartPoint < 0)
        {
            cursorStartPoint = 0;
        }
        g.setColor(themeColor.getCursor());
        g.drawLine(cursorStartPoint - offsetXAxis, 0, cursorStartPoint - offsetXAxis, this.getHeight());

        /* crtanje valova */
        g.setColor(themeColor.getLetters());
        for (int i = 0; i < waveForm.length; i++)
        {
            waveForm[i].setSignalValues(signalValues[i]);
        }

		for (int i = 0; i < waveForm.length; i++)
		{
            if (expandedSignalNames.containsKey(i) && expandedSignalNames.get(i))
            {
                String[] expandedValues = new String[signalValues[i].length];
                for (int j = 0; j < signalValues[i][0].length(); j++)
                {
                    for (int k = 0; k < signalValues[i].length; k++)
                    {
                        expandedValues[k] = String.valueOf(signalValues[i][k].charAt(j));
                    }
                    waveForm[i].setSignalValues(expandedValues);
                    waveForm[i].drawWave(g, this.getWidth(), yAxis, offsetXAxis, durationsInPixels, waveEndPointInPixels);
			        yAxis += WAVE_SPRING_SIZE;
                }
            }
            else
            {
                waveForm[i].drawWave(g, this.getWidth(), yAxis, offsetXAxis, durationsInPixels, waveEndPointInPixels);
			    yAxis += WAVE_SPRING_SIZE;
            }
		}
	}
}
