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
package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class CloseCommand extends ActionCommand {

    public static final String ID = "closeCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public CloseCommand() {
        super(ID);
        setEnabled(false);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("Executing " + getClass());
        editorManagerFactory.getSelected().close();
    }

}
