package hr.fer.zemris.vhdllab.simulations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;



/**
 * Panel koji sadrzi imena signala
 *
 * @author Boris Ozegovic
 */
class SignalNamesPanel extends JPanel
{
    /**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2059196312811436549L;

	private final Color BACKGROUND_COLOR = new Color(141, 176, 221);
   
    /* prvo ime signala pocinje od 30-tog piksela */
    private final int YAXIS_START_POINT = 30;

    /* svako se ime nalazi u springu (elasticnom pretincu) koji je visine 40 piksela */
	private final int SIGNAL_NAME_SPRING_HEIGHT = 45;

    /* maksimalna duljina koju panel moze poprimiti iznosi 150 piksela */
	private final int PANEL_MAX_WIDTH = 150;

    /* polje Stringova koje sadrzi sva imena signala */
    private String[] signalNames;

    /* polozaj trenutnog springa */
    private int yAxis;
    private int offsetYAxis;
    private int offsetXAxis;
    private int maximumSignalNameLength;

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
        this.maximumSignalNameLength = results.getMaximumSignalNameLength();
    }
    
      
    /**
     * Getter koji vraca preferirane dimenzije
     */
    public Dimension getPreferredSize() 
    { 
        return new Dimension(maximumSignalNameLength * 6 + 4, 
				signalNames.length * SIGNAL_NAME_SPRING_HEIGHT); 
    } 


    /**
     * Getter koji vraca preferirane dimenzije ako je ime najvece duljine manje
     * od 150 piksela, inace vraca 150 piksela
     */
    public Dimension getMaximumSize()
    {
        if (maximumSignalNameLength * 6 < PANEL_MAX_WIDTH)
        {
            return new Dimension(maximumSignalNameLength * 6, 
					signalNames.length * SIGNAL_NAME_SPRING_HEIGHT); 
        }
        else
        {
            return new Dimension(PANEL_MAX_WIDTH, signalNames.length * SIGNAL_NAME_SPRING_HEIGHT);
        }
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
     * Crta komponentu
     */
    public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

        /* ako treba oznaciti neki od signala */
        if (isClicked)
        {
            g.setColor(new Color(254, 217, 182));
            g.fillRect(0, (index * SIGNAL_NAME_SPRING_HEIGHT) + 15 -offsetYAxis, 
                    getMaximumSize().width, SIGNAL_NAME_SPRING_HEIGHT / 2 + 5);
        }
        g.setColor(new Color(51, 51, 51));
		yAxis = YAXIS_START_POINT - offsetYAxis;
		for (String string : signalNames)
		{
			g.drawString(string, 5 - offsetXAxis, yAxis);
			yAxis += SIGNAL_NAME_SPRING_HEIGHT;
		}
	}
}

