package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;

/**
 * Used to test persistence of {@link ProjectInfoTable}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfoDao extends AbstractEntityDao<ProjectInfoTable>
        implements EntityDao<ProjectInfoTable> {

    public ProjectInfoDao() {
        super(ProjectInfoTable.class);
    }

}
