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

import javax.swing.JComponent;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.text.HtmlPane;

public class KnownProblemsCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "knownProblemsCommand";

    public KnownProblemsCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        KnownProblemsDialog dialog = new KnownProblemsDialog();
        dialog.setParentComponent(getApplicationWindow().getControl());
        dialog.showDialog();
    }

    private static class KnownProblemsDialog extends ApplicationDialog {

        public KnownProblemsDialog() {
            super();

            setTitle("Known problems");
            setCloseAction(CloseAction.DISPOSE);
        }

        @Override
        protected JComponent createDialogContentPane() {
            HtmlPane pane = new HtmlPane();

            pane.setText("<html>Known problems of VHDLLab are located at:<br />"
                    + "<a href='http://morgoth.zemris.fer.hr/trac/vhdllab/wiki/Problemi'>"
                    + "morgoth.zemris.fer.hr/trac/vhdllab/wiki/Problemi</a></html>");

            return pane;
        }

        @Override
        protected boolean onFinish() {
            return true;
        }

        @Override
        protected Object[] getCommandGroupMembers() {
            return new Object[] { getFinishCommand() };
        }
    }

}
