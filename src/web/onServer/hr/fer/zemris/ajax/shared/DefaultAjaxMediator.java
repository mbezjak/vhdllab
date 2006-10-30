package hr.fer.zemris.ajax.shared;


import java.applet.Applet;
import java.util.ArrayList;
import java.util.List;

import netscape.javascript.JSObject;

/**
 * This is default implementation of AjaxMediator interface.
 * 
 * @author marcupic
 *
 */
public class DefaultAjaxMediator implements AjaxMediator {
	/**
	 * List of registered listeners.
	 */
	private List<AjaxOpListener> listeners = new ArrayList<AjaxOpListener>();
	/**
	 * Applet for which this class is mediator. Applet is needed
	 * in order to establish a communication among Java and JavaScript
	 * in Web browser.
	 */
	private Applet applet;
	/**
	 * Which URL will be used as defualt URL for AJAX requests.
	 */
	private String requestURL;
	
	/**
	 * Constructor.
	 * @param applet applet for which this object mediates AJAX calls
	 */
	public DefaultAjaxMediator(Applet applet) {
		super();
		this.applet = applet;
		this.requestURL = null;
	}
	
	/**
	 * Constructor.
	 * @param applet applet for which this object mediates AJAX calls
	 * @param requestURL URL to be used as defualt URL for AJAX requests; can be null
	 */
	public DefaultAjaxMediator(Applet applet, String requestURL) {
		super();
		this.applet = applet;
		this.requestURL = requestURL;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.AjaxMediator#registerResultListener(hr.fer.zemris.ajax.shared.AjaxOpListener)
	 */
	public void registerResultListener(AjaxOpListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.AjaxMediator#initiateAsynchronousCall(java.lang.String)
	 */
	public void initiateAsynchronousCall(String message) {
		privInitiateAsynchronousCall(null, message);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.AjaxMediator#initiateAsynchronousCall(java.lang.String, java.lang.String)
	 */
	public void initiateAsynchronousCall(String targetURL, String message) {
		privInitiateAsynchronousCall(targetURL, message);
	}

	/**
	 * Initiates asynchronous AJAX call. Target can be null.
	 * When this method is called, following occus<br>
	 * <ul>
	 * <li>if targetURL is not null, this is used as AJAX url</li>
	 * <li>if targetURL is null but requestURL is not, requestURL is used as AJAX url</li>
	 * <li>if previous two steps found an URL to which AJAX call must be
	 *     performed, then JavaScript method<br><code>performAjaxCallEx(URL, message)</code><br>will
	 *     be called to initiate AJAX call. If no URL was found, then JavaScript method<br>
	 *     <code>performAjaxCall(message)</code><br>will be called to initiate AJAX call.</li>
	 * </ul>
	 * @param targetURL target URL for AJAX call
	 * @param message message to be sent to server using AJAX
	 */
	protected void privInitiateAsynchronousCall(String targetURL, String message) {
		JSObject win = JSObject.getWindow(applet);
		if(targetURL==null) {
			targetURL = requestURL;
		}
		if(targetURL==null) {
			win.call("performAjaxCall",new Object[] {message});
		} else {
			win.call("performAjaxCallEx",new Object[] {targetURL, message});
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.AjaxMediator#initiateSynchronousCall(java.lang.String)
	 */
	public String initiateSynchronousCall(String message) {
		return privInitiateSynchronousCall(null, message);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.AjaxMediator#initiateSynchronousCall(java.lang.String, java.lang.String)
	 */
	public String initiateSynchronousCall(String targetURL, String message) {
		return privInitiateSynchronousCall(targetURL, message);
	}

	/**
	 * Initiates synchronous AJAX call. Target can be null.
	 * When this method is called, following occus<br>
	 * <ul>
	 * <li>if targetURL is not null, this is used as AJAX url</li>
	 * <li>if targetURL is null but requestURL is not, requestURL is used as AJAX url</li>
	 * <li>if previous two steps found an URL to which AJAX call must be
	 *     performed, then JavaScript method<br><code>performSyncAjaxCallEx(URL, message)</code><br>will
	 *     be called to initiate AJAX call. If no URL was found, then JavaScript method<br>
	 *     <code>performSyncAjaxCall(message)</code><br>will be called to initiate AJAX call.</li>
	 * </ul>
	 * @param targetURL target URL for AJAX call
	 * @param message message to be sent to server using AJAX
	 * @return response text
	 */

	protected String privInitiateSynchronousCall(String targetURL, String message) {
		JSObject win = JSObject.getWindow(applet);
		if(targetURL==null) {
			targetURL = requestURL;
		}
		Object response;
		if(targetURL==null) {
			response = win.call("performAjaxCall",new Object[] {message});
		} else {
			response = win.call("performAjaxCallEx",new Object[] {targetURL, message});
		}
		return response.toString();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.AjaxMediator#fireResultReceived(java.lang.String, int)
	 */
	public void fireResultReceived(String result, int code) {
		for(AjaxOpListener l : listeners) {
			l.resultReceived(result, code);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.AjaxMediator#initiateAbort()
	 */
	public void initiateAbort() {
		fireResultReceived(null,-1);
		JSObject win = JSObject.getWindow(applet);
		win.call("performAjaxAbort",new Object[] {});
	}
}