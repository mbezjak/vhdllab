package hr.fer.zemris.vhdllab.view.history;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class LogHistoryAppender extends AppenderSkeleton {

    private JTextPane textPane;

    public LogHistoryAppender(JTextPane textPane) {
        Validate.notNull(textPane, "Text pane can't be null");
        this.textPane = textPane;
    }

    @Override
    protected void append(LoggingEvent event) {
        if (!event.getLevel().equals(Level.INFO)) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        String time = DateFormatUtils.ISO_TIME_NO_T_FORMAT
                .format(event.timeStamp);
        sb.append(time).append("  ").append(event.getMessage());

        Document document = textPane.getDocument();
        int documentLength = document.getLength();
        try {
            document.insertString(documentLength, sb.toString() + "\n", null);
        } catch (BadLocationException e) {
            throw new IllegalStateException(e);
        }
        textPane.setCaretPosition(documentLength);
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}
