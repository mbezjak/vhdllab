package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.client.core.SystemContext;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.dialogs.login.LoginDialog;
import hr.fer.zemris.vhdllab.client.dialogs.login.UserCredential;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.communicaton.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.methods.GetSessionLengthMethod;
import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * Implementation of <code>Initiator</code> interface that uses
 * <code>HttpClient</code> to initiate requests to server.
 * 
 * @author Miro Bezjak
 */
public final class HttpClientInitiator implements Initiator {

	/**
	 * A server configuration file.
	 */
	private static final String SERVER_PROPERTIES = "server.properties";
	/**
	 * A key in properties whose value contains a port of a https protocol.
	 */
	private static final String HTTPS_PORT = "https.port";
	/**
	 * A key in properties whose value contains a hostname of vhdllab server.
	 */
	private static final String HOST = "host";
	/**
	 * A key in properties whose value contains a path for a cookie.
	 */
	private static final String COOKIE_PATH = "cookie.path";
	/**
	 * A key in properties whose value contains an url of an authentication
	 * servlet. That servlet doesn't return any value. It is just used to
	 * authenticate.
	 */
	private static final String AUTH_URL = "authentication.url";
	/**
	 * A key in properties whose value contains an url of main vhdllab servlet.
	 * This servlet processes all methods and returns a result.
	 */
	private static final String SERVLET_URL = "vhdllab.url";

	/**
	 * A name of a session cookie.
	 */
	private static final String COOKIE_NAME = "JSESSIONID";

	/**
	 * An estimate of how big an object will be after serialization (1KB).
	 */
	private static final int BYTE_ARRAY_SIZE = 1000;

	/**
	 * A minimum session length. Length is in seconds.
	 */
	private static final int MINIMUM_SESSION_LENGTH = 60;

	/**
	 * A properties in server configuration file.
	 */
	private Properties properties;

	/**
	 * A session identifier that is stored in a cookie to enable communication
	 * to server.
	 */
	private String sessionId;

	/**
	 * A user identifier that is logged in and for whom to send all methods are
	 * sent.
	 */
	private String userId;

	/**
	 * A password of a user.
	 */
	private String password;
	
	/**
	 * A timer that executes task that saves session.
	 */
	private Timer timer;
	
	/**
	 * Mediator responsible for initiating requests to server.
	 */
	private HttpClient client;

	/**
	 * Constructor.
	 * 
	 * @param sessionId
	 */
	public HttpClientInitiator(String sessionId) {
		this.sessionId = sessionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#init()
	 */
	@Override
	public void init() {
		userId = SystemContext.getUserId();
		readServerProperties();
		registerProtocol();
		initHttpClient();
		if (!isSessionValid()) {
			authenticate();
		}
		initSessionSaverBeacon();
	}

	/**
	 * 
	 */
	private void initSessionSaverBeacon() {
		int length = getSessionLength() * 1000; // in ms
		timer = new Timer(true);
		SessionSaverBeacon beacon = new SessionSaverBeacon();
		timer.schedule(beacon, length, length);
	}

	/**
	 * @return
	 */
	private int getSessionLength() {
		GetSessionLengthMethod method = new GetSessionLengthMethod();
		try {
			initiateCall(method);
		} catch (UniformAppletException e) {
			return MINIMUM_SESSION_LENGTH;
		}
		return method.getResult().intValue();
	}

	/**
	 * 
	 */
	private void authenticate() {
		if (userId == null || password == null) {
			LoginDialog dialog = new LoginDialog(SystemContext.getFrameOwner());
			dialog.setVisible(true); // controls are locked here
			int option = dialog.getOption();
			if (option == LoginDialog.OK_OPTION) {
				UserCredential uc = dialog.getCredentials();
				userId = uc.getUsername();
				password = uc.getPassword();
			} else {
				String bundleName = LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN;
				ResourceBundle bundle = ResourceBundleProvider
						.getBundle(bundleName);
				String message = bundle
						.getString(LanguageConstants.DIALOG_LOGIN_EXIT_NOTIFICATION);
				Frame parent = SystemContext.getFrameOwner();
				option = JOptionPane.showConfirmDialog(parent, message);
				if (option == JOptionPane.YES_OPTION) {
					// quit application
					// maybe like this
					// SystemContext.requestAbnormalApplicationTermination();
				} else {
					// jao majko mila zakompliciralo se.
				}
			}
		}
		Credentials c = new UsernamePasswordCredentials(userId, password);
		String host = properties.getProperty(HOST);
		AuthScope scope = new AuthScope(host, AuthScope.ANY_PORT);
		client.getState().setCredentials(scope, c);
		client.getState().clearCookies();
		client.getParams().setAuthenticationPreemptive(true);
		String url = properties.getProperty(AUTH_URL);
		GetMethod method = new GetMethod(url);
		method.setDoAuthentication(true);
		try {
			client.executeMethod(method);
		} catch (Exception e) {
			SystemLog.instance().addErrorMessage(e);
		}
		method.releaseConnection();
		restoreSessionIdFromCookie();

		/*
		 * A long comment on tomcat session handling authentication
		 */
		method = new GetMethod(url);
		method.setDoAuthentication(true);
		try {
			client.executeMethod(method);
		} catch (Exception e) {
			SystemLog.instance().addErrorMessage(e);
		}
		method.releaseConnection();

		client.getParams().setAuthenticationPreemptive(false);
	}

	/**
	 * 
	 */
	private void restoreSessionIdFromCookie() {
		Cookie[] cookies = client.getState().getCookies();
		if (cookies == null) {
			return;
		}
		for (Cookie c : cookies) {
			if (c.getName().equals(COOKIE_NAME)) {
				sessionId = c.getValue();
			}
		}
	}

	/**
	 * Reads server configuration properties file. This file contains important
	 * information, such as vhdllab server path, hostname and path for cookies
	 * etc.
	 */
	private void readServerProperties() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(SERVER_PROPERTIES);
		properties = FileUtil.getProperties(is);
	}

	/**
	 * Registers a new {@link ProtocolSocketFactory} for <code>https</code>
	 * protocol. This factory will accept self-signed certificate.
	 */
	private void registerProtocol() {
		int port = Integer.parseInt(properties.getProperty(HTTPS_PORT));
		Protocol easyhttps = new Protocol("https",
				new EasySSLProtocolSocketFactory(), port);
		Protocol.registerProtocol("https", easyhttps);
	}

	/**
	 * Initializes http client.
	 */
	private void initHttpClient() {
		client = new HttpClient();
		String host = properties.getProperty(HOST);
		String cookiePath = properties.getProperty(COOKIE_PATH);
		Cookie cookie = new Cookie(host, COOKIE_NAME, sessionId, cookiePath,
				null, false);
		client.getState().addCookie(cookie);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
	}

	/**
	 * Returns <code>true</code> if session is valid (a server doesn't return
	 * with <code>401-UNAUTHORIZED</code> status). If however server responds
	 * with <code>401</code> status this method will return <code>false</code>.
	 * 
	 * @return <code>false</code> if server returns <code>401</code> status
	 *         code; <code>false</code> otherwise
	 */
	private boolean isSessionValid() {
		String url = properties.getProperty(AUTH_URL);
		GetMethod method = new GetMethod(url);
		// session cookies is already set here!
		int status;
		try {
			status = client.executeMethod(method);
		} catch (Exception e) {
			SystemLog.instance().addErrorMessage(e);
			return false;
		}
		method.releaseConnection();
		return status != HttpStatus.SC_UNAUTHORIZED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#dispose()
	 */
	@Override
	public void dispose() {
		timer.cancel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#initiateCall(hr.fer.zemris.vhdllab.communicaton.IMethod)
	 */
	@Override
	public <T extends Serializable> void initiateCall(IMethod<T> method)
			throws UniformAppletException {
		String url = properties.getProperty(SERVLET_URL);
		PostMethod postMethod = new PostMethod(url);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(BYTE_ARRAY_SIZE);
		ObjectOutputStream oos;
		byte[] requestArray;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(method);
			requestArray = bos.toByteArray();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new UniformAppletException(e);
		}

		postMethod.setRequestEntity(new ByteArrayRequestEntity(requestArray));
		IMethod<T> returnedMethod;
		try {
//			System.out.println("in method cookies");
//			for (Cookie c : client.getState().getCookies()) {
//				System.out.println(c.getName() + "=" + c.getValue() + "///"
//						+ c.getDomain() + "$$" + c.getPath() + "##"
//						+ c.getExpiryDate() + "##" + c.getSecure());
//			}

			int executeMethod = client.executeMethod(postMethod);
			if (executeMethod == 401) {
				authenticate();
				// System.out.println("*******trying to authenticate");
				// client.getState().setCredentials(AuthScope.ANY, new
				// UsernamePasswordCredentials("test", "a"));
				// postMethod.setDoAuthentication(true);
				// executeMethod = client.executeMethod(postMethod);
				// client.getState().setCredentials(AuthScope.ANY, null);
				// System.out.println(client.getState().getCredentials(AuthScope.ANY));
			}
			System.out.println(executeMethod);
			InputStream is = postMethod.getResponseBodyAsStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			returnedMethod = (IMethod<T>) ois.readObject();

			postMethod.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new UniformAppletException("AJAX connection problems", e);
		}

		if (!returnedMethod.getMethod().equals(method.getMethod())) {
			throw new UniformAppletException();
		}

		if (returnedMethod.getStatusCode() != MethodConstants.STATUS_OK) {
			throw new UniformAppletException();
		}

		method.setStatus(returnedMethod.getStatusCode(), returnedMethod
				.getStatusMessage());
		method.setResult(returnedMethod.getResult());
	}

	private class SessionSaverBeacon extends TimerTask {
		@Override
		public void run() {
			String url = properties.getProperty(AUTH_URL);
			GetMethod method = new GetMethod(url);
			try {
				client.executeMethod(method);
			} catch (Exception e) {
				SystemLog.instance().addErrorMessage(e);
			}
			method.releaseConnection();
		}
	}
	
}
