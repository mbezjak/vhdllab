package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentShowModalDialogAction extends AbstractMenuAction {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(DevelopmentShowModalDialogAction.class);

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Frame owner = ApplicationContextHolder.getContext().getFrame();
            JOptionPane.showMessageDialog(owner, "is it modal?");
        } catch (Exception ex) {
            LOG.error("Forced error", ex);
        }
    }

}
