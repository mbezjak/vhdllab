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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

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
	 * @param basecode
	 *            a base codes
	 */
	public HttpClientInitiator(String basecode) {
		this.basecode = basecode;
		this.client = new HttpClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator#initiateCall(hr.fer.zemris.vhdllab.communicaton.IMethod)
	 */
	@Override
	public <T extends Serializable> void initiateCall(IMethod<T> method)
			throws UniformAppletException {
		PostMethod postMethod = new PostMethod(basecode + "doAjax");
		ByteArrayOutputStream bos = new ByteArrayOutputStream(200);
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
			int executeMethod = client.executeMethod(postMethod);
			InputStream is = postMethod.getResponseBodyAsStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			returnedMethod = (IMethod<T>) ois.readObject();
			postMethod.releaseConnection();
		} catch (Exception e) {
			throw new UniformAppletException("AJAX connection problems", e);
		}
		
		if(!returnedMethod.getMethod().equals(method.getMethod())) {
			throw new UniformAppletException();
		}

		if (returnedMethod.getStatusCode() != MethodConstants.STATUS_OK) {
			throw new UniformAppletException();
		}
		
		method.setStatus(returnedMethod.getStatusCode(), returnedMethod.getStatusMessage());
		method.setResult(returnedMethod.getResult());
	}

}
