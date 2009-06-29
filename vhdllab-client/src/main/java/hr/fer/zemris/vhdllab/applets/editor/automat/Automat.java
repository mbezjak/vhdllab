package hr.fer.zemris.vhdllab.applets.editor.automat;

import hr.fer.zemris.vhdllab.applets.editor.automaton.LanguageConstants;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 * 
 * @author ddelac
 * 
 */
public class Automat extends AbstractEditor {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2093412659859056334L;

    /**
     * AutoDrawer je JPanel na koji se crta automat
     */
    protected AutoDrawer adrw = null;

    private ResourceBundle bundle = null;

    public Automat() {
        super();
    }

    /**
     * Metoda koja kreira GUI automata uz nekoliko predpostavki. Poziva se
     * metoda AutoDrawer.setMinXY te se pri tom eksperimentalno zakljucuje da se
     * velicina umanjuje za 3 sto nemora biti tako.
     * 
     * 
     */
    private JComponent createGUI() {

        adrw = new AutoDrawer(this);

        if (bundle != null)
            adrw.setResourceBundle(bundle);

        Icon ic = new ImageIcon(getClass().getResource("AddMode1.png"));
        final JToggleButton dodajNoviSignal = new JToggleButton(ic);
        dodajNoviSignal.setActionCommand("Dodaj stanje");
        dodajNoviSignal.setToolTipText(bundle
                .getString(LanguageConstants.BUTTON_ADDSTATE));
        ic = new ImageIcon(getClass().getResource("AddMode2.png"));
        final JToggleButton dodajNoviPrijelaz = new JToggleButton(ic);
        dodajNoviPrijelaz.setActionCommand("Dodaj prijelaz");
        dodajNoviPrijelaz.setToolTipText(bundle
                .getString(LanguageConstants.BUTTON_ADDTRANSITION));
        ic = new ImageIcon(getClass().getResource("DeleteMode.png"));
        final JToggleButton brisi = new JToggleButton(ic);
        brisi.setActionCommand("Brisi");
        brisi.setToolTipText(bundle.getString(LanguageConstants.BUTTON_DELETE));
        ic = new ImageIcon(getClass().getResource("EditMode.png"));
        final JToggleButton normal = new JToggleButton(ic);
        normal.setActionCommand("Normal");
        normal
                .setToolTipText(bundle
                        .getString(LanguageConstants.BUTTON_NORMAL));
        ic = new ImageIcon(getClass().getResource("StartStateMode.png"));
        final JToggleButton pocStanje = new JToggleButton(ic);
        pocStanje.setActionCommand("pocStanje");
        pocStanje.setToolTipText(bundle
                .getString(LanguageConstants.BUTTON_SETINITIAL));
        final JButton podatci = new JButton("Podatci o automatu");
        podatci.setToolTipText(bundle.getString(LanguageConstants.BUTTON_DATA));

        normal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Normal"))
                    adrw.setStanjeRada(1);
            }

        });

        dodajNoviSignal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Dodaj stanje"))
                    adrw.setStanjeRada(2);
            }

        });

        dodajNoviPrijelaz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Dodaj prijelaz"))
                    adrw.setStanjeRada(3);
            }

        });

        brisi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Brisi"))
                    adrw.setStanjeRada(5);
            }
        });

        pocStanje.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("pocStanje"))
                    adrw.setStanjeRada(6);
            }
        });

        podatci.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                adrw.dataChange();
            }

        });

        final JToolBar tulbar = new JToolBar();
        final ButtonGroup grupa = new ButtonGroup();
        grupa.add(normal);
        grupa.add(dodajNoviSignal);
        grupa.add(dodajNoviPrijelaz);
        grupa.add(brisi);
        grupa.add(pocStanje);
        grupa.setSelected(normal.getModel(), true);
        tulbar.add(normal);
        tulbar.add(dodajNoviSignal);
        tulbar.add(dodajNoviPrijelaz);
        tulbar.add(brisi);
        tulbar.add(pocStanje);
        tulbar.add(podatci);

        JScrollPane p = new JScrollPane(adrw);
        JPanel control = new JPanel(new BorderLayout());
        control.add(p, BorderLayout.CENTER);
        control.add(tulbar, BorderLayout.NORTH);

        // predpostavke za -3:
        // adrw.setMinXY(Automat.this.getWidth() - 3, Automat.this.getHeight()
        // - tulbar.getHeight() - 3);
        /*
         * this.addComponentListener(new ComponentListener() {
         * 
         * public void componentResized(ComponentEvent arg0) {
         * adrw.setMinXY(Automat.this.getWidth() - 3, Automat.this .getHeight()
         * - tulbar.getHeight() - 3); }
         * 
         * public void componentMoved(ComponentEvent arg0) {
         * 
         * }
         * 
         * public void componentShown(ComponentEvent arg0) { }
         * 
         * public void componentHidden(ComponentEvent arg0) { }
         * 
         * });
         */

        tulbar.setFocusable(true);
        tulbar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JComponent source = (JComponent) e.getSource();
                source.requestFocusInWindow();
            }
        });
        adrw.setFocusable(true);
        adrw.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JComponent source = (JComponent) e.getSource();
                source.requestFocusInWindow();
            }
        });
        control.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "escape_key_action");
        control.getActionMap().put("escape_key_action", new AbstractAction() {
            private static final long serialVersionUID = 1844240025875439799L;

            @Override
            public void actionPerformed(ActionEvent e) {
                adrw.setStanjeRada(1);
                normal.setSelected(true);
                dodajNoviPrijelaz.setSelected(false);
                dodajNoviSignal.setSelected(false);
                brisi.setSelected(false);
                pocStanje.setSelected(false);
                podatci.setSelected(false);
            }
        });
        
        return control;
    }

    // *************************************************************************

    @Override
    protected void doInitWithData(File f) {
        if (adrw != null)
            adrw.setData(f.getData());
    }

    @Override
    protected void doDispose() {
    }

    @Override
    public String getData() {
        return adrw.getData();
    }

    @Override
    protected JComponent doInitWithoutData() {
        bundle = ResourceBundle
                .getBundle("Client_Automat_ApplicationResources");
        return createGUI();
    }

}
