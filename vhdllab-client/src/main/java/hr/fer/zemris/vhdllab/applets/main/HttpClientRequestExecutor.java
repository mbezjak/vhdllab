package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.client.core.SystemContext;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.dialogs.login.LoginDialog;
import hr.fer.zemris.vhdllab.client.dialogs.login.UserCredential;

import java.awt.Frame;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;

public class HttpClientRequestExecutor extends
        CommonsHttpInvokerRequestExecutor {

    private boolean showRetryMessage = false;

    public HttpClientRequestExecutor(HttpClient httpClient) throws IOException {
        super(httpClient);
        getHttpClient().getParams().setAuthenticationPreemptive(true);
        Properties properties = PropertiesLoaderUtils
                .loadAllProperties("server.properties");
        int port = Integer.parseInt(properties.getProperty("port"));
        Protocol easyhttps = new Protocol("https",
                new EasySSLProtocolSocketFactory(), port);
        Protocol.registerProtocol("https", easyhttps);
    }

    @Override
    protected void executePostMethod(HttpInvokerClientConfiguration config,
            HttpClient httpClient, PostMethod postMethod) throws IOException {
        AuthScope scope = new AuthScope(config.getCodebaseUrl(),
                AuthScope.ANY_PORT);
        super.executePostMethod(config, httpClient, postMethod);
        switch (postMethod.getStatusCode()) {
        case HttpStatus.SC_UNAUTHORIZED:
        case HttpStatus.SC_FORBIDDEN:
            UserCredential uc = showLoginDialog(showRetryMessage);
            showRetryMessage = true;
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                    uc.getUsername(), uc.getPassword());
            getHttpClient().getState().setCredentials(scope, credentials);
            executePostMethod(config, httpClient, postMethod);
            break;
        }
    }

    /**
     * Show a login dialog to a user and requests that a user enters username
     * and password that is needed to authenticate user. This method also sets
     * credentials (a private field).
     * 
     * @param displayRetryMessage
     *            a flag indicating if login dialog should show retry message
     *            (not a default message when it says that authentication is
     *            required but a message saying that he should try again because
     *            previous login attempt failed)
     * @return
     * @throws SecurityException
     *             if user refused to provider proper username and password
     */
    private UserCredential showLoginDialog(final boolean displayRetryMessage) {
        final UserCredential[] holder = new UserCredential[1];
        /*
         * This method only insures that showLoginDialogImpl method is invoked
         * by EDT.
         */
        if (SwingUtilities.isEventDispatchThread()) {
            return showLoginDialogImpl(displayRetryMessage);
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    holder[0] = showLoginDialogImpl(displayRetryMessage);
                }
            });
        } catch (InterruptedException e) {
            SystemLog.instance().addErrorMessage(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SecurityException) {
                throw new SecurityException(cause);
            }
            SystemLog.instance().addErrorMessage(e);
        }
        return holder[0];
    }

    /**
     * An actual implementation of showLoginDialog method. This method must be
     * invoked by EDT!
     * 
     * @param displayRetryMessage
     *            a flag indicating if login dialog should show retry message
     *            (not a default message when it says that authentication is
     *            required but a message saying that he should try again because
     *            previous login attempt failed)
     * @return
     * @throws SecurityException
     *             if user refused to provider proper username and password
     */
    UserCredential showLoginDialogImpl(boolean displayRetryMessage) {
        Frame owner = SystemContext.instance().getFrameOwner();
        String message;
        if (displayRetryMessage) {
            String name = LoginDialog.BUNDLE_NAME;
            String key = LoginDialog.RETRY_MESSAGE;
            message = ResourceBundleProvider.getBundle(name).getString(key);
        } else {
            message = null;
        }
        LoginDialog dialog = new LoginDialog(owner, message);
        dialog.setVisible(true); // controls are locked here
        int option = dialog.getOption();
        if (option == LoginDialog.OK_OPTION) {
            return dialog.getCredentials();
        }
        throw new SecurityException(
                "User refused to provide proper username and password");
    }

}
