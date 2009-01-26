package hr.fer.zemris.vhdllab.platform.gui.dialog.save;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SaveDialogManager extends LocalizationSupport implements
        DialogManager {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T showDialog(Object... args) {
        SaveDialog dialog = new SaveDialog(this);
        dialog.setSaveFiles((List<FileIdentifier>) args[0]);
        dialog.startDialog();
        return (T) dialog.getResult();
    }

}
