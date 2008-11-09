package hr.fer.zemris.vhdllab.client.core.log;

import hr.fer.zemris.vhdllab.utilities.ExceptionsUtil;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a message of the system. This class is immutable and therefor
 * thread-safe.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 19.8.2007
 */
public class SystemMessage {

	/**
	 * An exact time when message occurred.
	 */
	private Date date;
	/**
	 * A content of a message.
	 */
	private String content;
	/**
	 * A type of a message.
	 */
	private MessageType type;

	/**
	 * Constructs a message with current time and <code>ERROR</code> message
	 * type.
	 * 
	 * @param cause
	 *            an exception that occurred
	 * @throws NullPointerException
	 *             if <code>exception</code> is <code>null</code>
	 */
	public SystemMessage(Throwable cause) {
		this(ExceptionsUtil.printStackTrace(cause));
	}

	/**
	 * Constructs a message with current time and <code>ERROR</code> message
	 * type.
	 * 
	 * @param content
	 *            a content of a message
	 * @throws NullPointerException
	 *             if <code>content</code> is <code>null</code>
	 */
	public SystemMessage(String content) {
		this(content, MessageType.ERROR);
	}

	/**
	 * Constructs a message with current time.
	 * 
	 * @param content
	 *            a content of a message
	 * @param type
	 *            a type of a message
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public SystemMessage(String content, MessageType type) {
		this(new Date(), content, type);
	}

	/**
	 * Constructs a message with specified time, content and type.
	 * 
	 * @param date
	 *            an exact time when message occurred
	 * @param content
	 *            a content of a message
	 * @param type
	 *            a type of a message
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public SystemMessage(Date date, String content, MessageType type) {
		if (date == null) {
			throw new NullPointerException("Date cant be null");
		}
		if (content == null) {
			throw new NullPointerException("Content cant be null");
		}
		if (type == null) {
			throw new NullPointerException("Message type cant be null");
		}
		this.date = date;
		this.content = content;
		this.type = type;
	}

	/**
	 * Getter for a date.
	 * 
	 * @return a date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Getter for a content.
	 * 
	 * @return a content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Getter for a type.
	 * 
	 * @return a type
	 */
	public MessageType getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + date.hashCode();
		result = prime * result + type.hashCode();
		result = prime * result + content.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SystemMessage other = (SystemMessage) obj;
		if (!content.equals(other.content))
			return false;
		if (!date.equals(other.date))
			return false;
		if (!type.equals(other.type))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date) + "\t/" + type + "\t" + content;
	}
}
