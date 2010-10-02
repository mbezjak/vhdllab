/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;


/**
 * Panel koji sadrzi imena signala
 *
 * @author Boris Ozegovic
 */
class SignalNamesPanel extends JPanel
{
    /** Prvo ime signala pocinje od 30-tog piksela */
    private static final int YAXIS_START_POINT = 30;

    /** Svako se ime nalazi u springu (elasticnom pretincu) koji je visine 45 piksela */
	private static final int SIGNAL_NAME_SPRING_HEIGHT = 45;

    /** Maksimalna duljina koju panel moze poprimiti iznosi 650 piksela */
	private static final int PANEL_MAX_WIDTH = 650;

    /** Lista Stringova koje sadrzi sva imena signala */
    private List<String> signalNames;

	/** Sadrzi rezultate simulacije */
    private GhdlResults results;

    /** Polozaj trenutnog springa */
    private int yAxis;
    
    /** Trenutni offset po Y-osi */
    private int offsetYAxis;

    /** Trenutni offset po X-osi */
    private int offsetXAxis;

    /** Makimalna duljina imena signala */
    private int maximumSignalNameLength;
    
    /** Sirina panela s imenima signala */
    private int panelWidth;


	/** 
	 * Ako je kliknut border izmedu panela imena i valnih oblika tada je moguce rastegnuti 
	 * panel s imenima signala (ako je kliknut i ako se povuce, panel se rasteze)
	 */
	private boolean isBorderClicked = false;


    /** Sadrzi informaciju jesu li bit-vectori prosireni */
    private List<Boolean> expandedSignalNames = new ArrayList<Boolean>();

    /** 
     * Lista trenutnih indeksa vektora u listi sa signalima
     */
    private List<Integer> currentVectorIndex = new ArrayList<Integer>();

    /** Varijabla koja sadrzi informaciju je li trenutni signal oznacen misem */
    private boolean isClicked = false;

    /** Sadrzi indeks trenutno oznacenog signala misem */
    private int index = -1;

	/** ScrollBar */
	private JScrollBar scrollbar;

    /** Boje */
    private ThemeColor themeColor;

    /** SerialVersionUID */ 
    private static final long serialVersionUID = 1;


    /**
     * Constructor
     *
     * @param themeColor trenutna tema
     */
    public SignalNamesPanel (ThemeColor themeColor, JScrollBar scrollbar)
    {
        super();
		this.scrollbar = scrollbar;
        this.themeColor = themeColor;
    }


	/**
	 * Metoda postavlja novo stanje panela s imenima signala u ovisnosti o 
	 * rezultatu kojeg vraca GhdlResult
	 *
	 * @param results rezultati koje parsira GhdlResult
	 */
	public void setContent(GhdlResults results) {
		this.results = results;
        this.signalNames = results.getSignalNames();
        this.expandedSignalNames = results.getExpandedSignalNames();
        this.currentVectorIndex = results.getCurrentVectorIndex();
        this.maximumSignalNameLength = results.getMaximumSignalNameLength();
        this.panelWidth = this.maximumSignalNameLength * 6;
	}

    
      
    /**
     * Getter koji vraca preferirane dimenzije
     */
    @Override
    public Dimension getPreferredSize() 
    { 
        return new Dimension(panelWidth + 4, 
				signalNames.size() * SIGNAL_NAME_SPRING_HEIGHT); 
    } 


    /**
     * Getter koji vraca preferirane dimenzije ako je ime najvece duljine manje
     * od 650 piksela, inace vraca 650 piksela
     */
    @Override
    public Dimension getMaximumSize()
    {
        if (panelWidth < PANEL_MAX_WIDTH)
        {
            return new Dimension(panelWidth, 
					signalNames.size() * SIGNAL_NAME_SPRING_HEIGHT); 
        }

        return new Dimension(PANEL_MAX_WIDTH, signalNames.size() * 
				SIGNAL_NAME_SPRING_HEIGHT);
    }


    /**
     * Postavlja novu vrijednosti maksimalne velicine imena signala
     *
     * @param panelWidth nova vrijednost
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
     * Metoda postavlja novi set imena signala
     *
     * @param signalNames Nove set imena signala
     */
    public void setSignalNames (List<String> signalNames)
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
	 * Ako je border izmedu imena signala i valnih oblika kliknut
	 *
	 * @param isBorderClicked Je ili nije
	 */
	public void setIsBorderClicked (boolean isBorderClicked)
	{
		this.isBorderClicked = isBorderClicked;
	}


	/**
	 * Vrati trenutnu informaciju je li border kliknut ili nije
	 */
	public boolean getIsBorderClicked ()
	{
		return isBorderClicked;
	}


	/**
	 * Vraca visinu panela u pikselima
	 */
	public int getPanelHeightInPixels() {
		return signalNames.size() * SIGNAL_NAME_SPRING_HEIGHT;
	}


    /**
     * Vraca velicinu panela trenutno vidljivog na ekranu
     */
    public int getPanelHeight ()
    {
        return  getHeight();
    }

	/**
	 * Helper function for vector limits extraction.
	 * @param name full name of vector, eg. "+ /tb_d[0:15]"
	 * @return an 2-element array containing limits, eg. [0,15] 
	 */
	private static int[] extractVectorLimits(String name) {
		int start = name.indexOf('[');
		int end = name.indexOf(']');
		int colon = name.indexOf(':');
		return new int[] {
			Integer.parseInt(name.substring(start+1, colon)),
			Integer.parseInt(name.substring(colon+1, end))
		};
	}

	/**
	 * Ekspandira bit-vektor.  Umjesto bit-vektora zapisuju se njegovi clanovi u listu signala.
	 * Npr. Umjesto A[0:2] ulaze A[0], A[1] i A[2]
	 *
	 * @param index Index bit-vektora koji se ekspandira
	 */
	public void expand (int index)
	{
		/* defaultIndex je index bit-vektora u default polju imena signala s kojim se barata */
		Integer defaultIndex = currentVectorIndex.get(index);
		String tempSignalName;
		int[] limits = extractVectorLimits(signalNames.get(index));
        int startVector = limits[0];
        int endVector = limits[1];
        
/*      GRANICE MOGU BITI I VISEZNAMENKASTI BROJEVI!!!
 *      int startVector = Integer.valueOf(signalNames.get(index)
				.charAt(signalNames.get(index).length() - 4)) - 48;
        int endVector = Integer.valueOf(signalNames.get(index)
				.charAt(signalNames.get(index).length() - 2)) - 48;
*/
		/* duljine vektora, s tim da je duljina umanjena za 1 od stvarne duljine */
        int vectorSize = Math.abs(startVector - endVector);
		/* Izvadi ime vektora bez oznake velicine vektora ([...]) */
		tempSignalName = signalNames.get(index).substring(1, 
				signalNames.get(index).indexOf('['));
//		tempSignalName = signalNames.get(index).substring(1, 
//				signalNames.get(index).length() - 5);
		signalNames.remove(index);
		signalNames.add(index, "-" + tempSignalName + "[" + startVector + "]");
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
			signalNames.add(index + 1 + j, "  " + tempSignalName + "[" + startVector + "]");
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

		/* 
		 * refresha currentVectorIndex listu 
		 */
		for (int i = 0; i < vectorSize; i++)
		{
			currentVectorIndex.add(index, defaultIndex);
		}
	}


	/**
	 * Kolapsira bit-vektor nakon sto se klikne minus ispred imena signala
	 *
	 * @param index Index bit-vektora koji se kolapsira
	 */
	public void collapse (int index)
	{
		Integer defaultIndex = currentVectorIndex.get(index);
		int vectorSize = results.getDefaultSignalValues()[defaultIndex][0].length();
		for (int i = 0; i < vectorSize; i++)
		{
			signalNames.remove(index);
		}
		signalNames.add(index, results.getDefaultSignalNames()[defaultIndex]);

		/* Refresha currentVectorIndex listu nakon kolapsiranja odredenog bit-vektora */
		for (int i = 0; i < vectorSize - 1; i++)
		{
			currentVectorIndex.remove(index);
		}
	}


    /**
     * Crta komponentu
     *
     * @param g Graphics objekt
     */
    @Override
    public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		/* postavi vrijednost scrollbara */
		scrollbar.setMaximum(this.getMaximumSize().width);

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
		for (int i = 0; i < signalNames.size(); i++)
		{
            if (i < currentVectorIndex.size() && currentVectorIndex.get(i) != -1)
            {
				Integer defaultIndex = currentVectorIndex.get(i);
				if (expandedSignalNames.get(defaultIndex))
				{
					int vectorSize = results.getDefaultSignalValues()
						[defaultIndex][0].length();
					g.drawString(signalNames.get(i), 5 - offsetXAxis, yAxis);
					g.drawLine(7 - offsetXAxis, yAxis, 7 - offsetXAxis, 
							yAxis + 10);
					yAxis += SIGNAL_NAME_SPRING_HEIGHT;
      
					for (int j = 0; j < vectorSize - 1; j++)
					{   
						g.drawString(signalNames.get(++i), 5 - offsetXAxis, yAxis);
						g.drawLine(7 - offsetXAxis, yAxis - 2, 7 - offsetXAxis, 
								yAxis - SIGNAL_NAME_SPRING_HEIGHT);
						g.drawLine(7 - offsetXAxis, yAxis - 2, 9 - offsetXAxis, yAxis - 2);
						yAxis += SIGNAL_NAME_SPRING_HEIGHT;
					}
				}
				else
				{
					g.drawString(signalNames.get(i), 5 - offsetXAxis, yAxis);
					yAxis += SIGNAL_NAME_SPRING_HEIGHT;
				}
			}

            else
            {
			    g.drawString(signalNames.get(i), 5 - offsetXAxis, yAxis);
			    yAxis += SIGNAL_NAME_SPRING_HEIGHT;
            }
		}
	}
}
