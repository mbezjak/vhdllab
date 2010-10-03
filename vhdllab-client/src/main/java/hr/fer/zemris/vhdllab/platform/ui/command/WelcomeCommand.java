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

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandManager;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

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

        if (count < 5) {
            showWelcomeDialog();

            pref.putInt(PREF_WELCOME_DIALOG_SHOW_COUNT, count + 1);
        }
    }

    protected void showWelcomeDialog() {
        CommandManager cm = getApplicationWindow().getCommandManager();
        ActionCommand command = (ActionCommand) cm.getCommand("bugsCommand");
        command.execute();
    }

}
