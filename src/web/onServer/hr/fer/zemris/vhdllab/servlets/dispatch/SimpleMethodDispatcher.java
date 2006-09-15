package hr.fer.zemris.vhdllab.servlets.dispatch;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.MethodDispatcher;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Map;
import java.util.Properties;

public class SimpleMethodDispatcher implements MethodDispatcher {

	public Properties preformMethodDispatching(Properties p, Map<String, RegisteredMethod> regMap, VHDLLabManager labman) {
		if(p==null) throw new NullPointerException("Properties can not be null!");
		if(regMap==null) throw new NullPointerException("A map of registrated methods can not be null!");
		if(labman==null) throw new NullPointerException("A lab manager can not be null!");
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		RegisteredMethod regMethod = regMap.get(method);
		if(regMethod==null) return errorProperties(MethodConstants.SE_INVALID_METHOD_CALL, "Invalid method called!");
		return regMethod.run(p, labman);
	}

	/**
	 * This method is called if errors occur.
	 * @param errNo error message number
	 * @param errorMessage error message to pass back to caller
	 * @return a response Properties
	 */
	private Properties errorProperties(String errNo, String errorMessage) {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_STATUS,errNo);
		resProp.setProperty(MethodConstants.PROP_STATUS_CONTENT,errorMessage);
		return resProp;
	}
}