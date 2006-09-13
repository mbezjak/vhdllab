package hr.fer.zemris.ajax.shared;

/**
 * This interface is not used from other Java classes.
 * However, to simplify AJAX result passing, this interface
 * specifies a single method which Applet must implement. When
 * AJAX operation in browser completes, JavaScript can then call
 * this method to pass obtained results.<br><br>
 * If AjaxMediator is used on Java side, then this method will
 * have only one purpose: to forward received data to AJAX mediator.
 * 
 * @author marcupic
 *
 */
public interface AjaxToJavaInterface {
	
	/**
	 * This method is called from JavaScript when AJAX operation
	 * completes, and passes its results.
	 * @param result response of AJAX operation
	 * @param code HTTP status code, or -1 if operation was aborted
	 */
	public void ajaxCallResultReceived(String result, int code);
	
}
