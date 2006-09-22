package hr.fer.zemris.vhdllab.simulations;

import hr.fer.zemris.vhdllab.vhdl.simulations.VcdParser;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
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
    private JTextField interval = new RoundField(10);
    private CursorPanel cursorPanel;
    private DefaultListModel listSignals = new DefaultListModel();
    private JList list = new JList(listSignals);
    private JPopupMenu popup = new JPopupMenu();
    private JPopupMenu popupHelp = new JPopupMenu();
    private GhdlResults results;
    private HelpPanel helpPanel = new HelpPanel();
    

    
    /* ikone */
    private Icon navigate = new ImageIcon(getClass().getResource("navigate.png"));
    private Icon rightUpIcon = new ImageIcon(getClass().getResource("rightUp.png"));
    private Icon rightDownIcon = new ImageIcon(getClass().getResource("rightDown.png"));
    private Icon leftUpIcon = new ImageIcon(getClass().getResource("leftUp.png"));
    private Icon leftDownIcon = new ImageIcon(getClass().getResource("leftDown.png"));
    private Icon zoomInTwoIcon = new ImageIcon(getClass().getResource("+2.png"));
    private Icon zoomOutTwoIcon = new ImageIcon(getClass().getResource("-2.png"));
    private Icon zoomInTenIcon = new ImageIcon(getClass().getResource("+10.png"));
    private Icon zoomOutTenIcon = new ImageIcon(getClass().getResource("-10.png"));
    private Icon defaultIcon = new ImageIcon(getClass().getResource("default.png"));
    private Icon upIcon = new ImageIcon(getClass().getResource("up.png"));
    private Icon downIcon = new ImageIcon(getClass().getResource("down.png"));
    private Icon helpIcon = new ImageIcon(getClass().getResource("help.png"));

    /* buttons */
    private JButton navigateSignals = new JButton(navigate);
    private JButton rightUp = new JButton(rightUpIcon);
    private JButton rightDown = new JButton(rightDownIcon);
    private JButton leftUp = new JButton(leftUpIcon);
    private JButton leftDown = new JButton(leftDownIcon);
    private JButton zoomInTwoButton = new JButton(zoomInTwoIcon);
    private JButton zoomOutTwoButton = new JButton(zoomOutTwoIcon);
    private JButton zoomInTenButton = new JButton(zoomInTenIcon);
    private JButton zoomOutTenButton = new JButton(zoomOutTenIcon);
    private JButton defaultButton = new JButton(defaultIcon);
    private JButton upButton = new JButton(upIcon);
    private JButton downButton = new JButton(downIcon);
    private JButton helpButton = new JButton(helpIcon);

    /* prethodno pritisnuta tipka. Potrebna za kombinaciju dviju tipki */
    private char previousKey = 'A';


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
     * Slusa kotacic misa i pomice vertikalni scrollbar
     */
    private MouseWheelListener wheelListener = new MouseWheelListener()
    {
        public void mouseWheelMoved (MouseWheelEvent event)
        {
            int value = event.getWheelRotation() * 24;
            verticalScrollbar.setValue(verticalScrollbar.getValue() + value);
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
            cursorPanel.setOffset(horizontalScrollbar.getValue());
            waves.repaint();
            scale.repaint();
            cursorPanel.repaint();
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
    private ActionListener showNavigation = new ActionListener()
    {
        public void actionPerformed (ActionEvent event) 
        {
            popup.show(cp, 540, 35);

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
        } 
    };


   /**
    * Otvara help popup
    */
    private ActionListener showHelp = new ActionListener()
    {
        public void actionPerformed (ActionEvent event) 
        {
            popupHelp.show(cp, 200, 50);

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
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
            cursorPanel.setCursorStartPoint(cursorPanel.getCursorStartPoint() * 10);
            waves.setCursorStartPoint(waves.getCursorStartPoint() * 10);
            cursorPanel.repaint();
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
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
            cursorPanel.setCursorStartPoint(cursorPanel.getCursorStartPoint() * 2);
            waves.setCursorStartPoint(waves.getCursorStartPoint() * 2);
            cursorPanel.repaint();
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
        }
    };

    
    /**
     * Smanjuje skalu i vrijednost valnih oblika za 10
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
            cursorPanel.setCursorStartPoint(cursorPanel.getCursorStartPoint() / 10);
            waves.setCursorStartPoint(waves.getCursorStartPoint() / 10);
            cursorPanel.repaint();
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
        }
    };


    /**
     * Smanjuje skalu i vrijednost valnih oblika za 2
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
            cursorPanel.setCursorStartPoint(cursorPanel.getCursorStartPoint() / 2);
            waves.setCursorStartPoint(waves.getCursorStartPoint() / 2);
            cursorPanel.repaint();
            scale.repaint();
            waves.repaint();

            /* nova maksimalna vrijednost scrollbara */
            horizontalScrollbar.setMaximum(scale.getScaleEndPointInPixels());

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
        }
    };
        
    
    /**
     * Osluskuje 'up' button 
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

            /* refresha mapu koja sadrzi informaciju o extendanim signalima */
            /* ako je vektor otisao gore */
            if (index > 0 && results.getSignalValues()[index - 1][0].length() > 1)
            {
                boolean isExpanded = results.getExpandedSignalNames().get(index);
                /* ako iznad nije bio vektor */
                if (!results.getExpandedSignalNames().containsKey(index - 1))
                {
                    results.getExpandedSignalNames().remove(index);
                }
                else
                {
                    results.getExpandedSignalNames().put(index, results.getExpandedSignalNames().get(index - 1));
                }
                results.getExpandedSignalNames().put(index - 1, isExpanded);
                
            }
            else if (results.getExpandedSignalNames().containsKey(index - 1))
            {
                results.getExpandedSignalNames().put(index, results.getExpandedSignalNames().get(index - 1));
                results.getExpandedSignalNames().remove(index - 1);
            }
            signalNames.setSpringStartPoints(results.getSignalValues());

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
            
            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
        }
    };


    /**
     * Slusa 'down' button 
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

            /* refresha mapu koja sadrzi informaciju o extendanim signalima */
            /* ako je vektor otisao dolje */
            if (index < results.getSignalNames().length - 1 && results.getSignalValues()[index + 1][0].length() > 1)
            {
                boolean isExpanded = results.getExpandedSignalNames().get(index);
                /* ako ispod nije bio vektor */
                if (!results.getExpandedSignalNames().containsKey(index + 1))
                {
                    results.getExpandedSignalNames().remove(index);
                }
                else
                {
                    results.getExpandedSignalNames().put(index, results.getExpandedSignalNames().get(index + 1));
                }
                results.getExpandedSignalNames().put(index + 1, isExpanded);
                
            }
            /* inace ako je obican signal otisao dolje */
            else if (results.getExpandedSignalNames().containsKey(index + 1))
            {
                results.getExpandedSignalNames().put(index, results.getExpandedSignalNames().get(index + 1));
                results.getExpandedSignalNames().remove(index + 1);
            }
            signalNames.setSpringStartPoints(results.getSignalValues());

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

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
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
            results.setDefaultExpandedSignalNames();
            signalNames.setDefaultSpringStartPoints();


            /* postavlja novi objekt imena signala i njihovih vrijednosti */
            signalNames.setSignalNames(results.getSignalNames());
            waves.setSignalValues(results.getSignalValues());

            signalNames.setIndex(0);
            waves.setIndex(0);

            /* repainta panel s imenima signala i panel s valnim oblicima */
            signalNames.repaint();
            waves.repaint();

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
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

            /* racuna interval izmedu kursora i trenutne pozicije misa */
            double measuredTime = value - cursorPanel.getValue();
            interval.setText((Math.round(measuredTime * 100000d) / 100000d) + scale.getMeasureUnitName());

            /* 
             * samo zato da se strelica makne ako se pomakne mis prebrzo iz
             * panela s imenima signala
             */
            signalNames.setIsArrowVisible(false, 0);
            signalNames.repaint(); 
        } 

        /**
         * Metoda koja upravlja kursorom
         */
        public void mouseDragged(MouseEvent event)  
        {
            waves.setCursorStartPoint(event.getX() + waves.getHorizontalOffset());
            cursorPanel.setCursorStartPoint(event.getX() + waves.getHorizontalOffset());
            int rightBorder = horizontalScrollbar.getValue() + waves.getPanelWidth();
            int leftBorder = horizontalScrollbar.getValue();
            if (event.getX() + waves.getHorizontalOffset () + 20 >= rightBorder)
            {
                horizontalScrollbar.setValue(horizontalScrollbar.getValue() + 20);
                waves.setCursorStartPoint(rightBorder - 20);
                cursorPanel.setCursorStartPoint(rightBorder - 20);
            }
            else if (event.getX() + waves.getHorizontalOffset () < 
                    leftBorder && waves.getHorizontalOffset() != 0)
            {
                horizontalScrollbar.setValue(horizontalScrollbar.getValue() - 20);
                waves.setCursorStartPoint(leftBorder + 20);
                cursorPanel.setCursorStartPoint(leftBorder + 20);
            }
            
            /* trenutni offset + X-vrijednost kurosra misa */
            int xValue = event.getX() + horizontalScrollbar.getValue(); 
            /* podijeljeno s 100 jer je scaleStep za 100 piksela */
            double value = xValue * scale.getScaleStepInTime() / 100;
            textField.setText((Math.round(value * 100000d) / 100000d) + scale.getMeasureUnitName());
            cursorPanel.setString((Math.round(value * 100000d) / 100000d) + scale.getMeasureUnitName());
            if (value <= 0)
            {
                value = 0;
            }
            cursorPanel.setValue((Math.round(value * 100000d) / 100000d));
            waves.repaint();
            cursorPanel.repaint();
        }
    };


    /**
     * Mouse listener koji pomice kursor na sljedeci/prethodni padajuci/rastuci
     * brid
     */
    private ActionListener navigateListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent event) 
        {
            if (signalNames.getIsClicked())
            {
                String presentValue;
                String previousValue;
                String nextValue;
                int index = 0;
                int transitionPoint = scale.getDurationInPixels()[0];
                boolean isFound = false;
                while (cursorPanel.getCursorStartPoint() >= transitionPoint)
                {
                    index++;
                    transitionPoint += scale.getDurationInPixels()[index];
                }

                /* ako se trazi prethodni rastuci */
                if (event.getSource().equals(leftUp))
                {
                    for (--index; index >= 1;)
                    {
                        presentValue = results.getSignalValues()[signalNames.getIndex()][index];
                        previousValue = results.getSignalValues()[signalNames.getIndex()][--index];
                        if (presentValue.equals("1") && previousValue.equals("0"))
                        {
                            isFound = true;
                            break;
                        }
                    }
                }
                /* ako se trazi prethodni padajuci */
                else if (event.getSource().equals(leftDown))
                {
                    for (--index; index >= 1;)
                    {
                        presentValue = results.getSignalValues()[signalNames.getIndex()][index];
                        previousValue = results.getSignalValues()[signalNames.getIndex()][--index];
                        if (presentValue.equals("0") && previousValue.equals("1"))
                        {
                            isFound = true;
                            break;
                        }
                    }
                }
                /* ako se trazi sljedeci rastuci */
                else if (event.getSource().equals(rightUp))
                {
                    for (; index < results.getSignalValues()[0].length - 1; index++) 
                    {
                        presentValue = results.getSignalValues()[signalNames.getIndex()][index];
                        nextValue = results.getSignalValues()[signalNames.getIndex()][index + 1];
                        if (presentValue.equals("0") && nextValue.equals("1"))
                        {
                            isFound = true;
                            break;
                        }
                    }
                }
                /* ako se trazi sljedeci padajuci */
                else if (event.getSource().equals(rightDown))
                {
                    for (; index < results.getSignalValues()[0].length - 1; index++)
                    {
                        presentValue = results.getSignalValues()[signalNames.getIndex()][index];
                        nextValue = results.getSignalValues()[signalNames.getIndex()][index + 1];
                        if (presentValue.equals("1") && nextValue.equals("0"))
                        {
                            isFound = true;
                            break;
                        }
                    }
                }
                if (isFound)
                {
                    transitionPoint = 0;
                    for (; index >= 0; index--)
                    {
                        transitionPoint += scale.getDurationInPixels()[index];
                    }
                    cursorPanel.setCursorStartPoint(transitionPoint);
                    waves.setCursorStartPoint(transitionPoint);
                    horizontalScrollbar.setValue(transitionPoint - 100);
                    double value = transitionPoint * scale.getScaleStepInTime() / 100;
                    cursorPanel.setString((Math.round(value * 100000d) / 100000d) + 
                            scale.getMeasureUnitName());
                    cursorPanel.setValue((Math.round(value * 100000d) / 100000d));
                }
            }
            cursorPanel.repaint();
            waves.repaint();

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
        }
    };



        /**
     * Mouse listener koji osluskuje pokrete misa i svaki pokret registrira te na
     * temelju vrijednosti po X-osi i na temelju trenutnog stanja skale vraca
     * preciznu vrijednost
     */
    private MouseMotionListener signalNamesListener = new MouseMotionListener()
    {
        /**
         * Metoda koja upravlja eventom
         */
        public void mouseMoved(MouseEvent event)  
        {  
            /* 
             * ako je kursor misa pozicioniran na granici panela s imenima
             * signala pokazi strelicu za pomicanje panela
             */
            if (event.getX() < signalNames.getPanelWidth() - 2 && 
                    event.getX() >= signalNames.getPanelWidth() - 5)
            {
                signalNames.setIsArrowVisible(true, event.getY());
            }
            else
            {
                signalNames.setIsArrowVisible(false, 0);
            }
            signalNames.repaint();
        } 

        /**
         * Mijenja sirinu panela s imenima signala
         */
        public void mouseDragged(MouseEvent event)  
        {
            if (event.getX() < 2 || event.getX() >= 450)
            {
                return;
            }
            signalNames.setPanelWidth(event.getX());
            signalNames.repaint();
            cp.doLayout();
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
            boolean isClicked;
            
            int value = event.getY() + verticalScrollbar.getValue();
            int index = 0;

            /* pronalazi se index signala kojeg treba oznaciti */
            for (; index < results.getSignalNames().length; index++)
            {
                if (value <= signalNames.getSpringStartPoints()[index])
                {
                    break;
                }
            }
            index--;
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
            
            /* provjerava je li kliknut plusic na bit-vektoru */
            if (results.getExpandedSignalNames().containsKey(index) &&
                    (event.getX() >= 0 && event.getX() <= 15) && 
                    (value >= signalNames.getSpringStartPoints()[index] + 10 &&
                     value <= index * signalNames.getSpringStartPoints()[index] + 30))
            {
                if (!results.getExpandedSignalNames().get(index))
                {
                    results.getExpandedSignalNames().put(index, true);
                    signalNames.setSpringStartPoints(results.getSignalValues());
                }
                else
                {
                    results.getExpandedSignalNames().put(index, false);
                    signalNames.setSpringStartPoints(results.getSignalValues());
                }
            }
                
            signalNames.repaint();
            waves.repaint();

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
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
                if (results.getSignalValues()[i][0].length() > 1 && 
                        results.getExpandedSignalNames().get(i))
                {
                    String signalName = results.getSignalNames()[i];
                    char startPoint = signalName.charAt(signalName.length() - 4);
                    if (((signalName.substring(2, signalName.length() - 5) + 
                                    "[" + startPoint + "]").toLowerCase()).equals(input.toLowerCase()))
                    {
                        isFound = true;
                        index = i;
                        break;
                    }
                }
                /* ako usporeduje s vektorima, ali vektor nije ekspandiran */
                else if (results.getSignalValues()[i][0].length() > 1 && 
                        !results.getExpandedSignalNames().get(i))
                {
                     if ((results.getSignalNames()[i].substring(2, results.getSignalNames()[i].length()).
                                 toLowerCase()).equals(input.toLowerCase()))
                     {
                         isFound = true;
                         index = i;
                         break;
                     }
                }
                /* inace obican signal */
                else
                {
                    if (results.getSignalNames()[i].toLowerCase().equals(input.toLowerCase()))
                    {
                        isFound = true;
                        index = i;
                        break;
                    }
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
            }
            else
            {
                search.setText("Not found");
            }
            signalNames.repaint();
            waves.repaint();

            /* vraca fokus na kontejner */
            cp.requestFocusInWindow();
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


    /**
     * Key listener
     */
    private KeyListener keyListener = new KeyListener()
    {
        public void keyTyped(KeyEvent event) 
        {
            char key = event.getKeyChar();
            switch (key)
            {
                case '+' : 
                    zoomInTwoButton.doClick();
                    break;
                case '-' :
                    zoomOutTwoButton.doClick();
                    break;
                case 's' :
                    verticalScrollbar.setValue(verticalScrollbar.getValue() + 5);
                    break;
                case 'w' :
                    verticalScrollbar.setValue(verticalScrollbar.getValue() - 5);
                    break;
                case 'd' :
                    horizontalScrollbar.setValue(horizontalScrollbar.getValue() + 5);
                    break;
                case 'a' :
                    horizontalScrollbar.setValue(horizontalScrollbar.getValue() - 5);
                    break;
                case 'b' :
                    defaultButton.doClick();
                    break;
                case 'k' :
                    if (previousKey == 'l')
                    {
                        rightUp.doClick();
                    }
                    else if (previousKey == 'h')
                    {
                        leftUp.doClick();
                    }
                    break;
                case 'j' :
                    if (previousKey == 'l')
                    {
                        rightDown.doClick();
                    }
                    else if (previousKey == 'h')
                    {
                        leftDown.doClick();
                    }
                    break;
                case '(' :
                    zoomInTenButton.doClick();
                    break;
                case ')' :
                    zoomOutTenButton.doClick();
                    break;
            }
            previousKey = key;                        
        }

        public void keyPressed(KeyEvent event) 
        {
            int key = event.getKeyCode();
            switch (key)
            {
                case KeyEvent.VK_UP : 
                     verticalScrollbar.setValue(verticalScrollbar.getValue() - 5);
                    break;
                case KeyEvent.VK_DOWN : 
                     verticalScrollbar.setValue(verticalScrollbar.getValue() + 5);
                    break;
                case KeyEvent.VK_RIGHT :
                    horizontalScrollbar.setValue(horizontalScrollbar.getValue() + 5);
                    break;
                case KeyEvent.VK_LEFT :
                    horizontalScrollbar.setValue(horizontalScrollbar.getValue() - 5);
                    break;
                case KeyEvent.VK_HOME :
                    verticalScrollbar.setValue(0);
                    break;
                case KeyEvent.VK_END :
                    verticalScrollbar.setValue(waves.getPreferredSize().height);
                    break;
                case KeyEvent.VK_PAGE_UP :
                    verticalScrollbar.setValue(verticalScrollbar.getValue() - signalNames.getPanelHeight());
                    break;
                case KeyEvent.VK_PAGE_DOWN :
                    verticalScrollbar.setValue(verticalScrollbar.getValue() + signalNames.getPanelHeight());
                    break;
            }
        }

        public void keyReleased(KeyEvent event) 
        {
            ;
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
    interval.setText("Interval");
    interval.setEditable(false);

    /* rezultati prikazni stringom prenose se GhdlResults klasi */
    results = new GhdlResults(parser.getResultInString());

    /* GhdlResults parsira string i iznacuje rezultate simulacije */
    results.parseString();
   
    /* stvara se skala */
    scale = new Scale(results);

    /* panel s imenima signala */
    signalNames = new SignalNamesPanel(results);
    signalNames.addMouseListener(mouseClickListener);
    signalNames.addMouseMotionListener(signalNamesListener);
    signalNames.addMouseWheelListener(wheelListener);

    /* panel s valnim oblicima */
    waves = new WaveDrawBoard(results, scale, signalNames.getSignalNameSpringHeight(), 
            signalNames.getSpringStartPoints());
    waves.addMouseMotionListener(mouseListener);
    waves.addMouseListener(mouseClickListener);
    waves.addMouseWheelListener(wheelListener);

    /* panel u kojem klizi kursor */
    cursorPanel = new CursorPanel(scale.getScaleEndPointInPixels(), 
            waves.getHorizontalOffset(), scale.getMeasureUnitName());
    cursorPanel.addMouseMotionListener(mouseListener);


    /* svi scrollbarovi sadrzani u appletu */
    horizontalScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, 
            scale.getScaleEndPointInPixels()); 
    horizontalScrollbar.addAdjustmentListener(horizontalScrollListener);
    verticalScrollbar = new JScrollBar(SwingConstants.VERTICAL, 0, 0, 0, 
            waves.getPreferredSize().height); 
    verticalScrollbar.addAdjustmentListener(verticalScrollListener);
    signalNamesScrollbar = new JScrollBar(SwingConstants.HORIZONTAL, 0, 0, 0, 
            signalNames.getPreferredSize().width);
    signalNamesScrollbar.addAdjustmentListener(signalNamesScrollListener);



    /* 
     * Popup prozor koji ce izletjeti na zahtjev promjene poretka signala.
     * Popup prozor ce sadrzavati listu imena signala i primjerene buttone
     */
    rightUp.addActionListener(navigateListener);
    rightDown.addActionListener(navigateListener);
    leftUp.addActionListener(navigateListener);
    leftDown.addActionListener(navigateListener);

    rightUp.setToolTipText("Move to next right positive edge");
    rightDown.setToolTipText("Move to next right negative edge");
    leftUp.setToolTipText("Move to next left positive edge");
    leftDown.setToolTipText("Move to next left negative edge");

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(new Color(141, 176, 221));
    buttonPanel.setLayout(new BoxLayout (buttonPanel, BoxLayout.LINE_AXIS));
    buttonPanel.add(rightUp);
    buttonPanel.add(rightDown);
    buttonPanel.add(leftUp);
    buttonPanel.add(leftDown);
    popup.add(buttonPanel);
    /* kraj popup prozora */    


    /* toolbar */
    JPanel toolbar = new JPanel();
    toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
    toolbar.setBackground(new Color(141, 176, 221));
    
    zoomInTwoButton.addActionListener(zoomInTwoListener);
    zoomOutTwoButton.addActionListener(zoomOutTwoListener);
    zoomInTenButton.addActionListener(zoomInTenListener);
    zoomOutTenButton.addActionListener(zoomOutTenListener);
    defaultButton.addActionListener(defaultOrderListener);
    upButton.addActionListener(upListener);
    downButton.addActionListener(downListener);
    navigateSignals.addActionListener(showNavigation);
    helpButton.addActionListener(showHelp);
    
    zoomInTwoButton.setToolTipText("Zoom in by two");
    zoomOutTwoButton.setToolTipText("Zoom out by two");
    zoomInTenButton.setToolTipText("Zoom in by ten");
    zoomOutTenButton.setToolTipText("Zoom out by ten");
    defaultButton.setToolTipText("Change to default order");
    upButton.setToolTipText("Move signal up");
    downButton.setToolTipText("Move signal down");
    navigateSignals.setToolTipText("Move to next/previous right/left edge");

    toolbar.add(zoomInTwoButton);
    toolbar.add(zoomOutTwoButton);
    //toolbar.add(zoomInTenButton);
    //toolbar.add(zoomOutTenButton);
    toolbar.add(upButton);
    toolbar.add(downButton);
    toolbar.add(defaultButton);
    toolbar.add(navigateSignals);
    toolbar.add(helpButton);
    /* kraj toolbara */
    
    /* popup help */
    popupHelp.setPreferredSize(new Dimension(800, 600));
    popupHelp.add(helpPanel);
    
    /* postavljanje komponenti na applet */
    cp = getContentPane();
    cp.setFocusable(true);
    cp.addKeyListener(keyListener);
    cp.setLayout(new WaveLayoutManager());
    cp.setBackground(new Color(141, 176, 221));
    cp.add(toolbar, "toolbar");
    cp.add(textField, "textField");
    cp.add(cursorPanel, "cursorPanel");
    cp.add(search, "search");
    cp.add(interval, "interval");
    cp.add(signalNames, "signalNames");
    cp.add(waves, "waves");
    cp.add(scale, "scale");
    cp.add(verticalScrollbar, "verticalScrollbar");
    cp.add(horizontalScrollbar, "horizontalScrollbar");
    cp.add(signalNamesScrollbar, "signalNamesScrollbar");
  }


  public static void main(String[] args) 
  {
      Console.run(new WaveApplet(), 1024, 1000);
  }
} 
