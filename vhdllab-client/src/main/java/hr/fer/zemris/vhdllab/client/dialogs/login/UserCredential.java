/**
 * 
 */
package hr.fer.zemris.vhdllab.client.dialogs.login;

/**
 * Represents a user credentials (a username and password). This class is
 * immutable and therefor thread-safe.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 2/9/2007
 */
public final class UserCredential {

	private String username;
	private String password;

	/**
	 * Constructs new credentials using specified <code>username</code> and
	 * <code>password</code>.
	 * 
	 * @param username
	 *            a username
	 * @param password
	 *            a password
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>username</code> is an empty string
	 */
	public UserCredential(String username, String password) {
		if (username == null) {
			throw new NullPointerException("Username cant be null");
		}
		if (password == null) {
			throw new NullPointerException("Password cant be null");
		}
		if (username.equals("")) {
			throw new IllegalArgumentException("Username cant be empty");
		}
		this.username = username;
		this.password = password;
	}

	/**
	 * Returns a username.
	 * 
	 * @return a username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns a password.
	 * 
	 * @return a password
	 */
	public String getPassword() {
		return password;
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
		result = prime * result + password.hashCode();
		result = prime * result + username.hashCode();
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
		if (!(obj instanceof UserCredential))
			return false;
		final UserCredential other = (UserCredential) obj;
		if (!username.equals(other.username))
			return false;
		if (!password.equals(other.password))
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
		return username + ":" + password;
	}

}
