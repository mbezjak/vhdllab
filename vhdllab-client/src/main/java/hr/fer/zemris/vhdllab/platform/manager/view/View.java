package hr.fer.zemris.vhdllab.platform.manager.view;

import javax.swing.JPanel;

public interface View {

    void init();

    void dispose();

    JPanel getPanel();

}
