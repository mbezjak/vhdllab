package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 * Implementation of <code>Initiator</code> interface that uses
 * <code>HttpClient</code> to initiate requests to server.
 * 
 * @author Miro Bezjak
 */
public class HttpClientInitiator implements Initiator {

	/** Mediator responsible for initiating requests to server */
	private HttpClient client;
	private String basecode;

	/**
	 * Constructor.
	 * 
	 * @param client
	 *            an <code>HttpClient</code> that is responsible for
	 *            initiating requests to server
	 * @throws NullPointerException
	 *             if <code>ajax</code> is <code>null</code>
	 */
	public HttpClientInitiator(String basecode) {
		this.basecode = basecode;
		this.client = new HttpClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#initiateCall(java.util.Properties)
	 */
	public Properties initiateCall(Properties request)
			throws UniformAppletException {
		String method = request.getProperty(MethodConstants.PROP_METHOD, "");
		PostMethod postMethod = new PostMethod(basecode + "doAjax");
		postMethod.setRequestEntity(new StringRequestEntity(XMLUtil.serializeProperties(request)));
		String responseText = null;
		try {
			int executeMethod = client.executeMethod(postMethod);
//			System.out.println(executeMethod);
//			responseText = postMethod.getResponseBodyAsString();
			responseText = FileUtil.readFile(postMethod.getResponseBodyAsStream());
//			System.out.println(responseText);
			postMethod.releaseConnection();
//			System.out.println("*****************************************");
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
