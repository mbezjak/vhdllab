package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.communicaton.MethodConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 * Implementation of <code>Initiator</code> interface that uses
 * <code>HttpClient</code> to initiate requests to server.
 * 
 * @author Miro Bezjak
 */
public final class HttpClientInitiator implements Initiator {

	/**
	 * An estimate of how big an object will be after serialization (1KB).
	 */
	private static final int BYTE_ARRAY_SIZE = 1000;

	/** Mediator responsible for initiating requests to server */
	private final HttpClient client;
	private final String vhdllabPath;
	private final String authenticationPath;

	/**
	 * Constructor.
	 * 
	 * @param sessionId
	 * @param cookiePath
	 * @param cookieHost
	 * @param authenticationPath
	 * @param vhdllabPath
	 * @param sessionLength 
	 */
	public HttpClientInitiator(String vhdllabPath, final String authenticationPath,
			String cookieHost, String cookiePath, String sessionId, int sessionLength) {
		this.vhdllabPath = vhdllabPath;
		this.authenticationPath = authenticationPath;
		this.client = new HttpClient();
		Cookie cookie = new Cookie(cookieHost, "JSESSIONID", sessionId,
				cookiePath, null, false);
		client.getState().addCookie(cookie);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		Protocol easyhttps = new Protocol("https",
				new EasySSLProtocolSocketFactory(), 8443);
		Protocol.registerProtocol("https", easyhttps);
		
		sessionLength *= 1000;
		System.out.println(sessionLength);
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				GetMethod getMethod = new GetMethod(authenticationPath);
				try {
					client.executeMethod(getMethod);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getMethod.releaseConnection();
			}
		}, sessionLength, sessionLength);

		// Credentials credentials = new UsernamePasswordCredentials("test",
		// "a");
		// client.getState().setCredentials(AuthScope.ANY, credentials);
		// GetMethod getMethod = new
		// GetMethod("http://localhost:8080/vhdllab/vhdllab.jsp");
		// getMethod.setDoAuthentication(true);
		// try {
		// client.executeMethod(getMethod);
		// } catch (HttpException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#initiateCall(hr.fer.zemris.vhdllab.communicaton.IMethod)
	 */
	@Override
	public <T extends Serializable> void initiateCall(IMethod<T> method)
			throws UniformAppletException {
		System.out.println(vhdllabPath);
		PostMethod postMethod = new PostMethod(vhdllabPath);
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

}
