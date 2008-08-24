package hr.fer.zemris.vhdllab.applets.editor.automat.entityTable;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class NumberBox extends JTextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -636501698092103448L;
	
	public NumberBox(String tekst) {
		        super(tekst);
		    }
		


	protected Document createDefaultModel() {
	      return new DocumentNumber();
	}
	
	@Override
	public String getText() {
		String s=super.getText();
		if(s.equals(""))s="0";
		return s;
	}
	
	@Override
	public String toString() {
		return getText();
	}
	private class DocumentNumber extends PlainDocument {
		
	      /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void insertString(int offs, String str, AttributeSet a) 
		          throws BadLocationException {
	          if (str == null) {
	        	  return;
			  }
			  char[] cha = str.toCharArray();
			  if(!String.valueOf(cha[str.length()-1]).matches("[0123456789]")) str="";
			  
			  super.insertString(offs, str, a);
		}
	}

}
