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
package hr.fer.zemris.vhdllab.platform.support;

import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.stereotype.Component;

@Component
public class ConfirmExitDialog extends ConfirmationDialog {

    protected static final String QUIT_COMMAND_ID = "quitCommand";

    private boolean exitConfirmed;

    public boolean isExitConfirmed() {
        return exitConfirmed;
    }

    @Override
    protected void onConfirm() {
        exitConfirmed = true;
    }

    @Override
    protected String getFinishCommandId() {
        return QUIT_COMMAND_ID;
    }

    @Override
    protected String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        setConfirmationMessage(getMessage("confirmExitDialog.message"));
    }

}
