/**
 * 
 */
package hr.fer.zemris.ajax.shared;


import java.applet.Applet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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

	public void registerResultListener(AjaxOpListener listener) {
		listeners.add(listener);
	}

	public void initiateAsynchronousCall(String message) {
		privInitiateAsynchronousCall(null, message);
	}

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
		JOptionPane.showMessageDialog(null,message);
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

	public void fireResultReceived(String result, int code) {
		for(AjaxOpListener l : listeners) {
			l.resultReceived(result, code);
		}
	}

	public void initiateAbort() {
		fireResultReceived(null,-1);
		JSObject win = JSObject.getWindow(applet);
		win.call("performAjaxAbort",new Object[] {});
	}
}