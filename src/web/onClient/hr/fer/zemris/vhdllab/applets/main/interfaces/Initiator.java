package hr.fer.zemris.vhdllab.applets.main.interfaces;

import java.io.Serializable;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.communicaton.Method;

/**
 * This interface defines a way to initiate request to server.
 * 
 * @author Miro Bezjak
 */
public interface Initiator {
	
	void init() throws UniformAppletException;
	
	void dispose();

	/**
	 * Initiates request to server.
	 * <p>
	 * <code>request</code> properties must contain key/value pair where key
	 * is {@link MethodConstants#PROP_METHOD}! Other pairs are not used in this
	 * method and will simply be sent to server.
	 * <p>
	 * <code>response</code> properties must also contain
	 * {@link MethodConstants#PROP_METHOD} key and in addition it must contain a
	 * {@link MethodConstants#PROP_STATUS} key.
	 * <p>
	 * If <code>UniformAppletException</code> is thrown then value of
	 * {@link MethodConstants#PROP_STATUS_CONTENT} key will be set as
	 * exception's message.
	 * 
	 * @param method
	 *            a method, containing all information, to send to server
	 * @throws UniformAppletException
	 *             if any king of error occurs
	 */
	<T extends Serializable> void initiateCall(Method<T> method) throws UniformAppletException;

}
