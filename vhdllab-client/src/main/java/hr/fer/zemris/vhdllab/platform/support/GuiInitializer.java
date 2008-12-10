package hr.fer.zemris.vhdllab.platform.support;

import hr.fer.zemris.vhdllab.applets.main.VhdllabFrame;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public final class GuiInitializer implements Runnable {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(GuiInitializer.class);

    private ApplicationContext context;

    public GuiInitializer(ApplicationContext context) {
        this.context = context;
    }

    public void initGUI() {
        try {
            SwingUtilities.invokeAndWait(this);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        LOG.debug("GUI initialized");
    }

    @Override
    public void run() {
        initLookAndFeel();
        initFrame();
    }

    private void initLookAndFeel() {
        try {
            if (!System.getProperty("os.name").equals("Linux")) {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void initFrame() {
        Frame frame = new VhdllabFrame(context);
        ApplicationContextHolder.getContext().setFrame(frame);
    }

}
