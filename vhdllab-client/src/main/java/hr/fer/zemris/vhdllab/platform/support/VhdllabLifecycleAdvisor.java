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

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.config.ApplicationWindowConfigurer;
import org.springframework.richclient.application.config.DefaultApplicationLifecycleAdvisor;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandManager;

public class VhdllabLifecycleAdvisor extends DefaultApplicationLifecycleAdvisor {

    @Autowired
    private ShutdownManager shutdownManager;
    @Autowired
    private ConfirmExitDialog confirmExitDialog;
    @Autowired
    private WorkspaceInitializer workspaceInitializer;

    @Override
    public void onPreStartup() {
        new UserLocaleInitializer().initLocale();
        ToolTipManager.sharedInstance().setDismissDelay(15000); // 15 seconds
    }

    @Override
    public void onPreWindowOpen(ApplicationWindowConfigurer configurer) {
        super.onPreWindowOpen(configurer);
        configurer.setShowToolBar(false);
    }

    @Override
    public void onWindowOpened(ApplicationWindow window) {
        window.getControl().setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @Override
    public void onPostStartup() {
        workspaceInitializer.initWorkspace();

        showUserCredentials();
        showWelcomeDialog();
    }

    private void showUserCredentials() {
        ApplicationWindow window = getApplication().getActiveWindow();
        JFrame frame = window.getControl();
        String user = ApplicationContextHolder.getContext().getUserId();

        frame.setTitle(frame.getTitle() + ", " + user);
        window.getStatusBar().setMessage("Logged in as " + user);
    }

    private void showWelcomeDialog() {
        CommandManager cm = getApplication().getActiveWindow().getCommandManager();
        ActionCommand welcome = (ActionCommand) cm.getCommand("welcomeCommand");
        welcome.execute();
    }

    @Override
    public boolean onPreWindowClose(ApplicationWindow window) {
        if (((VhdllabApplication) Application.instance()).isForceShutdownInProgress()) {
            return true;
        }
        confirmExitDialog.showDialog();
        if (confirmExitDialog.isExitConfirmed()) {
            return !shutdownManager.shutdownWithGUI();
        }
        return false;
    }

    @Override
    public void onShutdown() {
        shutdownManager.shutdownWithoutGUI();
    }

}
