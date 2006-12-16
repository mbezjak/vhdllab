package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;


/**
 * Custom layout manager
 *
 * Manager je namjenski, pretpostavlja sve potrebne komponente i nece raditi ako
 * se izostave neke od komponenata.  Iskljucico je napravljen zbog
 * jednostavnosti raspodjele komponenata po containeru.
 *
 * @author Boris Ozegovic
 */
class WaveLayoutManager implements LayoutManager
{
    private final String TOOLBAR = "toolbar";
  //  private final String TEXTFIELD = "textField";
    private final String CURSOR_PANEL = "cursorPanel";
    private final String SEARCH = "search";
    private final String INTERVAL = "interval";
    private final String SIGNAL_NAMES = "signalNames";
    private final String SIGNAL_NAMES_SCROLLBAR = "signalNamesScrollbar";
	private final String DIVIDER1 = "divider1";
	private final String DIVIDER2 = "divider2";
    private final String SIGNAL_VALUES = "signalValues";
    private final String VALUES_SCROLLBAR = "valuesScrollbar";
    private final String WAVES = "waves";
    private final String SCALE = "scale";
    private final String VERTICAL_SCROLLBAR = "verticalScrollbar";
    private final String HORIZONTAL_SCROLLBAR = "horizontalScrollbar";

    private Component toolbar;
	//private Component textField;
    private Component cursorPanel;
    private Component search;
    private Component interval;
    private Component signalNames;
	private Component divider1;
	private Component divider2;
    private Component signalNamesScrollbar;
    private Component signalValues;
    private Component valuesScrollbar;
    private Component waves;
    private Component scale;
    private Component verticalScrollbar;
    private Component horizontalScrollbar;


    public void addLayoutComponent (String name, Component component) 
    {
        if (TOOLBAR.equals(name)) 
        {
            toolbar = component;
        }
		//else if (TEXTFIELD.equals(name)) 
		//{
		//    textField = component;
		//}
        else if (CURSOR_PANEL.equals(name)) 
        {
            cursorPanel = component;
        }
        else if (SEARCH.equals(name))
        {
            search = component;
        }
        else if (INTERVAL.equals(name))
        {
            interval = component;
        }
        else if (SIGNAL_NAMES.equals(name)) 
        {
            signalNames = component;
        }
		else if (DIVIDER1.equals(name)) 
        {
            divider1 = component;
        }
		else if (DIVIDER2.equals(name)) 
        {
            divider2 = component;
        }
        else if (SIGNAL_NAMES_SCROLLBAR.equals(name)) 
        {
            signalNamesScrollbar = component;
        }
		else if (SIGNAL_VALUES.equals(name)) 
        {
            signalValues = component;
        }
        else if (VALUES_SCROLLBAR.equals(name)) 
        {
            valuesScrollbar = component;
        }
        else if (WAVES.equals(name)) 
        {
            waves = component;
        }
        else if (SCALE.equals(name)) 
        {
            scale = component;
        }
        else if (VERTICAL_SCROLLBAR.equals(name)) 
        {
            verticalScrollbar = component;
        }
        else if (HORIZONTAL_SCROLLBAR.equals(name)) 
        {
            horizontalScrollbar = component;
        }
        else 
        {
            throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
        }
    }


    public void removeLayoutComponent (Component component) 
    {
        if (component == toolbar) 
        {
            toolbar = null;
        }
		//else if (component == textField) 
		//{
		//    textField = null;
		//}
        else if (component == cursorPanel) 
        {
            cursorPanel = null;
        }
        else if (component == search)
        {
            search = null;
        }
        else if (component == interval)
        {
            interval = null;
        }
        else if (component == signalNames) 
        {
            signalNames = null;
        }
        else if (component == divider1) 
        {
            divider1 = null;
        }		
        else if (component == divider2) 
        {
            divider2 = null;
		}
        else if (component == signalNamesScrollbar) 
        {
            signalNamesScrollbar = null;
        }
		else if (component == signalValues) 
        {
            signalValues = null;
        }
        else if (component == valuesScrollbar) 
        {
			valuesScrollbar = null;
        }
        else if (component == waves) 
        {
            waves = null;
        }
        else if (component == scale) 
        {
            scale = null;
        }
        else if (component == verticalScrollbar) 
        {
            verticalScrollbar = null;
        }
        else if (component == horizontalScrollbar) 
        {
            horizontalScrollbar = null;
        }
    }


  public Dimension minimumLayoutSize (Container parent) 
  {
    return preferredLayoutSize (parent);
  }


  public Dimension preferredLayoutSize(Container parent) 
  {
        Dimension dim = new Dimension(0, 0);
        int width = 0;
        int height = 0;

        if ((signalNames != null) && signalNames.isVisible()) 
        {
          width = signalNames.getPreferredSize().width;
          height = signalNames.getPreferredSize().height;
        }
        if ((waves != null) && waves.isVisible()) 
        {
          width += waves.getPreferredSize().width;
          height += waves.getPreferredSize().height;
        }
        if ((verticalScrollbar != null) && verticalScrollbar.isVisible())
        {
            width += verticalScrollbar.getPreferredSize().width;
        }
        if ((toolbar != null) && toolbar.isVisible())
        {
            height += toolbar.getPreferredSize().height;
        }
        if ((horizontalScrollbar != null) && horizontalScrollbar.isVisible())
        {
            height += horizontalScrollbar.getPreferredSize().height;
        }
        if ((scale != null) && scale.isVisible())
        {
            height += scale.getPreferredSize().height;
        }
        
        dim.width = width;
        dim.height = height + 10;

        Insets insets = parent.getInsets();
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;

        return dim;
  }


public void layoutContainer(Container target) 
{
    Insets insets = target.getInsets();
    int north = insets.top;
    int south = target.getSize().height - insets.bottom;
    int west = insets.left;
    int east = target.getSize().width - insets.right;

    int width;
    int height;

    /* prilikom resizea ne mijenjaju svoju duljinu */
    int widthToolbar = toolbar.getPreferredSize().width;
    int heightCursorPanel = cursorPanel.getPreferredSize().height;
	int widthDivider1 = divider1.getPreferredSize().width;
	int widthDivider2 = divider2.getPreferredSize().width;
    
    /* 
     * ako bi imena signala bila predugacka tada se korisno fiksno 450 piksela
     * duljine i to je maksimalna moguca vrijednost.  Zato maximumSize() 
     */
    int widthSignalNames = signalNames.getMaximumSize().width;
	int widthSignalValues = signalValues.getMaximumSize().width;
    int widthVerticalScrollbar = verticalScrollbar.getPreferredSize().width;
    int heightToolbar = toolbar.getPreferredSize().height;
    int heightScale = scale.getPreferredSize().height;
    int heightHorizontalScrollbar = horizontalScrollbar.getPreferredSize().height;

    if ((toolbar != null) && toolbar.isVisible())
    {
        width = toolbar.getPreferredSize().width;
        height = toolbar.getPreferredSize().height;
        toolbar.setSize(width, height);
        toolbar.setBounds
            (
             west, 
             north + 10, 
             width, 
             height
             );
    }
	//if ((textField != null) && textField.isVisible())
	//{
	//    width = textField.getPreferredSize().width;
	//    height = textField.getPreferredSize().height;
	//    textField.setSize(width, height);
	//    textField.setBounds
	//        (
	//         west + widthToolbar + 50,
	//         north + 10, 
	//         width,
	//         height
	//         );
	//}
	if ((interval != null) && interval.isVisible())
    {
        width = interval.getPreferredSize().width;
        height = interval.getPreferredSize().height;
        interval.setSize(width, height);
        interval.setBounds
            (
             west + widthToolbar + 5 + /*textField.getPreferredSize().width*/ + 20,
             north + 10, 
             width,
             height
             );
    }
    if ((search != null) && search.isVisible())
    {
        width = search.getPreferredSize().width;
        height = search.getPreferredSize().height;
        search.setSize(width, height);
        search.setBounds
            (
             west + widthToolbar + 5 + /*textField.getPreferredSize().width*/ + 20 + 
			 20 + interval.getPreferredSize().width,
             north + 10, 
             width,
             height
             );
    } 
    if ((cursorPanel != null) && cursorPanel.isVisible())
    {
        width = cursorPanel.getPreferredSize().width;
        height = cursorPanel.getPreferredSize().height;
        cursorPanel.setSize(width, height);
        cursorPanel.setBounds
            (
             west + widthSignalNames + widthSignalValues + widthDivider2 + widthDivider1, 
             north + 10 + heightToolbar + 10, 
             east - widthVerticalScrollbar - (west + widthSignalNames + widthSignalValues + widthDivider1 + widthDivider2),
             height
             );
    }
    if ((signalNames != null) && signalNames.isVisible())
    {
        width = signalNames.getMaximumSize().width;
        height = signalNames.getPreferredSize().height;
        signalNames.setSize(width, height);
        signalNames.setBounds
            (
             west,
             north + 10 + heightToolbar + 10 + heightCursorPanel,
             width, 
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10 + heightCursorPanel)
             );
    }
	if ((divider1 != null) && divider1.isVisible())
    {
        width = divider1.getPreferredSize().width;
        height = divider1.getPreferredSize().height;
        divider1.setSize(width, height);
        divider1.setBounds
            (
             west + widthSignalNames,
             north + 10 + heightToolbar + 10 + heightCursorPanel,
             width, 
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10 + heightCursorPanel)
             );
    }
    if ((signalNamesScrollbar != null) && signalNamesScrollbar.isVisible())
    {
        width = signalNamesScrollbar.getPreferredSize().width;
        height = signalNamesScrollbar.getPreferredSize().height;
        signalNamesScrollbar.setSize(width, height);
        signalNamesScrollbar.setBounds
            (
             west,
             south - heightHorizontalScrollbar - heightScale,
             widthSignalNames + widthDivider1,
             height
             );
    }
	if ((signalValues != null) && signalValues.isVisible())
    {
        width = signalValues.getMaximumSize().width;
        height = signalValues.getPreferredSize().height;
        signalValues.setSize(width, height);
        signalValues.setBounds
            (
             west + widthSignalNames + widthDivider1,
             north + 10 + heightToolbar + 10 + heightCursorPanel,
             width, 
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10 + heightCursorPanel)
             );
    }
	if ((divider2 != null) && divider2.isVisible())
    {
        width = divider2.getPreferredSize().width;
        height = divider2.getPreferredSize().height;
        divider2.setSize(width, height);
        divider2.setBounds
            (
             west + widthSignalNames + widthSignalValues + widthDivider1,
             north + 10 + heightToolbar + 10 + heightCursorPanel,
             width, 
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10 + heightCursorPanel)
             );
    }
    if ((valuesScrollbar != null) && valuesScrollbar.isVisible())
    {
        width = valuesScrollbar.getPreferredSize().width;
        height = valuesScrollbar.getPreferredSize().height;
        valuesScrollbar.setSize(width, height);
        valuesScrollbar.setBounds
            (
             west + widthSignalNames + widthDivider1,
             south - heightHorizontalScrollbar - heightScale,
             widthSignalValues,
             height
             );
    }
    if ((waves != null) && waves.isVisible())
    {
        width = waves.getPreferredSize().width;
        height = waves.getPreferredSize().height;
        waves.setSize(width, height);
        waves.setBounds
            (
             west + widthSignalNames + widthSignalValues + widthDivider1 + widthDivider2, 
             north + 10 + heightToolbar + 10 + heightCursorPanel, 
             east - widthVerticalScrollbar - (west + widthSignalNames + widthSignalValues + widthDivider1 + widthDivider2),
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10 + heightCursorPanel)
             );                  
    }
    if ((scale != null) && scale.isVisible())
    {
        width = scale.getPreferredSize().width;
        height = scale.getPreferredSize().height;
        scale.setSize(width, height);
        scale.setBounds
            (
             west + widthSignalNames + widthSignalValues + widthDivider1 + widthDivider2,
             south - heightHorizontalScrollbar - heightScale,
             east - widthVerticalScrollbar - (west + widthSignalNames + widthSignalValues + widthDivider1 + widthDivider2),
             heightScale
             );
    }
    if ((verticalScrollbar != null) && verticalScrollbar.isVisible())
    {
        width = verticalScrollbar.getPreferredSize().width;
        height = verticalScrollbar.getPreferredSize().height;
        verticalScrollbar.setSize(width, height);
        verticalScrollbar.setBounds
            (
             east - width, 
             north + 10 + heightToolbar + 10 + heightCursorPanel,
             width, 
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10 + heightCursorPanel)
             );                                
    }
    if ((horizontalScrollbar != null) && horizontalScrollbar.isVisible())
    {
        width = horizontalScrollbar.getPreferredSize().width;
        height = horizontalScrollbar.getPreferredSize().height;
        horizontalScrollbar.setSize(width, height);
        horizontalScrollbar.setBounds
            (
             west + widthSignalNames + widthSignalValues + widthDivider1 + widthDivider2,
             south - height,
             east - widthVerticalScrollbar - (west + widthSignalNames + widthSignalValues + widthDivider1 + widthDivider2),
             height
             );
    }
  }
} 
