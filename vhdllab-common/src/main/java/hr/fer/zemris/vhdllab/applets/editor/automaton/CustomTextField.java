/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.automaton;

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

	@Override
    protected Document createDefaultModel() {
	      return new Document10();
	}
	
	@Override
	public String getText() {
		String str=super.getText();
		while(str.length()<size)str+=(useAste?"-":"0");
		return str;	
	}
		
	protected class Document10 extends PlainDocument {
		
	      /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int curSize=0;
		@Override
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
