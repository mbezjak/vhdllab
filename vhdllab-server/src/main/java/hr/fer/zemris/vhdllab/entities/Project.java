package hr.fer.zemris.vhdllab.entities;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A project is a collection of files.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@Entity
@Table(name = "projects", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "userId" }) })
@NamedQueries(value = {
		@NamedQuery(name = Project.FIND_BY_NAME_QUERY, query = "select p from Project as p where p.userId = :userId and p.name = :name order by p.id"),
		@NamedQuery(name = Project.FIND_BY_USER_QUERY, query = "select p from Project as p where p.userId = :userId order by p.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project extends Container<File, Project> implements Ownable {

	private static final long serialVersionUID = 1L;
	/**
	 * A named query for finding projects by name.
	 */
	public static final String FIND_BY_NAME_QUERY = "project.find.by.name";
	/**
	 * A named query for finding all projects containing specified userId.
	 */
	public static final String FIND_BY_USER_QUERY = "project.find.by.user";

	@Basic
	@Column(name = "userId", length = 255, nullable = false, updatable = false)
	private String userId;

	/**
	 * Constructor for persistence provider.
	 */
	Project() {
		super();
	}

	/**
	 * Creates a project with specified userId and name.
	 * 
	 * @param userId
	 *            a user identifier that this user files belongs to
	 * @param name
	 *            a name of a project
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #NAME_LENGTH
	 */
	public Project(String userId, String name) {
		super(name);
		if (userId == null) {
			throw new NullPointerException("User identifier cant be null");
		}
		if (userId.length() > USER_ID_LENGTH) {
			throw new IllegalArgumentException("User identifier must be <= "
					+ USER_ID_LENGTH + " but was: " + userId.length());
		}
		this.userId = userId;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param p
	 *            a project object to duplicate
	 * @throws NullPointerException
	 *             if <code>p</code> is <code>null</code>
	 */
	public Project(Project p) {
		super(p);
		this.userId = p.getUserId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Ownable#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * Removes a file from this project.
	 * 
	 * @param f
	 *            a file to remove
	 * @throws NullPointerException
	 *             is <code>f</code> is <code>null</code>
	 */
	public void removeFile(File f) {
		removeChild(f);
	}
	
	/**
	 * Returns an unmodifiable set of files. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return an unmodifiable set of files
	 */
	public Set<File> getFiles() {
		return Collections.unmodifiableSet(getChildren());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + getUserId().toLowerCase().hashCode();
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
		if (!(obj instanceof Project))
			return false;
		if (!super.equals(obj)) {
			return false;
		}
		final Project other = (Project) obj;
		return getUserId().equalsIgnoreCase(other.getUserId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Container<File, Project> o) {
		if (this == o)
			return 0;
		if (o == null)
			throw new NullPointerException("Other project cant be null");
		if (!(o instanceof Project)) {
			throw new ClassCastException("Object is not of Project type");
		}
		final Project other = (Project) o;
		int val = super.compareTo(other);
		if (val != 0) {
			return val;
		}
		val = getUserId().compareToIgnoreCase(other.getUserId());

		if (val < 0) {
			return -1;
		} else if (val > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30 + getChildren().size() * 30);
		sb.append("Project [").append(super.toString()).append("]");
		return sb.toString();
	}
}