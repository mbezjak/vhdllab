package hr.fer.zemris.vhdllab.platform.manager.workspace.support;

import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;

import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class CantConnectDialogManager implements DialogManager<Void> {

    @Override
    public Void showDialog() {
        String name = LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN;
        String key = LanguageConstants.DIALOG_CANT_CONNECT;
        ResourceBundle bundle = ResourceBundleProvider.getBundle(name);
        String text = bundle.getString(key);
        Frame owner = ApplicationContextHolder.getContext().getFrame();
        JOptionPane.showMessageDialog(owner, text);
        return null;
    }

}
