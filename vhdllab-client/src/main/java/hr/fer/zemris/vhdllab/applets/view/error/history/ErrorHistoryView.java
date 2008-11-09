package hr.fer.zemris.vhdllab.applets.view.error.history;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.client.core.log.SystemError;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogAdapter;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogListener;
import hr.fer.zemris.vhdllab.client.core.log.SystemMessage;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * View that holds past error messages.
 * 
 * @author Miro Bezjak
 */
public class ErrorHistoryView extends JPanel implements IView {

	/** Serial version UID. */
	private static final long serialVersionUID = 6855260099884893907L;

	/** Time format to get current time */
	private String timeFormat = "HH:mm:ss";

	/** Formatter that will return string representation of current time. */
	private Format formatter;

	/** Pane where text will be displayed */
	private JTextPane textPane;

	/**
	 * A system container.
	 */
	private ISystemContainer container;

	private SystemLogListener systemLogListener;

	/**
	 * Constructor.
	 */
	public ErrorHistoryView() {
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
	public void init() {
		formatter = new SimpleDateFormat(timeFormat);
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setAutoscrolls(true);

		final JScrollPane scrollPane = new JScrollPane(textPane);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				scrollPane.setPreferredSize(ErrorHistoryView.this.getSize());
			}
		});
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		
		SystemLog systemLog = SystemLog.instance();
		for(SystemMessage message : systemLog.getErrorMessages()) {
			addMessage(message);
		}
		systemLogListener = new SystemLogAdapter() {
			@Override
			public void errorMessageAdded(SystemError message) {
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
	public void dispose() {
		SystemLog.instance().removeSystemLogListener(systemLogListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#setProjectContainer(hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer)
	 */
	public void setSystemContainer(ISystemContainer container) {
		this.container = container;
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

		Document doc = textPane.getDocument();
		try {
			doc.insertString(doc.getLength(), sb.toString(), null);
		} catch (BadLocationException e) {
			return;
		}
		textPane.setCaretPosition(doc.getLength());
	}
	
	@Override
	public <T> T asInterface(Class<T> clazz) {
		return null;
	}

}