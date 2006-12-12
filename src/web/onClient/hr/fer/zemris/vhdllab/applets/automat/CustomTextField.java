package hr.fer.zemris.vhdllab.applets.automat;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class CustomTextField extends JTextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6207163406381169565L;
	/**
	 * 
	 */
	public int size=0;
	public boolean useAste=true;
	
	public CustomTextField(String tekst,int size1) {
		  super(tekst);
		  size=size1;
	      useAste=true;
	}
		
	public CustomTextField(String string, int size1, boolean b) {
		super(string);
        size=size1;
        useAste=b;
	}

	protected Document createDefaultModel() {
	      return new Document10();
	}
	
	@Override
	public String getText() {
		String str=super.getText();
		while(str.length()<size)str+=(useAste?"-":"0");
		return str;	
	}
		
	private class Document10 extends PlainDocument {
		
	      /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int curSize=0;
		public void insertString(int offs, String str, AttributeSet a) 
		          throws BadLocationException {
	          if (str == null) {
	        	  return;
			  }
			  char[] cha = str.toCharArray();
			  curSize=getLength();
			  if(useAste){
				  if(curSize==size||!String.valueOf(cha[str.length()-1]).matches("[10-]")) str="";
				  }
			  else if(curSize==size||!String.valueOf(cha[str.length()-1]).matches("[10]")) str="";
			  
			  super.insertString(offs, str, a);
		}
	}

}
