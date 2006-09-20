package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.util.Map;
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
	 * @param regMap a map of registered methods.
	 * @param labman a VHDL lab manager.
	 * @return a response Properties.
	 */
	public Properties preformMethodDispatching(Properties p,
						Map<String, RegisteredMethod> regMap,
						VHDLLabManager labman);
	
}
