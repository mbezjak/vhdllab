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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Klasa sluzi za parsiranje stringa koji sadrzi rezultate simulacije prikazane
 * u jednom stringu, internog formata.  Parsirane rezultate direktno koristi
 * applet za iscrtavanje rezultata simulacije
 *
 * @author Boris Ozegovic
 */
public class GhdlResults
{
    /** Glavni limiter, razdvaja imena signala, vrijednosti i maxSignalName */
    private static final String HEAD_LIMITER = "%%%";
    
    /** Razdvaja svako ime signala posebno, vrijednosti signala itd. */
    private static final String LIMITER = "###";
    
    /* Razdvaja vrijednosti po signalima */
    private static final String VALUE_LIMITER = "&&&";
    
    /** Rezultat simulacije predstavljen kao string */
    private String resultInString;

    /** Sadrzi razdvojene signale */
    private String[] splitResults;
    
    /** 
     * Lista koji sadrzi imena signala nakon parsiranja, ovisno o ekspandiranim
     * vektorima moze biti veca od defaultne liste koja sadrzi imena vektora
     * prikazane kao vektor
     */
    private List<String> signalNames = new LinkedList<String>();

    /** Sadrzi default imena signala */
    private String[] defaultSignalNames;

    /** Svaki redak predstavlja sve vrijednosti pojedinog signala */
    private List<String[]> signalValues = new LinkedList<String[]>();
    
    /** Defaultne vrijednosti */
    private String[][] defaultSignalValues;

    /** Sadrzi tocke u kojima se dogada promjena vrijednosti signala */
    private String[] transitionPointsInStrings;
    
    /** Tocke u kojima se dogada promjena */
    private long[] transitionPoints;
    /**
     * Kod za crtanje modificira {@link #transitionPoints}. Zadaca ovog polja jest
     * sacuvati original.
     */
    private long[] unscaledTransitionPoints;
    
    /** Broj znakova najduljeg imena signala */
    private int maximumSignalNameLength;

	/** Makimalna duljina vektora, ukoliko postoje vektori */
    private int maximumVectorSize = 1;

    /** Sadrzi informaciju jesu li bit-vectori prosireni */
    private List<Boolean> expandedSignalNames = new ArrayList<Boolean>();

    /** 
     * Lista trenutnih indeksa vektora u listi sa signalima
     */
    private List<Integer> currentVectorIndex = new ArrayList<Integer>();

    /**
     * Pomocni objekt koji predstavlja cache za objekte tipa SignalChangeEvent.
     */
    private SignalChangeEventCache sceCache;

    /**
     * Objekt koji za svaki signal (ukljucivo i vektore i komponente vektora) cuva
     * trenutke kada su se dogodile promjene.
     */
    private SignalChangeTracker signalChangeTracker;
    
    /**
     * Metoda koja vrsi samo parsiranje stringa zapisanog u internom formatu
     */
    public void parseString (String resultString)
    {
        /* 
         * razdvaja u cetiri stringa, imena signala, vrijednosti, tocke 
         * promjene vrijednosti signala i konacno broj znakova najduljeg imena
         * signala
         */
        this.resultInString = resultString;
        splitResults = resultInString.split(HEAD_LIMITER);

        /* [0] su imena signala, [1] values, [2] tocke promjene, [3] max ime */
        defaultSignalNames = splitResults[0].split(LIMITER);
        
        /* prvo razdvaja sve signale (0&&&Z&&&1 itd za svaki) */
        String[] temp = splitResults[1].split(LIMITER);
        String[][] matrica = new String[temp.length][];

        /* a onda pojedinacno sve vrijednosti tih signala */
        for (int i = 0; i < temp.length; i++)
        {
            matrica[i] = temp[i].split(VALUE_LIMITER);
			expandedSignalNames.add(false);
			
			if (matrica[i][0].length() != 1)
            {
				currentVectorIndex.add(i);
				if (matrica[i][0].length() > maximumVectorSize)
				{
					maximumVectorSize = matrica[i][0].length();
				}
            }
			else
			{
				currentVectorIndex.add(-1);
			}
        }
        defaultSignalValues = matrica;

        /* dobivanje tocaka u kojima se dogada promjena vrijednosti signala */
        transitionPointsInStrings = splitResults[2].split(LIMITER);
        transitionPoints = new long[transitionPointsInStrings.length];
        unscaledTransitionPoints = new long[transitionPoints.length];

        for (int i = 0; i < transitionPointsInStrings.length; i++)
        {
            transitionPoints[i] = Long.valueOf(transitionPointsInStrings[i]).longValue();
            unscaledTransitionPoints[i] = transitionPoints[i];
        }
        
        /* broj znakova najduljeg imena signala */
        maximumSignalNameLength = Integer.valueOf(splitResults[3]).intValue();

        /* postavi vrijednosti liste na pocetno stanje */
        for (String string : defaultSignalNames)
        {
            signalNames.add(string);
        }
        for (String[] array : defaultSignalValues)
        {
            signalValues.add(array);
        }

        // Sada za svaki signal napravi popis samo njegovih promjena
        Map<String, SignalChangeEvent[]> signalChangeMap = rebuildSignalChangeMap();
        signalChangeTracker = new SignalChangeTracker(signalChangeMap);
    }

    
    private Map<String, SignalChangeEvent[]> rebuildSignalChangeMap() {
        // Sada za svaki signal napravi popis samo njegovih promjena
        Map<String, SignalChangeEvent[]> signalChangeMap = new HashMap<String, SignalChangeEvent[]>(signalNames.size()*2);
        if(sceCache==null) {
        	sceCache = new SignalChangeEventCache();
        }
        for(int signalIndex = 0; signalIndex < signalNames.size(); signalIndex++) {
	        List<SignalChangeEvent> scEvents = new ArrayList<SignalChangeEvent>();
        	String[] sValues = signalValues.get(signalIndex);
	        for(int transIndex = 0; transIndex < transitionPoints.length-1; transIndex++) {
        		String signalValue = sValues[transIndex];
        		        	// Ako je ovo prvi trenutak, dodaj ga i vozi dalje
        		if(transIndex==0) {
	        		scEvents.add(sceCache.get(signalValue, transitionPoints[transIndex]));
	        		continue;
	        	}
	        	// Inace usporedi vrijednost sa onom od prethodnog trenutka; ako su iste, ovo nije promjena!
        		if(signalValue.equals(sValues[transIndex-1])) continue;
        		scEvents.add(sceCache.get(signalValue, transitionPoints[transIndex]));
	        }
        	SignalChangeEvent[] array = new SignalChangeEvent[scEvents.size()];
        	scEvents.toArray(array);
        	signalChangeMap.put(signalNames.get(signalIndex), array);
        	signalChangeMap.put("!sig:"+signalIndex, array);
        	if(transitionPoints.length<1) continue;
        	// Ako je ovo vektor:
        	if(sValues[0].length()>1) {
        		int vectorLength = sValues[0].length();
        		for(int scalarIndex = 0; scalarIndex < vectorLength; scalarIndex++) {
        	        scEvents = new ArrayList<SignalChangeEvent>();
                	for(int transIndex = 0; transIndex < transitionPoints.length-1; transIndex++) {
                		char signalValue = sValues[transIndex].charAt(scalarIndex);
    		        	// Ako je ovo prvi trenutak, dodaj ga i vozi dalje
                		if(transIndex==0) {
        	        		scEvents.add(sceCache.get(Character.toString(signalValue), transitionPoints[transIndex]));
        	        		continue;
        	        	}
        	        	// Inace usporedi vrijednost sa onom od prethodnog trenutka; ako su iste, ovo nije promjena!
                		if(signalValue==sValues[transIndex-1].charAt(scalarIndex)) continue;
    	        		scEvents.add(sceCache.get(Character.toString(signalValue), transitionPoints[transIndex]));
        	        }
                	array = new SignalChangeEvent[scEvents.size()];
                	scEvents.toArray(array);
                	signalChangeMap.put(signalNames.get(signalIndex)+"\t"+scalarIndex, array);
                	signalChangeMap.put("!sig:"+signalIndex+"_"+scalarIndex, array);
        		}
        	}
        }
        return signalChangeMap;
    }
    
    /**
     * Dohvat objekta koji cuva promjene signala.
     * 
     * @return signalChangeTracker za trenutni rezultat simulacije
     */
    public SignalChangeTracker getSignalChangeTracker() {
		return signalChangeTracker;
	}
    
    /**
     * Metoda koja mijenja poredak signala prema gore.  Mijenja se poredak imena
     * i poredak vrijednosti..
     * 
     * MČ: promjene signala su u ovoj verziji vhdllab-a ONEMOGUCENE tako dugo dok se pamcenje
     *     stanja ne prebaci na jedno mjesto. Trenutno je postalo nemoguce sinkronizirati sve
     *     komponente koje pamte svaka za sebe je li nesto ekspandirano ili nije i gdje bi se
     *     to na ekranu trebalo nalaziti (2010-09-25).
     *
     * @param index Indeks signala koji se pomice prema gore
	 * @return Vraca indeks tako da se ponovno pozicionira na signal koji se pomicao
     */
    public int changeSignalOrderUp (int index)
    {
        /* ako je signal vec na vrhu */
        if (index == 0)
        {
            return index;
        }

    	return index;
//		/* prethodni index defaultnog polja s imenima signala */
//		Integer previousDefaultIndex;
//		int vectorSize;
//
//		/* ako gore ide obican signal ili neekspandirani vektor*/
//		if (currentVectorIndex.get(index) == -1 || 
//				(currentVectorIndex.get(index) != -1 && 
//				 !expandedSignalNames.get(currentVectorIndex.get(index))))
//		{
//			previousDefaultIndex = currentVectorIndex.get(index - 1);
//			/* ako je iznad obicnog signala bio obican signal ili neekspandirani vektor*/
//			if (previousDefaultIndex == -1 || 
//					(previousDefaultIndex != -1 && 
//					 !expandedSignalNames.get(previousDefaultIndex)))
//			{
//				/* promjena poretka imena signala */
//				signalNames.add(index - 1, signalNames.get(index));
//				signalNames.remove(index + 1);
//
//				/* promjena poretka vrijednosti signala */
//				signalValues.add(index - 1, signalValues.get(index));
//				signalValues.remove(index + 1);
//
//				/* promjena poretka u listi indeksa */
//				currentVectorIndex.add(index - 1, currentVectorIndex.get(index));
//				currentVectorIndex.remove(index + 1);
//				
//				index--;
//			}
//			/* inace ako je iznad obicnog signala bio ekspandirani vektor */
//			else if (previousDefaultIndex != -1 && 
//					expandedSignalNames.get(previousDefaultIndex) == true)
//			{
//				vectorSize = defaultSignalValues[previousDefaultIndex][0].length();
//				
//				/* promjena poretka imena signala */
//				signalNames.add(index - vectorSize, signalNames.get(index));
//				signalNames.remove(index + 1);
//				
//				/* promjena poretka vrijednosti */
//				signalValues.add(index - vectorSize, signalValues.get(index));
//				signalValues.remove(index + 1);
//
//				/* promjena u listi indeksa */
//				currentVectorIndex.add(index - vectorSize, 
//						currentVectorIndex.get(index));
//				currentVectorIndex.remove(index + 1);
//
//				index -= vectorSize;
//			}
//		}
//		/* Inace ako gore ide ekspandirani vektor */
//		else
//		{
//			vectorSize = defaultSignalValues[currentVectorIndex.get(index)][0].length();
//			int vectorStartIndex = currentVectorIndex.indexOf(currentVectorIndex.get(index));
//			if (vectorStartIndex == 0)
//			{
//				return index;
//			}
//			previousDefaultIndex = currentVectorIndex.get(vectorStartIndex - 1);
//
//			/* ako je iznad bio obican signal ili neekspandirani vektor */
//			if (previousDefaultIndex == -1 || 
//				(previousDefaultIndex != -1 && 
//				 !expandedSignalNames.get(previousDefaultIndex)))
//			{
//				/* promjena poretka imena signala */
//				signalNames.add(vectorStartIndex + vectorSize, 
//						signalNames.get(vectorStartIndex - 1));
//				signalNames.remove(vectorStartIndex - 1);
//
//				/* promjena poretka vrijednosti */
//				signalValues.add(vectorStartIndex + vectorSize, 
//						signalValues.get(vectorStartIndex - 1));
//				signalValues.remove(vectorStartIndex - 1);
//
//				/* promjena poretka indeksa u listi */
//				currentVectorIndex.add(vectorStartIndex + vectorSize, 
//						currentVectorIndex.get(vectorStartIndex - 1));
//				currentVectorIndex.remove(vectorStartIndex - 1);
//
//				index--;
//			}
//			/* ako je iznad bio ekspandirani vektor */
//			else
//			{
//				vectorStartIndex = currentVectorIndex.indexOf(currentVectorIndex.get(index));
//				int vectorSizeUp = defaultSignalValues[currentVectorIndex.get(index)][0].length();
//				int vectorSizeDown = 
//					defaultSignalValues[currentVectorIndex.get(vectorStartIndex - 1)][0].length();
//
//				/* promjena poretka imena signala */
//				for (int i = 0; i < vectorSizeDown; i++)
//				{
//					signalNames.add(vectorStartIndex + vectorSizeUp - i, 
//							signalNames.get(vectorStartIndex - 1 - i));
//					signalNames.remove(vectorStartIndex - i - 1);
//				}
//
//				/* promjena poretka vrijednosti */
//				for (int i = 0; i < vectorSizeDown; i++)
//				{
//					signalValues.add(vectorStartIndex + vectorSizeUp - i, 
//							signalValues.get(vectorStartIndex - 1 - i));
//					signalValues.remove(vectorStartIndex - i - 1);
//				}
//
//				/* promjena poretka indeksa */
//				for (int i = 0; i < vectorSizeDown; i++)
//				{
//					currentVectorIndex.add(vectorStartIndex + vectorSizeUp - i, 
//							currentVectorIndex.get(vectorStartIndex - 1 - i));
//					currentVectorIndex.remove(vectorStartIndex - i - 1);
//				}
//
//				index -= vectorSizeDown;
//			}
//		}
//		signalChangeTracker.update(rebuildSignalChangeMap());
//		/* Vraca indeks tako da se pozicionira na signal koji se pomicao u pocetku */
//		return index;
	}


    /**
     * Metoda koja mijenja poredak signala prema dolje. 
     * 
     * MČ: promjene signala su u ovoj verziji vhdllab-a ONEMOGUCENE tako dugo dok se pamcenje
     *     stanja ne prebaci na jedno mjesto. Trenutno je postalo nemoguce sinkronizirati sve
     *     komponente koje pamte svaka za sebe je li nesto ekspandirano ili nije i gdje bi se
     *     to na ekranu trebalo nalaziti (2010-09-25).
     * 
     * @param index indeks signala koji se pomice prema dolje
	 * @return Vraca indeks tako da se ponovno pozicionira na signal koji se pomicao
     */
    public int changeSignalOrderDown (int index)
    {
        /* ako je signal vec na dnu */
        if (index == (signalNames.size() - 1))
        {
            return index;
        }

        return index;
        
//		/* Sljedeci index defaultnog polja s imenima signala */
//		Integer nextDefaultIndex;
//		int vectorSize;
//
//		/* ako dolje ide obican signal ili neekspandirani vektor*/
//		if (currentVectorIndex.get(index) == -1 || 
//				(currentVectorIndex.get(index) != -1 && 
//				 !expandedSignalNames.get(currentVectorIndex.get(index))))
//		{
//			nextDefaultIndex = currentVectorIndex.get(index + 1);
//			/* ako je ispod obicnog signala bio obican signal ili neekspandirani vektor */
//			if (nextDefaultIndex == -1 || 
//					(nextDefaultIndex != -1 && 
//					 !expandedSignalNames.get(nextDefaultIndex)))
//			{
//				/* promjena poretka imena signala */
//				signalNames.add(index, signalNames.get(index + 1));
//				signalNames.remove(index + 2);
//
//				/* promjena poretka vrijednosti signala */
//				signalValues.add(index, signalValues.get(index + 1));
//				signalValues.remove(index + 2);
//
//				/* promjena poretka u listi indeksa */
//				currentVectorIndex.add(index, currentVectorIndex.get(index + 1));
//				currentVectorIndex.remove(index + 2);
//
//				index++;
//			}
//			/* inace ako je ispod obicnog signala bio ekspandirani vektor */
//			else if (nextDefaultIndex != -1 && 
//					expandedSignalNames.get(nextDefaultIndex) == true)
//			{
//				vectorSize = defaultSignalValues[nextDefaultIndex][0].length();
//				
//				/* promjena poretka imena signala */
//				signalNames.add(index + vectorSize + 1, signalNames.get(index));
//				signalNames.remove(index);
//				
//				/* promjena poretka vrijednosti */
//				signalValues.add(index + vectorSize + 1, signalValues.get(index));
//				signalValues.remove(index);
//
//				/* promjena u listi indeksa */
//				currentVectorIndex.add(index + vectorSize + 1, 
//						currentVectorIndex.get(index));
//				currentVectorIndex.remove(index);
//
//				index += vectorSize;
//			}
//		}
//		/* Inace ako dolje ide ekspandirani vektor */
//		else
//		{
//			vectorSize = 
//				defaultSignalValues[currentVectorIndex.get(index)][0].length();
//			int vectorStartIndex = 
//				currentVectorIndex.indexOf(currentVectorIndex.get(index));
//			if (vectorStartIndex + vectorSize - 1 == (signalNames.size() - 1))
//			{
//				return index;
//			}
//			nextDefaultIndex = currentVectorIndex.get(vectorStartIndex + vectorSize);
//
//			/* ako je ispod bio obican signal ili neekspandirani vektor */
//			if (nextDefaultIndex == -1 || 
//				(nextDefaultIndex != -1 && 
//				 !expandedSignalNames.get(nextDefaultIndex)))
//			{
//				/* promjena poretka imena signala */
//				signalNames.add(vectorStartIndex, 
//						signalNames.get(vectorStartIndex + vectorSize));
//				signalNames.remove(vectorStartIndex + vectorSize + 1);
//
//				/* promjena poretka vrijednosti */
//				signalValues.add(vectorStartIndex, 
//						signalValues.get(vectorStartIndex + vectorSize));
//				signalValues.remove(vectorStartIndex + vectorSize + 1);
//
//				/* promjena poretka indeksa u listi */
//				currentVectorIndex.add(vectorStartIndex, 
//						currentVectorIndex.get(vectorStartIndex + vectorSize));
//				currentVectorIndex.remove(vectorStartIndex + vectorSize + 1);
//
//				index++;
//			}
//			/* ako je ispod bio ekspandirani vektor */
//			else
//			{
//				vectorStartIndex = 
//					currentVectorIndex.indexOf(currentVectorIndex.get(index));
//				int vectorSizeDown = 
//					defaultSignalValues[currentVectorIndex.get(index)][0].length();
//				int vectorSizeUp = 
//					defaultSignalValues[currentVectorIndex.get(vectorStartIndex + 
//							vectorSizeDown)][0].length();
//
//				/* promjena poretka imena signala */
//				for (int i = 0; i < vectorSizeDown; i++)
//				{
//					signalNames.add(vectorStartIndex + vectorSizeUp + vectorSizeDown - i, 
//							signalNames.get(vectorStartIndex + vectorSizeDown - 1 - i));
//					signalNames.remove(vectorStartIndex + vectorSizeDown - i - 1);
//				}
//
//				/* promjena poretka vrijednosti */
//				for (int i = 0; i < vectorSizeDown; i++)
//				{
//					signalValues.add(vectorStartIndex + vectorSizeUp + vectorSizeDown - i, 
//							signalValues.get(vectorStartIndex + vectorSizeDown - 1 - i));
//					signalValues.remove(vectorStartIndex + vectorSizeDown - i - 1);
//				}
//
//				/* promjena poretka indeksa */
//				for (int i = 0; i < vectorSizeDown; i++)
//				{
//					currentVectorIndex.add(vectorStartIndex + vectorSizeUp + vectorSizeDown - i, 
//							currentVectorIndex.get(vectorStartIndex + vectorSizeDown - 1 - i));
//					currentVectorIndex.remove(vectorStartIndex + vectorSizeDown - i - 1);
//				}
//
//				index += vectorSizeUp;
//			}
//		}
//		signalChangeTracker.update(rebuildSignalChangeMap());
//		/* Vraca indeks tako da se ponovno pozicionira na signal koji se pomicao */
//		return index;
	}


    /**
     * Metoda koja vraca defaultni poredak
     */
    public void setDefaultOrder ()
    {
        signalNames.clear();
        signalValues.clear();
        for (String string : defaultSignalNames)
        {
            signalNames.add(string);
        }
        for (String[] array : defaultSignalValues)
        {
            signalValues.add(array);
        }
    }


    /**
     * Getter vrijednosti po signalima
     */
    public List<String[]> getSignalValues ()
    {
        return signalValues;
    }


    /**
     * Getter imena signala
     */
    public List<String> getSignalNames ()
    {
        return signalNames;
    }


	/**
     * Getter vrijednosti defaultnih signala
     */
    public String[][] getDefaultSignalValues ()
    {
        return defaultSignalValues;
    }


    /**
     * Getter default imena signala
     */
    public String[] getDefaultSignalNames ()
    {
        return defaultSignalNames;
    }


    /**
     * Vraca polje tocaka u kojima se dogada promjena signala
     */
    public long[] getTransitionPoints()
    {
        return transitionPoints;
    }

    /**
     * Vraca polje tocaka u kojima se dogada promjena signala; pri tome su vrijednosti
     * u mjernim jedinicama u kojima radi sam simulator.
     * @return polje vremena promjena
     */
    public long[] getUnscaledTransitionPoints() {
		return unscaledTransitionPoints;
	}

    /**
     * Vraca najveci trenutak u kojem se je dogodila bilo kakva promjena.
     * @return najveci trenutak bilo kakve promjene
     */
    public long getMaxUnscaledSimulationTime() {
    	if(unscaledTransitionPoints.length==0) return 0;
    	return unscaledTransitionPoints[unscaledTransitionPoints.length-1];
    }
    
    /**
     * Vraca broj znakova najduljeg imena signala
     */
    public int getMaximumSignalNameLength()
    {
        return maximumSignalNameLength;
    }


	/**
     * Vraca duljinu najveceg vektora
     */
    public int getMaximumVectorSize()
    {
        return maximumVectorSize;
    }


    /**
     * Vraca informaciju o ekspandiranim bit-vektorima u panelu s imenima
     * signala
     */
    public List<Boolean> getExpandedSignalNames()
    {
        return expandedSignalNames;
    }


    /**
     * Zatvara sve expandirane bit-vektore
     */
    public void setDefaultExpandedSignalNames()
    {
        expandedSignalNames.clear();
        currentVectorIndex.clear();
        for (int i = 0; i < defaultSignalValues.length; i++)
        {	
			expandedSignalNames.add(false);
			if (defaultSignalValues[i].length >= 2 && defaultSignalValues[i][1].length() != 1)
            {
				currentVectorIndex.add(i);
            }
			else
			{
				currentVectorIndex.add(-1);
			}
        }
    }


    /**
     * Trenutni ideksi vektora u listi sa signalima
     */
    public List<Integer> getCurrentVectorIndex()
    {
        return currentVectorIndex;
    }


    /**
     * Test metoda
     */
    public static void main (String[] args)
    {
//        VcdParser parser = new VcdParser("adder2.vcd");
//        parser.parse();
//        GhdlResults sParser = new GhdlResults();
//        sParser.parseString(parser.getResultInString());

		//System.out.println(sParser.getSignalNames());
		//System.out.println(sParser.getSignalValues());
        
//        System.out.println(sParser.getExpandedSignalNames());
//        System.out.println(sParser.getCurrentVectorIndex());
    }
}
