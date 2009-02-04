package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentCreateNewErrorAction extends AbstractMenuAction {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(DevelopmentCreateNewErrorAction.class);

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int error = 1 / 0;
            System.out.println(error);
        } catch (RuntimeException ex) {
            LOG.error("Forced error", ex);
        }
    }

}
