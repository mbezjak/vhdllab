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
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
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




public class WaveApplet extends JApplet 
{
    private Container cp;
    private SignalNamesPanel signalNames;
    private WaveDrawBoard waves;
    private Scale scale;
    private JScrollBar verticalScrollbar;
    private JScrollBar horizontalScrollbar;
    private JScrollBar signalNamesScrollbar;
    private JTextField textField = new RoundField(10);
    private JTextField search = new RoundField(10);
    private DefaultListModel listSignals = new DefaultListModel();
    private JList list = new JList(listSignals);
    private JPopupMenu popup = new JPopupMenu();
    private GhdlResults results;
    

    /**
     * Listener za vertikalni scrollbar koji pomice panel s valnim oblicima i panel
     * s imenima signala
     */
    private AdjustmentListener verticalScrollListener = new AdjustmentListener()
    {
        /*
         * Postavlja odgovarajuci offset na temelju trenutne vrijednosti scrollbara
         * i ponovno crta obje komponente
         */
        public void adjustmentValueChanged (AdjustmentEvent event)
        {
            waves.setVerticalOffset(verticalScrollbar.getValue());
            signalNames.setVerticalOffset(verticalScrollbar.getValue());
            waves.repaint();
            signalNames.repaint();
        }
    };


    /**
     * Listener horizontalnog scrollbara koji scrolla panel s valnim oblicima i
     * panel sa skalom
     */
    private AdjustmentListener horizontalScrollListener = new AdjustmentListener()
    {
         /*
         * Postavlja odgovarajuci offset na temelju trenutne vrijednosti scrollbara
         * i ponovno crta obje komponente
         */
        public void adjustmentValueChanged (AdjustmentEvent event)
        {
            waves.setHorizontalOffset(horizontalScrollbar.getValue());
            scale.setHorizontalOffset(horizontalScrollbar.getValue());
            waves.repaint();
            scale.repaint();
            
        }
    };


    /**
     * Listener horizontalnog scrollbara koji scrolla panel s valnim oblicima i
     * panel sa skalom
     */
   private AdjustmentListener signalNamesScrollListener = new AdjustmentListener()
   {
        /**
         * Postavlja odgovarajuci offset na temelju trenutne vrijednosti scrollbara
         * i ponovno crta obje komponente
         */
        public void adjustmentValueChanged (AdjustmentEvent event)
        {
            signalNames.setHorizontalOffset(signalNamesScrollbar.getValue());
            signalNames.repaint();
        }
    };


   /**
    * Listener buttona koji pokrece popup prozor
    */
    private ActionListener changeSignalMenu = new ActionListener()
    {
        public void actionPerformed (ActionEvent event) 
        {
            popup.show(cp, 400, 75);
        }
    };


    /**
     * Povecava skalu i vrijednost valnih oblika za 10
     */
    ActionListener zoomInTenListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent event) 
        {
            /* postavlja nove vrijednosti i automatski podesava sve parametre */
            scale.setDurationsInPixelsAfterZoom(10d);
            int offset = horizontalScrollbar.getValue();

            /* scrollbar ostaje na istom mjestu */
            horizontalScrollbar.setValue(offset * 10);
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());
        }
    };


    /**
     * Povecava skalu i vrijednost valnih oblika za 2
     */
    ActionListener zoomInTwoListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent event) 
        {
            /* postavlja nove vrijednosti i automatski podesava sve parametre */
            scale.setDurationsInPixelsAfterZoom(2d);
            int offset = horizontalScrollbar.getValue();

            /* scrollbar ostaje na istom mjestu */
            horizontalScrollbar.setValue(offset * 2);
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());
        }
    };

    
    /**
     * Smanjuje skalu i vrijednost valnih oblika za 2
     */
    ActionListener zoomOutTenListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent event) 
        {
            /* postavlja nove vrijednosti i automatski podesava sve parametre */
            scale.setDurationsInPixelsAfterZoom(0.1d);
            int offset = horizontalScrollbar.getValue();

            /* scrollbar ostaje na istom mjestu */
            horizontalScrollbar.setValue(offset / 10);
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());
        }
    };


    /**
     * Smanjuje skalu i vrijednost valnih oblika za 10
     */
    ActionListener zoomOutTwoListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent event) 
        {
            /* postavlja nove vrijednosti i automatski podesava sve parametre */
            scale.setDurationsInPixelsAfterZoom(0.5d);
            int offset = horizontalScrollbar.getValue();

            /* scrollbar ostaje na istom mjestu */
            horizontalScrollbar.setValue(offset / 2);
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());
        }
    };
        
    
    /**
     * Osluskuje 'up' button u popup meniju
     */
    private ActionListener upListener = new ActionListener()
    {
        public void actionPerformed (ActionEvent event) 
        {
            int index = signalNames.getIndex();
            boolean isClicked = signalNames.getIsClicked();
            
            /* ako niti jedan signal nije selektiran */
            if (!isClicked)
            {
                return;
            }
 
            /* promijeni poredak signala prema gore */
            results.changeSignalOrderUp(index);

            if (index > 0)
            {
                signalNames.setIndex(index - 1);
                waves.setIndex(index - 1);
            }

            /* repainta panel s imenima signala i panel s valnim oblicima */
            signalNames.repaint();
            waves.repaint();
            if (index * 45 <= signalNames.getVerticalOffset())
            {
                verticalScrollbar.setValue(verticalScrollbar.getValue() - 200);
            }
        }
    };


    /**
     * Slusa 'down' button popup meniju
     */
    private ActionListener downListener = new ActionListener()
    {
        public void actionPerformed (ActionEvent event) 
        {
            int index = signalNames.getIndex();
            boolean isClicked = signalNames.getIsClicked();
            
            /* ako niti jedan signal nije selektiran */
            if (!isClicked)
            {
                return;
            }
 
           /* promijeni poredak signala prema dolje */
            results.changeSignalOrderDown(index);


            if (index < results.getSignalNames().length - 1)
            {
                signalNames.setIndex(index + 1);
                waves.setIndex(index + 1);
            }

            /* repainta panel s imenima signala i panel s valnim oblicima */
            signalNames.repaint();
            waves.repaint();
            if ((index + 1) * 45 + 50 >= signalNames.getHeight() + signalNames.getVerticalOffset())
            {
                verticalScrollbar.setValue(verticalScrollbar.getValue() + 200);
            }
        }
    };


    /**
     * slusa 'default' button u popup meniju
     */
    private ActionListener defaultOrderListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent event) 
        {
            /* promijeni natrag na defaultni poredak */
            results.setDefaultOrder();

            /* iscrtaj novi poredak u listi */
            DefaultListModel listSignals = (DefaultListModel)list.getModel();
            listSignals.removeAllElements();
            for (String ime : results.getSignalNames())
            {
                listSignals.addElement(ime);
            }

            /* postavlja novi objekt imena signala i njihovih vrijednosti */
            signalNames.setSignalNames(results.getSignalNames());
            waves.setSignalValues(results.getSignalValues());

            /* repainta panel s imenima signala i panel s valnim oblicima */
            signalNames.repaint();
            waves.repaint();
        }
    };


    /**
     * Mouse listener koji osluskuje pokrete misa i svaki pokret registrira te na
     * temelju vrijednosti po X-osi i na temelju trenutnog stanja skale vraca
     * preciznu vrijednost
     */
    private MouseMotionListener mouseListener = new MouseMotionListener()
    {
        /**
         * Metoda koja upravlja eventom
         */
        public void mouseMoved(MouseEvent event)  
        {  
            /* trenutni offset + X-vrijednost kurosra misa */
            int xValue = event.getX() + horizontalScrollbar.getValue(); 
            /* podijeljeno s 100 jer je scaleStep za 100 piksela */
            double value = xValue * scale.getScaleStepInTime() / 100;
            textField.setText((Math.round(value * 100000d) / 100000d) + scale.getMeasureUnitName());
        } 

        /**
         * Za buducu upotrebu
         */
        public void mouseDragged(MouseEvent me)  
        {
            ;
        }
    };


    /**
     * Mouse listener koji osluskuje klik misa iznad panela sa imenima signala i
     * panela s valnim oblicima te na temelju trenutne vrijednosti po X-osi
     * mijenja background iznad trenutno oznacenog signala
     */
    private MouseListener mouseClickListener = new MouseListener()
    {

        public void mousePressed(MouseEvent event) 
        {
            ; 
        }

        public void mouseReleased(MouseEvent event) 
        {
            ;
        }

        public void mouseEntered(MouseEvent event) 
        {
            ;
        }

        public void mouseExited(MouseEvent event) 
        {
            ;
        }

        public void mouseClicked(MouseEvent event) 
        {
            int index;
            boolean isClicked;
            
            /* 
             * nadi tocno oznaceni spring, prvo se umanji za ostatak dijeljenja s
             * 10, zatim se uzastopno umanjuje za 10 dok se ne nade broj dijeljiv s
             * 40
             */
            int value = event.getY() + verticalScrollbar.getValue();
            while (value % 45 == 0)
            {
                value -= 1;
            }

            /* pronalazi se index signala kojeg treba oznaciti */
            index = value / 45;
            isClicked = signalNames.getIsClicked();

            /* postavlja se vrijednost suprotna od one koja je do sada bila */
            if (waves.getIndex() == index && waves.getIsClicked() == true)
            {
                signalNames.setIsClicked(false);
                waves.setIsClicked(false);
            }
            else
            {
                signalNames.setIsClicked(true);
                signalNames.setIndex(index);
                waves.setIsClicked(true);
                waves.setIndex(index);
            }
            signalNames.repaint();
            waves.repaint();
        }
    };


    /**
     * Listener koji provjerava je li sto upisano u search
     */
    private ActionListener searchListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            String input = search.getText();
            boolean isFound = false;
            int index = 0;

            /* pretrazivanje imena signala */
            for (int i = 0; i < results.getSignalNames().length; i++)
            {
                if (results.getSignalNames()[i].toLowerCase().equals(input.toLowerCase()))
                {
                    isFound = true;
                    index = i;
                    break;
                }
            }

            /* ako je nasao */
            if (isFound)
            {
                signalNames.setIsClicked(true);
                signalNames.setIndex(index);
                waves.setIsClicked(true);
                waves.setIndex(index);
                verticalScrollbar.setValue(index * 45);
                System.out.println(index);
            }
            else
            {
                search.setText("Not found");
            }
            signalNames.repaint();
            waves.repaint();
        }
    };


    /**
     * Listener koji resetira search polje kada se klikne na njega
     */
    private MouseListener searchClickListener = new MouseListener()
    {
        public void mousePressed(MouseEvent event) 
        {
            ; 
        }

        public void mouseReleased(MouseEvent event) 
        {
            ;
        }

        public void mouseEntered(MouseEvent event) 
        {
            ;
        }

        public void mouseExited(MouseEvent event) 
        {
            ;
        }

        public void mouseClicked(MouseEvent event) 
        {
            search.setText("");
        }
    };
        

      
  public void init() 
  {
    VcdParser parser = new VcdParser("adder2.vcd");
	parser.parse();
    parser.resultToString();
    textField.setEditable(false);
    search.setText("search signal");
    search.addMouseListener(searchClickListener);
    search.addActionListener(searchListener);

    /* rezultati prikazni stringom prenose se GhdlResults klasi */
    results = new GhdlResults(parser.getResultInString());

    /* GhdlResults parsira string i iznacuje rezultate simulacije */
    results.parseString();
   
    /* stvara se skala */
    scale = new Scale(results);

    /* panel s imenima signala */
    signalNames = new SignalNamesPanel(results);
    signalNames.addMouseListener(mouseClickListener);

    /* panel s valnim oblicima */
    waves = new WaveDrawBoard(results , scale);
    waves.addMouseMotionListener(mouseListener);
    waves.addMouseListener(mouseClickListener);


    /* svi scrollbarovi sadrzani u appletu */
    horizontalScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, scale.getScaleEndPointInPixels()); 
    horizontalScrollbar.addAdjustmentListener(horizontalScrollListener);
    verticalScrollbar = new JScrollBar(SwingConstants.VERTICAL, 0, 0, 0, waves.getPreferredSize().height); 
    verticalScrollbar.addAdjustmentListener(verticalScrollListener);
    signalNamesScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, signalNames.getPreferredSize().width);
    signalNamesScrollbar.addAdjustmentListener(signalNamesScrollListener);



    /* 
     * Popup prozor koji ce izletjeti na zahtjev promjene poretka signala.
     * Popup prozor ce sadrzavati listu imena signala i primjerene buttone
     */
    
    /* lista signala i njezina inicijalizacija */
    list.setBackground(new Color(141, 176, 221));
    list.setSelectionBackground(new Color(254, 217, 182));
    for (String ime : results.getSignalNames())
    {
        listSignals.addElement(ime);
    }
    JScrollPane listScroller = new JScrollPane(list);
    //
    //JButton upButton = new JButton ("Up");
    //upButton.addActionListener(upListener);
    //JButton downButton = new JButton("Down");
    //downButton.addActionListener(downListener);
    //JButton defaultButton = new JButton("Default order");
    //defaultButton.addActionListener(defaultOrderListener);
    //
    //JPanel buttonPanel = new JPanel();
    //buttonPanel.setBackground(new Color(141, 176, 221));
    //buttonPanel.setLayout(new BoxLayout (buttonPanel, BoxLayout.PAGE_AXIS));
    //buttonPanel.add(upButton);
    //buttonPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    //buttonPanel.add(downButton);
    //buttonPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    //buttonPanel.add(defaultButton);
    //
    //JPanel pane = new JPanel();
    //pane.setBackground(new Color(141, 176, 221));
    //pane.setLayout(new BoxLayout (pane, BoxLayout.LINE_AXIS));
    //pane.add(listScroller);
    //pane.add(buttonPanel);
    //
    //popup.setPreferredSize(new Dimension(300, 800));
    //popup.add(pane);
    /* kraj popup prozora */    


    /* toolbar */
	JPanel toolbar = new JPanel();
    toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
    toolbar.setBackground(new Color(141, 176, 221));
    Icon ikona1 = new ImageIcon(getClass().getResource("+2.png"));
    JButton button1 = new JButton(ikona1);
    button1.addActionListener(zoomInTwoListener);
    button1.setToolTipText("Zoom in by two");
    Icon ikona2 = new ImageIcon(getClass().getResource("-2.png"));
    JButton button2 = new JButton(ikona2);
    button2.addActionListener(zoomOutTwoListener);
    button2.setToolTipText("Zoom out by two");
    Icon ikona3 = new ImageIcon(getClass().getResource("+10.png"));
    JButton button3 = new JButton(ikona3);
	button3.addActionListener(zoomInTenListener);
    button3.setToolTipText("Zoom in by ten");
    Icon ikona4 = new ImageIcon(getClass().getResource("-10.png"));
    JButton button4 = new JButton(ikona4);
	button4.addActionListener(zoomOutTenListener);
    button4.setToolTipText("Zoom out by ten");
    Icon ikona5 = new ImageIcon(getClass().getResource("upDown.png"));
    JButton button5 = new JButton(ikona5);
    button5.addActionListener(changeSignalMenu);
    button5.setToolTipText("Change signal order");
    Icon ikona6 = new ImageIcon(getClass().getResource("up.png"));
    JButton upButton = new JButton (ikona6);
    upButton.setToolTipText("Signal up");
    upButton.addActionListener(upListener);
    Icon ikona7 = new ImageIcon(getClass().getResource("down.png"));
    JButton downButton = new JButton(ikona7);
    downButton.setToolTipText("Signal down");
    downButton.addActionListener(downListener);
	toolbar.add(button1);
	toolbar.add(button2);
	toolbar.add(button3);
	toolbar.add(button4);
    toolbar.add(upButton);
    toolbar.add(downButton);
    toolbar.add(button5);
	
    
    /* postavljanje komponenti na applet */
	cp = getContentPane();
    cp.setLayout(new WaveLayoutManager());
    cp.setBackground(new Color(141, 176, 221));
    cp.add(toolbar, "toolbar");
    cp.add(textField, "textField");
    cp.add(search, "search");
    cp.add(signalNames, "signalNames");
    cp.add(waves, "waves");
    cp.add(scale, "scale");
    cp.add(verticalScrollbar, "verticalScrollbar");
    cp.add(horizontalScrollbar, "horizontalScrollbar");
    cp.add(signalNamesScrollbar, "signalNamesScrollbar");
  }


  public static void main(String[] args) 
  {
      Console.run(new WaveApplet(), 1000, 1000);
  }
} 

