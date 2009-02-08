package hr.fer.zemris.vhdllab.platform.support;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.richclient.application.config.JGoodiesLooksConfigurer;
import org.springframework.richclient.application.config.UIManagerConfigurer;

import com.jgoodies.looks.plastic.theme.ExperienceBlue;

public class OSBasedUIManagerConfigurer extends UIManagerConfigurer {

    @Override
    protected void doInstallCustomDefaults() throws Exception {
        if (System.getProperty("os.name").equals("Linux")) {
            try {
                installJGoodiesLooks();
            } catch (UnsupportedLookAndFeelException e) {
                // then leave cross platform look and feel (default)
                UIManager.put("swing.boldMetal", Boolean.FALSE);
            }
        } else {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
    }

    private void installJGoodiesLooks() throws Exception {
        JGoodiesLooksConfigurer configurer = new JGoodiesLooksConfigurer(this);
        configurer.setPopupDropShadowEnabled(false);
        configurer.setTheme(new ExperienceBlue());
        configurer.afterPropertiesSet();
    }

}
