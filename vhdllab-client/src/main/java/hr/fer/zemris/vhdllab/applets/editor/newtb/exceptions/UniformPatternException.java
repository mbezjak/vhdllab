package hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions;

import java.awt.Color;

import javax.swing.JTextField;

/**
 * 
 * @author Ivan Cesar
 *
 */
public class UniformPatternException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private JTextField source;

	public UniformPatternException() {
		super();
	}

	public UniformPatternException(String arg0) {
		super(arg0);
	}
	
	public UniformPatternException(String arg0, JTextField source)
	{
		super(arg0);
		this.source = source;
		source.setBackground(new Color(255,200,200));
	}
	
	public JTextField getSource()
	{
		return source;
	}

}
