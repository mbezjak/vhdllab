package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.communicaton.MethodConstants;

import java.io.Serializable;

/**
 * This interface represents one registered method for a given request. All
 * registered methods are registered in {@link MethodFactory}
 * 
 * @author Miro Bezjak
 * @see MethodFactory#getMethod(String)
 */
public interface RegisteredMethod extends MethodConstants {

	/**
	 * This method is called when certain request is received.
	 * 
	 * @param method
	 *            a method representing accepted request
	 * @param provider
	 *            a manager provider
	 */
	public void run(IMethod<Serializable> method, ManagerProvider provider);

}