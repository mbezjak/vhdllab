package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.entity.ProjectInfo;

import javax.persistence.Entity;

/**
 * Used as an entity to test persistence of {@link ProjectInfo}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Entity
public class ProjectInfoTable extends ProjectInfo {

    private static final long serialVersionUID = 1L;

    public ProjectInfoTable() {
        super();
    }

}
