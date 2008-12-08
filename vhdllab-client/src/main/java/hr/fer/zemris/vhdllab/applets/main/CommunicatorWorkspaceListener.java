package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.platform.workspace.support.WorkspaceInitializationListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommunicatorWorkspaceListener implements
        WorkspaceInitializationListener {

    @Autowired
    private ICommunicator communicator;

    @Override
    public void workspaceInitialized(Workspace workspace) {
        try {
            communicator.init();
        } catch (UniformAppletException e) {
            throw new IllegalStateException(e);
        }
    }

}
