package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;

/**
 * Used to test persistence of {@link HistoryTable}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class HistoryDao extends AbstractEntityDao<HistoryTable> {

    public HistoryDao() {
        super(HistoryTable.class);
    }

}
