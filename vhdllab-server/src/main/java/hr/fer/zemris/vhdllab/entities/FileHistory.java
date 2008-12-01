package hr.fer.zemris.vhdllab.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * This an actual entity for file history.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileHistory extends FileInfoHistory {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for persistence provider.
     */
    FileHistory() {
        super();
    }

    /**
     * Constructs a file history out of specified <code>file</code> and
     * <code>history</code>.
     * 
     * @param file
     *            a file to duplicate
     * @param history
     *            a history to set
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     */
    public FileHistory(FileInfo file, History history) {
        super(file, history, false);
    }

}
