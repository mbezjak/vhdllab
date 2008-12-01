package hr.fer.zemris.vhdllab.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * This an actual entity for project history.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectHistory extends ProjectInfoHistory {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for persistence provider.
     */
    ProjectHistory() {
        super();
    }

    /**
     * Constructs a project history out of specified <code>project</code> and
     * <code>history</code>.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties of
     * <code>project</code> are not copied.
     * </p>
     * 
     * @param project
     *            a project to duplicate
     * @param history
     *            a history to set
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     */
    public ProjectHistory(ProjectInfo project, History history) {
        super(project, history, false);
    }

}
