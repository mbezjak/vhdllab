package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.EvaluationMethod;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.PulsePattern;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PulsePanel extends ScalarPatternPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String kojaSlika = getLow();
	
	protected JPanel left;
	protected JPanel mid = new JPanel();
	protected JPanel right;
	
	protected Integer nula = Integer.valueOf(0);
	protected Integer jedan = Integer.valueOf(1);
	
	protected JComboBox initialValue = new JComboBox(new Integer[]{nula, jedan});
	protected JComboBox pulseValue = new JComboBox(new Integer[]{jedan, nula});
	protected JTextField initLenTB = new JTextField(5);
	protected JLabel initLenLab = new JLabel("Initial delay:");
	protected JTextField pulseLenTB = new JTextField(5);
	
	protected ActionListener listener = new ActionListener() {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == pulseValue)
			{
				if(pulseValue.getSelectedItem() == initialValue.getSelectedItem())
					if(pulseValue.getSelectedItem() == nula)
						initialValue.setSelectedItem(jedan);
					else
						initialValue.setSelectedItem(nula);
			}
			else if(e.getSource() == initialValue)
			{
				if(pulseValue.getSelectedItem() == initialValue.getSelectedItem())
					if(pulseValue.getSelectedItem() == nula)
						pulseValue.setSelectedItem(jedan);
					else
						pulseValue.setSelectedItem(nula);
			}
			if(pulseValue.getSelectedItem() == nula)
				kojaSlika = getHigh();
			else
				kojaSlika = getLow();
			initMid();
		}
	
	};
	
	private void initLeft()
	{
		left = new JPanel(new BorderLayout());
		JPanel tempPanel = new JPanel(new GridLayout(4,1));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("Initial value:"));
		tempPanel.add(initialValue);
		JPanel tempPanel2 = new JPanel(new GridLayout(5,1));
		tempPanel2.add(initLenLab);
		tempPanel2.add(initLenTB);
		tempPanel2.add(new JLabel("       "));
		tempPanel2.add(new JLabel("       "));
		tempPanel2.add(new JLabel("       "));
		left.add(tempPanel, BorderLayout.NORTH);
		left.add(tempPanel2, BorderLayout.SOUTH);
	}
	protected String getLow()
	{
		return "pulseLow.png";
	}
	protected String getHigh()
	{
		return "pulseHigh.png";
	}
	protected void initMid()
	{
		mid.removeAll();
		mid.setLayout(new BorderLayout());
		JLabel p = new JLabel(new ImageIcon(PulsePanel.class.getResource(kojaSlika)));
		//p.setPreferredSize(new Dimension(113,113));
		mid.add(p);
		mid.repaint();
		mid.updateUI();
	}
	
	private void initRight()
	{
		right = new JPanel(new BorderLayout());
		JPanel tempPanel = new JPanel(new GridLayout(4,1));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("Pulse value:"));
		tempPanel.add(pulseValue);
		right.add(tempPanel, BorderLayout.NORTH);
		JPanel tempPanel2 = new JPanel(new GridLayout(5,1));
		tempPanel2.add(new JLabel("Pulse length (periods):"));
		tempPanel2.add(pulseLenTB);
		tempPanel2.add(new JLabel("       "));
		tempPanel2.add(new JLabel("       "));
		tempPanel2.add(new JLabel("       "));
		right.add(tempPanel2, BorderLayout.SOUTH);
	}
	
	public PulsePanel() {
		super();
		main.setLayout(new GridLayout(1,3));
		
		pulseValue.addActionListener(listener);
		initialValue.addActionListener(listener);
		
		initLeft();
		initMid();
		initRight();
		main.add(left);
		main.add(mid);
		main.add(right);
		main.setPreferredSize(new Dimension(200,113));
		pulseLenTB.addKeyListener(getKeyListener());
		initLenTB.addKeyListener(getKeyListener());
	}

	@Override
	public String toString() {
		return "Pulse";
	}

	@Override
	protected Pattern getPattern(int cycles, long periodLength) throws NumberFormatException, UniformPatternException {
		
		evaluate(initLenTB, EvaluationMethod.ParseLong);
		evaluate(pulseLenTB, EvaluationMethod.ParseLong);
		
		return new PulsePattern(
				cycles,
				((Integer)initialValue.getSelectedItem()).intValue(),
				Long.parseLong(initLenTB.getText()) * periodLength,
				((Integer)pulseValue.getSelectedItem()).intValue(),
				Long.parseLong(pulseLenTB.getText()) * periodLength
				);
	}

}
