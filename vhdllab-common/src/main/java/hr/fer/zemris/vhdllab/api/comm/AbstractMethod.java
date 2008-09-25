/**
 * 
 */
package hr.fer.zemris.vhdllab.api.comm;

import hr.fer.zemris.vhdllab.entities.Caseless;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Miro Bezjak
 * @param <T>
 */
public abstract class AbstractMethod<T extends Serializable> implements
		Method<T> {

    private static final long serialVersionUID = 1L;
    
    private final String method;
	private Caseless userId;
	private final Map<String, Serializable> parameters;

	private T result;
	private int statusCode;
	private String statusMessage;

	public AbstractMethod(String method, Caseless userId) {
		if (method == null) {
			throw new NullPointerException("Method cant be null");
		}
		if (userId == null) {
			throw new NullPointerException("User identifier cant be null");
		}
		this.method = method;
		setUserId(userId);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getUserId()
	 */
	@Override
	public Caseless getUserId() {
		return userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.Method#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(Caseless userId) {
		if(userId == null) {
			throw new NullPointerException("User identifier cant be null");
		}
		this.userId = userId;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getResult()
	 */
	@Override
	public T getResult() {
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.Method#join(hr.fer.zemris.vhdllab.communicaton.Method)
	 */
	@Override
	public void join(Method<T> joinMethod) {
		if (joinMethod == null) {
			throw new NullPointerException("Method cant be null");
		}
		if(!this.equals(joinMethod)) {
			throw new IllegalArgumentException("Incompatible methods");
		}
		result = joinMethod.getResult();
		statusCode = joinMethod.getStatusCode();
		statusMessage = joinMethod.getStatusMessage();
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hashResult = 1;
		hashResult = prime * hashResult + ((method == null) ? 0 : method.hashCode());
		hashResult = prime * hashResult
				+ ((parameters == null) ? 0 : parameters.hashCode());
		hashResult = prime * hashResult + ((userId == null) ? 0 : userId.hashCode());
		return hashResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractMethod))
			return false;
		final AbstractMethod<?> other = (AbstractMethod<?>) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
