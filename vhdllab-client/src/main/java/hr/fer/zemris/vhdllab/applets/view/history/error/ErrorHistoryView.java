package hr.fer.zemris.vhdllab.applets.view.history.error;

import hr.fer.zemris.vhdllab.client.core.log.SystemError;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogAdapter;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogListener;
import hr.fer.zemris.vhdllab.client.core.log.SystemMessage;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractView;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.springframework.stereotype.Component;

/**
 * View that holds past error messages.
 * 
 * @author Miro Bezjak
 */
@Component
public class ErrorHistoryView extends AbstractView {

    /** Serial version UID. */
    private static final long serialVersionUID = 6855260099884893907L;

    /** Time format to get current time */
    private String timeFormat = "HH:mm:ss";

    /** Formatter that will return string representation of current time. */
    private Format formatter;

    /** Pane where text will be displayed */
    private JTextPane textPane;

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
    public void doInit() {
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
        for (SystemMessage message : systemLog.getErrorMessages()) {
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

        Document doc = textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), sb.toString(), null);
        } catch (BadLocationException e) {
            return;
        }
        textPane.setCaretPosition(doc.getLength());
    }

}