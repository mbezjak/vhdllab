package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;

import java.util.Properties;

/**
 * This interface is used to initiate request to server.
 * @author Miro Bezjak
 */
public interface Initiator {

	/**
	 * Initiates request to server.
	 * <p>
	 * <code>request</code> properties must contain key/value pair where key is
	 * {@link MethodConstants#PROP_METHOD}! Other pairs are not used in this 
	 * method and will simply be sent to server.
	 * <p>
	 * <code>response</code> properties must also contain
	 * {@link MethodConstants#PROP_METHOD} key and in addition it must contain
	 * a {@link MethodConstants#PROP_STATUS} key.
	 * <p>
	 * If <code>UniformAppletException</code> is thrown then value of
	 * {@link MethodConstants#PROP_STATUS_CONTENT} key will be set as exception's
	 * message.
	 * 
	 * @param request a property, containing all information, to send to server
	 * @return a response properties
	 * @throws UniformAppletException if any king of error occures
	 */
	Properties initiateCall(Properties request) throws UniformAppletException;
	
}
