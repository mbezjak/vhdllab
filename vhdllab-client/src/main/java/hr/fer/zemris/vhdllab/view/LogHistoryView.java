package hr.fer.zemris.vhdllab.view;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.richclient.application.support.AbstractView;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since 6.2.2009.
 */
public class LogHistoryView extends AbstractView {

    @Override
    protected JComponent createControl() {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setAutoscrolls(true);

        setupLogAppender(textPane);

        return new JScrollPane(textPane);
    }

    private void setupLogAppender(final JTextPane textPane) {
        Logger.getRootLogger().addAppender(new AppenderSkeleton() {
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
                    document.insertString(documentLength, sb.toString() + "\n",
                            null);
                } catch (BadLocationException e) {
                    throw new IllegalStateException(e);
                }
                // scroll to end of document
                textPane.setCaretPosition(documentLength);
            }

            @Override
            public boolean requiresLayout() {
                return false;
            }

            @Override
            public void close() {
            }
        });
    }

}
