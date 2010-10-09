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

public class BugsCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "bugsCommand";

    public BugsCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("Executing " + getClass());

        BugsDialog dialog = new BugsDialog();
        dialog.setParentComponent(getApplicationWindow().getControl());
        dialog.showDialog();
    }

    private static class BugsDialog extends ApplicationDialog {

        public BugsDialog() {
            super();

            setTitle("Report a bug");
            setModal(false);
            setCloseAction(CloseAction.DISPOSE);
        }

        @Override
        protected JComponent createDialogContentPane() {
            HtmlPane pane = new HtmlPane();

            pane.setText("<html>If you notice any bugs in VHDLLab please report them on official<br />"
                            + "VHDLLab issue tracker: <a href='http://morgoth.zemris.fer.hr/trac/vhdllab/newticket'>http://morgoth.zemris.fer.hr/trac/vhdllab/newticket</a>.<br />"
                            + "When creating new issue please document reproducibility of a bug. It will go a<br />"
                            + "long way towards fixing or at least mitigating it.<br />"
                            + "<br />"
                            + "Notice, however, that some bugs will not be fixed. You can recognize, learn<br />"
                            + "how to avoid them and find out why they won't be fixed on a wiki page:<br />"
                            + "<a href='http://morgoth.zemris.fer.hr/trac/vhdllab/wiki/Problemi'>morgoth.zemris.fer.hr/trac/vhdllab/wiki/Problemi</a><br />"
                            + "<br />"
                            + "Note for non-Windows users. You will need Firefox web browser to open links.");

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
