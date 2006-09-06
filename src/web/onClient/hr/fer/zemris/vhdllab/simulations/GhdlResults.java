/**
 * Klasa sluzi za parsiranje stringa koji sadrzi rezultate simulacije prikazane
 * u jednom stringu, internog formata.  Parsirane rezultate direktno koristi
 * applet za iscrtavanje rezultata simulacije
 *
 * @author Boris Ozegovic
 */
public class GhdlResults
{
    private final String HEAD_LIMITER = "%%%";
    private final String LIMITER = "###";
    private final String VALUE_LIMITER = "&&&";
    private String resultInString;
    private String[] splitResults;
    
    /* polje koji sadrzi imena signala nakon parsiranja */
    private String[] signalNames;
    private String[] defaultSignalNames;

    /* svaki redak predstavlja sve vrijednosti pojedinog signala */
    private String[][] signalValues;
    private String[][] defaultSignalValues;

    /* sadrzi tocke u kojima se dogada promjena vrijednosti signala */
    private String[] transitionPointsInStrings;
    private long[] transitionPoints;

    /* broj znakova najduljeg imena signala */
    private int maximumSignalNameLength;

    /* privremeni string kod mijenjanja poretka signala */
    String tempString;

    /* privremeno polje stringova kod mijenjanja poretka signala */
    String[] tempArrayString;


    /**
     * Constructor
     *
     * @param resultInString uzima string dobiven preko HTTP-a
     */
    public GhdlResults (String resultInString)
    {
        this.resultInString = resultInString;
    }


    /**
     * Metoda koja vrsi samo parsiranje stringa zapisanog u internom formatu
     */
    public void parseString ()
    {
        /* 
         * razdvaja u cetiri stringa, imena signala, vrijednosti, tocke 
         * promjene vrijednosti signala i konacno broj znakova najduljeg imena
         * signala
         */
        splitResults = resultInString.split(HEAD_LIMITER);

        /* [0] su imena signala, [1] values, [2] tocke promjene, [3] max ime */
        signalNames = splitResults[0].split(LIMITER);
        
        /* prvo razdvaja sve signale (0&&&Z&&&1 itd za svaki) */
        String[] temp = splitResults[1].split(LIMITER);
        String[][] matrica = new String[temp.length][];
        
        /* a onda pojedinacno sve vrijednosti tih signala */
        for (int i = 0; i < temp.length; i++)
        {
            matrica[i] = temp[i].split(VALUE_LIMITER);
        }
        signalValues = matrica;

        /* dobivanje tocaka u kojima se dogada promjena vrijednosti signala */
        transitionPointsInStrings = splitResults[2].split(LIMITER);
        transitionPoints = new long[transitionPointsInStrings.length];
        for (int i = 0; i < transitionPointsInStrings.length; i++)
        {
            transitionPoints[i] = Long.valueOf(transitionPointsInStrings[i]).longValue();
        }

        /* broj znakova najduljeg imena signala */
        maximumSignalNameLength = Integer.valueOf(splitResults[3]).intValue();

        /* postavi defaultni poredak koji ostaje konstantan */
        defaultSignalNames = signalNames.clone();
        defaultSignalValues = signalValues.clone();
    }


    /**
     * Metoda koja mijenja poredak signala prema gore.  Mijenja se poredak imena
     * i poredak vrijednosti..
     *
     * @param index indeks signala koji se pomice prema gore
     */
    public void changeSignalOrderUp (int index)
    {
        /* ako je signal vec na vrhu */
        if (index == 0)
        {
            return;
        }

        /* promjena poretka imena signala */
        tempString = signalNames[index];
        signalNames[index] = signalNames[index - 1];
        signalNames[index - 1] = tempString;

        /* promjena poretka vrijednosti signala */
        tempArrayString = signalValues[index];
        signalValues[index] = signalValues[index - 1];
        signalValues[index - 1] = tempArrayString;
    }


    /**
     * Metoda koja mijenja poredak signala prema dolje. 
     *
     * @param index indeks signala koji se pomice prema dolje
     */
    public void changeSignalOrderDown (int index)
    {
        /* ako je signal vec na dnu */
        if (index == (signalNames.length - 1))
        {
            return;
        }

        /* promjena poretka imena signala */
        tempString = signalNames[index];
        signalNames[index] = signalNames[index + 1];
        signalNames[index + 1] = tempString;

        /* promjena poretka vrijednosti signala */
        tempArrayString = signalValues[index];
        signalValues[index] = signalValues[index + 1];
        signalValues[index + 1] = tempArrayString;
    }


    /**
     * Metoda koja vraca defaultni poredak
     */
    public void setDefaultOrder ()
    {
        signalNames = defaultSignalNames.clone();
        signalValues = defaultSignalValues.clone();
    }


    /**
     * Getter vrijednosti po signalima
     */
    public String[][] getSignalValues ()
    {
        return signalValues;
    }


    /**
     * Getter imena signala
     */
    public String[] getSignalNames ()
    {
        return signalNames;
    }


    /**
     * Vraca polje tocaka u kojima se dogada promjena signala
     */
    public long[] getTransitionPoints()
    {
        return transitionPoints;
    }


    /**
     * Vraca boj znakova najduljeg imena signala
     */
    public int getMaximumSignalNameLength()
    {
        return maximumSignalNameLength;
    }


    /**
     * Test metoda
     */
    public static void main (String[] args)
    {
        VcdParser parser = new VcdParser("adder.vcd");
        parser.parse();
        parser.resultToString();
        GhdlResults sParser = new GhdlResults(parser.getResultInString());
        sParser.parseString();
        for (String s : sParser.getSignalNames())
        {
            System.out.println(s);
        }
        for (String[] p : sParser.getSignalValues())
        {
            for (String s : p)
            {
                System.out.print(s + " ");
            }
            System.out.println("");
        }
        for (long broj : sParser.getTransitionPoints())
        {
            System.out.println(broj);
        }
        System.out.println(sParser.getMaximumSignalNameLength());
    }
}




