package hr.fer.zemris.vhdllab.platform.manager.view;

import javax.swing.JPanel;

public interface View {

    PlatformContainer getContainer();

    void setContainer(PlatformContainer container);

    void init();

    JPanel getPanel();

}
