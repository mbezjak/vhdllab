package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.PreferencesFileDao;
import hr.fer.zemris.vhdllab.entity.PreferencesFile;

/**
 * This is a default implementation of {@link PreferencesFileDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class PreferencesFileDaoImpl extends
        AbstractOwnedEntityDao<PreferencesFile> implements PreferencesFileDao {

    public PreferencesFileDaoImpl() {
        super(PreferencesFile.class);
    }

}
