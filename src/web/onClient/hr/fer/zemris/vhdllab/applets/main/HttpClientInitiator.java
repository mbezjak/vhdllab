package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.client.core.SystemContext;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
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
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
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
	 * A properties in server configuration file. Note that access to properties
	 * is not synchronized. This is because this properties will not change its
	 * data! Once properties are set they will remain constant for as long as
	 * this initiator is in use!
	 */
	private Properties properties;

	/**
	 * A session identifier. Stored here only for {@link #init()} method! Other
	 * then that this field is not used!
	 */
	private String sessionId;

	/**
	 * A cached user credentials that is automatically used to authenticate a
	 * user.
	 */
	private Credentials credentials;

	/**
	 * A timer for executing a task that saves session.
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
	 *            an initial session identifier
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
	public void init() throws UniformAppletException {
		readServerProperties();
		registerProtocol();
		initHttpClient();
		if (!isSessionValid()) {
			authenticate();
		}
		initSessionSaverBeacon();
		/*
		 * Dispose of session identifier (it is no longer needed because a
		 * session cookie is inside of http client).
		 */
		sessionId = null;
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

		// set session cookie
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
		/*
		 * This method is called only once: during initialization!
		 */
		String url = properties.getProperty(AUTH_URL);
		GetMethod method = new GetMethod(url);
		// session cookies is already set here!
		int status;
		try {
			status = execute(method, true);
		} catch (UniformAppletException e) {
			return false;
		}
		return status != HttpStatus.SC_UNAUTHORIZED;
	}

	/**
	 * Initializes a timer that executes task that saves session.
	 */
	private void initSessionSaverBeacon() {
		int length = getSessionLength() * 1000; // in ms
		timer = new Timer(true);
		SessionSaverBeacon beacon = new SessionSaverBeacon();
		timer.schedule(beacon, length, length);
	}

	/**
	 * Returns a maximum inactive interval before a session is invalidated.
	 * 
	 * @return a maximum inactive interval before a session is invalidated
	 */
	private int getSessionLength() {
		GetSessionLengthMethod method = new GetSessionLengthMethod();
		try {
			initiateCall(method);
		} catch (UniformAppletException e) {
			return MINIMUM_SESSION_LENGTH;
		}
		int length = method.getResult().intValue();
		if (length < MINIMUM_SESSION_LENGTH) {
			length = MINIMUM_SESSION_LENGTH;
		}
		return length - (MINIMUM_SESSION_LENGTH / 2);
	}

	/**
	 * @throws UniformAppletException 
	 */
	private void authenticate() throws UniformAppletException {
		if (credentials == null) {
			// also sets credentials
			try {
				showLoginDialog();
			} catch (SecurityException e) {
				throw new UniformAppletException(e);
			}
		}
		String host = properties.getProperty(HOST);
		AuthScope scope = new AuthScope(host, AuthScope.ANY_PORT);
		client.getState().clearCookies();
		client.getState().setCredentials(scope, credentials);
		client.getParams().setAuthenticationPreemptive(true);
		String url = properties.getProperty(AUTH_URL);
		GetMethod method = new GetMethod(url);
		method.setDoAuthentication(true);
		int status = execute(method, true);
		System.out.println(status);
		if(status == HttpStatus.SC_UNAUTHORIZED) {
			credentials = null;
			client.getState().clearCredentials();
			authenticate(); // TODO tu stavit retry message u login dialog
		} else {
			Cookie[] cookies = client.getState().getCookies();
			if(cookies == null) {
				// TODO nesto!!
			}
			for(Cookie c : cookies) {
				if(c.getName().equals(COOKIE_NAME)) {
					c.setSecure(false);
				}
			}
			
			/*
			 * A long comment on tomcat session handling authentication
			 */
			method = new GetMethod(url);
			method.setDoAuthentication(true);
			status = execute(method, true);
			System.out.println(status);

			client.getParams().setAuthenticationPreemptive(false);
			client.getState().clearCredentials();
		}
	}

	/**
	 * Show a login dialog to a user and requests that a user enters username
	 * and password that is needed to authenticate user. This method also sets
	 * credentials (a private field).
	 * 
	 * @throws SecurityException
	 *             if user refused to provider proper username and password
	 */
	private void showLoginDialog() {
		/*
		 * This method only insures that showLoginDialogImpl method is invoked
		 * by EDT.
		 */
		if (SwingUtilities.isEventDispatchThread()) {
			showLoginDialogImpl();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						showLoginDialogImpl();
					}
				});
			} catch (InterruptedException e) {
				SystemLog.instance().addErrorMessage(e);
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				if (cause instanceof SecurityException) {
					throw new SecurityException(cause);
				} else {
					SystemLog.instance().addErrorMessage(e);
				}
			}
		}
	}

	/**
	 * An actual implementation of showLoginDialog method. This method must be
	 * invoked by EDT!
	 */
	private void showLoginDialogImpl() {
		Frame owner = SystemContext.getFrameOwner();
		LoginDialog dialog = new LoginDialog(owner);
		dialog.setVisible(true); // controls are locked here
		int option = dialog.getOption();
		if (option == LoginDialog.OK_OPTION) {
			UserCredential uc = dialog.getCredentials();
			String userId = uc.getUsername();
			String password = uc.getPassword();
			Credentials c = new UsernamePasswordCredentials(userId, password);
			credentials = c;
		} else {
			String name = LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN;
			String key = LanguageConstants.DIALOG_LOGIN_CANCELED;
			String text = ResourceBundleProvider.getBundle(name).getString(key);
			option = JOptionPane.showConfirmDialog(owner, text);
			if (option == JOptionPane.YES_OPTION) {
				throw new SecurityException(
						"User refused to provide proper username and password");
			} else {
				// show login dialog again
				showLoginDialogImpl();
				// after recursive loop simply exit
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#dispose()
	 */
	@Override
	public void dispose() {
		timer.cancel();
		properties.clear();
		credentials = null;
		client = null;
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
			System.out.println("in method cookies");
			for (Cookie c : client.getState().getCookies()) {
				System.out.println(c.getName() + "=" + c.getValue() + "///"
						+ c.getDomain() + "$$" + c.getPath() + "##"
						+ c.getExpiryDate() + "##" + c.getSecure());
			}

			int executeMethod = client.executeMethod(postMethod);
			if (executeMethod == 401) {
				// authenticate();
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

	/**
	 * Executes a method in http client and returns a status code.
	 * 
	 * @param method
	 *            a method to execute
	 * @param shouldRelease
	 *            a flag indicating if a specified method should release
	 *            connection at the end
	 * @return a http status code
	 * @throws UniformAppletException
	 *             if any exception occurred
	 */
	private int execute(HttpMethod method, boolean shouldRelease)
			throws UniformAppletException {
		try {
			return client.executeMethod(method);
		} catch (HttpException e) {
			// a http exception should never occur, so just log it
			SystemLog.instance().addErrorMessage(e);
			throw new UniformAppletException(e);
		} catch (IOException e) {
			SystemLog log = SystemLog.instance();
			log.addErrorMessage(e); // log an exception
			String name = LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN;
			String key = LanguageConstants.STATUSBAR_NO_CONNECTION;
			String text = ResourceBundleProvider.getBundle(name).getString(key);
			// report an error (a custom message for a user to see)
			log.addSystemMessage(text, MessageType.ERROR);
			// rethrow as UniformAppletException
			throw new UniformAppletException(e);
		} finally {
			if (shouldRelease) {
				method.releaseConnection();
			}
		}
	}

	/**
	 * A timer task that saves a session by sending a beacon http request so
	 * that a server does not invalidate a session because user was inactive for
	 * a longer period of time.
	 * 
	 * @author Miro Bezjak
	 * @version 1.0
	 * @since 7/9/2007
	 */
	private class SessionSaverBeacon extends TimerTask {
		@Override
		public void run() {
			String url = properties.getProperty(AUTH_URL);
			GetMethod method = new GetMethod(url);
			try {
				execute(method, true);
			} catch (Exception ignored) {
				// ignored because exxceptions are already logged!
			}
		}
	}

}
