/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton;

import java.io.Serializable;

/**
 * @param <T>
 * @author Miro Bezjak
 */
public interface IMethod<T extends Serializable> extends Serializable, MethodConstants {

	String getMethod();
	
	String getFingerprint();
	
	String getUserId();
	
	Object getParameter(String param);
	
	<X> X getParameter(Class<X> clazz, String param);
	
	void setResult(T result);
	
	T getResult();
	
	void setStatus(int statusCode);
	
	void setStatus(int statusCode, String message);
	
	int getStatusCode();
	
	String getStatusMessage();



}
