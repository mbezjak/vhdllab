package hr.fer.zemris.vhdllab.platform.support;

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
        ActionCommand command = (ActionCommand) getOpeningWindow().getCommandManager().getCommand("newSourceCommand");
        command.execute();
    }

    @Override
    public boolean onPreWindowClose(ApplicationWindow window) {
        if (((VhdllabApplication) Application.instance())
                .isForceShutdownInProgress()) {
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
