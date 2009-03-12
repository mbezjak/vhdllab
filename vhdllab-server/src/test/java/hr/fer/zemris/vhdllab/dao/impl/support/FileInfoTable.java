package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entity.FileInfo;

import javax.persistence.Entity;

/**
 * Used as an entity to test persistence of {@link FileInfo}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Entity
public class FileInfoTable extends FileInfo {

    private static final long serialVersionUID = 1L;

    public FileInfoTable() {
        super();
    }

}
