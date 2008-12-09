package hr.fer.zemris.vhdllab.platform.manager.workspace.support;

import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import java.security.cert.CertificateExpiredException;

import javax.annotation.Resource;
import javax.net.ssl.SSLHandshakeException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceInitializer extends
        AbstractEventPublisher<WorkspaceInitializationListener> {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(WorkspaceInitializer.class);

    /**
     * An exit status error indicating abnormal application termination.
     */
    private static final int EXIT_STATUS_ERROR = 1;

    @Autowired
    private WorkspaceManager workspaceManager;
    @Resource(name = "cantConnectDialogManager")
    private DialogManager<?> cantConnectDialogManager;
    @Resource(name = "certificateExpiredDialogManager")
    private DialogManager<?> certificateExpiredDialogManager;
    @Resource(name = "unknownExceptionDialogManager")
    private DialogManager<?> unknownExceptionDialogManager;

    public WorkspaceInitializer() {
        super(WorkspaceInitializationListener.class);
    }

    public void initializeWorkspace() {
        Workspace workspace;
        try {
            workspace = workspaceManager.getWorkspace();
        } catch (RemoteConnectFailureException e) {
            cantConnectDialogManager.showDialog();
            exit();
            /*
             * Return statement is just to silence a compiler (variable
             * workspace might not have been initialized).
             */
            return;
        } catch (RemoteAccessException e) {
            Throwable directCause = e.getCause();
            if (directCause instanceof SSLHandshakeException
                    && directCause.getCause() instanceof CertificateExpiredException) {
                certificateExpiredDialogManager.showDialog();
            } else if (directCause instanceof SecurityException) {
                /*
                 * This means that user refused to provide proper username and
                 * password (i.e. he clicked cancel on a login dialog).
                 * 
                 * When this happens no dialog is raised. Application simply
                 * shuts down.
                 */
            } else {
                unknownExceptionDialogManager.showDialog();
            }
            exit();
            /*
             * Return statement is just to silence a compiler (variable
             * workspace might not have been initialized).
             */
            return;
        }
        fireInitialize(workspace);
        LOG.debug("Workspace initialized with " + workspace.getProjectCount()
                + " projects");
    }

    private void fireInitialize(Workspace workspace) {
        for (WorkspaceInitializationListener l : getListeners()) {
            l.initialize(workspace);
        }
    }

    private void exit() {
        /*
         * Can forcefully exit because application has not yet been fully
         * initialized (so shutdownManager can't be called).
         */
        System.exit(EXIT_STATUS_ERROR);
    }

}
