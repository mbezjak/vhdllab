package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.EvaluationMethod;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;

import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class PatternPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JPanel main;
	protected GroupBox paramGB;
	
	protected static Color defaultBadColor = new Color(255,200,200);

	public PatternPanel()
	{
		this.setLayout(new BorderLayout());
		main = new JPanel();
		paramGB = new GroupBox("Pattern parameters", main);
		this.add(paramGB, BorderLayout.CENTER);
	}
	/**
	 * Evaluates TextFields with given EvaulationMethod method
	 * @param b
	 * @param m
	 */
	public void evaluate(JTextField b, EvaluationMethod m)
	{
		if(!b.isOpaque())
			b.setOpaque(true);
		if(m == EvaluationMethod.ParseInt)
		{
			try
			{
				if(Integer.parseInt(b.getText()) < 0)
					throw new Exception();
				b.setBackground(Color.white);
			}
			catch(Exception e)
			{
				b.setBackground(defaultBadColor);
			}
		}
		else if(m == EvaluationMethod.ParseLong)
		{
			try
			{
				if(Long.parseLong(b.getText()) <= 0)
					throw new Exception();
				b.setBackground(Color.white);
			}
			catch(Exception e)
			{
				b.setBackground(defaultBadColor);
			}
		}
		else if(m == EvaluationMethod.ParseBigInt)
		{
			try
			{
				if(new BigInteger(b.getText()).compareTo(BigInteger.ZERO) <= 0)
					throw new Exception();
				b.setBackground(Color.white);
			}
			catch(Exception e)
			{
				b.setBackground(defaultBadColor);
			}
		}
		b.validate();
	}
	protected KeyListener getKeyListener()
	{
		return new KeyListener() {
		
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(!(arg0.getKeyChar() >= '0' && arg0.getKeyChar()<='9'))
					arg0.consume();
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
		};
	}
	/**
	 * Every panel which extends this one must have this method
	 * @param cycles
	 * @param dim
	 * @param periodLength
	 * @throws NumberFormatException
	 * @throws UniformSignalChangeException
	 * @throws UniformPatternException
	 */
	public abstract Pattern getPattern(int cycles, int dim, long periodLength) throws NumberFormatException, UniformSignalChangeException, UniformPatternException;
}
