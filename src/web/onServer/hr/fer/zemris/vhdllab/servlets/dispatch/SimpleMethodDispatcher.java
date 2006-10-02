package hr.fer.zemris.vhdllab.servlets.dispatch;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.MethodDispatcher;
import hr.fer.zemris.vhdllab.servlets.MethodFactory;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class simply calls a registered method written in
 * Properties (argument of constructor).
 * @author Miro Bezjak
 */
public class SimpleMethodDispatcher implements MethodDispatcher {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.MethodDispatcher#preformMethodDispatching(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties preformMethodDispatching(Properties p, ManagerProvider mprov) {
		if(p==null) throw new NullPointerException("Properties can not be null!");
		if(mprov==null) throw new NullPointerException("Manager provider can not be null!");
		
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		RegisteredMethod regMethod = MethodFactory.getMethod(method);
		if(regMethod==null) return errorProperties(method, MethodConstants.SE_INVALID_METHOD_CALL, "Invalid method called!");
		return regMethod.run(p, mprov);
	}
	
	/**
	 * This method is called if error occurs.
	 * @param method a method that caused this error
	 * @param errNo error message number
	 * @param errorMessage error message to pass back to caller
	 * @return a response Properties
	 */
	private Properties errorProperties(String method, String errNo, String errorMessage) {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,errNo);
		resProp.setProperty(MethodConstants.PROP_STATUS_CONTENT,errorMessage);
		return resProp;
	}
}