package hr.fer.zemris.vhdllab.platform.i18n;

import javax.swing.JFrame;

public interface LocalizationSource {

    String getMessage(String code, Object[] args);

    String getMessage(String code);

    JFrame getFrame();

}
