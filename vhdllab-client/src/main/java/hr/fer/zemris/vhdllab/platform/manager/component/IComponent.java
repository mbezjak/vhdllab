package hr.fer.zemris.vhdllab.platform.manager.component;

import javax.swing.JPanel;

public interface IComponent {

    void init();

    void dispose();

    JPanel getPanel();

}
