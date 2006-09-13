package hr.fer.zemris.ajax.shared;

/**
 * This interface represents one way of simplifing communication
 * from Java to http server over AJAX technology. Objects implementing
 * this interface will know how to contact JavaScript in Web browser,
 * pass request for AJAX, accept the answer and distribute that
 * answer to all interested parties (previously registered as 
 * listeners).
 * 
 * @author marcupic
 *
 */
public interface AjaxMediator {
	/**
	 * Register listener interested for AJAX result arrival notification.
	 * @param listener listener to register
	 */
	public void registerResultListener(AjaxOpListener listener);
	/**
	 * Requests that a message is sent to the server. This call is
	 * asynchronous. When result arrives, registered listeners will
	 * be notified. Message is sent to default destination (as specified
	 * in JavaScript implementation).
	 * @param message message to send
	 */
	public void initiateAsynchronousCall(String message);
	/**
	 * Requests that a message is sent to the server. This call is
	 * asynchronous. When result arrives, registered listeners will
	 * be notified. Message is sent to specified destination, or if
	 * this is null, then to default destination (as specified
	 * in JavaScript implementation).
	 * @param targetURL where to send message (URL, can be relative)
	 * @param message message to send
	 */
	public void initiateAsynchronousCall(String targetURL, String message);
	/**
	 * This method will be called from applet when AJAX response
	 * arrives. Then, registered listeners will be notified.
	 * @param result response of the initiated call
	 * @param code HTTP status code of response, or -1 if request was aborted
	 */
	public void fireResultReceived(String result, int code);
	/**
	 * Requests abort of initiated request.
	 */
	public void initiateAbort();
}