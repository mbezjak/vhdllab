package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.EvaluationMethod;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.CountPattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.math.BigInteger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CountPanel extends VectorPatternPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel left;
	private JPanel right = new JPanel();
	private JPanel control1;
	private JPanel control2;
	private JPanel control3;
	private JPanel control4;
	
	private JTextField ATB = new JTextField(5);
	private JTextField BTB = new JTextField(5);
	private JTextField LenTB = new JTextField(5);
	private JTextField StepTB = new JTextField(5);
	
	private String kojaSlika = "count.png";
	
	private void initLeft()
	{
		control1 = new JPanel(new GridLayout(1,2));
		control1.add(new JLabel("Begin value: "));
		control1.add(ATB);
		
		control2 = new JPanel(new GridLayout(1,2));
		control2.add(new JLabel("End value: "));
		control2.add(BTB);
		
		control3 = new JPanel(new GridLayout(1,2));
		control3.add(new JLabel("N of periods: "));
		control3.add(LenTB);
		
		control4 = new JPanel(new GridLayout(1,2));
		control4.add(new JLabel("Step (x): "));
		control4.add(StepTB);
		
		left = new JPanel(new GridLayout(6,1,0,8));
		left.add(new JLabel(" "));
		left.add(control3);
		left.add(control4);
		left.add(control1);
		left.add(control2);
		left.add(new JLabel(" "));
	}
	
	private void initRight()
	{
		right.removeAll();
		right.setLayout(new BorderLayout());
		JLabel p = new JLabel(new ImageIcon(PulsePanel.class.getResource(kojaSlika)));
		p.setPreferredSize(new Dimension(113,113));
		right.add(p, BorderLayout.CENTER);
		right.repaint();
		right.updateUI();
	}
	
	public CountPanel() {
		super();
		main.setLayout(new GridLayout(1,2));
		initLeft();
		initRight();
		main.add(left);
		main.add(right);
		ATB.addKeyListener(getKeyListener());
		BTB.addKeyListener(getKeyListener());
	}

	@Override
	public String toString() {
		return "Count";
	}

	@Override
	public Pattern getPattern(int cycles, int dim, long periodLength) 
		throws NumberFormatException, UniformSignalChangeException, UniformPatternException {
		
			evaluate(ATB, EvaluationMethod.GetValueBig);
			evaluate(BTB, EvaluationMethod.GetValueBig);
			evaluate(LenTB, EvaluationMethod.ParseLong);
			evaluate(StepTB, EvaluationMethod.ParseBigInt);
		
			return new CountPattern(
					cycles,
					Long.parseLong(LenTB.getText()) * periodLength,
					getValueBig(ATB.getText()),
					getValueBig(BTB.getText()),
					dim,
					new BigInteger(StepTB.getText()),
					StepTB,
					LenTB,
					ATB,
					BTB);
	}

}
