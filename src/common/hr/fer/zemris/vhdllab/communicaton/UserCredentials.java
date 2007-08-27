/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton;

/**
 * @author Miro Bezjak
 */
public final class UserCredentials {

	private static final UserCredentials INSTANCE = new UserCredentials();
	
	private String userId;
	private String fingerprint;

	private UserCredentials() {
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the fingerprint
	 */
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * @param fingerprint the fingerprint to set
	 */
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public static UserCredentials instance() {
		return INSTANCE;
	}
}
