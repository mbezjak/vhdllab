package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.EvaluationMethod;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.numbers.Binary;
import hr.fer.zemris.vhdllab.applets.editor.newtb.numbers.Hex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public abstract class VectorPatternPanel extends PatternPanel {

	private static final long serialVersionUID = 1L;
	
	protected String radixChoice = "Decimal";
	protected JPanel upperPanel;
	protected GroupBox radixGB;
	protected ButtonGroup radixButGroup = new ButtonGroup();
	
	private String[] choices = new String[]{"Binary", "Decimal", "Hexadecimal"};
	
	public VectorPatternPanel()
	{
		super();
		upperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		for (String name : choices) {
			JRadioButton b = new JRadioButton(name);
			b.addActionListener(listener);
			upperPanel.add(b);
			radixButGroup.add(b);
			if(name.equals("Decimal"))
				b.setSelected(true);
		}
		radixGB = new GroupBox("Radix",upperPanel);
		this.add(radixGB, BorderLayout.NORTH);
		
	}
	
	private ActionListener listener = new ActionListener() {
        public void actionPerformed (ActionEvent e) {
           radixChoice = e.getActionCommand();
        }
     };
     
     protected int getValue(String x) throws UniformPatternException, NumberFormatException
     {
    	 if(radixChoice.equals("Binary"))
    		 return Binary.parseBinary(x);
    	 if(radixChoice.equals("Decimal"))
    		 return Integer.parseInt(x);
    	 if(radixChoice.equals("Hexadecimal"))
    		 return Hex.parseHex(x);
    	 throw new UniformPatternException("Greska u sustavu!! Provjeriti VectorPatternPanel, choices");
     }
     protected BigInteger getValueBig(String x) throws UniformPatternException, NumberFormatException
     {
    	 if(radixChoice.equals("Binary"))
    		 return new BigInteger(x, 2);
    	 if(radixChoice.equals("Decimal"))
    		 return new BigInteger(x, 10);
    	 if(radixChoice.equals("Hexadecimal"))
    		 return new BigInteger(x, 16);
    	 throw new UniformPatternException("Greska u sustavu!! Provjeriti VectorPatternPanel, choices");
     }
     @Override
     protected KeyListener getKeyListener()
 	{
 		return new KeyListener() {
 		
 			@Override
 			public void keyTyped(KeyEvent arg0) {
 				if(radixChoice.equals("Decimal") && !(arg0.getKeyChar() >= '0' && arg0.getKeyChar()<='9'))
 					arg0.consume();
 				if(radixChoice.equals("Binary") && !(arg0.getKeyChar() >= '0' && arg0.getKeyChar()<='1'))
 					arg0.consume();
 				if(radixChoice.equals("Hexadecimal") && 
 						!(arg0.getKeyChar() >= '0' && arg0.getKeyChar()<='9' 
 							|| arg0.getKeyChar() >= 'a' && arg0.getKeyChar()<='f'
 								|| arg0.getKeyChar() >= 'A' && arg0.getKeyChar()<='F'))
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
     
     @Override
    public void evaluate(JTextField b, EvaluationMethod m) {
    	 if(!b.isOpaque())
    		 b.setOpaque(true);
    	if(m == EvaluationMethod.GetValue)
    	{
    		try
    		{
    			getValue(b.getText());
    			b.setBackground(Color.white);
    		}
    		catch(Exception e)
    		{
    			b.setBackground(defaultBadColor);
    		}
    		finally
    		{
    			b.validate();
    		}
    	}
    	else if(m == EvaluationMethod.GetValueBig)
    	{
    		try
    		{
    			getValueBig(b.getText());
    			b.setBackground(Color.white);
    		}
    		catch(Exception e)
    		{
    			b.setBackground(defaultBadColor);
    		}
    		finally
    		{
    			b.validate();
    		}
    	}
    	else super.evaluate(b, m);
    }

}
