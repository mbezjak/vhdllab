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
package hr.fer.zemris.vhdllab.platform.javaws;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;

import javax.jnlp.SingleInstanceListener;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationWindow;

public final class EnforceSingleInstanceListener implements
        SingleInstanceListener {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(EnforceSingleInstanceListener.class);

    private final DialogManager dialogManager;

    public EnforceSingleInstanceListener(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    @Override
    public void newActivation(String[] args) {
        LOG.debug("Denied start of second client application");
        ApplicationWindow window = Application.instance().getActiveWindow();
        if (window != null && window.getControl() != null) {
            window.getControl().toFront();
            dialogManager.showDialog();
        }
    }

}
