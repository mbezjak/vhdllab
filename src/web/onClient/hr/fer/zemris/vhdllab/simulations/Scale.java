package hr.fer.zemris.vhdllab.simulations;

import hr.fer.zemris.vhdllab.vhdl.simulations.VcdParser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JPanel;

/**
 * Skala uzima vrijednosti koje generira VcdParser i vrsi sve proracune kod
 * event-hendlanja.  Prilikom svakog povecanja proracunava trajanje valnih
 * oblika u pikselima.
 *
 * @author Boris Ozegovic
 */
class Scale extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4038995129761128970L;
	private final byte FEMTO_SECONDS = 1; 
    private final double PICO_SECONDS = 1e-3; 
    private final double NANO_SECONDS = 1e-6; 
    private final double MICRO_SECONDS = 1e-9; 
    private final double MILI_SECONDS = 1e-12;
    private final double SECONDS = 1e-15;

    /* razmaka izmedu dvije tocke na skali je uvijek 100 piksela */
    private final int SCALE_STEP_IN_PIXELS = 100;
    private final int SCALE_HEIGHT = 30;

    /* 
     * podrazumijevana velicina ekrana - skale ne iscrtava automatski sve
     * vrijednost vec vrsi proracuna na temelju offseta i iscrtava samo 2000
     * piksela odjednom, cim se dobiva na performansama
     */
    private final int SCREEN_SIZE_IN_PIXELS = 2000;

    /* svaki vrijednost na skali pocinje od 19. piksela */
    private final int SCALE_VALUE_YAXIS = 19;

    /* os pocinje od 4. piksela */
    private final int SCALE_MAIN_LINE_YAXIS = 4;
    private final int SCALE_TAG_LINE_YSTART = 2;
    private final int SCALE_TAG_LINE_YEND = 6;
    private final Color BACKGROUND_COLOR = new Color(255, 146, 26);
    private java.util.List <Long> transitionPoints;

    /* GHDL simulator generira sve u femtosekundama */
    private double[] durationsInFemtoSeconds;

    /* trajanje u pikselima koje ide direktno za crtanje valnih oblika */
    private int[] durationsInPixels; 

    /* povecava/smanjuje skalu za neku vrijednost */
    private double scaleFactor = 1f;
    private String measureUnitName;
    private double measureUnit;

    /* u pikselima - odreduje tocno odredeni piksel na kojem skala zavrsava */
    private int scaleEndPointInPixels;

    /* piksel na kojem pocinje iscrtavanje sadrzaja vidljivog na ekranu */
    private int scaleStartPointInPixels;

    /* korak skale u vremenu, ovisno o scaleFactor */
    private double scaleStepInTime = 100;

    /* vrijednost koja se kumulativno povecava, ovisno o scaleStepInTime */
    private double scaleValue;

    /* 
     * ovisno o trenutnom offsetu scrollbara. Ako je offset npr. 1350, onda se
     * interno preracuna u 1300 i od 1300 se crta skala
     */
    private int screenStartPointInPixels;

    /* screenStartPointInPixels + 2000 */
    private int screenEndPointInPixels;
    private final String[] units = {"fs", "ps", "ns", "us", "ms", "s", "ks", "ms"};
    private int offsetXAxis;


    /**
     * Constructor
     *
     * @param parser 
     */
    public Scale (VcdParser parser)
    {
        this.transitionPoints = parser.getTransitionPoints();
        this.scaleStartPointInPixels = parser.getMaximumSignalNameLength() * 6  + 1; 
        durationsInFemtoSeconds = new double[transitionPoints.size() - 1];
        durationsInPixels = new int[durationsInFemtoSeconds.length];
        for (int i = 0; i < durationsInFemtoSeconds.length; i++)
        {
            double operand1 = transitionPoints.get(i + 1).longValue();
			double operand2 = transitionPoints.get(i).longValue();
			durationsInFemtoSeconds[i] = operand1 - operand2;

            /* crta pocetne valne oblike sa scaleFaktorom 1 */
            drawDefaultWave();
        }
        setBackground(BACKGROUND_COLOR);
    }


    /**
     * Metoda koja crta pocetne oblike
     */
    public void drawDefaultWave () 
    {
        /* radi lokalnu kopiju jer sortiranje mijenja poredak */
        double[] temporaryDurations = durationsInFemtoSeconds.clone();
        Arrays.sort(temporaryDurations);

        /* 
         * nakon sortiranja minimalno se trajanje nalazi na prvom mjestu u polju
         * i na temelju tog minimalnog trajanja odreduje pocetnu jedinicu (ns,
         * ps, itd).  Castanje zbog toga da ne produ znamenke iza dec. zareza.
         * Pomocu string.length() izracunava broj znamenki i na temelju broj
         * znamenki odreduje mjernu jedinicu.  Odreduje se najmanja promejna, te
         * se na temelju postavlja skala u najmanju jedinicu i mnozi ostatak s
         * odgovarajucim 10^x faktorom.
         * */
        String minimumDuration = String.valueOf((int)temporaryDurations[0]);
        int numberOfDigits = minimumDuration.length();
        switch (numberOfDigits)
        {
            case 1 :
            case 2 :
            case 3 :
                measureUnitName = "fs";
                measureUnit = FEMTO_SECONDS;
                break;
            case 4 :
            case 5 :
            case 6 :
                measureUnitName = "ps";
                measureUnit = PICO_SECONDS;
                break;
            case 7 :
            case 8 :
            case 9 :
                measureUnitName = "ns";
                measureUnit = NANO_SECONDS;
                break;
            case 10 :
            case 11 : 
            case 12 :
                measureUnitName = "us";
                measureUnit = MICRO_SECONDS;
                break;
            case 13 :
            case 14 :
            case 15 :
                measureUnitName = "ms";
                measureUnit = MILI_SECONDS;
                break;
            case 16 :
            case 17 :
            case 18 :
                measureUnitName = "s";
                measureUnit = SECONDS;
                break;
        }

        scaleEndPointInPixels = 0;
        for (int i = 0; i < durationsInPixels.length; i++)
        {
            durationsInPixels[i] = (int)(durationsInFemtoSeconds[i] * measureUnit);
			scaleEndPointInPixels += durationsInPixels[i];
        }
    }


    /**
     * Metoda koja se brine o trajanju piksela nakon event-hendlanja i o tocnom
     * postavljanju scaleEndPointInPixels
     */
    public void setDurationsInPixelsAfterZoom (float scaleFactor)
    {
        /* 
         * mnozi se originalni durationsInFemtoSeconds koji je tipa double tako
         * da se sacuva polozaj i nakon sto se prilikom smanjena ode preko nule
         */
        for (int i = 0; i < durationsInFemtoSeconds.length; i++)
        {
            durationsInFemtoSeconds[i] *= scaleFactor;
        }
        scaleEndPointInPixels = 0;
        for (int i = 0; i < durationsInPixels.length; i++)
        {
            /* s istim 10^x faktorom mnozi se cijelo vrijeme */
            durationsInPixels[i] = (int)(durationsInFemtoSeconds[i] * measureUnit);
			scaleEndPointInPixels += durationsInPixels[i];
        }
        this.scaleFactor *= scaleFactor;
    }


    /**
     * Getter trajanje u pikselima za valne oblike
     */
    public int[] getDurationInPixels ()
    {
        return durationsInPixels;
    }
    
                
    /**
     * Setter postavlja scaleFactor
     */
    public void setScaleFactor (float scaleFactor)
    {
        this.scaleFactor = scaleFactor;
    }


    /**
     * Getter
     */
    public double getScaleFactor ()
    {
        return scaleFactor;
    }


    /**
     * Getter koji je potreban event-hendleru za precizno oznacavanje pozicije
     */
    public String getMeasureUnitName ()
    {
        return measureUnitName;
    }


    /**
     * Setter koji postavlja horizontalni offset u ovisnosti i scrollbaru
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
     * Getter koji daje krajnju tocku u pikselima, potreban za postavljanje nove
     * vrijednosti scrollbara prilikom povecanja/smanjenja
     */
    public int getScaleEndPointInPixels ()
    {
        return scaleEndPointInPixels;
    }


    /**
     * Getter koji daje trenutnu vrijednost skale u vremenu (npr. 345 ns na 100
     * piksela)
     */
    public double getScaleStepInTime ()
    {
        return scaleStepInTime;
    }


    /**
     * Getter
     */
    public Dimension getPreferredSize ()
    { 
        return new Dimension(scaleEndPointInPixels, SCALE_HEIGHT); 
    } 


    /**
     * Crtanje komponente
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        /* bilo koja vrijednost postavlja se na visekratnik broja 100 */
        screenStartPointInPixels = offsetXAxis - offsetXAxis % 100;
        screenEndPointInPixels = screenStartPointInPixels + SCREEN_SIZE_IN_PIXELS;

        /* npr. scaleFactor = 2 => scaleStepInTime = 50 */
        scaleStepInTime = SCALE_STEP_IN_PIXELS / scaleFactor;

        /* scaleStepInTime ostaje fiksan jer je potrebna pocetna vrijednost */
        double tempScaleStepInTime = scaleStepInTime;

        /* x se mijenja u koracima od 100 piksela */
		int x = 0;

        /* 
         * samo ako je screenStartPointInPixels == 0 npr offset = 30, crta od
         * nultog piksela, ali pomatnut ulijevo za offset, pa 30 pocinje od
         * nultog piksela 
         */
        if (screenStartPointInPixels == scaleStartPointInPixels)
        {
			g.drawLine(x - offsetXAxis, SCALE_TAG_LINE_YSTART, x - offsetXAxis, SCALE_TAG_LINE_YEND);
			g.drawString("0" + measureUnitName, x - offsetXAxis, SCALE_VALUE_YAXIS);
            x = SCALE_STEP_IN_PIXELS;
            scaleValue = scaleStepInTime;
        }

        /* 
         * inace moze biti npr. 2500.  scaleValue se odmah postavlja na pocetnu
         * vrijednost u skladu s brojem od kojeg pocinje crtanje
         */
        else
        {
            x = screenStartPointInPixels;
            scaleValue = screenStartPointInPixels / 100 * scaleStepInTime;
        }

        int endPoint;
        
        /* screenEndPointInPixels moze biti manji od zavrsetka skale... */
        if (scaleEndPointInPixels > screenEndPointInPixels)
        {
            endPoint = screenEndPointInPixels;
        }
        else 
        {
            endPoint = scaleEndPointInPixels;
        }
        g.drawLine(screenStartPointInPixels - offsetXAxis, SCALE_MAIN_LINE_YAXIS, 
				endPoint - offsetXAxis, SCALE_MAIN_LINE_YAXIS);
        String tempMeasureUnitName = measureUnitName;
        
        int endPointInPixels = endPoint;
        while (x < endPointInPixels)
        {
            /* svaka se vrijednost zaokruzuje na 4 decimale */
			scaleValue = Math.round(scaleValue * 10000) / 10000.0;

            /* 
             * ako vrijednost prijede 1000, prebacuje se na sljedecu vecu
             * jedinicu i ujedno smanjuje scaleStepInTime za 1000
             */
			if (scaleValue >= 1000)
			{
			    for (int i = 0; i < units.length; i++)
                {
                    if (units[i].equals(tempMeasureUnitName) && i < units.length - 1)
                    {
                        tempMeasureUnitName = units[i + 1];
                        scaleValue /= 1000.0;
                        tempScaleStepInTime /= 1000;
                        endPoint /= 1000;
                        break;
                    }
                }
            }

            /* inace ako prijede u manju jedinicu */
			else if (scaleValue < 1 && scaleValue != 0)
			{
				for (int i = 0; i < units.length; i++)
                {
                    if (units[i].equals(tempMeasureUnitName) && i > 0)
                    {
                        tempMeasureUnitName = units[i - 1];
				        scaleValue *= 1000;
				        tempScaleStepInTime *= 1000;
                        break;
                    }
				}
			}

            /* 
             * bez obzira na vrijednost x, sve se crta pomaknuto za offset, tako
             * da na ekranu bude slika od nultog piksela do 2000-tog
             */
            g.drawLine(x - offsetXAxis, SCALE_TAG_LINE_YSTART, x - offsetXAxis, SCALE_TAG_LINE_YEND);
            g.drawString(scaleValue + tempMeasureUnitName, 
					x -((scaleValue + tempMeasureUnitName).length() * 5) / 2 - offsetXAxis, SCALE_VALUE_YAXIS);
            scaleValue += tempScaleStepInTime;


            x += SCALE_STEP_IN_PIXELS;
        }

        /* na kraju dodaj endPoint  Ovo volje izvesti */
		scaleValue = Math.round(scaleValue * 100) / 100.0;
        g.drawLine(endPointInPixels - offsetXAxis, SCALE_TAG_LINE_YSTART, endPointInPixels - offsetXAxis, SCALE_TAG_LINE_YEND);
        String string = String.valueOf((endPoint / scaleFactor));
        g.drawString(string + tempMeasureUnitName, endPointInPixels - offsetXAxis -
                        (string.length() * 6 + 14), 19);
    }
}
