package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.UserFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFile;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link UserFileDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class UserFileDaoImpl extends AbstractEntityDao<UserFile> implements
        UserFileDao {

    /**
     * Default constructor.
     */
    public UserFileDaoImpl() {
        super(UserFile.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.dao.UserFileDAO#findByName(hr.fer.zemris.vhdllab
     * .entities.Caseless, hr.fer.zemris.vhdllab.entities.Caseless)
     */
    @Override
    public UserFile findByName(Caseless userId, Caseless name) {
        Validate.notNull(userId, "User identifier can't be null");
        Validate.notNull(name, "Name can't be null");
        String query = "select f from UserFile as f where f.userId = ?1 and f.name = ?2 order by f.id";
        return findUniqueResult(query, userId, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.dao.UserFileDAO#findByUser(hr.fer.zemris.vhdllab
     * .entities.Caseless)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserFile> findByUser(Caseless userId) {
        Validate.notNull(userId, "User identifier can't be null");
        String query = "select f from UserFile as f where f.userId = ?1 order by f.id";
        return getJpaTemplate().find(query, userId);
    }

}
