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
import java.awt.Font;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Highlighter;

import org.springframework.binding.value.CommitTrigger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.text.TextComponentPopup;

public class TextEditor extends AbstractEditor implements CaretListener, ModificationListener {

    private CustomJTextPane textPane;
    private CommitTrigger commitTrigger;

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
        commitTrigger = new CommitTrigger();
        TextComponentPopup.attachPopup(textPane, commitTrigger);

        return jsp;
    }

    @Override
    protected void doInitWithData(File f) {
        textPane.setText(f.getData());
        commitTrigger.commit();
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
		
		public CustomJTextPane(ModificationListener modificationListener) {
			this.modificationListener = modificationListener;
			Font font = this.getFont();
			int fontSize = font.getSize();
			Font newFont = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
			this.setFont(newFont);
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
		
		public static synchronized String getClipboardText() {
			return clipboardText;
		}
		
		public static synchronized void setClipboardText(String clipboardText) {
			CustomJTextPane.clipboardText = clipboardText;
		}
    }
}
