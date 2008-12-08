package hr.fer.zemris.vhdllab.platform.shutdown;

import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;

import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class ExitApplicationDialogManager implements DialogManager<Boolean> {

    @Override
    public Boolean showDialog() {
        String name = LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN;
        String key = LanguageConstants.DIALOG_CONFIRM_EXIT;
        ResourceBundle bundle = ResourceBundleProvider.getBundle(name);
        String text = bundle.getString(key);
        Frame owner = ApplicationContextHolder.getContext().getFrame();
        int option = JOptionPane.showConfirmDialog(owner, text);
        return Boolean.valueOf(option == JOptionPane.YES_OPTION);
    }

}
