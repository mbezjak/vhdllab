package hr.fer.zemris.vhdllab.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * This an actual entity for user file.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserFile extends UserFileInfo {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for persistence provider.
     */
    UserFile() {
        super();
    }

    /**
     * Constructs a user file out of specified <code>userId</code>,
     * <code>name</code> and <code>data</code>.
     * 
     * @param userId
     *            the user identifier to set
     * @param name
     *            the name to set
     * @param data
     *            the data to set
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if either parameter is too long or <code>name</code> isn't
     *             correctly formatted
     * @see #USER_ID_LENGTH
     * @see #NAME_LENGTH
     * @see #DATA_LENGTH
     */
    public UserFile(Caseless userId, Caseless name, String data) {
        super(userId, name, data);
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param userFile
     *            a user file to duplicate
     * @throws NullPointerException
     *             if <code>userFile</code> is <code>null</code>
     */
    public UserFile(UserFile userFile) {
        super(userFile, false);
    }

}
