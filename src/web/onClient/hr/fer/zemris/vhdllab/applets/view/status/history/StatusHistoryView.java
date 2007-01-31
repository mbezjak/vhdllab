package hr.fer.zemris.vhdllab.applets.view.status.history;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/** 
 * View that holds past texts diplayed in status bar.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since 30.1.2007.
 */
public class StatusHistoryView extends JPanel implements IView {
	
	/** Serial version UID. */
	private static final long serialVersionUID = -5905330862355651657L;
	
	/** Standard suffing to text being appended */
	private static final String SUFFIX_AFTER_APPENDING_TEXT = "\n";
	
	/** Time format to get current time */
	private static final String TIME_FORMAT = "HH:mm:ss";

	/** Pane where text will be displayed */
	private JTextPane textPane;
	
    /**
     * Constructor.
     */
    public StatusHistoryView()
    {
    	textPane = new JTextPane();
    	textPane.setEditable(false);
    	textPane.setAutoscrolls(true);
    	final JScrollPane scrollPane = new JScrollPane(textPane);
    	this.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				scrollPane.setPreferredSize(StatusHistoryView.this.getSize());
			}
			public void componentShown(ComponentEvent e) {}
    	});
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    private String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		return sdf.format(new Date());
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#appendData(java.lang.Object)
	 */
	public void appendData(Object data) {
		if(data == null) return;
		if(!(data instanceof String)) {
			throw new IllegalArgumentException("Unknown data!");
		}
		String text = (String) data;
		StringBuilder sb = new StringBuilder(text.length() + 10);
		sb.append(getCurrentTime()).append("  ")
			.append(text).append(SUFFIX_AFTER_APPENDING_TEXT);
		StyledDocument doc = (StyledDocument) textPane.getDocument();
		Style style = doc.addStyle("new Style", null);
		StyleConstants.setForeground(style, Color.RED);
		int length = doc.getLength();
		textPane.setCaretPosition(length);
		try {
			doc.insertString(doc.getLength(), sb.toString(), style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		appendData(data);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#setProjectContainer(hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer)
	 */
	public void setProjectContainer(ProjectContainer container) {}
 
}