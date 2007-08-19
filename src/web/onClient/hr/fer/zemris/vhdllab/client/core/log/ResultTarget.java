package hr.fer.zemris.vhdllab.client.core.log;

import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.vhdl.Result;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A result target contains information regarding time when result occurred, a
 * resource for which this result stands for and an actual result. This class is
 * immutable and therefor thread-safe.
 * 
 * @param <T>
 *            either a compilation or a simulation result
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 19.8.2007
 */
public final class ResultTarget<T extends Result> {

	/**
	 * A time when result was created.
	 */
	private Date date;
	/**
	 * A resource for which this result stands for.
	 */
	private FileIdentifier resource;
	/**
	 * A result.
	 */
	private T result;

	/**
	 * Constructs a result target with current time.
	 * 
	 * @param resource
	 *            a resource for which <code>result</code> stands for
	 * @param result
	 *            a result
	 */
	public ResultTarget(FileIdentifier resource, T result) {
		this(new Date(), resource, result);
	}

	/**
	 * Constructs a result target with specified time, resource and result.
	 * 
	 * @param date
	 *            a time when <code>result</code> was created
	 * @param resource
	 *            a resource for which <code>result</code> stands for
	 * @param result
	 *            a result
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public ResultTarget(Date date, FileIdentifier resource, T result) {
		if (date == null) {
			throw new NullPointerException("Date cant be null");
		}
		if (resource == null) {
			throw new NullPointerException("Resource cant be null");
		}
		if (result == null) {
			throw new NullPointerException("Result cant be null");
		}
		this.date = date;
		this.resource = resource;
		this.result = result;
	}

	/**
	 * Getter for date.
	 * 
	 * @return a date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Getter for a resource.
	 * 
	 * @return a resource
	 */
	public FileIdentifier getResource() {
		return resource;
	}

	/**
	 * Getter for a result
	 * 
	 * @return a result
	 */
	public T getResult() {
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 1;
		hash = prime * hash + date.hashCode();
		hash = prime * hash + resource.hashCode();
		hash = prime * hash + this.result.hashCode();
		return hash;
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
		final ResultTarget<?> other = (ResultTarget<?>) obj;
		if (!date.equals(other.date))
			return false;
		if (!resource.equals(other.resource))
			return false;
		if (result.getClass() != other.result.getClass()) {
			return false;
		}
		if (!result.equals(other.result))
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
		return formatter.format(date) + "\t" + resource + "/" + result;
	}

}
