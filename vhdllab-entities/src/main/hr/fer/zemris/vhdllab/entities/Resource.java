package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This is a superclass for all resources. Basically it defines all properties
 * that a resource must have.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@MappedSuperclass
class Resource implements Serializable, Comparable<Resource> {

	private static final long serialVersionUID = 1L;

	/**
	 * Maximum name length.
	 */
	public static final int NAME_LENGTH = 255;
	/**
	 * Maximum type length.
	 */
	public static final int TYPE_LENGTH = 255;
	/**
	 * Maximum content length.
	 */
	public static final int CONTENT_LENGTH = 16000000; // ~ 16 MB

	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, insertable = false)
	private Long id;
	@Basic
	@Column(name = "name", length = NAME_LENGTH, nullable = false)
	private String name;
	@Basic
	@Column(name = "type", length = TYPE_LENGTH, nullable = false, updatable = false)
	private String type;
	@Basic
	@Column(name = "content", length = CONTENT_LENGTH, nullable = false)
	private String content;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, updatable = false)
	private Date created;

	/**
	 * Constructor for persistence provider.
	 */
	Resource() {
		super();
	}

	/**
	 * Creates a resource with specified name and type. Content will be set to
	 * empty string.
	 * 
	 * @param name
	 *            a name of a resource
	 * @param type
	 *            a type of a resource
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public Resource(String name, String type) {
		this(name, type, "");
	}

	/**
	 * Creates a resource with specified name, type and content.
	 * 
	 * @param name
	 *            a name of a resource
	 * @param type
	 *            a type of a resource
	 * @param a
	 *            content of a resource
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public Resource(String name, String type, String content) {
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
		if (name.length() > NAME_LENGTH) {
			throw new IllegalArgumentException("Name must be <= " + NAME_LENGTH
					+ " but was: " + name.length());
		}
		if (type == null) {
			throw new NullPointerException("Type cant be null");
		}
		if (type.length() > TYPE_LENGTH) {
			throw new IllegalArgumentException("Type must be <= " + TYPE_LENGTH
					+ " but was: " + type.length());
		}
		this.type = type;
		this.name = name;
		setContent(content);
		this.created = new Date();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param r
	 *            a resource object to duplicate
	 * @throws NullPointerException
	 *             if <code>r</code> is <code>null</code>
	 */
	public Resource(Resource r) {
		if (r == null) {
			throw new NullPointerException("Resource cant be null");
		}
		this.id = r.id;
		this.name = r.name;
		this.type = r.type;
		this.content = r.content;
		this.created = r.created;
	}

	/**
	 * Returns a unique identifier for every instance. If an instance was not
	 * persisted then this method will return <code>null</code>.
	 * 
	 * @return a unique identifier for every instance
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns a name of this resource. Return value will never be
	 * <code>null</code>. A name is case insensitive.
	 * 
	 * @return a name of this resource
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a type of this resource. Return value will never be
	 * <code>null</code>. A type is case insensitive.
	 * 
	 * @return a type of this resource
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns a content of this resource. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return a content of this resource
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets a content for this resource.
	 * 
	 * @param content
	 *            a content for this resource
	 * @throws NullPointerException
	 *             if <code>content</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>content</code> is too long
	 * @see #CONTENT_LENGTH
	 */
	public void setContent(String content) {
		if (content == null) {
			throw new NullPointerException("Name cant be null");
		}
		if (content.length() > CONTENT_LENGTH) {
			throw new IllegalArgumentException("Content must be <= "
					+ CONTENT_LENGTH + " but was: " + content.length());
		}
		this.content = content;
	}

	/**
	 * Returns a created date of this resource. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return a created date of this resource
	 */
	public Date getCreated() {
		return created;
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
		result = prime * result + name.toLowerCase().hashCode();
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
		if (!(obj instanceof Resource))
			return false;
		final Resource other = (Resource) obj;
		return name.equalsIgnoreCase(other.name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Resource other) {
		if (this == other)
			return 0;
		if (other == null)
			throw new NullPointerException("Other resource cant be null");
		long val = name.compareToIgnoreCase(other.name);

		if (val < 0)
			return -1;
		else if (val > 0)
			return 1;
		else
			return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30);
		sb.append("id=").append(id);
		sb.append(", name=").append(name);
		sb.append(", type=").append(type);
		sb.append(", created=").append(created);
		sb.append(", contentLength=").append(content.length());
		return sb.toString();
	}

}
