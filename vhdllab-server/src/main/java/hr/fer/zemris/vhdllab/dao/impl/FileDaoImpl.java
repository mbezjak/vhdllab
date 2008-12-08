package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link FileDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileDaoImpl extends AbstractEntityDao<File> implements FileDao {

    /**
     * Default constructor.
     */
    public FileDaoImpl() {
        super(File.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.EntityDAO#delete(java.lang.Object)
     */
    @Override
    public void delete(File entity) {
        /*
         * If file and it's project are persisted and then a file attempts to be
         * deleted, all in the same session (EntityManager), file needs to be
         * removed from project before deleting it. Check
         * FileDAOImplTest#delete2() test to see an example.
         */
        entity.getProject().removeFile(entity);
        super.delete(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.FileDAO#findByName(java.lang.Integer,
     * hr.fer.zemris.vhdllab.entities.Caseless)
     */
    @Override
    public File findByName(Integer projectId, Caseless name) {
        Validate.notNull(projectId, "Project identifier can't be null");
        Validate.notNull(name, "Name can't be null");
        String query = "select f from File as f where f.project.id = ?1 and f.name = ?2 order by f.id";
        return findUniqueResult(query, projectId, name);
    }

}
