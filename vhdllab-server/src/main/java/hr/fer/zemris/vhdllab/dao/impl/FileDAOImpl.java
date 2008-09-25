package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a default implementation of {@link FileDAO}.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class FileDAOImpl extends AbstractEntityDAO<File> implements
        FileDAO {

    /**
     * Sole constructor.
     */
    public FileDAOImpl() {
        super(File.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDAO#preDeleteAction(java.lang.Object)
     */
    @Override
    protected void preDeleteAction(File file) {
        /*
         * If file and it's project are persisted and then a file attempts to be
         * deleted, all in the same session (EntityManager), file needs to be
         * removed from project before deleting it. Check
         * FileDAOImplTest#delete2() test to see an example.
         */
        file.getProject().removeFile(file);
        super.preDeleteAction(file);
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long,
     *      java.lang.String)
     */
    @Override
    public boolean exists(Integer projectId, Caseless name) throws DAOException {
        checkParameters(projectId, name);
        String query = "select f from File as f where f.project.id = :id and f.name = :name order by f.id";
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("id", projectId);
        params.put("name", name);
        return existsEntity(query, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.dao.FileDAO#findByName(java.lang.Long,
     *      java.lang.String)
     */
    @Override
    public File findByName(Integer projectId, Caseless name) throws DAOException {
        checkParameters(projectId, name);
        String query = "select f from File as f where f.project.id = :id and f.name = :name order by f.id";
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("id", projectId);
        params.put("name", name);
        return findSingleEntity(query, params);
    }

    /**
     * Throws {@link NullPointerException} is any parameter is <code>null</code>.
     */
    private void checkParameters(Integer projectId, Caseless name) {
        if (projectId == null) {
            throw new NullPointerException("Project identifier cant be null");
        }
        if (name == null) {
            throw new NullPointerException("Name cant be null");
        }
    }

}
