package hr.fer.zemris.vhdllab.applets.main.component.statusbar;

/**
 * Contains message content and message type.
 * @author Miro Bezjak
 */
public class StatusContent {

	/** Message content */
	private String message;
	
	/** Message type */
	private MessageEnum messageType;
	
	/**
	 * Constructor.
	 * @param message a message content
	 * @param messageType a message type
	 */
	public StatusContent(String message, MessageEnum messageType) {
		super();
		if(message == null) {
			throw new NullPointerException("Message content can not be null.");
		}
		if(messageType == null) {
			throw new NullPointerException("Message type can not be null.");
		}
		this.message = message;
		this.messageType = messageType;
	}
	
	/**
	 * Getter for message content.
	 * @return a message conent
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Getter for message type.
	 * @return a message type
	 */
	public MessageEnum getMessageType() {
		return messageType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if (!(obj instanceof StatusContent)) return false;
		StatusContent other = (StatusContent) obj;
		
		return this.messageType.equals(other.messageType) &&
				this.message.equals(other.message);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(message.length() + 15);
		sb.append("Status content of type ").append(messageType.toString())
			.append(" with conent: [").append(message).append("]");
		return sb.toString();
	}
	
}
