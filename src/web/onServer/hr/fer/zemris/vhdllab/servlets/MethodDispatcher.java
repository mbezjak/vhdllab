package hr.fer.zemris.vhdllab.servlets;

import java.util.Properties;

/**
 * This interface must resolve a method and call a registered
 * method to deal with it.
 * @author Miro Bezjak
 */
public interface MethodDispatcher {
	
	/**
	 * This method resolves a method and calls a registered
	 * method to deal with it.
	 * @param p a properties that will be processed.
	 * @param mprov a manager provider.
	 * @return a response Properties.
	 */
	public Properties preformMethodDispatching(Properties p,
						ManagerProvider mprov);
	
}
