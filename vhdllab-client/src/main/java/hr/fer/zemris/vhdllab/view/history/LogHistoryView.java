package hr.fer.zemris.vhdllab.view.history;

import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractView;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.log4j.Logger;

/**
 * View that holds past messages displayed in status bar.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 3.2.2007.
 */
public class LogHistoryView extends AbstractView {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setAutoscrolls(true);
        setLayout(new BorderLayout());
        add(new JScrollPane(textPane), BorderLayout.CENTER);

        Logger.getRootLogger().addAppender(new LogHistoryAppender(textPane));
    }

}
