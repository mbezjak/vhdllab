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


import java.awt.Graphics;


/**
 * Crtanje valnog oblika
 *
 * @author Boris Ozegovic
 */
class WaveForm
{
    /** Svi oblici koji su jednaki nuli pripadaju grupi nula */
    private static final byte ZERO_SHAPES = 1;

    /** Svi oblici koji su jedinic pripadaju grupi jedinica */
	private static final byte ONE_SHAPES = 2;

    /** Svi oblici koji predstavljaju vektor ili su neinicijalizirani */
	private static final byte HEXAGON_SHAPES = 3;

    /** Valni oblik koji ima visoku impedanciju */
    private static final byte HIGH_IMPEDANCE = 4;

    /** Nepoznata vrijednost */
    private static final byte UNKNOWN = 5;

    /** Valni oblici U, H, L, W */
    private static final byte OTHERS = 6;

    /** Trajanje u pikselima */
    private int[] durationsInPixels;

    /** Pocetna tocka crtanja valnog oblika */
    private static final int WAVE_START_POINT_IN_PIXELS = 0;
    
    /** Duljina ekrana u pikselima */
    private int screenSizeInPixels;

    /** Vrijednosti valnih oblika */
	private String[] values;
	
    /** 
     * Tocka od koje pocinje iscrtavanje.  Valni oblici se ne iscrtavaju
     * kompletno vec u ovisnosti u trenutnom offsetu
     */
    private int screenStartPointInPixels;
    
    /** Iscrtavanje zavrsava ovisno u velicini ekrana, tj. o rezoluciji */
    private int screenEndPointInPixels;
    
    /** Svi valni oblici */
    private Shape[] shapes;    

    /**
     * Objekt koji cuva promjene svih signala
     */
    SignalChangeTracker signalChangeTracker;
    /**
     * Indeks signala. Ako je cisti skalar ili vektor, bit ce oblika "broj"; npr. "3"; 
     * ako je druga komponenta vektora koji je na poziciji tri, bit ce "3_2". 
     */
    String signalIndex;

    /**
     * Kljuc za dohvat podataka iz signalChangeTracker-a.
     */
    String signalKey;

    long[] transitionPoints;
    long maxUnscaledSimulationTime;
    
    /**
     * Constructor
     *
     * @param values polje vrijednosti za svaki signal
     * @param durationsInPixels vrijeme trajanja u pikselima
     * @param signalChangeTracker sve promjene po svim signalima
     * @param signalIndex identifikator ovog konkretnog signala
     */
	public WaveForm (String[] values, int[] durationsInPixels, Shape[] shapes, SignalChangeTracker signalChangeTracker, String signalIndex, long[] transitionPoints, long maxUnscaledSimulationTime)
	{
		this.values = values;
        this.durationsInPixels = durationsInPixels;
        this.shapes = shapes;
        this.signalKey = "!sig:"+signalIndex;
        this.transitionPoints = transitionPoints;
        this.signalChangeTracker = signalChangeTracker;
        this.maxUnscaledSimulationTime = maxUnscaledSimulationTime;
	}
	
	
    /**
     * Metoda koja odlucuje koji dio valnih oblika ce se iscrtati u ovisnosti u
     * screenEndPointInPixels
     * 
     * @param g Graphics
     * @param yAxis na koji dio y-osi se crta
     * @param offsetXAxis ovisno o offsetu crta dio ekrana
     * @param durationsInPixels trajanje dobivena iz skale
     * @param waveEndPointInPixels dobivenu iz skale
     */
	public void drawWave (Graphics g, int screenWidth, int yAxis, int offsetXAxis,
			int[] durationsInPixels, int waveEndPointInPixels) 
	{
        /* 
         * postoji sadasnja i sljedeca grupa, jer postoji vise vrsta oblika,
         * npr. jedinica koja slijedi iza nule drugacije je od jednice koja
         * slijedi iza vektora
         */
		byte previousGroup = 0;
		byte presentGroup = 0;
        this.durationsInPixels = durationsInPixels;
        screenStartPointInPixels = offsetXAxis;
        screenSizeInPixels = screenWidth + 200;
        screenEndPointInPixels = screenStartPointInPixels + screenSizeInPixels;
        int x1 = WAVE_START_POINT_IN_PIXELS;
		int y1 = yAxis;
		int x2;
  
        /* 
         * samo ako nije jednak nuli screenStartPointInPixels je umanjen za
         * jedan zato da ne iscrta na ekranu pocetak signala, vec treba ostaviti
         * dojam da signal traje od prije 
         */
        if (screenStartPointInPixels != WAVE_START_POINT_IN_PIXELS)
        {
            screenStartPointInPixels -= 1 ;
        }

        SignalChangeEvent[] changes = signalChangeTracker.get(signalKey);
        if(changes==null) {
        	System.out.println("Error: nemam podatke za iscrtati signal ["+signalKey+"].");
        	return;
        }
        
        int currTimeIndex = 0;
        for(int changeIndex = 0; changeIndex < changes.length; changeIndex++) {
        	SignalChangeEvent ev = changes[changeIndex];
        	long endTime = changeIndex==changes.length-1 ? maxUnscaledSimulationTime : changes[changeIndex+1].getTimestamp();
        	x2 = x1;
        	while(currTimeIndex<transitionPoints.length-1 && transitionPoints[currTimeIndex]<endTime) {
        		x2 += this.durationsInPixels[currTimeIndex];
        		currTimeIndex++;
        	}

        	String string = ev.getValue();
			if (string.equals("0"))
            {
				presentGroup = ZERO_SHAPES;
			}
			else if (string.equals("1"))
            {
				presentGroup = ONE_SHAPES;
			}
			else if (string.toUpperCase().equals("Z"))
            {
				presentGroup = HIGH_IMPEDANCE;
			}
            else if (string.toUpperCase().equals("X"))
            {
                presentGroup = UNKNOWN;
            }
            else if (string.toUpperCase().equals("U") || string.toUpperCase().equals("H") ||
                    string.toUpperCase().equals("L") || string.toUpperCase().equals("W"))
            {
                presentGroup = OTHERS;
            }
            else
            {
                presentGroup = HEXAGON_SHAPES;
            }
            
            /* 
             * ako je x2 manji od pocetka na kojem treba iscrtati jednostavno
             * ignorira te signale
             */
            if (x2 > screenStartPointInPixels)
            {
                /* 
                 * ako neki signal ide preko ekrana pocinje njegovo iscrtavanje,
                 * medutim, treba ograniciti jer signal moze trajati po tisucu
                 * piksela, a  moze biti i i manji od screenEndPointInPixels
                 */
                if (x2 < screenEndPointInPixels)
                {
                    draw(g, string, previousGroup, presentGroup, 
                            screenStartPointInPixels - offsetXAxis, y1, 
                            x2 - offsetXAxis);
                }
                else
                {
                    /* ako je screenEndPointInPixels jednak tocno kraju skale */
                    if (screenEndPointInPixels == waveEndPointInPixels)
                    {
                        draw(g, string, previousGroup, presentGroup, 
                                screenStartPointInPixels - offsetXAxis, 
                                y1, screenEndPointInPixels - offsetXAxis);
                    }

                    /* 
                     * inace crta s jednim prosirenim pikselom koji daje dojam
                     * da signal ne zavrsava, vec se nastavlja
                     */
                    else
                    {
                        draw(g, string, previousGroup, presentGroup, 
                                screenStartPointInPixels - offsetXAxis, 
                                y1, screenEndPointInPixels + 1 - offsetXAxis);
                        break;
                    }
                }
                screenStartPointInPixels = x2;
            }
			previousGroup = presentGroup;
			x1 = x2;
        }
        
		/*
		 * 
		int i = 0;

        /* za svaku pojedinu vrijednost signala *-/
		for (String string : values)
        {
			x2 = x1 + this.durationsInPixels[i]; 
            if (this.durationsInPixels[i] == 0)
            {
                i++;
                continue;
            }

			if (string.equals("0"))
            {
				presentGroup = ZERO_SHAPES;
			}
			else if (string.equals("1"))
            {
				presentGroup = ONE_SHAPES;
			}
			else if (string.toUpperCase().equals("Z"))
            {
				presentGroup = HIGH_IMPEDANCE;
			}
            else if (string.toUpperCase().equals("X"))
            {
                presentGroup = UNKNOWN;
            }
            else if (string.toUpperCase().equals("U") || string.toUpperCase().equals("H") ||
                    string.toUpperCase().equals("L") || string.toUpperCase().equals("W"))
            {
                presentGroup = OTHERS;
            }
            else
            {
                presentGroup = HEXAGON_SHAPES;
            }
            
            /* 
             * ako je x2 manji od pocetka na kojem treba iscrtati jednostavno
             * ignorira te signale
             *-/
            if (x2 > screenStartPointInPixels)
            {
                /* 
                 * ako neki signal ide preko ekrana pocinje njegovo iscrtavanje,
                 * medutim, treba ograniciti jer signalmoze trajati po tisucu
                 * piksela, a  moze biti i i manji od screenEndPointInPixels
                 *-/
                if (x2 < screenEndPointInPixels)
                {
                    draw(g, string, previousGroup, presentGroup, 
                            screenStartPointInPixels - offsetXAxis, y1, 
                            x2 - offsetXAxis);
                }
                else
                {
                    /* ako je screenEndPointInPixels jednak tocno kraju skale *-/
                    if (screenEndPointInPixels == waveEndPointInPixels)
                    {
                        draw(g, string, previousGroup, presentGroup, 
                                screenStartPointInPixels - offsetXAxis, 
                                y1, screenEndPointInPixels - offsetXAxis);
                    }

                    /* 
                     * inace crta s jednim prosirenim pikselom koji daje dojam
                     * da signal ne zavrsava, vec se nastavlja
                     *-/
                    else
                    {
                        draw(g, string, previousGroup, presentGroup, 
                                screenStartPointInPixels - offsetXAxis, 
                                y1, screenEndPointInPixels + 1 - offsetXAxis);
                        break;
                    }
                }
                screenStartPointInPixels = x2;
            }
			previousGroup = presentGroup;
			x1 = x2;
			i++;
		}
		 * 
		 */
		
		
	}


    /**
     * Metoda za samo iscrtavanje
     *
     * @param g Graphics
     * @param string vrijednost signala
     * @param previousGroup prijasnja grupa
     * @param presentGroup sadasnja grupa
     * @param x1 vrijednost od koje pocinje crtanje
     * @param y1 vrijednost po y-osi
     * @param x2 trajanje ove vrijednosti signala u pikselima
     */
    public void draw (Graphics g, String string, int previousGroup, 
                      int presentGroup, int x1, int y1, int x2)
    {
        switch (previousGroup)
        {
            case ZERO_SHAPES : 
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[0].draw(g, x1, y1, x2); 
                        break;
                    case ONE_SHAPES : 
                        shapes[4].draw(g, x1, y1, x2); 
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[8].draw(g, x1, y1, x2); 
                        shapes[8].putLabel(g, string, x1, y1, x2); 
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[10].draw(g, x1, y1, x2);
                        shapes[10].putLabel(g, string, x1, y1, x2);
                        break;
                    case OTHERS :
                        shapes[14].draw(g, x1, y1, x2);
                        shapes[14].putLabel(g, string, x1, y1, x2);
                        break;
                    case UNKNOWN : 
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case ONE_SHAPES : 
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[1].draw(g, x1, y1, x2); 
                        break;
                    case ONE_SHAPES : 
                        shapes[3].draw(g, x1, y1, x2); 
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[7].draw(g, x1, y1, x2); 
                        shapes[7].putLabel(g, string, x1, y1, x2); 
                        break;
                    case HIGH_IMPEDANCE : 
                        shapes[11].draw(g, x1, y1, x2);
                        shapes[11].putLabel(g, string, x1, y1, x2);
                        break;
                    case OTHERS :
                        shapes[15].draw(g, x1, y1, x2);
                        shapes[15].putLabel(g, string, x1, y1, x2);
                        break;
                    case UNKNOWN : 
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case HEXAGON_SHAPES :
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[2].draw(g, x1, y1, x2); 
                        break;
                    case ONE_SHAPES : 
                        shapes[5].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[6].draw(g, x1, y1, x2); 
                        shapes[6].putLabel(g, string, x1, y1, x2); 
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        shapes[9].putLabel(g, string, x1, y1, x2);
                        break;
                    case OTHERS :
                        shapes[13].draw(g, x1, y1, x2);
                        shapes[13].putLabel(g, string, x1, y1, x2);
                        break;
                    case UNKNOWN :
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case HIGH_IMPEDANCE :
                switch (presentGroup)
                {
                    case ZERO_SHAPES :
                        shapes[2].draw(g, x1, y1, x2);
                        break;
                    case ONE_SHAPES :
                        shapes[5].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES :
                        shapes[6].draw(g, x1, y1, x2);
                        shapes[6].putLabel(g, string, x1, y1, x2);
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        break;
                    case OTHERS :
                        shapes[13].draw(g, x1, y1, x2);
                        shapes[13].putLabel(g, string, x1, y1, x2);
                        break;
                    case UNKNOWN :
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case OTHERS :
                switch (presentGroup)
                {
                    case ZERO_SHAPES :
                        shapes[2].draw(g, x1, y1, x2);
                        break;
                    case ONE_SHAPES :
                        shapes[5].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES :
                        shapes[6].draw(g, x1, y1, x2);
                        shapes[6].putLabel(g, string, x1, y1, x2);
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        shapes[9].putLabel(g, string, x1, y1, x2);
                        break;
                    case OTHERS :
                        shapes[13].draw(g, x1, y1, x2);
                        break;
                    case UNKNOWN :
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case UNKNOWN :
                switch (presentGroup)
                {
                    case ZERO_SHAPES :
                        shapes[0].draw(g, x1, y1, x2);
                        break;
                    case ONE_SHAPES :
                        shapes[3].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES :
                        shapes[6].draw(g, x1, y1, x2);
                        shapes[6].putLabel(g, string, x1, y1, x2);
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        shapes[9].putLabel(g, string, x1, y1, x2);
                        break;
                    case OTHERS :
                        shapes[13].draw(g, x1, y1, x2);
                        shapes[13].putLabel(g, string, x1, y1, x2);
                        break;
                    case UNKNOWN : 
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            default : 
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[0].draw(g, x1, y1, x2);
                        break;
                    case ONE_SHAPES : 
                        shapes[4].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[6].draw(g, x1, y1, x2);
                        shapes[6].putLabel(g, string, x1, y1, x2);
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        shapes[9].putLabel(g, string, x1, y1, x2);
                        break;
                    case OTHERS :
                        shapes[13].draw(g, x1, y1, x2);
                        shapes[13].putLabel(g, string, x1, y1, x2);
                        break;
                    case UNKNOWN :
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
        }
    }


    /**
     * Metoda postavlja nove vrijednosti
     *
     * @param values Vrijednosti po signalima
     */
    public void setSignalValues (String[] values)
    {
        this.values = values;
    }
}
