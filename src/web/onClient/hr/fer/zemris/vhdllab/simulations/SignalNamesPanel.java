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
	private final int SIGNAL_NAME_SPRING_HEIGHT = 40;

    /* maksimalna duljina koju panel moze poprimiti iznosi 150 piksela */
	private final int PANEL_MAX_WIDTH = 150;

    /* mapa koja je dobivena iz VcdParsera i ciji kljucevi jesu imena signala */
    private Map <String, java.util.List <String>> signalValues;

    /* polozaj trenutnog springa */
    private int yAxis;
    private int offsetYAxis;
    private int offsetXAxis;
    private int maximumSignalNameLength;


    /**
     * Constructor
     *
     * @param signalValues kljucevi mape jesu imena signala, dobiveno iz parsera 
     * @param maximumSignalNameLength ime najvece duljine
     */
    public SignalNamesPanel (Map <String, java.util.List <String>> signalValues, 
			int maximumSignalNameLength)
    {
        super();
        setBackground(BACKGROUND_COLOR);
        this.signalValues = signalValues;
        this.maximumSignalNameLength = maximumSignalNameLength;
    }
    
      
    /**
     * Getter koji vraca preferirane dimenzije
     */
    public Dimension getPreferredSize() 
    { 
        return new Dimension(maximumSignalNameLength * 6 + 4, 
				signalValues.size() * SIGNAL_NAME_SPRING_HEIGHT); 
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
					signalValues.size() * SIGNAL_NAME_SPRING_HEIGHT); 
        }
        else
        {
            return new Dimension(PANEL_MAX_WIDTH, signalValues.size() * SIGNAL_NAME_SPRING_HEIGHT);
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
     * Setter koji postavlja horizontalni offset
     */
    public void setHorizontalOffset (int offset)
    {
        this.offsetXAxis = offset;
    }

    
    /**
     * Crta komponentu
     */
    public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		yAxis = YAXIS_START_POINT - offsetYAxis;
		for (String string : signalValues.keySet())
		{
			g.drawString(string, 5 - offsetXAxis, yAxis);
			yAxis += SIGNAL_NAME_SPRING_HEIGHT;
		}
	}
}
