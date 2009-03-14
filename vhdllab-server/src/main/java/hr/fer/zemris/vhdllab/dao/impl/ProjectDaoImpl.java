package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link ProjectDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectDaoImpl extends AbstractEntityDao<Project> implements
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
     * @see hr.fer.zemris.vhdllab.dao.ProjectDao#findByName(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Project findByName(String userId, String name) {
        Validate.notNull(userId, "User identifier can't be null");
        Validate.notNull(name, "Name can't be null");
        return findProject(userId, ProjectType.USER, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.ProjectDao#findByUser(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Project> findByUser(String userId) {
        Validate.notNull(userId, "User identifier can't be null");
        String query = "select p from Project as p where p.userId = ?1 and p.type = ?2 order by p.id";
        return getJpaTemplate().find(query, userId, ProjectType.USER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.dao.ProjectDao#getPreferencesProject(java.lang.
     * String)
     */
    @Override
    public Project getPreferencesProject(String userId) {
        ProjectType type = ProjectType.PREFERENCES;
        String projectName = type.toString().toLowerCase();
        Project preferencesProject = findProject(userId, type, projectName);
        if (preferencesProject == null) {
            preferencesProject = new Project(userId, projectName);
            preferencesProject.setType(type);
            persist(preferencesProject);
        }
        return preferencesProject;
    }

    private Project findProject(String userId, ProjectType type, String name) {
        String query = "select p from Project as p where p.userId = ?1 and p.type = ?2 and p.name = ?3 order by p.id";
        return findUniqueResult(query, userId, type, name);
    }

}
