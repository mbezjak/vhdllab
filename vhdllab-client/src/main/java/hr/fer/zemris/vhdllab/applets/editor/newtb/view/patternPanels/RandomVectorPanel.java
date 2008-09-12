package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.EvaluationMethod;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.RandomVectorPattern;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RandomVectorPanel extends VectorPatternPanel {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 7196995412968869715L;
	
	private static final long serialVersionUID = 1L;

	protected JPanel left;
	protected JPanel mid = new JPanel();
	protected JPanel right;
		
	protected JTextField pulseLenTB = new JTextField(5);
	
	

	private void initLeft()
	{
		left = new JPanel(new BorderLayout());
		//JPanel tempPanel = new JPanel(new GridLayout(3,1));
		//tempPanel.add(new JLabel("       "));
		//tempPanel.add(new JLabel("Max gen number:"));
		//tempPanel.add(maxLenTB);
		//left.add(tempPanel, BorderLayout.NORTH);
	}
	
	private void initMid()
	{
		mid.removeAll();
		mid.setLayout(new BorderLayout());
		JLabel p = new JLabel(new ImageIcon(PulsePanel.class.getResource("randomVector.png")));
		//p.setPreferredSize(new Dimension(113,113));
		mid.add(p);
		mid.repaint();
		mid.updateUI();
	}
	
	private void initRight()
	{
		right = new JPanel(new BorderLayout());
		JPanel tempPanel = new JPanel(new GridLayout(5,1));
		tempPanel.add(new JLabel("N of periods:"));
		tempPanel.add(pulseLenTB);
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("       "));
		right.add(tempPanel, BorderLayout.SOUTH);
	}
	
	public RandomVectorPanel( ) {
		super();
		this.radixGB.setVisible(false);
		main.setLayout(new GridLayout(1,2));
		initLeft();
		initMid();
		initRight();
		main.add(mid);
		main.add(right);
		this.updateUI();
	}
	
	@Override
	public String toString() {
		return "Random ";
	}

	@Override
	public Pattern getPattern(int cycles, int dim, long periodLength)
			throws NumberFormatException, UniformSignalChangeException,
			UniformPatternException {
		
		evaluate(pulseLenTB, EvaluationMethod.ParseLong);
		
		return new RandomVectorPattern(
				cycles, 
				Long.parseLong(this.pulseLenTB.getText()) * periodLength,
				(short) dim);
	}

}
