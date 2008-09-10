package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.api.comm.MethodConstants;
import hr.fer.zemris.vhdllab.servlets.dispatch.AdvancedMethodDispatcher;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is used to return appropriate RegisteredMethod
 * to deal with cirtain methods.
 * 
 * @author Miro Bezjak
 */
public class MethodFactory {
	
	private static Map<String, RegisteredMethod> regMap;
	static {
		try {
			initializeMap();
		} catch (MethodFactoryException e) {
			e.printStackTrace();
			regMap = new HashMap<String, RegisteredMethod>();
		}
	}

	/**
	 * Returns a registered method which knows how to resolve
	 * <code>methodName</code>.
	 * @param methodName a method to deal with.
	 * @return a registered method which knows how to resolve
	 * 		   <code>methodName</code>.
	 */
	public static RegisteredMethod getMethod(String methodName) {
		if(isSimpleMethodName(methodName)) {
			return regMap.get(methodName);
		}
        return new ComplexMethodExecutor();
	}

	/**
	 * Initialize a map of registered methods. This method reads a file
	 * in order to do that.
	 * @throws MethodFactoryException if exception is thrown during initializing.
	 */
	private static void initializeMap() throws MethodFactoryException {
		regMap = new HashMap<String, RegisteredMethod>();
		InputStream in = MethodFactory.class.getResourceAsStream("registeredMethods.properties");
		if(in==null) throw new MethodFactoryException("Could not load file: registeredMethods.properties!");
		Properties properties = new Properties();
		try {
			properties.load(in);
			for(Object key : properties.keySet()){
				String name = (String) key;
				String mtdClass = properties.getProperty(name);
				RegisteredMethod regMethod = (RegisteredMethod)Class.forName(mtdClass).newInstance();
				regMap.put(name, regMethod);
			}
		} catch (Exception e) {
			throw new MethodFactoryException("Can not initialize map!");
		} finally {
			try {in.close();} catch(Throwable ignore) {}
		}
	}

	/**
	 * Check if method is simple.
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing.
	 * @param methodName a method to check.
	 * @return <code>true</code> is method is simple; <code>false</code> otherwise.
	 */
	private static boolean isSimpleMethodName(String methodName) {
		return !methodName.contains(MethodConstants.OP_METHOD_SEPARATOR) &&
				!methodName.contains(MethodConstants.OP_REDIRECT_TO_RIGHT) &&
				!methodName.contains(MethodConstants.OP_REDIRECT_TO_LEFT) &&
				!methodName.contains(MethodConstants.OP_LEFT_BRACKET) &&
				!methodName.contains(MethodConstants.OP_RIGHT_BRACKET);
	}

	/**
	 * This class is used to resolve complex methods.<br/>
	 * Complex method is defined as non-simple method.<br/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing.
	 * @author Miro Bezjak
	 */
	static class ComplexMethodExecutor implements RegisteredMethod {

		/* (non-Javadoc)
		 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.api.comm.Method, javax.servlet.http.HttpServletRequest)
		 */
		@Override
		public void run(Method<Serializable> method, HttpServletRequest request) {
			MethodDispatcher disp = new AdvancedMethodDispatcher();
			disp.preformMethodDispatching(method, request);
		}
		
	}
}