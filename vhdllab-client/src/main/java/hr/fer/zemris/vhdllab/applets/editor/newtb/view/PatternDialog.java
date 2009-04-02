package hr.fer.zemris.vhdllab.applets.editor.newtb.view;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.EvaluationMethod;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.help.HelpManager;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.AlternatePanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.CountPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.LShiftPatternPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.PatternPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.PulsePanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.RShiftPatternPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.RandomPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.RandomVectorPanel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.TogglePanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Main pattern dialog class. Used by calling static method PatternDialog.getResultVector/Scalar
 * @author Ivan Cesar
 *
 */
public class PatternDialog extends JDialog implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JButton okButton;
    private JButton helpButton;
    private JButton cancelButton;
    private PatternPanel panel;
    private JPanel upperPanel;
    private JPanel downPanel;
    private JPanel card;
    private JComboBox combo;
    private boolean isVectorSignal;
    private JTextField cycles = new JTextField("1", 3);
    private Pattern value;
    private int dimenzija;
    private long periodLength;
    
    PatternPanel[] paneli = new PatternPanel[]{
            new AlternatePanel(), new CountPanel(), new PulsePanel(), new RandomPanel(),new TogglePanel(), 
            new RShiftPatternPanel(), new LShiftPatternPanel(), new RandomVectorPanel() };

    private void initComboBox() {
        
        if (isVectorSignal)
            combo = new JComboBox(new Object[] { paneli[0], paneli[1], paneli[5], paneli[6], paneli[7] });
        else
            combo = new JComboBox(new Object[] { paneli[4], paneli[3], paneli[2] });
        
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePanel((PatternPanel)combo.getSelectedItem());
            }
        });
    }
    /**
     * After each combo box change, updates layout - shows corresponding panel.
     * @param p
     */
    protected void updatePanel(PatternPanel p)
    {   
        CardLayout cl = (CardLayout)(card.getLayout());
        cl.show(card, p.toString());
        panel = p;
        combo.revalidate();
    }
    /**
     * Panel which contains comboBox and cycles input field.
     */
    private void initUpperPanel() {
        initComboBox();
        upperPanel = new JPanel(new FlowLayout());//new GridLayout(1,4));
        upperPanel.add(new JLabel("Pattern type: "));
        upperPanel.add(combo);
        upperPanel.add(new JLabel("Cycles: "));
        upperPanel.add(cycles);
    }
    /**
     * Panel which contains OK, cancel and help buttons.
     */
    private void initDownPanel() {
        downPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        okButton = new JButton("OK");
        okButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        
        
        helpButton = new JButton("Help");
        helpButton.addActionListener(this);

        downPanel.add(helpButton);
        downPanel.add(new JLabel("   "));
        downPanel.add(cancelButton);
        downPanel.add(okButton);
    }
    
    private PatternDialog(int dimenzija, long period, boolean isVectorSignal) {
        setTitle("Pattern dialog");
        setModal(true);
        this.dimenzija = dimenzija;
        this.isVectorSignal = isVectorSignal;
        this.periodLength = period;

        initUpperPanel();
        initDownPanel();
        
        card = new JPanel(new CardLayout());
        for (PatternPanel p : paneli) {
            card.add(p, p.toString());
        }
        panel = (PatternPanel)combo.getSelectedItem();
        updatePanel(panel);
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(upperPanel, BorderLayout.NORTH);
        this.getContentPane().add(card); 
        this.getContentPane().add(downPanel, BorderLayout.SOUTH);
        this.validate();
        
        this.pack();
        this.setResizable(false);
    }
    /**
     * Called when used this dialog externally
     * @param signalDimension Dimension of vector
     * @param periodLength Length of period
     */
    public static Pattern getResultVector(int signalDimension, long periodLength) {
        PatternDialog d = new PatternDialog(signalDimension, periodLength, true);
        d.setVisible(true);
        return d.value;
    }
    /**
     * Same as getResultVector(), just without dimension becaouse it is one.
     * @param periodLength Length of period
     */
    public static Pattern getResultScalar(long periodLength) {
        PatternDialog d = new PatternDialog(1, periodLength, false);
        d.setVisible(true);
        return d.value;
    }
    private void sendErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE); 
        
    }
    @Override
    /**
     * Performs actions regarding which button was pressed.
     */
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == okButton){
                panel.evaluate(cycles, EvaluationMethod.ParseInt);
                value = panel.getPattern(Integer.parseInt(cycles.getText()),
                        dimenzija, periodLength);
            }
            else if (e.getSource() == cancelButton)
                value = null;
            else if( e.getSource() == helpButton)
            {
                HelpManager.openHelpDialog(HelpManager.getHelpCode(panel.getClass()));
                return;
            }
            dispose();
            
        } catch (NumberFormatException e1) {
            sendErrorMessage(Messages.wrongNumFormat + "\n " + e1.getMessage());
        } catch (UniformSignalChangeException e1) {
            sendErrorMessage("Error message: " + e1.getMessage());
        } catch (UniformPatternException e1) {
            sendErrorMessage("Error message: " + e1.getMessage());
        }
    }

}
