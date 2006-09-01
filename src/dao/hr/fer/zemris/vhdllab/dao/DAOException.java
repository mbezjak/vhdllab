package hr.fer.zemris.vhdllab.dao;

public class DAOException extends Exception {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -188255765125051430L;

	public DAOException() {
		super();
	}

	public DAOException(String s) {
		super(s);
	}

	public DAOException(String s, Throwable t) {
		super(s, t);
	}

	public DAOException(Throwable t) {
		super(t);
	}
}
