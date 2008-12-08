package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.gui.dialog.login.UserCredential;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

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

    @Resource(name = "loginDialogManager")
    private DialogManager<UserCredential> dialogManager;
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
            UserCredential uc;
            if (ApplicationContextHolder.getContext().isDevelopment()
                    && !showRetryMessage) {
                uc = new UserCredential("test", "test");
            } else {
                uc = dialogManager.showDialog();
            }
            if(uc == null) {
                throw new SecurityException("User refused to provide proper username and password");
            }
            ApplicationContextHolder.getContext().setUserId(new Caseless(uc.getUsername()));
            showRetryMessage = true;
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                    uc.getUsername(), uc.getPassword());
            getHttpClient().getState().setCredentials(scope, credentials);
            executePostMethod(config, httpClient, postMethod);
            break;
        }
    }

}
