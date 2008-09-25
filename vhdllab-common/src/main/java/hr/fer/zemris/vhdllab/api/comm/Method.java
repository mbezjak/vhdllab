package hr.fer.zemris.vhdllab.api.comm;

import hr.fer.zemris.vhdllab.entities.Caseless;

import java.io.Serializable;

/**
 * @param <T>
 * @author Miro Bezjak
 */
public interface Method<T extends Serializable> extends Serializable, MethodConstants {

	String getMethod();
	
	void setUserId(Caseless userId);

	Caseless getUserId();
	
	Object getParameter(String param);
	
	<X> X getParameter(Class<X> clazz, String param);
	
	void setResult(T result);
	
	T getResult();
	
	void setStatus(int statusCode);
	
	void setStatus(int statusCode, String message);
	
	int getStatusCode();
	
	String getStatusMessage();

	void join(Method<T> method);
	
	int hashCode();
	
	boolean equals(Object o);
	
}
