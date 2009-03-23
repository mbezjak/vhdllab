package hr.fer.zemris.vhdllab.platform.gui.dialog.save;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;

import java.util.List;
import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

@Component
public class SaveDialogManager extends LocalizationSupport implements
        DialogManager {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T showDialog(Object... args) {
        List<File> identifiers = (List<File>) args[0];
        SaveContext context = (SaveContext) args[1];
        Preferences preferences = Preferences
                .userNodeForPackage(SaveDialog.class);
        if (preferences.getBoolean(SaveDialog.SHOULD_AUTO_SAVE, false)) {
            return (T) identifiers;
        }

        SaveDialog dialog = new SaveDialog(this, context);
        dialog.setSaveFiles(identifiers);
        dialog.startDialog();
        return (T) dialog.getResult();
    }

}
