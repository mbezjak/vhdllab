package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

/**
 * A project is a collection of files.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@EntityListeners(HistoryListener.class)
public class Project extends ProjectInfo {

	private static final long serialVersionUID = 1L;

    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<File> files;

    /**
     * Default constructor for persistence provider.
     */
	Project() {
		super();
        initFiles();
	}

    /**
     * Constructs a project out of specified <code>userId</code> and
     * <code>name</code>.
     * 
     * @param userId
     *            a user identifier that this user files belongs to
     * @param name
     *            a name of a project
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if either parameter is too long or <code>name</code> isn't
     *             correctly formatted
     * @see #USER_ID_LENGTH
     * @see #NAME_LENGTH
     */
	public Project(Caseless userId, Caseless name) {
		super(userId, name);
        initFiles();
	}

	/**
	 * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
	 * <p>
	 * Note: files are not duplicated to reduce aliasing problems.
	 * </p>
	 * 
	 * @param project
	 *            a project to duplicate
	 * @throws NullPointerException
	 *             if <code>project</code> is <code>null</code>
	 */
	public Project(Project project) {
		super(project);
        /*
         * Files is not referenced to reduce aliasing problems!
         */
		initFiles();
	}

	/**
	 * Initializes file collection.
	 */
	private void initFiles() {
	    this.files = new HashSet<File>();
	}

    /**
     * Adds a <code>file</code> to a this project.
     * 
     * @param file
     *            a file to add
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if a <code>file</code> already belongs to a project
     */
    public void addFile(File file) {
        if (file == null) {
            throw new NullPointerException("File can't be null");
        }
        if (file.getProject() != null) {
            if(file.getProject() == this) {
                // a file is already in this project
                return;
            }
            throw new IllegalArgumentException(file.toString()
                    + " already belongs to some project; this project is "
                    + this.toString());
        }
        /*
         * Must set a project to a file before adding it to a collection because
         * project is required for hash code calculation (in file).
         * @see File#hashCode()
         */
        file.setProject(this);
        getFiles().add(file);
    }

	/**
	 * Removes a file from this project.
	 * 
	 * @param file
	 *            a file to remove
	 * @throws NullPointerException
	 *             is <code>file</code> is <code>null</code>
	 */
	public void removeFile(File file) {
        if (file == null) {
            throw new NullPointerException("File can't be null");
        }
        if (file.getProject() != this) {
            throw new IllegalArgumentException(file.toString()
                    + " doesn't belong to this " + this.toString());
        }
        // referencing though getter because of lazy loading
        if (getFiles().remove(file)) {
            file.setProject(null);
        }
	}

    /**
     * Returns a set of files. Return value will never be <code>null</code>.
     * <p>
     * Note: although this method returns direct reference to files (and not
     * unmodifiable proxy) adding and deleting a file <b>must</b> be done using
     * {@link #addFile(File)} and {@link #removeFile(File)} methods.
     * </p>
     * 
     * @return a set of files
     */
	public Set<File> getFiles() {
		return files;
	}

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // referencing though getter because of lazy loading
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("files", getFiles(), false)
                    .toString();
    }

    /**
     * Ensures that properties are in correct state after deserialization.
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        if(files == null) {
            throw new NullPointerException("Files can't be null");
        }
    }

}
