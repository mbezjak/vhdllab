package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;

import java.util.Properties;

/**
 * Implementation of <code>Initiator</code> interface that uses
 * <code>AjaxMediator</code> to initiate requests to server.
 * 
 * @author Miro Bezjak
 */
public class AjaxInitiator implements Initiator {

	/** Mediator responsible for initiating requests to server */
	private AjaxMediator ajax;

	/**
	 * Constructor.
	 * 
	 * @param ajax
	 *            an <code>AjaxMediator</code> that is responsible for
	 *            initiating requests to server
	 * @throws NullPointerException
	 *             if <code>ajax</code> is <code>null</code>
	 */
	public AjaxInitiator(AjaxMediator ajax) {
		if (ajax == null) {
			throw new NullPointerException("Ajax mediator can not be null");
		}
		this.ajax = ajax;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#initiateCall(java.util.Properties)
	 */
	public Properties initiateCall(Properties request)
			throws UniformAppletException {
		String method = request.getProperty(MethodConstants.PROP_METHOD, "");
		String responseText = null;
		try {
			responseText = ajax.initiateSynchronousCall(XMLUtil
					.serializeProperties(request));
		} catch (Exception e) {
			throw new UniformAppletException("AJAX connection problems", e);
		}
		if (responseText == null) {
			throw new UniformAppletException("AJAX connection problems");
		}

		Properties response = XMLUtil.deserializeProperties(responseText);
		String resMethod = response.getProperty(MethodConstants.PROP_METHOD);
		if (!method.equalsIgnoreCase(resMethod)) {
			throw new UniformAppletException(
					"Wrong method returned! Expected: " + method + " but was: "
							+ resMethod);
		}
		String status = response.getProperty(MethodConstants.PROP_STATUS, "");
		if (!status.equals(MethodConstants.STATUS_OK)) {
			throw new UniformAppletException(response.getProperty(
					MethodConstants.PROP_STATUS_CONTENT, "Unknown error."));
		}

		return response;
	}

}
