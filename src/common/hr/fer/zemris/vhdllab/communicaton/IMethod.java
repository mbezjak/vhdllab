/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton;

/**
 * @param <T>
 * @author Miro Bezjak
 */
public interface IMethod<T> extends MethodConstants {

	String getMethod();
	
	String getParameter(String paramName);
	
	Boolean getParameterAsBoolean(String paramName);
	
	Double getParameterAsDouble(String paramName);
	
	Integer getParameterAsInteger(String paramName);
	
	Long getParameterAsLong(String paramName);

	T getResult();
	
	int getStatusCode();
	
	String getStatusMessage();

	String serialize();

}
