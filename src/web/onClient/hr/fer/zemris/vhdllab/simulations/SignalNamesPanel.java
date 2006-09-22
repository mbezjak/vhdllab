package hr.fer.zemris.vhdllab.simulations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JPanel;



/**
 * Panel koji sadrzi imena signala
 *
 * @author Boris Ozegovic
 */
class SignalNamesPanel extends JPanel
{
    private final Color BACKGROUND_COLOR = new Color(141, 176, 221);
   
    /* prvo ime signala pocinje od 30-tog piksela */
    private final int YAXIS_START_POINT = 30;

    /* svako se ime nalazi u springu (elasticnom pretincu) koji je visine 40 piksela */
	private final int SIGNAL_NAME_SPRING_HEIGHT = 45;

    /* maksimalna duljina koju panel moze poprimiti iznosi 150 piksela */
	private final int PANEL_MAX_WIDTH = 450;

    /* polje Stringova koje sadrzi sva imena signala */
    private String[] signalNames;

    /* 
     * polje koje sadrzi pocetne tocke springova po Y-osi.  U ovisnoti o
     * ekspandiranosti bit-vektora pocetne se tocke mogu mijenjati
     */
    private int[] springStartPoints;

    /* polozaj trenutnog springa */
    private int yAxis;
    private int offsetYAxis;
    private int offsetXAxis;
    private int maximumSignalNameLength;
    private int panelWidth;

    /* informacija o pokazivanju strelice za pomicanje sirine panela */
    private boolean isArrowVisible;
    private int yArrow;

    /* sadrzi informaciju o expanded bit-vektorima */
    private Map<Integer, Boolean> expandedSignalNames;

    /* varijabla koja sadrzi informaciju je li trenutni signal oznacen misem */
    private boolean isClicked = false;

    /* sadrzi indeks trenutno oznacenog signala misem */
    private int index = -1;


    /**
     * Constructor
     *
     * @param results rezultati dobiveni HTTP-om parsirani od GhdlResults klase 
     */
    public SignalNamesPanel (GhdlResults results)
    {
        super();
        setBackground(BACKGROUND_COLOR);
        this.signalNames = results.getSignalNames();
        this.expandedSignalNames = results.getExpandedSignalNames();
        this.maximumSignalNameLength = results.getMaximumSignalNameLength();
        panelWidth = maximumSignalNameLength * 6;
        springStartPoints = new int[signalNames.length];
        
        /* 
         * postavlja defaultne pocetne tocke, koj ce kasniju mogu promijeniti u
         * ovisnosti je li neki bit-vektor ekspandiran
         */
        for (int i = 0; i < springStartPoints.length; i++)
        {
            springStartPoints[i] = i * SIGNAL_NAME_SPRING_HEIGHT;
        }
    }
    
      
    /**
     * Getter koji vraca preferirane dimenzije
     */
    public Dimension getPreferredSize() 
    { 
        return new Dimension(panelWidth + 4, 
				signalNames.length * SIGNAL_NAME_SPRING_HEIGHT); 
    } 


    /**
     * Getter koji vraca preferirane dimenzije ako je ime najvece duljine manje
     * od 150 piksela, inace vraca 150 piksela
     */
    public Dimension getMaximumSize()
    {
        if (panelWidth < PANEL_MAX_WIDTH)
        {
            return new Dimension(panelWidth, 
					signalNames.length * SIGNAL_NAME_SPRING_HEIGHT); 
        }
        else
        {
            return new Dimension(PANEL_MAX_WIDTH, signalNames.length * SIGNAL_NAME_SPRING_HEIGHT);
        }
    }


    /**
     * Postavlja novu vrijednosti maksimalne velicine imena signala
     *
     * @param maximumSignalNameLength nova vrijednost
     */
    public void setPanelWidth (int panelWidth)
    {
        this.panelWidth = panelWidth;
    }


    /**
     * Vraca width panela
     */
    public int getPanelWidth ()
    {
        return panelWidth;
    }
 

    /**
     * Setter koji postavlja vertikalni offset
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
     */
    public void setHorizontalOffset (int offset)
    {
        this.offsetXAxis = offset;
    }


    /**
     * Metoda postavlja novi set imena signala
     */
    public void setSignalNames (String[] signalNames)
    {
        this.signalNames = signalNames;
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
     * Vraca visinu springa u kojoj je smjesteno ime signala 
     */
    public int getSignalNameSpringHeight ()
    {
        return SIGNAL_NAME_SPRING_HEIGHT;
    }


    /**
     * Modificira pocetne tocke springova nakon ekspandiranja nekog bit-vectora
     *
     * @param signalValues vrijednosti po signalima
     */
    public void setSpringStartPoints (String[][] signalValues)
    {
        for (int i = 0; i < springStartPoints.length; i++)
        {
            if (expandedSignalNames.containsKey(i) && expandedSignalNames.get(i) && i < springStartPoints.length - 1)
            {
                springStartPoints[i + 1] = springStartPoints[i] + 
                    signalValues[i][0].length() * SIGNAL_NAME_SPRING_HEIGHT;
            }
            else if (i < springStartPoints.length - 1)
            {
                springStartPoints[i + 1] = springStartPoints[i] + SIGNAL_NAME_SPRING_HEIGHT;
            }
        }
    }


    /**
     * Tocke u kojima pocinju springovi imena signala (u pikselima)
     */
    public int[] getSpringStartPoints ()
    {
        return springStartPoints;
    }


    /**
     * Defaultne vrijednosti za defaultni poredak 
     */
    public void setDefaultSpringStartPoints()
    {
        for (int i = 0; i < springStartPoints.length; i++)
        {
            springStartPoints[i] = i * SIGNAL_NAME_SPRING_HEIGHT;
        }
    }


    /**
     * Ako je kurosr misa iznad granice s imenima signala tada prikazuj strelicu
     * za pomicanje
     *
     * @param isArrowVisible treba li je pokazati ili ne
     * @param y tocka na y osi
     */
    public void setIsArrowVisible (boolean isArrowVisible, int y)
    {
        this.yArrow = y;
        this.isArrowVisible = isArrowVisible;
    }


    /**
     * Vraca velicinu panela trenutno vidljivog na ekranu
     */
    public int getPanelHeight ()
    {
        return  getHeight();
    }



    /**
     * Crta komponentu
     */
    public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

        /* ako treba oznaciti neki od signala */
        if (isClicked)
        {
            g.setColor(new Color(254, 217, 182));
            g.fillRect(0, (springStartPoints[index]) + 15 - offsetYAxis, 
                    getMaximumSize().width, SIGNAL_NAME_SPRING_HEIGHT / 2 + 5);
        }
        g.setColor(new Color(51, 51, 51));
		yAxis = YAXIS_START_POINT - offsetYAxis;
		for (int i = 0; i < signalNames.length; i++)
		{
            if (expandedSignalNames.containsKey(i) && expandedSignalNames.get(i))
            {
                String tempSignalName;
                int startVector = Integer.valueOf(signalNames[i].charAt(signalNames[i].length() - 4)) - 48;
                int endVector = Integer.valueOf(signalNames[i].charAt(signalNames[i].length() - 2)) - 48;
                int vectorSize = Math.abs(startVector - endVector);
                tempSignalName = signalNames[i].substring(1, signalNames[i].length() - 5);
                g.drawString("-" + tempSignalName + "[" + startVector + "]", 5 - offsetXAxis, yAxis);
                g.drawLine(7, yAxis, 7, yAxis + 10);
                yAxis += SIGNAL_NAME_SPRING_HEIGHT;
                if (startVector < endVector)
                {
                    startVector++;
                }
                else
                {
                    startVector--;
                }
                
                for (int j = 0; j < vectorSize; j++)
                {   
                    g.drawString("  " + tempSignalName + "[" + startVector + "]", 5 - offsetXAxis, yAxis);
                    g.drawLine(7, yAxis - 2, 7, yAxis - SIGNAL_NAME_SPRING_HEIGHT);
                    g.drawLine(7, yAxis - 2, 9, yAxis - 2);
                    yAxis += SIGNAL_NAME_SPRING_HEIGHT;
                    if (startVector < endVector)
                    {
                        startVector++;
                    }
                    else
                    {
                        startVector--;
                    }
                    //(startVector < endVector) ? (startVector++) : (startVector--);
                }
            }

            else
            {
			    g.drawString(signalNames[i], 5 - offsetXAxis, yAxis);
			    yAxis += SIGNAL_NAME_SPRING_HEIGHT;
            }
		}

        /* crta granicu izmedu panela s imenima signala i valnim oblicima */
        g.setColor(new Color(86, 104, 176));
        /* 
         * getHeight() visina prikazana na ekranu, a ne preferred visina cijelog
         * panela 
         */
        g.drawLine(panelWidth - 3, 0, panelWidth - 3, getHeight()); 
        g.drawLine(panelWidth - 2, 0, panelWidth - 2, getHeight()); 
        g.drawLine(panelWidth - 1, 0, panelWidth - 1, getHeight()); 
        g.drawLine(panelWidth, 0, panelWidth, getHeight()); 

        /* crta strelicu koja je indikator za pomicanje sirine panela */
        if (isArrowVisible)
        {
            g.setColor(new Color(51, 51, 51));
            g.drawLine(panelWidth - 8, yArrow, panelWidth - 6, yArrow - 2);
            g.drawLine(panelWidth - 8, yArrow, panelWidth - 6, yArrow + 2);
            g.drawLine(panelWidth - 2, yArrow - 2, panelWidth, yArrow);
            g.drawLine(panelWidth - 2, yArrow + 2, panelWidth, yArrow);
            g.drawLine(panelWidth - 8, yArrow, panelWidth, yArrow);
        }
	}
}
