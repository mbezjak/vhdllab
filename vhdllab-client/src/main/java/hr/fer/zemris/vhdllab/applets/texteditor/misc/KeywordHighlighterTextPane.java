package hr.fer.zemris.vhdllab.applets.texteditor.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.JTextPane;
import javax.swing.LookAndFeel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TextAction;

/**
 * <p>Razred koji predstavlja editor vhdl-a koji zna bojati kljucne rijeci,
 * tipove podataka i slicno. Podrzano je i uvlacenje na pritisak tipke
 * ENTER.</p>
 * 
 * <p>Prilikom rada s ovom komponentom nije dobro koristiti DocumentListener
 * za signalizaciju je li dokument mijenjan ili nije. Naime, bojanje kljucnih
 * rijeci se u tom smislu dojavljuje takoder kao promjena. Umjesto toga, komponenta
 * omogucava da se preko konstruktora preda referenca na objekt koji implementira
 * sucelje {@link ModificationListener} putem kojeg ce se dojavljivati samo promjene
 * u tekstu (a ne i u stilu dokumenta). To se sucelje moze koristiti za hvatanje
 * signala da je dokument izmijenjen.</p>
 * 
 * <p>Bojanje je izvedeno asinkrono, u smislu da tako dugo dok korisnik nesto tipka
 * razumnom brzinom, bojanje ce biti odgodeno. Konkretna analiza teksta zapocet ce tek
 * kada korisnik napravi pauzu.</p>
 * 
 * @author marcupic
 *
 */
public class KeywordHighlighterTextPane extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean dirty;
	private long modifTime;
	private Timer timer;
	protected boolean analysisInProgress;
	private Map<String, Style> styleMap;

	public KeywordHighlighterTextPane() {
		Style plainStyle = this.addStyle("obicni", null);
		StyleConstants.setFontFamily(plainStyle, Font.MONOSPACED);
		StyleConstants.setFontSize(plainStyle, 12);
		Style kwStyle = this.addStyle("kwstil", null);
		StyleConstants.setFontFamily(kwStyle, Font.MONOSPACED);
		StyleConstants.setFontSize(kwStyle, 12);
		StyleConstants.setBold(kwStyle, true);
		StyleConstants.setForeground(kwStyle, Color.BLUE);
		Style operStyle = this.addStyle("operstil", null);
		StyleConstants.setFontFamily(operStyle, Font.MONOSPACED);
		StyleConstants.setFontSize(operStyle, 12);
		StyleConstants.setBold(operStyle, true);
		StyleConstants.setForeground(operStyle, new Color(200,0,0));
		Style typeStyle = this.addStyle("typestil", null);
		StyleConstants.setFontFamily(typeStyle, Font.MONOSPACED);
		StyleConstants.setFontSize(typeStyle, 12);
		StyleConstants.setBold(typeStyle, true);
		StyleConstants.setForeground(typeStyle, new Color(200,0,200));
		Style komentarStyle = this.addStyle("komentarstil", null);
		StyleConstants.setFontFamily(komentarStyle, Font.MONOSPACED);
		StyleConstants.setFontSize(komentarStyle, 12);
		StyleConstants.setForeground(komentarStyle, new Color(0,200,0));
		
		styleMap = new HashMap<String, Style>(32);
		styleMap.put("ENTITY", kwStyle);
		styleMap.put("IS", kwStyle);
		styleMap.put("PORT", kwStyle);
		styleMap.put("ARCHITECTURE", kwStyle);
		styleMap.put("BEGIN", kwStyle);
		styleMap.put("END", kwStyle);
		styleMap.put("CASE", kwStyle);
		styleMap.put("COMPONENT", kwStyle);
		styleMap.put("WHEN", kwStyle);
		styleMap.put("OTHERS", kwStyle);
		styleMap.put("AND", operStyle);
		styleMap.put("OR", operStyle);
		styleMap.put("XOR", operStyle);
		styleMap.put("NOT", operStyle);
		styleMap.put("NAND", operStyle);
		styleMap.put("NOR", operStyle);
		styleMap.put("NXOR", operStyle);
		styleMap.put("LIBRARY", kwStyle);
		styleMap.put("USE", kwStyle);
		styleMap.put("TO", kwStyle);
		styleMap.put("DOWNTO", kwStyle);
		styleMap.put("AFTER", kwStyle);
		styleMap.put("OF", kwStyle);
		styleMap.put("IN", kwStyle);
		styleMap.put("OUT", kwStyle);
		styleMap.put("INOUT", kwStyle);
		styleMap.put("BUFFER", kwStyle);
		styleMap.put("SIGNAL", kwStyle);
		styleMap.put("VARIABLE", kwStyle);
		styleMap.put("MAP", kwStyle);
		styleMap.put("TYPE", kwStyle);
		styleMap.put("GENERIC", kwStyle);
		styleMap.put("STD_LOGIC", typeStyle);
		styleMap.put("STD_LOGIC_VECTOR", typeStyle);
		styleMap.put("BIT", typeStyle);
		
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		timer = new Timer(250, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processTImerEvent();
			}
		});
		this.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(analysisInProgress) return;
				dirty = true;
				modifTime = System.currentTimeMillis();
				if(!timer.isRunning()) {
					timer.start();
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(analysisInProgress) return;
				dirty = true;
				modifTime = System.currentTimeMillis();
				if(!timer.isRunning()) {
					timer.start();
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				if(analysisInProgress) return;
				dirty = true;
				modifTime = System.currentTimeMillis();
				if(!timer.isRunning()) {
					timer.start();
				}
			}
		});
		
		ActionMap actionMap = this.getActionMap();
		actionMap.put(DefaultEditorKit.insertBreakAction, new SmartIndenter());
	}

	protected void processTImerEvent() {
		if(!dirty) {
			timer.stop();
			return;
		}
		if(System.currentTimeMillis()-modifTime<450) return;
		dirty = false;
		timer.stop();
		analysisInProgress=true;
		analiziraj();
		analysisInProgress=false;
	}

	private void analiziraj() {
		StyledDocument doc = this.getStyledDocument();
		int docLen = doc.getLength();
		String s;
		try {
			s = doc.getText(0, docLen);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		char[] chars = s.toCharArray();
		int poc = 0;
		while(poc<chars.length) {
			int st = poc;
			while(poc<chars.length && chars[poc]!='\n') {
				poc++;
			}
			if(poc<chars.length && chars[poc]=='\n') {
				poc++;
			}
			// Sada imam jedan redak:
			int pos = s.indexOf("--", st);
			if(pos==-1) {
				procesiraj(doc, st, poc, chars, s);
			} else if(pos > st) {
				procesiraj(doc, st, pos, chars, s);
				// komentar je od pos do poc
				doc.setCharacterAttributes(pos, poc-pos, doc.getStyle("komentarstil"), true);
			} else {
				// komentar je od st do poc
				doc.setCharacterAttributes(st, poc-st, doc.getStyle("komentarstil"), true);
			}
		}
	}

	private void procesiraj(StyledDocument doc, int poc, int kraj, char[] chars, String text) {
		while(poc<kraj) {
			int cur = poc;
			if(Character.isLetter(chars[cur]) || chars[cur]=='_') {
				cur++;
				while(cur<kraj && (Character.isLetter(chars[cur]) || chars[cur]=='_' || chars[cur]=='.')) {
					cur++;
				}
				String word = text.substring(poc, cur);
				Style stil = styleMap.get(word.toUpperCase());
				if(stil!=null) {
					doc.setCharacterAttributes(poc, cur-poc, stil, true);
				} else {
					doc.setCharacterAttributes(poc, cur-poc, doc.getStyle("obicni"), true);
				}
			} else {
				cur++;
				while(cur<kraj && !Character.isLetter(chars[cur])) {
					cur++;
				}
				doc.setCharacterAttributes(poc, cur-poc, doc.getStyle("obicni"), true);
			}
			poc = cur;
		}
	}
	
	private static class SmartIndenter extends TextAction {

		private static final long serialVersionUID = 1L;

		public SmartIndenter() {
			super(DefaultEditorKit.insertBreakAction);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent textPane = getTextComponent(e);
			if(textPane==null) return;
			if(!textPane.isEditable() || !textPane.isEnabled()) {
				LookAndFeel laf = UIManager.getLookAndFeel(); 
				if(laf!=null) laf.provideErrorFeedback(textPane);
				return;
			}
			try {
				Document doc = textPane.getDocument();
				Element root = doc.getDefaultRootElement();
				int selStart = textPane.getSelectionStart();
				int line = root.getElementIndex(selStart);
				int lineStart = root.getElement(line).getStartOffset();
				int lineEnd = root.getElement(line).getEndOffset();
				int length = lineEnd - lineStart;
				String lineText = doc.getText(lineStart, length);
				int emptyChars = countEmptyChars(lineText);
				if(selStart - lineStart > emptyChars) {
					textPane.replaceSelection("\n"+lineText.substring(0, emptyChars));
				} else {
					textPane.replaceSelection("\n");
				}
			} catch(Exception ex) {
				LookAndFeel laf = UIManager.getLookAndFeel(); 
				if(laf!=null) laf.provideErrorFeedback(textPane);
			}
		}

		private int countEmptyChars(String text) {
			if(text==null || text.isEmpty()) return 0;
			int count = 0;
			for(; count < text.length(); count++) {
				char c = text.charAt(count);
				if(c==' ' || c=='\t') continue;
				break;
			}
			return count;
		}
	}
	
	
}
