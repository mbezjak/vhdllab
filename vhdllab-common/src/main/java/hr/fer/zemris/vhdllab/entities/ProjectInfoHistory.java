package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A project info history contains historical information about one time in
 * history.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfoHistory extends ProjectInfo {

    private static final long serialVersionUID = 1L;

    private History history;

    /**
     * Default constructor for persistence provider.
     */
    ProjectInfoHistory() {
        super();
    }

    /**
     * Constructs a project info history out of specified <code>project</code> and
     * <code>history</code>.
     * 
     * @param project
     *            a project to duplicate
     * @param history
     *            a history to set
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     */
    ProjectInfoHistory(ProjectInfo project, History history) {
        super(project);
        this.history = history;
        checkProperties();
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param info
     *            a history info to duplicate
     * @throws NullPointerException
     *             if <code>info</code> is <code>null</code>
     */
    public ProjectInfoHistory(ProjectInfoHistory info) {
        super(info);
        this.history = info.history;
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>history</code> is <code>null</code>
     */
    private void checkProperties() {
        if (history == null) {
            throw new NullPointerException("History can't be null");
        }
    }

    /**
     * Getter for history.
     * 
     * @return the history
     */
    public final History getHistory() {
        return history;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(history.getInsertVersion())
                    .append(history.getUpdateVersion())
                    .appendSuper(super.hashCode())
                    .toHashCode();
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
        if (!(obj instanceof ProjectInfoHistory))
            return false;
        ProjectInfoHistory other = (ProjectInfoHistory) obj;
        return new EqualsBuilder()
                    .append(history.getInsertVersion(),
                            other.history.getInsertVersion())
                    .append(history.getUpdateVersion(),
                            other.history.getUpdateVersion())
                    .appendSuper(super.equals(obj))
                    .isEquals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("history", history)
                    .appendSuper(super.toString())
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
