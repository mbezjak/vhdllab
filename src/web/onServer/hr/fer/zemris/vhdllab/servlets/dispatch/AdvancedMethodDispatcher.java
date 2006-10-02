package hr.fer.zemris.vhdllab.servlets.dispatch;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.MethodDispatcher;
import hr.fer.zemris.vhdllab.servlets.MethodFactory;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;
import java.util.Set;

/**
 * This class can resolve complex method.
 * <p/>
 * Complex method is defined as non-simple method.<br/>
 * Simple method is defined as a method which contains no operators
 * and only one registered method that will be dispatched without
 * processing using private method resolveMethod(String).
 * @author Miro Bezjak
 */
public class AdvancedMethodDispatcher implements MethodDispatcher {
	
	/**
	 * A Properties that will be processed.
	 */
	private Properties properties;
	/**
	 * A manager provider.
	 */
	private ManagerProvider mprov;
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.MethodDispatcher#preformMethodDispatching(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties preformMethodDispatching(Properties p, ManagerProvider mprov) {
		if(p==null) throw new NullPointerException("Properties can not be null!");
		if(mprov==null) throw new NullPointerException("A manager provider can not be null!");
		this.properties = p;
		this.mprov = mprov;
		
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		Properties retProp = resolveOperators(method, properties);
		retProp.setProperty(MethodConstants.PROP_METHOD, method);
		return retProp;
	}
	
	/**
	 * This method resolves an operator or a simple method.
	 * <p/>
	 * Complex method is defined as non-simple method.<br/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing using private method resolveMethod(String).
	 * @param method a complex or simple method.
	 * @param properties a Properties from where to draw information.
	 * @return a response Properties.
	 */
	private Properties resolveOperators(String method, Properties properties) {
		String status = properties.getProperty(MethodConstants.PROP_STATUS);
		if(status != null && !status.equals(MethodConstants.STATUS_OK)) return properties;
		properties = mergeProperties(method, properties, this.properties);
		
		Properties p;
		//lowest priority
		if( (p = resolveRedirect(method, properties)) != null ) return p;
		//medium priority
		if( (p = resolveMethodMerging(method, properties)) != null ) return p;
		//highest priority
		if( (p = resolveBrackets(method, properties)) != null ) return p;
		//there is no operators in method
		return resolveMethod(method, properties);
	}

	/**
	 * This method resolves redirecting of one method (possibly complex)
	 * into another one (possibly complex). Or if redirect operator
	 * is not present as a top level then it returns <code>null</code>.
	 * <p/>
	 * See private method isTopLevel(String, int) for more information
	 * on term: 'top level'.
	 * <p/>
	 * Complex method is defined as non-simple method.<br/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing using private method resolveMethod(String).
	 * @param method a complex method.
	 * @param properties a Properties from where to draw information.
	 * @return a response Properties or <code>null</code> if
	 *         <code>method</code> does not contain top level redirect
	 *         operator.
	 */
	private Properties resolveRedirect(String method, Properties properties) {
		int redirectPos = indexOfRedirectOP(method);
		if(redirectPos == -1) return null;
		
		String[] innerMethods = splitMethod(method, redirectPos);
		String operator = String.valueOf(method.charAt(redirectPos));
		
		if(operator.equals(MethodConstants.OP_REDIRECT_TO_RIGHT)) {
			Properties redirectProp = resolveOperators(innerMethods[0], properties);
			String status = redirectProp.getProperty(MethodConstants.PROP_STATUS);
			if(status != null && !status.equals(MethodConstants.STATUS_OK)) return redirectProp;
			
			return resolveOperators(innerMethods[1], redirectProp);
		} else {
			Properties redirectProp = resolveOperators(innerMethods[1], properties);
			String status = redirectProp.getProperty(MethodConstants.PROP_STATUS);
			if(status != null && !status.equals(MethodConstants.STATUS_OK)) return redirectProp;
			
			return resolveOperators(innerMethods[0], redirectProp);
		}
	}
	
	/**
	 * This method resolves merge of two methods (possibly complex).
	 * Or if method separator is not present as a top level then it
	 * returns <code>null</code>.
	 * <p/>
	 * See private method isTopLevel(String, int) for more information
	 * on term: 'top level'.
	 * <p/>
	 * Complex method is defined as non-simple method.<br/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing using private method resolveMethod(String).
	 * @param method a complex method.
	 * @param properties a Properties from where to draw information.
	 * @return a response Properties or <code>null</code> if
	 *         <code>method</code> does not contain top level method
	 *         separator.
	 */
	private Properties resolveMethodMerging(String method, Properties properties) {
		int mergePos = indexOfMethodSeparator(method);
		if(mergePos == -1) return null;
		
		String[] innerMethods = splitMethod(method, mergePos);
		Properties left = resolveOperators(innerMethods[0], properties);
		Properties right = resolveOperators(innerMethods[1], properties);
		return mergeProperties(method, left, right);
	}

	/**
	 * This method removes brackets that surrounds <code>method</code>
	 * and then resolves that method (possibly complex). Or if no brackets
	 * surroundes <code>method</code>, returnes <code>null</code>.
	 * <p/>
	 * Complex method is defined as non-simple method.<br/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing using private method resolveMethod(String).
	 * @param method a complex method.
	 * @param properties a Properties from where to draw information.
	 * @return a response Properties or <code>null</code> if
	 *         <code>method</code> is not surrounded with brackets.
	 */
	private Properties resolveBrackets(String method, Properties properties) {
		String mtd = removeBrackets(method);
		if(mtd==null) return null;
		return resolveOperators(mtd, properties);
	}
	
	/**
	 * This method resolves simple <code>method</code> and calls registered
	 * method to deal with this method. If no registered methods exists
	 * for <code>method</code> then <code>errorProperties</code> will 
	 * be returned.
	 * <p/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing.
	 * @param method containing which method to call.
	 * @param properties a Properties from where to draw information.
	 * @return a response Properties.
	 */
	private Properties resolveMethod(String method, Properties properties) {
		Properties retProp = new Properties();
		int num = 1;
		while(true) {
			Properties p = mergeProperties(method, properties, this.properties);
			Properties modified = new Properties();
			String numSuffix = String.valueOf(num);
			for(Object key : p.keySet()) {
				String strKey = (String) key;
				if(strKey.matches("^[a-zA-Z.]+[.]{1}"+numSuffix+"$")) {
					String newKey = strKey.replaceAll("[.]{1}"+numSuffix, "");
					modified.setProperty(newKey, p.getProperty(strKey));
				}
			}
			
			if(num!=1 && modified.size()==0) return retProp;
			for(Object key : modified.keySet()) {
				p.put(key, modified.get(key));
			}
			
			Properties ret;
			RegisteredMethod regMethod = MethodFactory.getMethod(method);
			if(regMethod==null) ret = errorProperties(method, MethodConstants.SE_INVALID_METHOD_CALL, "Invalid method called!");
			else ret = regMethod.run(p, mprov);
			if(num==1 && modified.size()==0) return ret;
			for(Object key : ret.keySet()) {
				String strKey = (String) key;
				//there is no need to add #PROP_METHOD
				if(strKey.equals(MethodConstants.PROP_METHOD)) continue;
				if(strKey.equals(MethodConstants.PROP_STATUS) ||
					strKey.equals(MethodConstants.PROP_STATUS_CONTENT)) {
					retProp.put(key, ret.get(key));
					continue;
				}
				
				if(strKey.matches("^[a-zA-Z.]+[.]{1}[0-9]$")) {
					retProp.put(key, ret.get(key));
					continue;
				}
				retProp.setProperty(strKey+"."+numSuffix, ret.getProperty(strKey));
			}
			
			String status = retProp.getProperty(MethodConstants.PROP_STATUS);
			if(status != null && !status.equals(MethodConstants.STATUS_OK)) return retProp;
			
			if(modified.size()==0) return retProp;
			num++;
		}
	}
	
	/**
	 * Merge two Properties into one. Note that if these two properties have
	 * the same key then left will win because it has higher priority. Also
	 * if one Properties contains {@linkplain MethodConstants#PROP_STATUS}
	 * other then {@linkplain MethodConstants#STATUS_OK} then returned
	 * Properties will also have that status.
	 * @param method a method to which returned Properties will be set.
	 * @param left a left Properties
	 * @param right a right Properties
	 * @return a Properties that will contain information of <code>left</code>
	 *         and <code>right</code> properties.
	 */
	private Properties mergeProperties(String method, Properties left, Properties right) {
		Properties p = new Properties();
		Set<Object> keys;

		keys = right.keySet();
		for(Object key : keys) {
			p.put(key, right.get(key));
		}
		keys = left.keySet();
		for(Object key : keys) {
			if( key.equals(MethodConstants.PROP_STATUS) &&
				left.get(key).equals(MethodConstants.STATUS_OK) ) {
				String status = p.getProperty(MethodConstants.PROP_STATUS);
				if(status != null && !status.equals(MethodConstants.STATUS_OK)) continue;
			}
			p.put(key, left.get(key));
		}
		p.setProperty(MethodConstants.PROP_METHOD, method);
		return p;
	}
	
	/**
	 * Returns an index of first occurance of top level redirect operator
	 * in a <code>method</code> or <code>-1</code> if such operator does
	 * not exists. See private method isTopLevel(String, int) for more
	 * information on term: 'top level'.
	 * @param method a method where to look for an operator.
	 * @return first occurance of top level redirect operator in a 
	 *         <code>method</code> or <code>-1</code> if such operator
	 *         does not exists.
	 */
	private int indexOfRedirectOP(String method) {
		int operatorPos = -1;
		int offset = 0;
		//repeat until there is no more redirect operators in a method
		while((operatorPos = indexOfFarLeftRedirectOP(method, offset)) != -1) {
			if(isTopLevel(method, operatorPos)) return operatorPos;
			offset = operatorPos + 1;
			continue;
		}
		return -1;
	}
	
	/**
	 * Returns an index of first occurance of top level method separator
	 * in a <code>method</code> or <code>-1</code> if such operator does
	 * not exists. See private method isTopLevel(String, int) for more
	 * information on term: 'top level'.
	 * @param method a method where to look for an operator.
	 * @return first occurance of top level method separator in a 
	 *         <code>method</code> or <code>-1</code> if such operator
	 *         does not exists.
	 */
	private int indexOfMethodSeparator(String method) {
		int operatorPos = -1;
		int offset = 0;
		//repeat until there is no more method separators in a method
		while((operatorPos = method.indexOf(MethodConstants.OP_METHOD_SEPARATOR, offset)) != -1) {
			if(isTopLevel(method, operatorPos)) return operatorPos;
			offset = operatorPos + 1;
			continue;
		}
		return -1;
	}

	/**
	 * Search for redirect operator that is the furthest to the left.
	 * Operators that are further to the left have higher priority!
	 * @param method a method where to look for redirect operator.
	 * @param offset the index from which to start the search.
	 * @return position of far left redirect operator or <code>-1</code>
	 *         if redirect operator does not exists in <code>method</code>.
	 */
	private int indexOfFarLeftRedirectOP(String method, int offset) {
		int redirectToRight = method.indexOf(MethodConstants.OP_REDIRECT_TO_RIGHT, offset);
		int redirectToLeft = method.indexOf(MethodConstants.OP_REDIRECT_TO_LEFT, offset);
		if(redirectToRight == -1) return redirectToLeft;
		if(redirectToLeft == -1) return redirectToRight;
		return redirectToRight < redirectToLeft ? redirectToRight : redirectToLeft;
	}
	
	/**
	 * Check weather character (usualy operator) at <code>opPos</code> is
	 * top level. Top level means that operator must not be placed inside
	 * brackets. Or in the other words it checks if this operator will be
	 * resolved last.
	 * <p/>
	 * Example:
	 * <blockquote>
	 * (method1>method2)&method3
	 * </blockquote>
	 * <p/>
	 * In this example operator '&' will be resolved last and therefor
	 * it is top level.
	 * @param method a method where to check if operator is top level.
	 * @param opPos operator position.
	 * @return <code>true</code> if operator at <code>opPos</code> is top level;
	 *         <code>false</code> otherwise.
	 */
	private boolean isTopLevel(String method, int opPos) {
		int r = method.indexOf(MethodConstants.OP_RIGHT_BRACKET);
		int l = method.lastIndexOf(MethodConstants.OP_LEFT_BRACKET, r);
		if(r==-1 && l == -1) return true;
		
		if(opPos>l && opPos<r) return false;
		StringBuilder sb = new StringBuilder(method.length() - (r-l+1));
		sb.append(method.substring(0, l));
		sb.append(method.substring(r+1, method.length()));
		if(opPos>l) opPos = opPos - (r-l+1);
		return isTopLevel(sb.toString(), opPos);
	}
	
	/**
	 * Remove brackets that surrounds <code>method</code>.
	 * @param method a method from where to remove brackets.
	 * @return a method that is not surrounded with brackets; <code>null</code> if
	 *         <code>method</code> does not start with a left bracket or does not
	 *         end with right bracket.
	 */
	private String removeBrackets(String method) {
		if(!method.startsWith(MethodConstants.OP_LEFT_BRACKET)) return null;
		if(!method.endsWith(MethodConstants.OP_RIGHT_BRACKET)) return null;
		return method.substring(1, method.length()-1);
	}
	
	/**
	 * Split method into two parts. Character at <code>splitPos</code> will be ignored.
	 * @param method a method to split.
	 * @param splitPos a position where to split method.
	 * @return an array of string which has two elemets: first and second part of a method.
	 */
	private String[] splitMethod(String method, int splitPos) {
		String firstPart = method.substring(0, splitPos);
		String lastPart = method.substring(splitPos+1, method.length());
		return new String[] {firstPart, lastPart};
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