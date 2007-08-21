/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.Properties;

/**
 * @author Miro Bezjak
 * @param <T>
 */
public abstract class AbstractMethod<T> implements IMethod<T> {

	private Properties p;

	public AbstractMethod(String method) {
		if (method == null) {
			throw new NullPointerException("Method cant be null");
		}
		p = new Properties();
		p.setProperty(METHOD_KEY, method);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getMethod()
	 */
	@Override
	public String getMethod() {
		return p.getProperty(METHOD_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String paramName) {
		if (paramName == null) {
			throw new NullPointerException("Parameter name cant be null");
		}
		String property = p.getProperty(PROPERTY_PREFIX + paramName);
		if (property == null) {
			setStatus(SE_METHOD_ARGUMENT_ERROR, "Parameter " + paramName
					+ " is not set.");
		}
		return property;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getParameterAsBoolean(java.lang.String)
	 */
	@Override
	public Boolean getParameterAsBoolean(String paramName) {
		String parameter = getParameter(paramName);
		if (parameter == null) {
			return null;
		}
		return Boolean.valueOf(parameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getParameterAsDouble(java.lang.String)
	 */
	@Override
	public Double getParameterAsDouble(String paramName) {
		String parameter = getParameter(paramName);
		if (parameter == null) {
			return null;
		}
		try {
			return Double.valueOf(parameter);
		} catch (NumberFormatException e) {
			setStatus(SE_PARSE_ERROR, "Parameter " + paramName
					+ " does't contain a double value but: " + parameter);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getParameterAsInteger(java.lang.String)
	 */
	@Override
	public Integer getParameterAsInteger(String paramName) {
		String parameter = getParameter(paramName);
		if (parameter == null) {
			return null;
		}
		try {
			return Integer.valueOf(parameter);
		} catch (NumberFormatException e) {
			setStatus(SE_PARSE_ERROR, "Parameter " + paramName
					+ " does't contain an integer value but: " + parameter);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getParameterAsLong(java.lang.String)
	 */
	@Override
	public Long getParameterAsLong(String paramName) {
		String parameter = getParameter(paramName);
		if (parameter == null) {
			return null;
		}
		try {
			return Long.valueOf(parameter);
		} catch (NumberFormatException e) {
			setStatus(SE_PARSE_ERROR, "Parameter " + paramName
					+ " does't contain a long value but: " + parameter);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		try {
			return Integer.parseInt(p.getProperty(STATUS_CODE));
		} catch (NumberFormatException e) {
			return SE_STATUS_CODE_PARSE_ERROR;
		}
	}

	protected void setOKStatus() {
		setStatus(STATUS_OK);
	}

	protected void setStatus(int statusCode) {
		setStatus(statusCode, null);
	}

	protected void setStatus(int statusCode, String message) {
		p.setProperty(STATUS_CODE, String.valueOf(statusCode));
		if (message != null) {
			p.setProperty(STATUS_MESSAGE, message);
		}
	}

	protected boolean isStatusSet() {
		return p.containsKey(STATUS_CODE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#getStatusMessage()
	 */
	@Override
	public String getStatusMessage() {
		return p.getProperty(STATUS_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.communicaton.IMethod#serialize()
	 */
	@Override
	public String serialize() {
		return XMLUtil.serializeProperties(p);
	}

}
