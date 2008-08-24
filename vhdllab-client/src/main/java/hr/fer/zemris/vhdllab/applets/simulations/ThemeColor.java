package hr.fer.zemris.vhdllab.applets.simulations;


import java.awt.Color;


/**
 * Klasa sadrzi boje za sve vidljive komponente na appletu za prikaz rezultata
 * simulacije
 *
 * @author Boris Ozegovic
 */
public class ThemeColor
{
    /** Index teme koja se trenutno koristi, redom: custom, default, FER */
    private int themeIndex = 0;

    /** Background boja panela s imenima signala */
    private Color[] signalNames = {new Color(141, 176, 221), 
		new Color(141, 176, 221), new Color(214, 214, 214)};

    /** Background boja panela s valnim oblicima */
    private Color[] waves = {new Color(201, 211, 236), 
		new Color(201, 211, 236), new Color(255, 255, 239)};

    /** Background boja panela koji sadrzi skalu */
    private Color[] scale = {new Color(254, 217, 182), 
		new Color(254, 217, 182), new Color(255, 243, 239)};

    /** Boja isprekidanih crta u panelu s valnim oblicima */
    private Color[] wavesNet = {new Color(163, 179, 225), 
		new Color(163, 179, 225), new Color(163, 179, 225)};
    
    /** Boja pasivnog kursora */
    private Color[] pasiveCursor = {new Color(129, 82, 182), 
		new Color(129, 82, 182), new Color(129, 82, 182)};

    /** Aktivni kursor */
    private Color[] activeCursor = {new Color(131, 170, 85), 
		new Color(116, 70, 57), new Color(116, 70, 57)};

    /** Boja panela s ikonicama za sljedeci/prethodni padajuci/rastuci brid */
    private Color[] buttonPanel = {new Color(141, 176, 221), 
		new Color(141, 176, 221), new Color(214, 214, 214)};
    
    /** Background boja toolbara */
    private Color[] toolbar = {new Color(141, 176, 221), 
		new Color(141, 176, 221), new Color(214, 214, 214)};
    
    /** Background boja citavog appleta */
    private Color[] applet = {new Color(254, 217, 182), 
		new Color(254, 217, 182), new Color(214, 214, 214)};
    
    /** Foreground boja slova */
    private Color[] letters = {new Color(51, 51, 51), 
		new Color(51, 51, 51), new Color(51, 51, 51)};
    
    /** Boja granicnika izmedu panela s imenim signala i valnih oblika */
    private Color[] divider = {new Color(86, 104, 176), 
		new Color(86, 104, 176), new Color(86, 104, 176)};
    
    /** Background boja kursor-panela */
    private Color[] cursorPanel = {new Color(255, 255, 255), 
		new Color(255, 255, 255), new Color(255, 255, 255)};
    

    /**
     * Mijenja trenutnu temu
     */
    public void setThemeIndex (int themeIndex)
    {
        this.themeIndex = themeIndex;
    }


    /**
     * Vraca background boju panela s imenima signala
     */
    public Color getSignalNames ()
    {
        return signalNames[themeIndex];
    }


    /**
     * Postavlja custom boju panela s imenima signala
     *
     * @param color zeljena boja
     */
    public void setSignalNames (Color color)
    {
        signalNames[0] = color;
    }


    /**
     * Vraca background boju panela s valnim oblicima
     */
    public Color getWaves ()
    {
        return waves[themeIndex];
    }


    /**
     * Postavlja custom boju panela s valnim oblicima
     *
     * @param color zeljena boja
     */
    public void setWaves (Color color)
    {
        waves[0] = color;
    }


    /**
     * Vraca background boju panela koji sadrzi skalu 
     */
    public Color getScale ()
    {
        return scale[themeIndex];
    }


    /**
     * Postavlja custom boju panela koji sadrzi skalu
     *
     * @param color zeljena boja
     */
    public void setScale (Color color)
    {
        scale[0] = color;
    }


    /**
     * Vraca boju isprekidanih crta u panelu s valnim oblicima
     */
    public Color getWavesNet ()
    {
        return wavesNet[themeIndex];
    }


    /**
     * Postavlja custom boju isprekidanih crta u panel s valnim oblicima
     *
     * @param color zeljena boja
     */
    public void setWavesNet (Color color)
    {
        wavesNet[0] = color;
    }


    /**
     * Vraca boju aktivnog kursora
     */
    public Color getActiveCursor ()
    {
        return activeCursor[themeIndex];
    }


    /**
     * Postavlja custom boju aktivnog kursora
     *
     * @param color zeljena boja
     */
    public void setActiveCursor (Color color)
    {
        activeCursor[0] = color;
    }


    /**
     * Vraca boju pasivnog kursora
     */
    public Color getPasiveCursor ()
    {
        return pasiveCursor[themeIndex];
    }


    /**
     * Postavlja custom boju pasivnog kursora
     *
     * @param color zeljena boja
     */
    public void setPasiveCursor (Color color)
    {
        pasiveCursor[0] = color;
    }


    /**
     * Vraca boju panela koji sadrzi ikone za sljedeci/prethodni
     * padajuci/rastuci brid signala
     */
    public Color getButtonPanel ()
    {
        return buttonPanel[themeIndex];
    }


    /**
     * Postavlja custom boju panela koji sadrzi ikone za sljedeci/prethodni
     * padajuci/rastuci brid signala
     *
     * @param color zeljena boja
     */
    public void setButtonPanel (Color color)
    {
        buttonPanel[0] = color;
    }


    /**
     * Vraca boju toolbara
     */
    public Color getToolbar ()
    {
        return toolbar[themeIndex];
    }


    /**
     * Postavlja custom boju toolbara
     *
     * @param color zeljena boja
     */
    public void setToolbar (Color color)
    {
        toolbar[0] = color;
    }


    /**
     * Vraca boju glavnog appleta
     */
    public Color getApplet ()
    {
        return applet[themeIndex];
    }


    /**
     * Postavlja custom boju appleta
     *
     * @param color zeljena boja
     */
    public void setApplet (Color color)
    {
        applet[0] = color;
    }
    

    /**
     * Vraca boju slova
     */
    public Color getLetters ()
    {
        return letters[themeIndex];
    }


    /**
     * Postavlja custom boju slova
     *
     * @param color zeljena boja
     */
    public void setLetters (Color color)
    {
        letters[0] = color;
    }
    

    /**
     * Vraca boju granicnika izmedu panela s imenima signala i panela s valnim
     * oblicima
     */
    public Color getDivider ()
    {
        return divider[themeIndex];
    }


    /**
     * Postavlja custom boju granicnika izmedu panela s imenima signala i panela
     * s valnim oblicima
     *
     * @param color zeljena boja
     */
    public void setDivider (Color color)
    {
        divider[0] = color;
    }

    
    /**
     * Vraca boju panela koji sadrzi kursor
     */
    public Color getCursorPanel ()
    {
        return cursorPanel[themeIndex];
    }


    /**
     * Postavlja custom boju cursor-panela
     *
     * @param color zeljena boja
     */
    public void setCursorPanel (Color color)
    {
        cursorPanel[0] = color;
    }
}
