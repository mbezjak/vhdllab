package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;

/**
 * Used to test persistence of {@link FileInfoTable}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileInfoDao extends AbstractEntityDao<FileInfoTable> implements
        EntityDao<FileInfoTable> {

    public FileInfoDao() {
        super(FileInfoTable.class);
    }

}
