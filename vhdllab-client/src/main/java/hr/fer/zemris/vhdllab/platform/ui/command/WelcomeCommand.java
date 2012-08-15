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

import java.util.prefs.Preferences;

import javax.swing.JComponent;

import org.apache.commons.lang.SystemUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandManager;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.text.HtmlPane;

public class WelcomeCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "welcomeCommand";

    protected static final String PREF_WELCOME_DIALOG_SHOW_COUNT = "welcome.dialog.show.count";

    public WelcomeCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        Preferences pref = Preferences.userNodeForPackage(WelcomeCommand.class);
        int count = pref.getInt(PREF_WELCOME_DIALOG_SHOW_COUNT, 0);

        if (count < 10) {
            showWelcomeDialog();
            showUpdateJavaDialog();

            pref.putInt(PREF_WELCOME_DIALOG_SHOW_COUNT, count + 1);
        }
    }

    protected void showWelcomeDialog() {
        CommandManager cm = getApplicationWindow().getCommandManager();
        ActionCommand command = (ActionCommand) cm.getCommand("bugsCommand");
        command.execute();
    }

    protected void showUpdateJavaDialog() {
        if (!SystemUtils.JAVA_RUNTIME_VERSION.startsWith("1.7")) {
            UpdateJavaDialog dialog = new UpdateJavaDialog();
            dialog.setParentComponent(getApplicationWindow().getControl());
            dialog.showDialog();
        }
    }

    private static class UpdateJavaDialog extends ApplicationDialog {

        public UpdateJavaDialog() {
            super();

            setTitle("Update Java");
            setModal(false);
            setCloseAction(CloseAction.DISPOSE);
        }

        @Override
        protected JComponent createDialogContentPane() {
            HtmlPane pane = new HtmlPane();

            pane.setText("<html>A number of VHDLLab bugs are actually bugs in Java itself.<br />"
                        + "Please update Java for added stability.<br />"
                        + "<a href='http://www.java.com/en/download/index.jsp'>http://www.java.com/en/download/index.jsp</a>");

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
