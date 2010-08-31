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
package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;
import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownAdapter;
import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveEditorsOnShutdownListener extends ShutdownAdapter {

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    @Override
    public void shutdownWithGUI(ShutdownEvent event) {
        EditorManager openedEditors = editorManagerFactory.getAll();
        boolean saved = openedEditors.save(true,
                SaveContext.SHUTDOWN_AFTER_SAVE);
        if (!saved && openedEditors instanceof MulticastEditorManager) {
            event.cancelShutdown();
        }
    }

}
