package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.api.comm.MethodConstants;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

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
	 * @param request
	 *            a http request (used for example to logout a user, return
	 *            session information etc.)
	 */
	public void run(Method<Serializable> method, HttpServletRequest request);

}
