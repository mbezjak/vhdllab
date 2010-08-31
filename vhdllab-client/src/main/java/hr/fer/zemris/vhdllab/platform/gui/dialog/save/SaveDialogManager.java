/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
