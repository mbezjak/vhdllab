package hr.fer.zemris.vhdllab.simulations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;




/**
 * Klasa koja predstavlja panel po kojem se crtaju valni oblici
 *
 * @author Boris Ozegovic
 */
class WaveDrawBoard extends JPanel 
{
    private final int WAVE_START_POINT_IN_PIXELS = 0;

    /* prvi valni oblik pocinje po y-osi od 20. piksela */
    private final int FIRST_WAVE_YAXIS_START = 20;

    /* svaki spring (elasticni pretinac) vala iznosi 40 piksela */
    private final int WAVE_SPRING_SIZE = 45;
    private final Color BACKGROUND_COLOR = new Color (201, 211, 236);
    
    /* vrijednosti signala */
    private String[][] signalValues;
    /* trajanje u pikselima svake od vrijednosti u signalValues */
    private int[] durationsInPixels;

    /* sljedeca vrijednost vala na y-osi */
    private int yAxis;
    private int offsetXAxis;
    private int offsetYAxis;

    /* kraja vala u pikselima, dobiven iz getScaleEndPointInPixels */
    private int waveEndPointInPixels;

    /* panel se sastoji od polja valnih oblika */
    private WaveForm[] waveForm;
    private Scale scale;
    
    /* varijabla koja sadrzi informaciju je li trenutni signal oznacen misem */
    private boolean isClicked = false;

    /* sadrzi indeks trenutno oznacenog signala misem */
    private int index = -1;


    /**
     * Constructor
     *
     * @param results rezultati simulacije, parsirani od GhdlResults klase
     * @param scale skala
     */
    public WaveDrawBoard (GhdlResults results, Scale scale)
    {
        super();
        this.signalValues = results.getSignalValues();

        /* skala proracunava sve parametre i panel koristi gotove parametre */
        this.durationsInPixels = scale.getDurationInPixels();
        this.waveEndPointInPixels = scale.getScaleEndPointInPixels();
        this.scale = scale;
        waveForm = new WaveForm[signalValues.length];
        setBackground(BACKGROUND_COLOR);
        int i = 0;

        /* 
         * inicijalizacija pojedinih valnih oblika.  Svakom se valnom obliku
         * odmah dodaje lista vrijednosti (1, 0, U, Z...)
         */
        for (String[] polje : signalValues)
        {
            waveForm[i++] = new WaveForm(polje, durationsInPixels);
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
     * Setter
     */
    public void setHorizontalOffset (int offset) 
    {
        this.offsetXAxis = offset;
    }


    /**
     * Getter
     */
    public int getHorizontalOffset ()
    {
        return offsetXAxis;
    }
    

    /**
     * Setter
     */
    public void setVerticalOffset (int offset) 
    {
        this.offsetYAxis = offset;
    }


    /**
     * Setter koristi se pri event-hendlanju
     */
    public void setDurationsInPixels (int[] durationsInPixels)
    {
        this.durationsInPixels = durationsInPixels;
    }


    /**
     * Setter
     */
    public void setWaveEndPointInPixels ()
    {
        this.waveEndPointInPixels = waveEndPointInPixels;
    }


    /**
     * Metoda koja postavlja novi set vrijednosti signala 
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
     * Crtanje valnih oblika 
     */
	public void paintComponent (Graphics g) 
	{
		super.paintComponent(g);
        waveEndPointInPixels = scale.getScaleEndPointInPixels();
		yAxis = FIRST_WAVE_YAXIS_START - offsetYAxis;

        /* crtanje isprekidanih crta */
        //g.setColor(new Color(255, 241, 255));
        g.setColor(new Color(163, 179, 225));
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
            g.setColor(new Color(254, 217, 182));
            g.fillRect(0, (index * WAVE_SPRING_SIZE) + 15 - offsetYAxis,
                    getPreferredSize().width, WAVE_SPRING_SIZE / 2 + 5);
        }
        g.setColor(new Color(51, 51, 51));

        /* crtanje valova */
        g.setColor(new Color(51, 51, 51));
        for (int i = 0; i < waveForm.length; i++)
        {
            waveForm[i].setSignalValues(signalValues[i]);
        }
		for (WaveForm wave : waveForm)
		{
			wave.drawWave(g, this.getWidth(), yAxis, offsetXAxis, durationsInPixels, waveEndPointInPixels);
			yAxis += WAVE_SPRING_SIZE;
		}
	}
}
