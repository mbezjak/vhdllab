/**
 * 
 */
package hr.fer.zemris.ajax.shared;

/**
 * This interface represents listeners interested
 * for notification of AJAX call completion.
 * 
 * @author marcupic
 *
 */
public interface AjaxOpListener {
	/**
	 * When AJAX call completes, this method will
	 * be called with generated response and HTTP status
	 * code.
	 * @param result call generated response
	 * @param code HTTP status code, or -1 if call was aborted
	 */
	public void resultReceived(String result, int code);
}