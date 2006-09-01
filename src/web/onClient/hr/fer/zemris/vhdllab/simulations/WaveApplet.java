package hr.fer.zemris.vhdllab.simulations;

import hr.fer.zemris.vhdllab.vhdl.simulations.VcdParser;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class Console {
  // Create a title string from the class name:
  public static String title(Object o) {
    String t = o.getClass().toString();
    // Remove the word "class":
    if(t.indexOf("class") != -1)
      t = t.substring(6);
    return t;
  }
  public static void
  run(JFrame frame, int width, int height) {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(width, height);
    frame.setVisible(true);
  }
  public static void
  run(JApplet applet, int width, int height) {
    JFrame frame = new JFrame(title(applet));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(applet);
    frame.setSize(width, height);
    applet.init();
    applet.start();
    frame.setVisible(true);
  }
  public static void
  run(JPanel panel, int width, int height) {
    JFrame frame = new JFrame(title(panel));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(panel);
    frame.setSize(width, height);
    frame.setVisible(true);
  }
} ///:~


/**
 * Listener horizontalnog scrollbara koji scrolla panel s valnim oblicima i
 * panel sa skalom
 *
 * @author Boris Ozegovic
 */
class HorizontalScrollBarListener implements AdjustmentListener
{
    private WaveDrawBoard waves;
    private JScrollBar scrollBar;
    private Scale scale;


    public HorizontalScrollBarListener (WaveDrawBoard waves, Scale scale)
    {
        this.waves = waves;
        this.scale = scale;
    }


     /**
     * Postavlja odgovarajuci offset na temelju trenutne vrijednosti scrollbara
     * i ponovno crta obje komponente
     */
    public void adjustmentValueChanged (AdjustmentEvent event)
    {
        JScrollBar source = (JScrollBar)event.getSource();
        waves.setHorizontalOffset(source.getValue());
        waves.repaint();
        scale.setHorizontalOffset(source.getValue());
        scale.repaint();
        
    }
}


/**
 * Listener za vertikalni scrollbar koji pomice panel s valnim oblicima i panel
 * s imenima signala
 *
 * @author Boris Ozegovic
 */
class VerticalScrollBarListener implements AdjustmentListener
{
    private WaveDrawBoard waves;
    private SignalNamesPanel namesPanel;
    private JScrollBar scrollBar;


    /**
     * Constructor
     *
     * @param waves je panel s valnim oblicima
     * @param namesPanel je panel s imenima signala
     */
    public VerticalScrollBarListener (WaveDrawBoard waves, SignalNamesPanel namesPanel)
    {
        this.waves = waves;
        this.namesPanel = namesPanel;
    }


    /**
     * Postavlja odgovarajuci offset na temelju trenutne vrijednosti scrollbara
     * i ponovno crta obje komponente
     */
    public void adjustmentValueChanged (AdjustmentEvent event)
    {
        JScrollBar source = (JScrollBar)event.getSource();
        waves.setVerticalOffset(source.getValue());
        waves.repaint();
        namesPanel.setVerticalOffset(source.getValue());
        namesPanel.repaint();
    }
}


/**
 * Listener za panel s imenima signala.  Buduci da imena mogu biti povece
 * duljine, potrebno je bilo staviti ogranicenje duljine panela i postaviti
 * scrollbar.
 *
 * @author Boris Ozegovic
 */
class NamesPanelScrollbarListener implements AdjustmentListener
{
    private SignalNamesPanel namesPanel;


    /**
     * Constructor
     * 
     * @param namesPanel panel s imenima signala
     */
    public NamesPanelScrollbarListener (SignalNamesPanel namesPanel)
    {
        this.namesPanel = namesPanel;
    }


    /**
     * Metoda koja upravlja eventom i repainta panel s obzirom na offset
     */
    public void adjustmentValueChanged (AdjustmentEvent event)
    {
        JScrollBar source = (JScrollBar)event.getSource();
        namesPanel.setHorizontalOffset(source.getValue());
        namesPanel.repaint();
    }
}



class ZoomInTwo implements ActionListener 
{
    /* panel u kojem su iscrtani valni oblici */
    private WaveDrawBoard waves;
    private Scale scale;
    
    /* scrollbar potreban zato da se prilikom povecanja smanji raspon */
    private JScrollBar scrollbar;

    /* namjesta se isti polozaj scrollbara koji je imao prije povecanja */
    private int offset;


    /**
     * Constructor
     *
     * @param waves panel koji sadrzi valne oblike 
     * @param scale skala koju je potrebno povecati
     * @param scrollbar fino podesavanje
     */
    public ZoomInTwo (WaveDrawBoard waves, Scale scale, JScrollBar scrollbar)
    {
        this.waves = waves;
        this.scale = scale;
        this.scrollbar = scrollbar;
    }


    public void actionPerformed(ActionEvent event) 
    {
        scale.setDurationsInPixelsAfterZoom(2);
		offset = scrollbar.getValue();
		scrollbar.setValue(offset * 2);
        scale.repaint();
        waves.repaint();
        scrollbar.setMaximum(scale.getScaleEndPointInPixels());
    }
}


/**
 * Smanjuje skalu i vrijednost valnih oblika za 10
 *
 * @author Boris Ozegovic
 */
class ZoomOutTwo implements ActionListener 
{
    /* panel u kojem su iscrtani valni oblici */
    private WaveDrawBoard waves;
    private Scale scale;
    
    /* scrollbar potreban zato da se prilikom smanjena smanji raspon */
    private JScrollBar scrollbar;

    /* namjesta se isti polozaj scrollbara koji je imao prije smanjenja */
    private int offset;
    

    /**
     * Constructor
     *
     * @param waves panel koji sadrzi valne oblike 
     * @param scale skala koju je potrbno smanjiti
     * @param scrollbar fino podesavanje
     */
    public ZoomOutTwo (WaveDrawBoard waves, Scale scale, JScrollBar scrollbar)
    {
        this.waves = waves;
        this.scale = scale;
        this.scrollbar = scrollbar;
    }


    public void actionPerformed(ActionEvent event) 
    {
        /* postavlja nove vrijednosti i automatski podesava sve parametre */
        scale.setDurationsInPixelsAfterZoom(0.5f);
		offset = scrollbar.getValue();

        /* scrollbar ostaje na istom mjestu */
		scrollbar.setValue(offset / 2);
        scale.repaint();
        waves.repaint();

        /* nova maksimalna vrijednost scrollbara */
        scrollbar.setMaximum(scale.getScaleEndPointInPixels());
    }
}


/**
 * Povecava skalu i vrijednost valnih oblika za 10
 *
 * @author Boris Ozegovic
 */
class ZoomInTen implements ActionListener 
{
    /* panel u kojem su iscrtani valni oblici */
    private WaveDrawBoard waves;
    private Scale scale;
    
    /* scrollbar potreban zato da se prilikom povecanja smanji raspon */
    private JScrollBar scrollbar;

    /* namjesta se isti polozaj scrollbara koji je imao prije povecanja */
    private int offset;

    /**
     * Constructor
     *
     * @param waves panel koji sadrzi valne oblike 
     * @param scale skala koju je potrebno povecati
     * @param scrollbar fino podesavanje
     */
    public ZoomInTen (WaveDrawBoard waves, Scale scale, JScrollBar scrollbar)
    {
        this.waves = waves;
        this.scale = scale;
        this.scrollbar = scrollbar;
    }


    public void actionPerformed(ActionEvent event) 
    {
        /* postavlja nove vrijednosti i automatski podesava sve parametre */
        scale.setDurationsInPixelsAfterZoom(10);
		offset = scrollbar.getValue();

        /* scrollbar ostaje na istom mjestu */
		scrollbar.setValue(offset * 10);
        scale.repaint();
        waves.repaint();

        /* nova maksimalna vrijednost scrollbara */
        scrollbar.setMaximum(scale.getScaleEndPointInPixels());
    }
}


/**
 * Smanjuje skalu i vrijednost valnih oblika za 10
 *
 * @author Boris Ozegovic
 */
class ZoomOutTen implements ActionListener 
{
    /* panel u kojem su iscrtani valni oblici */
    private WaveDrawBoard waves;
    private Scale scale;
    
    /* scrollbar potreban zato da se prilikom smanjena smanji raspon */
    private JScrollBar scrollbar;

    /* namjesta se isti polozaj scrollbara koji je imao prije smanjenja */
    private int offset;


    /**
     * Constructor
     *
     * @param waves panel koji sadrzi valne oblike 
     * @param scale skala koju je potrbno smanjiti
     * @param scrollbar fino podesavanje
     */
    public ZoomOutTen (WaveDrawBoard waves, Scale scale, JScrollBar scrollbar)
    {
        this.waves = waves;
        this.scale = scale;
        this.scrollbar = scrollbar;
    }


    /**
     * Metoda koja upravlja eventom
     */
    public void actionPerformed(ActionEvent event) 
    {
        /* postavlja nove vrijednosti i automatski podesava sve parametre */
        scale.setDurationsInPixelsAfterZoom(0.1f);
		offset = scrollbar.getValue();

        /* scrollbar ostaje na istom mjestu */
		scrollbar.setValue(offset / 10);
        scale.repaint();
        waves.repaint();

        /* nova maksimalna vrijednost scrollbara */
        scrollbar.setMaximum(scale.getScaleEndPointInPixels());
    }
}


public class WaveApplet extends JApplet 
{
  //private WaveDrawBoard signalBoard = new SignalDrawBoard();
  
  public void init() 
  {
    VcdParser parser = new VcdParser("adder2.vcd");
	parser.parse();
	Map <String, java.util.List <String>> signalValues = parser.getSignalValues();
   
    Scale scale = new Scale(parser);
    SignalNamesPanel signalNames = new SignalNamesPanel(signalValues, 
                                                            parser.getMaximumSignalNameLength());
    WaveDrawBoard waveDrawBoard = new WaveDrawBoard(signalValues, scale);
    
    JScrollBar horizontalScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, scale.getScaleEndPointInPixels()); //pazi, scrollbar mora biti iste duzine kao graf signala
    HorizontalScrollBarListener scroll = new HorizontalScrollBarListener(waveDrawBoard, scale);
    horizontalScrollbar.addAdjustmentListener(scroll);

    JScrollBar verticalScrollbar = new JScrollBar(SwingConstants.VERTICAL, 0, 0, 0, waveDrawBoard.getPreferredSize().height); //pazi, scrollbar mora biti iste duzine kao graf signala
    VerticalScrollBarListener scroll2 = new VerticalScrollBarListener(waveDrawBoard, signalNames);
    verticalScrollbar.addAdjustmentListener(scroll2);
    
    JScrollBar signalNamesScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, signalNames.getPreferredSize().width);
    NamesPanelScrollbarListener namesPanelListener = new NamesPanelScrollbarListener(signalNames);
    signalNamesScrollbar.addAdjustmentListener(namesPanelListener);


	JPanel toolbar = new JPanel();
    toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
    toolbar.setBackground(new Color(141, 176, 221));
    Icon ikona1 = new ImageIcon(getClass().getResource("+2.png"));
    JButton button1 = new JButton(ikona1);
	ZoomInTwo zoomInTwo = new ZoomInTwo(waveDrawBoard, scale, horizontalScrollbar);
    button1.addActionListener(zoomInTwo);
    Icon ikona2 = new ImageIcon(getClass().getResource("-2.png"));
    JButton button2 = new JButton(ikona2);
    ZoomOutTwo zoomOutTwo = new ZoomOutTwo(waveDrawBoard, scale, horizontalScrollbar);
    button2.addActionListener(zoomOutTwo);
    Icon ikona3 = new ImageIcon(getClass().getResource("+10.png"));
    JButton button3 = new JButton(ikona3);
	ZoomInTen zoomInTen = new ZoomInTen(waveDrawBoard, scale, horizontalScrollbar);
	button3.addActionListener(zoomInTen);
    Icon ikona4 = new ImageIcon(getClass().getResource("-10.png"));
    JButton button4 = new JButton(ikona4);
	ZoomOutTen zoomOutTen = new ZoomOutTen(waveDrawBoard, scale, horizontalScrollbar);
	button4.addActionListener(zoomOutTen);
	toolbar.add(button1);
	toolbar.add(button2);
	toolbar.add(button3);
	toolbar.add(button4);
	
    JTextField textField = new JTextField(10);
    textField.setEditable(false);

	Container cp = getContentPane();
    cp.setLayout(new WaveLayoutManager());
    cp.setBackground(new Color(141, 176, 221));
    cp.add(toolbar, "toolbar");
    cp.add(textField, "textField");
    cp.add(signalNames, "signalNames");
    cp.add(waveDrawBoard, "waves");
    cp.add(scale, "scale");
    cp.add(verticalScrollbar, "verticalScrollbar");
    cp.add(horizontalScrollbar, "horizontalScrollbar");
    cp.add(signalNamesScrollbar, "signalNamesScrollbar");
    MouseMotionListener mouseListener = new MouseListenerImpl(textField, scale, horizontalScrollbar);
    waveDrawBoard.addMouseMotionListener(mouseListener);

     //addMouseMotionListener(this);


  }


  public static void main(String[] args) 
  {
      Console.run(new WaveApplet(), 1000, 1000);
  }
}

/**
 * Mouse listener koji osluskuje pokrete misa i svaki pokret registrira te na
 * temelju vrijednosti po X-osi i na temelju trenutnog stanja skale vraca
 * preciznu vrijednost
 *
 * @author Boris Ozegovic
 */
class MouseListenerImpl implements MouseMotionListener
{
    private JTextField textField;
    private Scale scale;
    private JScrollBar scroll;
    private int xValue;
    private double value;


    /**
     * Constructor
     *
     * @param textField polje u kojem se ispisuje precizna vrijednost
     * @param scale skala koja vrsi proracune prilikom zumiranja
     * @param scroll scrollbar potreban zbog trenutnog offseta
     */
    public MouseListenerImpl (JTextField textField, Scale scale, JScrollBar scroll)
    {
        this.textField = textField;
        this.scale = scale;
        this.scroll = scroll;
    }


    /**
     * Metoda koja upravlja eventom
     */
    public void mouseMoved(MouseEvent me)  
    {  
        /* trenutni offset + X-vrijednost kurosra misa */
        xValue = me.getX() + scroll.getValue(); 

        /* podijeljeno s 100 jer je scaleStep za 100 piksela */
        value = xValue * scale.getScaleStepInTime() / 100;
        textField.setText (value + scale.getMeasureUnitName());
    } 

    /**
     * Buducu upotrebu
     */
    public void mouseDragged(MouseEvent me)  
    {
        ;
    }
}