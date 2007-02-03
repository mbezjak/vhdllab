package hr.fer.zemris.vhdllab.applets.view.status.history;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.StatusContent;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.StatusListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/** 
 * View that holds past messages diplayed in status bar.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since 3.2.2007.
 */
public class StatusHistoryView extends JPanel implements IView {

	/** Serial version UID. */
	private static final long serialVersionUID = -5905330862355651657L;

	/** Time format to get current time */
	private String timeFormat = "HH:mm:ss";

	/** Formatter that will return string representation of current time. */
	private Format formatter;
	
	/** Contains style for every message type */
	private Map<MessageEnum, Style> messageTypeStyles;

	/** Pane where text will be displayed */
	private JTextPane textPane;

	/**
	 * Constructor.
	 */
	public StatusHistoryView() {
		formatter = new SimpleDateFormat(timeFormat);		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setAutoscrolls(true);
		
		MessageEnum[] messageTypes = MessageEnum.values();
		messageTypeStyles = new HashMap<MessageEnum, Style>(messageTypes.length);
		StyledDocument doc = (StyledDocument) textPane.getDocument();
		for(MessageEnum e : messageTypes) {
			if(e.isSuccessful()) {
				Style style = doc.addStyle("Successfull Style Message", null);
				StyleConstants.setForeground(style, new Color(12, 46, 233));
				messageTypeStyles.put(e, style);
			} else if(e.isError()) {
				Style style = doc.addStyle("Error Style Message", null);
				StyleConstants.setForeground(style, new Color(237, 7, 7));
				messageTypeStyles.put(e, style);
			} else if(e.isInformation()) {
				Style style = doc.addStyle("Information Style Message", null);
				StyleConstants.setForeground(style, new Color(43, 46, 59));
				messageTypeStyles.put(e, style);
			}
		}

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

	/**
	 * Returns string representation of current time.
	 * @return
	 */
	protected String getCurrentTime() {
		return formatter.format(new Date());
	}

	/**
	 * This method is ignored since this View has much better way of accessing
	 * information.
	 */
	public void appendData(Object data) {
		/*if(data == null) return;
		if(!(data instanceof String)) {
			throw new IllegalArgumentException("Unknown data!");
		}
		String text = (String) data;
		StringBuilder sb = new StringBuilder(text.length() + 10);
		sb.append(getCurrentTime()).append("  ")
		.append(text).append("\n");
		StyledDocument doc = (StyledDocument) textPane.getDocument();
		Style style = doc.addStyle("new Style", null);
		StyleConstants.setForeground(style, Color.RED);
		try {
			doc.insertString(doc.getLength(), sb.toString(), style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		textPane.setCaretPosition(doc.getLength());*/

	}

	/**
	 * This method is ignored since this View has much better way of accessing
	 * information.
	 */
	public void setData(Object data) {}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#setProjectContainer(hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer)
	 */
	public void setProjectContainer(ProjectContainer container) {
		container.getStatusBar().addStatusListener(new StatusListener() {
			public void statusChanged(StatusContent c) {
				addStatus(c.getMessage(), c.getMessageType());
			}
		});
	}
	
	/**
	 * Appends a status content to the end of status history.
	 * @param message a message content
	 * @param messageType a message type
	 */
	protected void addStatus(String message, MessageEnum messageType) {
		StringBuilder sb = new StringBuilder(message.length() + 10);
		sb.append(getCurrentTime()).append("  ").append(message).append("\n");
		
		Style style = messageTypeStyles.get(messageType);
		Document doc = (StyledDocument) textPane.getDocument();
		try {
			doc.insertString(doc.getLength(), sb.toString(), style);
		} catch (BadLocationException e) {
			return;
		}
		textPane.setCaretPosition(doc.getLength());
	}

}