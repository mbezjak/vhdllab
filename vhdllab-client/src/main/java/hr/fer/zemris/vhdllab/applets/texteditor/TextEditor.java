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
package hr.fer.zemris.vhdllab.applets.texteditor;

import hr.fer.zemris.vhdllab.applets.texteditor.misc.KeywordHighlighterTextPane;

import hr.fer.zemris.vhdllab.applets.texteditor.misc.LineNumbers;
import hr.fer.zemris.vhdllab.applets.texteditor.misc.ModificationListener;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditor;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Highlighter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.springframework.richclient.application.Application;

// Pogledaj: http://download.oracle.com/javase/tutorial/uiswing/components/generaltext.html#undo
// http://spring-rich-c.sourceforge.net/1.1.0/apidocs/org/springframework/binding/value/CommitTrigger.html

public class TextEditor extends AbstractEditor implements CaretListener, ModificationListener {

    private CustomJTextPane textPane;
    //private CommitTrigger commitTrigger;

    private Object highlighted;

    @Override
    protected JComponent doInitWithoutData() {
        textPane = new CustomJTextPane(this);
        wrapInScrollPane = false;
        
        JScrollPane jsp = new JScrollPane(textPane);
        LineNumbers.createInstance(textPane, jsp, 30);
        
        Document document = textPane.getDocument();

        if (document instanceof AbstractDocument) {
            ((AbstractDocument) document).setDocumentFilter(new DocumentFilter() {

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    if (text != null && (text.length() - length) > 10 && !text.equals(CustomJTextPane.getClipboardText())) {
                        informPastingIsDisabled();
                    } else {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }

                private void informPastingIsDisabled() {
                    JFrame frame = Application.instance().getActiveWindow().getControl();
                    JOptionPane.showMessageDialog(frame, "Pasting text from outside of vhdllab is disabled!", "Paste text",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            });
        }

        textPane.addCaretListener(this);
        // commitTrigger = new CommitTrigger();
        // TextComponentPopup.attachPopup(textPane, commitTrigger);

        return jsp;
    }

    @Override
    protected void doInitWithData(File f) {
        textPane.setText(f.getData());
        textPane.resetUndoManager();
        // commitTrigger.commit();
    }

    @Override
    protected String getData() {
        return textPane.getText();
    }

    @Override
    protected void doDispose() {
    }

    @Override
    public void setEditable(boolean flag) {
        textPane.setEditable(flag);
    }

    @Override
    public void highlightLine(int line) {
        int caret = textPane.getCaretPosition();
        Highlighter h = textPane.getHighlighter();
        h.removeAllHighlights();
        String content = textPane.getText();
        textPane.setCaretPosition(caret);

        int pos = 0;
        line--;
        while (line > 0) {
            pos = content.indexOf('\n', pos) + 1;
            line--;
        }
        int last = content.indexOf('\n', pos) + 1;
        if (last == 0) {
            last = content.length();
        }
        try {
            highlighted = h.addHighlight(pos, last, new DefaultHighlighter.DefaultHighlightPainter(new Color(180, 210,
                    238)));
        } catch (BadLocationException e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            JOptionPane.showMessageDialog(null, sw.toString());
        }
    }

    //
    // ModificationListener implementation
    //
    @Override
    public void contentModified() {
    	setModified(true);
    }

    //
    // CaretListener implementation
    //

    @Override
    public void caretUpdate(CaretEvent e) {
        if (highlighted != null) {
            textPane.getHighlighter().removeHighlight(highlighted);
            highlighted = null;
        }
    }

    /**
     * Custom razred koji omogucava presretanje copy&paste operacija.
     * Interno, u globalnu se varijablu pamti sve sto je korisnik
     * kopirao/izrezao iz uredivaca tako da se pastanje toga dozvoljava
     * ali pastanje bilo cega drugoga veceg od 10 znakova ne.
     * Dodatno, font se postavlja na monospaced sto je uobicajeno
     * za uredivace koda.
     */
    private static class CustomJTextPane extends KeywordHighlighterTextPane {

		private static final long serialVersionUID = 1L;

		private static String clipboardText;
		private ModificationListener modificationListener;
		protected UndoManager undoManager;
		protected UndoAction undoAction;
		protected RedoAction redoAction;
		
		public CustomJTextPane(ModificationListener modificationListener) {
			this.modificationListener = modificationListener;
			Font font = this.getFont();
			int fontSize = font.getSize();
			Font newFont = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
			this.setFont(newFont);
			
			undoManager = new UndoManager();
			undoAction = new UndoAction();
			redoAction = new RedoAction();
			
			InputMap inputMap = getInputMap();
			KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
			inputMap.put(undoKeyStroke, undoAction);
			KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
			inputMap.put(redoKeyStroke, redoAction);
			
			getDocument().addUndoableEditListener(new UndoableEditListener() {
				@Override
				public void undoableEditHappened(UndoableEditEvent e) {
					if(analysisInProgress) return;
					undoManager.addEdit(e.getEdit());
					undoAction.updateUndoState();
					redoAction.updateRedoState();
				}
			});
			getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					if(!analysisInProgress && CustomJTextPane.this.modificationListener!=null) {
						CustomJTextPane.this.modificationListener.contentModified();
					}
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					if(!analysisInProgress && CustomJTextPane.this.modificationListener!=null) {
						CustomJTextPane.this.modificationListener.contentModified();
					}
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					if(!analysisInProgress && CustomJTextPane.this.modificationListener!=null) {
						CustomJTextPane.this.modificationListener.contentModified();
					}
				}
			});
		}
		
		@Override
		public void copy() {
			setClipboardText(this.getSelectedText());
			super.copy();
		}
		
		@Override
		public void cut() {
			setClipboardText(this.getSelectedText());
			super.cut();
		}

		public void resetUndoManager() {
			undoManager.discardAllEdits();
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
		
		public static synchronized String getClipboardText() {
			return clipboardText;
		}
		
		public static synchronized void setClipboardText(String clipboardText) {
			CustomJTextPane.clipboardText = clipboardText;
		}
		
	    class UndoAction extends AbstractAction {
	    	
			private static final long serialVersionUID = 1L;

			public UndoAction() {
	            super("Undo");
	            setEnabled(false);
	        }

	        public void actionPerformed(ActionEvent e) {
	            try {
	                undoManager.undo();
	            } catch (CannotUndoException ex) {
	                System.out.println("Unable to undo: " + ex);
	                ex.printStackTrace();
	            }
	            updateUndoState();
	            redoAction.updateRedoState();
	        }

	        protected void updateUndoState() {
	            if (undoManager.canUndo()) {
	                setEnabled(true);
	                putValue(Action.NAME, undoManager.getUndoPresentationName());
	            } else {
	                setEnabled(false);
	                putValue(Action.NAME, "Undo");
	            }
	        }
	    }

	    class RedoAction extends AbstractAction {
	    	
			private static final long serialVersionUID = 1L;
			
	        public RedoAction() {
	            super("Redo");
	            setEnabled(false);
	        }

	        public void actionPerformed(ActionEvent e) {
	            try {
	                undoManager.redo();
	            } catch (CannotRedoException ex) {
	                System.out.println("Unable to redo: " + ex);
	                ex.printStackTrace();
	            }
	            updateRedoState();
	            undoAction.updateUndoState();
	        }

	        protected void updateRedoState() {
	            if (undoManager.canRedo()) {
	                setEnabled(true);
	                putValue(Action.NAME, undoManager.getRedoPresentationName());
	            } else {
	                setEnabled(false);
	                putValue(Action.NAME, "Redo");
	            }
	        }
	    }
    }
}
