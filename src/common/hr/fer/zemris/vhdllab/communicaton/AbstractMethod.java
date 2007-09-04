/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton;

import hr.fer.zemris.vhdllab.applets.main.SystemContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Miro Bezjak
 * @param <T>
 */
public abstract class AbstractMethod<T extends Serializable> implements
		IMethod<T> {

	private String method;
	private String userId;
	private Map<String, Serializable> parameters;
	private T result;
	private int statusCode;
	private String statusMessage;

	public AbstractMethod(String method) {
		this.method = method;
		if (this.method == null) {
			throw new NullPointerException("Method cant be null");
		}
		userId = SystemContext.getUserId();
		parameters = new HashMap<String, Serializable>();
		result = null;
		statusCode = STATUS_NOT_SET;
		statusMessage = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getMethod()
	 */
	@Override
	public String getMethod() {
		return method;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getParameter(java.lang.String)
	 */
	@Override
	public Object getParameter(String param) {
		return getParameter(Object.class, param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getParameter(java.lang.Class,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <X> X getParameter(Class<X> clazz, String param) {
		if (clazz == null) {
			throw new NullPointerException("Return value class cant be null");
		}
		if (param == null) {
			throw new NullPointerException("Parameter name cant be null");
		}
		Object property = parameters.get(param);
		if (property == null) {
			setStatus(SE_METHOD_ARGUMENT_ERROR, "Parameter " + param
					+ " is not set.");
		}
		try {
			return (X) property;
		} catch (RuntimeException e) {
			setStatus(SE_PARSE_ERROR, "Parameter " + param + "=" + property
					+ " cant be casted to " + clazz.getCanonicalName());
			return null;
		}
	}

	protected <V extends Serializable> void setParameter(String param, V value) {
		if (param == null) {
			throw new NullPointerException("Parameter cant be null");
		}
		if (value == null) {
			throw new NullPointerException("Parameter value cant be null");
		}
		parameters.put(param, value);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getResult()
	 */
	@Override
	public T getResult() {
		return result;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#setResult(java.io.Serializable)
	 */
	@Override
	public void setResult(T result) {
		this.result = result;
		setStatusOK();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return statusCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getStatusMessage()
	 */
	@Override
	public String getStatusMessage() {
		return statusMessage;
	}

	protected void setStatusOK() {
		setStatus(STATUS_OK);
	}

	@Override
	public void setStatus(int statusCode) {
		setStatus(statusCode, null);
	}

	@Override
	public void setStatus(int statusCode, String message) {
		this.statusCode = statusCode;
		this.statusMessage = message;
	}

	protected boolean isStatusSet() {
		return statusCode != STATUS_NOT_SET;
	}

}
