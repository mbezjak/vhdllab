package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link ProjectDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class ProjectDaoImpl extends AbstractEntityDao<Project> implements
        ProjectDao {

    /**
     * Default constructor.
     */
    public ProjectDaoImpl() {
        super(Project.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.dao.ProjectDAO#findByName(hr.fer.zemris.vhdllab
     * .entities.Caseless, hr.fer.zemris.vhdllab.entities.Caseless)
     */
    @Override
    public Project findByName(Caseless userId, Caseless name) {
        Validate.notNull(userId, "User identifier can't be null");
        Validate.notNull(name, "Name can't be null");
        String query = "select p from Project as p where p.userId = ?1 and p.name = ?2 order by p.id";
        return findUniqueResult(query, userId, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.dao.ProjectDAO#findByUser(hr.fer.zemris.vhdllab
     * .entities.Caseless)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Project> findByUser(Caseless userId) {
        Validate.notNull(userId, "User identifier can't be null");
        String query = "select p from Project as p where p.userId = ?1 order by p.id";
        return getJpaTemplate().find(query, userId);
    }

}
