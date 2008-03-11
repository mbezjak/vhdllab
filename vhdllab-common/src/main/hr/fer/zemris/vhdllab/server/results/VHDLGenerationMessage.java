package hr.fer.zemris.vhdllab.server.results;

/**
 * Represents a vhdl generation message.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class VHDLGenerationMessage extends Message {
	/*
	 * This class is immutable and therefor thread-safe.
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a vhdl generation message out of specified parameters. Entity
	 * name will be set to <code>null</code>.
	 * 
	 * @param type
	 *            a type of a message
	 * @param messageText
	 *            a text of a message
	 * @throws NullPointerException
	 *             if any parameter is <code>null</code>
	 */
	public VHDLGenerationMessage(MessageType type, String messageText) {
		this(type, messageText, null);
	}

	/**
	 * Creates a vhdl generation message out of specified parameters.
	 * 
	 * @param type
	 *            a type of a message
	 * @param messageText
	 *            a text of a message
	 * @param entityName
	 *            an entity name of a unit for which message is generated.
	 *            Please note that this can be an empty string, if appropriate
	 *            information could not be determined
	 * @throws NullPointerException
	 *             if <code>type</code> or <code>messageText</code> is
	 *             <code>null</code>
	 */
	public VHDLGenerationMessage(MessageType type, String messageText,
			String entityName) {
		super(type, messageText, entityName);
	}

}
