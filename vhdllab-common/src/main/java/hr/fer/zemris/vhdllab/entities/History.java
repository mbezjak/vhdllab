package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * All entity classes that implement this feature also have a separate table
 * that contains all changes throughout a history for every unique entity in
 * original table. In other words every change in an entity, be that change of a
 * name, data or simple deletion of an entity, is recorded in separate history
 * table for archival and statistical purposes.
 * <p>
 * History entity names are composed of original entity name plus <i>History</i>
 * suffix.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer insertVersion;
    private Integer updateVersion;
    private Date createdOn;
    private Date deletedOn;

    /**
     * Default constructor for persistence provider.
     */
    History() {
        this(0, 0);
    }

    /**
     * Constructs a history out of specified <code>insertVersion</code> and
     * <code>updateVersion</code>.
     * 
     * @param insertVersion
     *            an insert version
     * @param updateVersion
     *            an update version
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if either parameter is negative
     */
    public History(Integer insertVersion, Integer updateVersion) {
        super();
        this.insertVersion = insertVersion;
        this.updateVersion = updateVersion;
        this.createdOn = new Date();
        checkProperties();
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>insertVersion</code>, <code>updateVersion</code> or
     *             <code>createdOn</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>insertVersion</code> or <code>updateVersion</code>
     *             is negative or if createdOn.compareTo(deletedOn) > 0
     */
    private void checkProperties() {
        if (insertVersion == null) {
            throw new NullPointerException("Insert version can't be null");
        }
        if (updateVersion == null) {
            throw new NullPointerException("Update version can't be null");
        }
        if (createdOn == null) {
            throw new NullPointerException("Created on can't be null");
        }
        if (insertVersion < 0) {
            throw new IllegalArgumentException(
                    "Insert version can't be negative but was: "
                            + insertVersion);
        }
        if (updateVersion < 0) {
            throw new IllegalArgumentException(
                    "Update version can't be negative but was: "
                            + updateVersion);
        }
        if (deletedOn != null && createdOn.compareTo(deletedOn) > 0) {
            throw new IllegalArgumentException("Created on (" + createdOn
                    + ") is after deleted on (" + deletedOn + ")");
        }
    }

    /**
     * Insert version is essentially a number of times that one particular
     * unique entity was created. For example, an entity is considered unique if
     * it has unique properties A and B. If such entity is created, then deleted
     * and created again insert version would be 2 and history table would show
     * all such changes. Insert versions start at 0.
     * 
     * @return an insert version
     */
    public final Integer getInsertVersion() {
        return insertVersion;
    }

    /**
     * Update version is a number of times that an entity was updated (changed).
     * For example, if a name of an entity was changed then update version would
     * be incremented. Update versions starts at 0.
     * <p>
     * Note: update version is similar to version property that every entity
     * already has. But unlike version property, update version is manually set
     * and is not used for optimistic locking.
     * </p>
     * 
     * @return an update version
     */
    public final Integer getUpdateVersion() {
        return updateVersion;
    }

    /**
     * Returns a timestamp when an history was created.
     * <p>
     * Note that this timestamp can be used to determine when an entity was
     * created. An adequate insert version and a version
     * (EntityObject#getVersion()) of <code>0</code> is the first history of an
     * entity an thus represents it's creation timestamp.
     * </p>
     * 
     * @return a timestamp when an history was created
     */
    public final Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Returns a timestamp when an entity was deleted. Return value can be
     * <code>null</code> if entity still exists.
     * 
     * @return a timestamp when an entity was deleted
     */
    public final Date getDeletedOn() {
        return deletedOn;
    }

    /**
     * Sets deleted on timestamp.
     * 
     * @param timestamp
     *            a timestamp to set; can be <code>null</code>
     * @throws IllegalArgumentException
     *             if createdOn.compareTo(deletedOn) > 0
     */
    public final void setDeletedOn(Date timestamp) {
       this.deletedOn = timestamp;
       checkProperties();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("insertVersion", insertVersion)
                    .append("updateVersion", updateVersion)
                    .append("createdOn", createdOn)
                    .append("deletedOn", deletedOn)
                    .toString();
    }

    /**
     * Ensures that properties are in correct state after deserialization.
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        checkProperties();
    }

}
