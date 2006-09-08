package hr.fer.zemris.vhdllab.servlets.dispatch;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.MethodDispatcher;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class AdvancedMethodDispatcher implements MethodDispatcher {
	
	private Properties properties = null;
	private Map<String, JavaToAjaxRegisteredMethod> regMap = null;
	private VHDLLabManager labman = null;
	
	public Properties preformMethodDispatching(Properties p, Map<String, JavaToAjaxRegisteredMethod> regMap, VHDLLabManager labman) {
		if(p==null) throw new NullPointerException("Properties can not be null!");
		if(regMap==null) throw new NullPointerException("A map of registrated methods can not be null!");
		if(labman==null) throw new NullPointerException("A lab manager can not be null!");
		this.properties = p;
		this.regMap = regMap;
		this.labman = labman;
		
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		return resolveOperators(method, properties);
	}
	
	private Properties resolveOperators(String method, Properties prop) {
		Properties p;
		//lowest priority
		if( (p = resolveRedirecting(method)) != null ) return p;
		//medium priority
		if( (p = resolveMethodMerging(method, prop)) != null ) return p;
		//highest priority
		if( (p = resolveBrackets(method, prop)) != null ) return p;
		//there is no operators in method
		return resolveMethod(method, prop);
	}

	/**
	 * This method resolves redirecting of one method (possibly complex)
	 * into another one (possibly complex). Or if redirecting separator
	 * is not present as a top level (see private method
	 * isTopLevel(String, int) for more information on term: top level)
	 * then it returns <code>null</code>.
	 * <p/>
	 * Complex method is defined as non-simple method.<br/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing using private method resolveMethod(String).
	 * @param method a complex method.
	 * @return a responce Properties or <code>null</code> if
	 *         <code>method</code> does not contain redirecting operator.
	 */
	private Properties resolveRedirecting(String method) {
		int redirectPos = 0;
		int offset = 0;
		//repeat until there is no more redirecting operators in a method
		while((redirectPos = indexOfMoreLeftRedirectOP(method, offset)) != -1) {
			if(!isTopLevel(method, redirectPos)) {
				offset = redirectPos + 1;
				continue;
			}
			String[] innerMethods = splitMethod(method, redirectPos);
			String operator = String.valueOf(method.charAt(redirectPos));
			
			Properties retProp;
			if(operator.equals(MethodConstants.OP_REDIRECT_TO_RIGHT)) {
				Properties redirectProp = resolveOperators(innerMethods[0], properties);
				retProp = redirectProperties(innerMethods[1], redirectProp);
			} else {
				Properties redirectProp = resolveOperators(innerMethods[1], properties);
				retProp = redirectProperties(innerMethods[0], redirectProp);
			}
			retProp.setProperty(MethodConstants.PROP_METHOD, method);
			retProp.setProperty(MethodConstants.PROP_STATUS, MethodConstants.STATUS_OK);
			return retProp;
		}
		return null;
	}
	
	/**
	 * This method resolves merge of two methods (possibly complex).
	 * Or if method separator is not present as a top level (see private
	 * method isTopLevel(String, int) for more information on term: top
	 * level) then it returns <code>null</code>.
	 * <p/>
	 * Complex method is defined as non-simple method.<br/>
	 * Simple method is defined as a method which contains no operators
	 * and only one registered method that will be dispatched without
	 * processing using private method resolveMethod(String).
	 * @param method a complex method.
	 * @return a responce Properties or <code>null</code> if
	 *         <code>method</code> does not contain top level method
	 *         separator.
	 */
	private Properties resolveMethodMerging(String method, Properties prop) {
		int mergePos = 0;
		int offset = 0;
		//repeat until there is no more method separators in a method
		while((mergePos = method.indexOf(MethodConstants.OP_METHOD_SEPARATOR, offset)) != -1) {
			if(!isTopLevel(method, mergePos)) {
				offset = mergePos + 1;
				continue;
			}
			String[] innerMethods = splitMethod(method, mergePos);
			Properties left = resolveOperators(innerMethods[0], prop);
			Properties right = resolveOperators(innerMethods[1], prop);
			return mergeProperties(method, left, right);
		}
		return null;
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
	 * @return a responce Properties or <code>null</code> if
	 *         <code>method</code> is not surrounded with brackets.
	 */
	private Properties resolveBrackets(String method, Properties prop) {
		String mtd = removeBrackets(method);
		if(mtd==null) return null;
		Properties p = resolveOperators(mtd, prop);
		p.setProperty(MethodConstants.PROP_METHOD, method);
		return p;
	}
	
	/**
	 * This method resolves <code>method</code> and calls registered
	 * method to deal with this method. If no registered methods exists
	 * for <code>method</code> then <code>errorProperties</code> will 
	 * be returned.
	 * @param method containing which method to call.
	 * @param properties a Properties from where to draw information
	 * @return a responce Properties.
	 */
	private Properties resolveMethod(String method, Properties properties) {
		Properties p = prepairPropertiesToDispatch(method, properties);
		JavaToAjaxRegisteredMethod regMethod = regMap.get(method);
		if(regMethod==null) return errorProperties(MethodConstants.SE_INVALID_METHOD_CALL, "Invalid method called!");
		return regMethod.run(p, labman);
	}
	
	/**
	 * Redirect output of Properties <code>redirectProp</code> and direct
	 * it as an input Properties with method <code>method</code>.
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param method a method to which returned Properties will be set.
	 * @param redirectProp a Properties that will be redirected.
	 * @return a Properties that will contain information of <code>left</code>
	 *         and <code>right</code> properties.
	 */
	private Properties redirectProperties(String method, Properties redirectProp) {
		cleanProperties(redirectProp);
		Set<Object> keys = redirectProp.keySet();
		Properties retProp = new Properties();
		while(true) {
			Properties p = prepairPropertiesToDispatch(method, properties);
			Set<Object> rem = new TreeSet<Object>();
			String numberSuffix = null;
			for(Object key : keys) {
				String propKey = (String) key;
				String newKey = propKey.replaceAll("[.]{1}[0-9]+$", "");
				if(propKey.matches("^[a-zA-Z.]+[.]{1}[0-9]$")) {
					String[] splited = propKey.split("[.]");
					if(numberSuffix == null) numberSuffix = splited[splited.length-1];
					else if(!numberSuffix.equals(splited[splited.length-1])) continue;
				}
				p.put(newKey, redirectProp.get(key));
				rem.add(key);
			}
			for(Object k : rem) {
				keys.remove(k);
			}
			
			Properties ret = resolveOperators(method, p);
			cleanProperties(ret);
			Set<Object> retKeys = ret.keySet();
			for(Object key : retKeys) {
				if(numberSuffix!=null) {
					retProp.put(key+"."+numberSuffix, ret.get(key));
				} else {
					retProp.put(key, ret.get(key));
				}
			}
			if(keys.size() == 0) break;
		}
		return retProp;
	}
	
	/**
	 * Merge two Properties into one.
	 * @param method a method to which returned Properties will be set.
	 * @param left left Properties
	 * @param right right Properties
	 * @return a Properties that will contain information of <code>left</code>
	 *         and <code>right</code> properties.
	 */
	private Properties mergeProperties(String method, Properties left, Properties right) {
		Properties p = new Properties();
		p.setProperty(MethodConstants.PROP_METHOD, method);
		cleanProperties(left);
		cleanProperties(right);
		Set<Object> keys;
		
		keys = left.keySet();
		for(Object key : keys) {
			p.put(key, left.get(key));
		}
		keys = right.keySet();
		for(Object key : keys) {
			p.put(key, right.get(key));
		}
		p.setProperty(MethodConstants.PROP_STATUS, MethodConstants.STATUS_OK);
		return p;
	}
	
	/**
	 * This method creates a new Properties that is prepaired to dispatch. It will
	 * have a <code>method</code> method and all argumets in <code>refProp</code>.
	 * @param method a method to which prepaired properties will be set.
	 * @param refProp a properties from where to draw information.
	 * @return a prepaired properties.
	 */
	private Properties prepairPropertiesToDispatch(String method, Properties refProp) {
		Properties p = new Properties();
		p.setProperty(MethodConstants.PROP_METHOD, method);
		cleanProperties(refProp);
		Set<Object> keys = refProp.keySet();
		for(Object key : keys) {
			p.put(key, refProp.get(key));
		}
		return p;
	}
	
	/**
	 * Delete following key from Properties
	 * <ul>
	 * <li>{@linkplain hr.fer.zemris.ajax.shared.MethodConstants#PROP_METHOD}</li>
	 * <li>{@linkplain hr.fer.zemris.ajax.shared.MethodConstants#PROP_STATUS}</li>
	 * <li>{@linkplain hr.fer.zemris.ajax.shared.MethodConstants#STATUS_CONTENT}</li>
	 * </ul>
	 * @param p a properties to clean.
	 */
	private void cleanProperties(Properties p) {
		p.remove(MethodConstants.PROP_METHOD);
		p.remove(MethodConstants.PROP_STATUS);
		p.remove(MethodConstants.STATUS_CONTENT);
	}

	/**
	 * Search for redirecting operator that is the furthest to the left.
	 * Operators that are further to the left have higher priority.
	 * @param method a method where to look for redirecting operator.
	 * @param offset the index from which to start the search.
	 * @return position of more left redirecting operator.
	 */
	private int indexOfMoreLeftRedirectOP(String method, int offset) {
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
	 * @param opPos a position from where to check if operator is top level. 
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
		int pos = 0;
		if(opPos<l) pos = opPos;
		else pos = opPos - (r-l+1);
		return isTopLevel(sb.toString(), pos);
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
	 * @return an array of string which has two elemets: first part of a method and a second part of a method.
	 */
	private String[] splitMethod(String method, int splitPos) {
		String firstPart = method.substring(0, splitPos);
		String lastPart = method.substring(splitPos+1, method.length());
		return new String[] {firstPart, lastPart};
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
		resProp.setProperty(MethodConstants.STATUS_CONTENT,errorMessage);
		return resProp;
	}
}