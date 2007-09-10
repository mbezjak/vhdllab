package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.communicaton.Method;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface must resolve a method and call a registered
 * method to deal with it.
 * @author Miro Bezjak
 */
public interface MethodDispatcher {
	
	/**
	 * This method resolves a method and calls a registered
	 * method to deal with it.
	 * @param method a method that will be processed
	 * @param provider a manager provider
	 */
	public void preformMethodDispatching(Method<Serializable> method,
						ManagerProvider provider, HttpServletRequest request);
	
}
