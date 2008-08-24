package hr.fer.zemris.vhdllab.applets.texteditor;

import java.awt.Color;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledEditorKit;

 

public class EditorScanner extends Thread {
		private boolean stoprequested = false;
		
		public void stopScanner() {
			stoprequested = true;
		}

		public void run () {
			//AbstractDocument doc;
			DefaultStyledDocument doc = new DefaultStyledDocument();
			new StyledEditorKit.ForegroundAction("Red", Color.red);
			
			//doc.setCharacterAttributes(offset, length, s, replace)
			//doc.
		while (true) {
			
			 try {
				EditorScanner.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (stoprequested) break;
		 boolean flag=true;
         int i;
		 String temp = null;
		 //String input = "1 fish 2 fish red fish blue blah #$%#";
		 final String[] vhdlWords = { "abs", "access", "after",
				"alias", "all", "and", "architecture", "array", "assert",
				"attribute", "begin", "block", "body", "buffer", "bus", "case",
				"component", "configuration", "constant", "disconnect", "downto",
				"else", "elsif", "end", "entity", "exit", "file", "for",
				"function", "generate", "generic", "guarded", "if", "impure", "in",
				"inertial", "inout", "is", "label", "library", "linkage",
				"literal", "loop", "map", "mod", "nand", "new", "next", "nor",
				"not", "null", "of", "on", "open", "or", "others", "out",
				"package", "port", "postponed", "procedure", "process", "pure",
				"range", "record", "register", "reject", "rem", "report", "return",
				"rol", "ror", "select", "severity", "shared", "signal", "sla",
				"sll", "sra", "srl", "subtype", "then", "to", "transport", "type",
				"unaffected", "units", "until", "use", "variable", "wait", "when",
				"while", "with", "xnor", "xor" };
//	     Scanner s = new Scanner(text.getText());
//	     
//	     while(s.hasNext()) {
//	    	 temp = s.next();
//	    	 for (i=0; i<vhdlWords.length; i++)	{ 
//	    		 					if ((temp.toString()).equals(vhdlWords[i])){
//	    		 						System.out.println(temp + "- ovo je prepoznato");
//	    		 						flag=false;
//	    		 						break;
//	    		 						} else {flag=true;}
//	    	 
//	    	 }
//	    	 if (flag) {
//	    	     System.out.println(temp +"- ovo nije prepoznato");
//	    	     flag=true;}
//         }
	     
	     
	     
	     }
	     
	     
		}

 }
	
	
