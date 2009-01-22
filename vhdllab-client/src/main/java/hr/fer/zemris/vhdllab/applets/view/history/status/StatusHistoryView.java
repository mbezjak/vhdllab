package hr.fer.zemris.vhdllab.applets.view.history.status;

import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogAdapter;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogListener;
import hr.fer.zemris.vhdllab.client.core.log.SystemMessage;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * View that holds past messages displayed in status bar.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 3.2.2007.
 */
public class StatusHistoryView extends AbstractView {

	/** Serial version UID. */
	private static final long serialVersionUID = -5905330862355651657L;

	/** Time format to get current time */
	private String timeFormat = "HH:mm:ss";

	/** Formatter that will return string representation of current time. */
	private Format formatter;

	/** Contains style for every message type */
	private Map<MessageType, Style> messageTypeStyles;

	/** Pane where text will be displayed */
	private JTextPane textPane;

	private SystemLogListener systemLogListener;

	/**
	 * Constructor.
	 */
	public StatusHistoryView() {
	}

	/**
	 * Returns string representation of current time.
	 * 
	 * @return
	 */
	protected String getFormattedTime(Date date) {
		return formatter.format(date);
	}

	@Override
	public void doInit() {
		formatter = new SimpleDateFormat(timeFormat);
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setAutoscrolls(true);

		MessageType[] messageTypes = MessageType.values();
		messageTypeStyles = new HashMap<MessageType, Style>(messageTypes.length);
		StyledDocument doc = (StyledDocument) textPane.getDocument();
		for (MessageType e : messageTypes) {
			if (e.isSuccessful()) {
				Style style = doc.addStyle("Successfull Style Message", null);
				StyleConstants.setForeground(style, new Color(12, 46, 233));
				messageTypeStyles.put(e, style);
			} else if (e.isError()) {
				Style style = doc.addStyle("Error Style Message", null);
				StyleConstants.setForeground(style, new Color(237, 7, 7));
				messageTypeStyles.put(e, style);
			} else if (e.isInformation()) {
				Style style = doc.addStyle("Information Style Message", null);
				StyleConstants.setForeground(style, new Color(43, 46, 59));
				messageTypeStyles.put(e, style);
			}
		}

		final JScrollPane scrollPane = new JScrollPane(textPane);
		this.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentResized(ComponentEvent e) {
				scrollPane.setPreferredSize(StatusHistoryView.this.getSize());
			}

			public void componentShown(ComponentEvent e) {
			}
		});
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		
		SystemLog systemLog = SystemLog.instance();
		for(SystemMessage message : systemLog.getSystemMessages()) {
			addMessage(message);
		}
		systemLogListener = new SystemLogAdapter() {
			@Override
			public void systemMessageAdded(SystemMessage message) {
				addMessage(message);
			}
		};
		systemLog.addSystemLogListener(systemLogListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IView#dispose()
	 */
	@Override
	public void doDispose() {
		SystemLog.instance().removeSystemLogListener(systemLogListener);
	}
	
	/**
	 * Appends a system message to the end of status history.
	 * 
	 * @param message
	 *            a system message to add
	 */
	private void addMessage(SystemMessage message) {
		String content = message.getContent();
		StringBuilder sb = new StringBuilder(content.length() + 10);
		sb.append(getFormattedTime(message.getDate())).append("  ").append(
				content).append("\n");

		Style style = messageTypeStyles.get(message.getType());
		Document doc = textPane.getDocument();
		try {
			doc.insertString(doc.getLength(), sb.toString(), style);
		} catch (BadLocationException e) {
			return;
		}
		textPane.setCaretPosition(doc.getLength());
	}
	
}