package hr.fer.zemris.vhdllab.servlets;

import java.util.Properties;

/**
 * This interface represents one registered method for a given
 * request. All registered methods are registered in
 * {@linkplain hr.fer.zemris.vhdllab.servlets.MethodFactory}
 * 
 * @author Miro Bezjak
 * @see MethodFactory#getMethod(String)
 */
public interface RegisteredMethod {
	
	/**
	 * This method is called when cirtain request is received.
	 * 
	 * @param p Properties representing accepted request
	 * @param mprov a manager provider
	 * @return a response Properties
	 */
	public Properties run(Properties p,	ManagerProvider mprov);
	
}