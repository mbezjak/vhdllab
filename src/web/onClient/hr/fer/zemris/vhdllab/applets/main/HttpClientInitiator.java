package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.client.core.SystemContext;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.dialogs.login.LoginDialog;
import hr.fer.zemris.vhdllab.client.dialogs.login.UserCredential;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.communicaton.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.methods.GetSessionLengthMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LogoutMethod;
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
 * <p>
 * This implementation is designed to work with Apache Tomcat powered server
 * (preferably with version 6) and if server is changed then there is a chance
 * that this implementation will have to change aswell.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 7/9/2007
 */
public final class HttpClientInitiator implements Initiator {
	/*
	 * If server changes this things possibly needs to be modified in order for
	 * this initiator to work. COOKIE_NAME (a private field) could need
	 * changing. Also check authentication method to see if it needs to be
	 * modified.
	 */

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
	 * An estimate of how big an object (a method that is sent to server) will
	 * be after serialization (1KB).
	 */
	private static final int BYTE_ARRAY_SIZE = 1000;

	/**
	 * A minimum session length. Length is in seconds.
	 */
	private static final int MINIMUM_SESSION_LENGTH = 60;

	/**
	 * A properties in server configuration file. Note that access to properties
	 * is not synchronized. This is because properties will not change its data!
	 * Once properties are set they will remain constant for as long as this
	 * initiator is in use!
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
	private UsernamePasswordCredentials credentials;

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
	@SuppressWarnings("unused")
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
	 * Authenticates user. This method will first try to authenticate user using
	 * cached credentials and if that fails or there is no cached credentials
	 * this method will invoke {@link #showLoginDialog(boolean)} to ask user for
	 * proper credentials.
	 * 
	 * @throws UniformAppletException
	 *             if any exception occurs such as: user refused to provider
	 *             proper credentials or server is not responding etc.
	 */
	private void authenticate() throws UniformAppletException {
		authenticate(false);
	}

	/**
	 * Authenticates user. This method will first try to authenticate user using
	 * cached credentials and if that fails or there is no cached credentials
	 * this method will invoke {@link #showLoginDialog(boolean)} to ask user for
	 * proper credentials.
	 * 
	 * @param displayRetryMessage
	 *            a flag indicating if login dialog should show retry message
	 *            (not a default message when it says that authentication is
	 *            required but a message saying that he should try again because
	 *            previous login attempt failed)
	 * @throws UniformAppletException
	 *             if any exception occurs such as: user refused to provider
	 *             proper credentials or server is not responding etc.
	 */
	private void authenticate(boolean displayRetryMessage)
			throws UniformAppletException {
		if (credentials == null) {
			try {
				// also sets credentials
				showLoginDialog(displayRetryMessage);
			} catch (SecurityException e) {
				throw new UniformAppletException(e);
			}
		}
		client.getState().clearCookies();
		String host = properties.getProperty(HOST);
		AuthScope scope = new AuthScope(host, AuthScope.ANY_PORT);
		client.getState().setCredentials(scope, credentials);
		client.getParams().setAuthenticationPreemptive(true);
		String url = properties.getProperty(AUTH_URL);

		/*
		 * Send method to authenticate user.
		 */
		GetMethod method = new GetMethod(url);
		method.setDoAuthentication(true);
		int status = execute(method, true);
		if (status == HttpStatus.SC_UNAUTHORIZED) {
			// retry authentication if current authentication failed
			credentials = null;
			client.getState().clearCredentials();
			authenticate(true);
		} else {
			Cookie[] cookies = client.getState().getCookies();
			if (cookies == null) {
				/*
				 * This must never happen because it would indicate that server
				 * did not create session cookie or it is using some other
				 * method of session tracking. This implementation of initiator
				 * depends on session tracking through cookie! If server changes
				 * implementation of session tracking then so must this
				 * HttpClientInititator implementation. Until then this method
				 * throws exception.
				 */
				throw new UniformAppletException(
						"Unknown session tracking mechanism or server didn't return appropriate session cookie");
			}
			/*
			 * Sets session cookie's secure flag to false.
			 * 
			 * Description: This is a possible bug in
			 * EasySSLProtocolSocketFactory or EasyX509TrustManager classes.
			 * When using secure connection (https) to authenticate, tomcat
			 * returns valid session cookie that is marked as secure (must be
			 * sent only over secure connections). Then when http client tries
			 * to send the same request again (again over https) he does not
			 * send that session cookie! However if that cookie is set as
			 * insecure (can be sent over http) everything works fine.
			 * 
			 * These classes are used in registerProtocol method!
			 * 
			 * Found in: Mozilla Firefox 2.0.0.6 (Linux version); Apache Tomcat
			 * 6.0.13; Apache HttpClient 3.0.1
			 * 
			 * @since 7/9/2007
			 */
			for (Cookie c : cookies) {
				if (c.getName().equals(COOKIE_NAME)) {
					c.setSecure(false);
				}
			}

			/*
			 * Following code is a workaround for a problem with tomcat session
			 * handling (most likely not a bug but whether it is a feature is
			 * still uncertain).
			 * 
			 * Description: It appears that tomcat authentication and session
			 * management is a little fuzzy. This is a process for
			 * authenticating to tomcat and using its session tracking (using
			 * BASIC authentication scheme and cookie based session tracking).
			 * First when a request is made to secured content with an
			 * authenticate header tomcat returns a cookie with specific name
			 * and session id value. However this session is still new since
			 * client has not reported that he accepts this session (and thus
			 * session tracking). This would not be a problem since next request
			 * would simply send this cookie and thus accepting session
			 * tracking. From there on client could only send that session
			 * cookie (no longer would authentication data needed to be sent in
			 * every request) and he would have access to secured content.
			 * However it appears that since this session is new, when client
			 * does sent request (and thus accepting session tracing), tomcat
			 * expects an authentication header aswell. It does not matter that
			 * client obtained this session by sending authentication header.
			 * There for this next code requests the same content with providing
			 * authentication header (because no credentials was cleared from
			 * HttpState) and the same returned session cookie.
			 * 
			 * Found in: Apache Tomcat 6.0.13 (also in 5.5.23)
			 * 
			 * This problem was tested using regular java sockets that connected
			 * to Apache Tomcat.
			 * 
			 * @since 7/9/2007
			 */
			method = new GetMethod(url);
			method.setDoAuthentication(true);
			execute(method, true);

			/*
			 * Authentication cleanup (clears credentials aswell because from
			 * here on it uses session identifier instead).
			 */
			client.getParams().setAuthenticationPreemptive(false);
			client.getState().clearCredentials();
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
	 * @throws SecurityException
	 *             if user refused to provider proper username and password
	 */
	private void showLoginDialog(final boolean displayRetryMessage) {
		/*
		 * This method only insures that showLoginDialogImpl method is invoked
		 * by EDT.
		 */
		if (SwingUtilities.isEventDispatchThread()) {
			showLoginDialogImpl(displayRetryMessage);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						showLoginDialogImpl(displayRetryMessage);
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
	 * 
	 * @param displayRetryMessage
	 *            a flag indicating if login dialog should show retry message
	 *            (not a default message when it says that authentication is
	 *            required but a message saying that he should try again because
	 *            previous login attempt failed)
	 * @throws SecurityException
	 *             if user refused to provider proper username and password
	 */
	private void showLoginDialogImpl(boolean displayRetryMessage) {
		Frame owner = SystemContext.getFrameOwner();
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
			UserCredential uc = dialog.getCredentials();
			String userId = uc.getUsername();
			String password = uc.getPassword();
			credentials = new UsernamePasswordCredentials(userId, password);
		} else {
			String name = LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN;
			String key = LanguageConstants.DIALOG_LOGIN_CANCELED;
			String text = ResourceBundleProvider.getBundle(name).getString(key);
			option = JOptionPane.showConfirmDialog(owner, text);
			if (option == JOptionPane.YES_OPTION) {
				throw new SecurityException(
						"User refused to provide proper username and password");
			} else {
				/*
				 * Since this is a recursive loop and a user controls it, by
				 * pressing no to a confirm dialog and a cancel to login dialog,
				 * may lead to OutOfMemoryError but since these conditions are
				 * so extreme (because user would have to repeat this loop
				 * possibly thousands or even millions of times) there is no
				 * need to prevent OutOfMemoryError.
				 */
				// show login dialog again
				showLoginDialogImpl(displayRetryMessage);
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
		// send a logout method
		LogoutMethod method = new LogoutMethod();
		try {
			initiateCall(method);
		} catch (UniformAppletException ignored) {
		}
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
	public <T extends Serializable> void initiateCall(Method<T> requestMethod)
			throws UniformAppletException {
		String url = properties.getProperty(SERVLET_URL);
		PostMethod postMethod = new PostMethod(url);
		byte[] requestArray = serializeObject(requestMethod);
		postMethod.setRequestEntity(new ByteArrayRequestEntity(requestArray));

		int status = execute(postMethod, false);
		if (status == HttpStatus.SC_UNAUTHORIZED) {
			postMethod.releaseConnection();
			authenticate();
			status = execute(postMethod, false);
			// reset userId in system context
			SystemContext.setUserId(credentials.getUserName());
			// reset userId in request method
			requestMethod.setUserId(SystemContext.getUserId());
		}
		if (status != HttpStatus.SC_OK) {
			throw new UniformAppletException("Unexpected http status code: "
					+ status);
		}

		Method<T> responseMethod = getResult(postMethod);
		postMethod.releaseConnection();

		try {
			requestMethod.join(responseMethod);
		} catch (IllegalArgumentException e) {
			throw new UniformAppletException(e);
		}
		if (requestMethod.getStatusCode() != MethodConstants.STATUS_OK) {
			throw new UniformAppletException("Returned status code is "
					+ requestMethod.getStatusCode());
		}
	}

	/**
	 * Serializes a specified method.
	 * 
	 * @param <T>
	 *            a method's result type
	 * @param method
	 *            a specified method
	 * @return serialized method in a form of bytes
	 * @throws UniformAppletException
	 *             if any exception occurs (such as IOException)
	 */
	private <T extends Serializable> byte[] serializeObject(Method<T> method)
			throws UniformAppletException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(BYTE_ARRAY_SIZE);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(method);
		} catch (IOException e) {
			SystemLog.instance().addErrorMessage(e);
			throw new UniformAppletException(e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException ignored) {
				}
			}
		}
		return bos.toByteArray();
	}

	/**
	 * Deserializes a method and returns it.
	 * 
	 * @param <T>
	 *            a method's result type
	 * @param method
	 *            a http method from where to extract serialized method
	 * @return a method
	 * @throws UniformAppletException
	 *             if any exception occurs (such as IOException,
	 *             ClassNotFoundException etc.)
	 */
	@SuppressWarnings("unchecked")
	private <T extends Serializable> Method<T> getResult(HttpMethod method)
			throws UniformAppletException {
		ObjectInputStream ois = null;
		try {
			InputStream is = method.getResponseBodyAsStream();
			ois = new ObjectInputStream(is);
			return (Method<T>) ois.readObject();
		} catch (IOException e) {
			SystemLog.instance().addErrorMessage(e);
			throw new UniformAppletException(e);
		} catch (ClassNotFoundException e) {
			SystemLog.instance().addErrorMessage(e);
			throw new UniformAppletException(e);
		} catch (ClassCastException e) {
			SystemLog.instance().addErrorMessage(e);
			throw new UniformAppletException(e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException ignored) {
				}
			}
		}
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
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			String url = properties.getProperty(AUTH_URL);
			GetMethod method = new GetMethod(url);
			try {
				execute(method, true);
			} catch (Exception ignored) {
				// ignored because exceptions are already logged!
			}
		}
	}

}
