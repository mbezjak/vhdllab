package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.getDate;

import java.util.Date;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface IHistoryStub {

    static final Integer INSERT_VERSION = Integer.valueOf(2);
    static final Integer INSERT_VERSION_2 = Integer.valueOf(5);
    static final Integer INSERT_VERSION_NEGATIVE = Integer.valueOf(-1);

    static final Integer UPDATE_VERSION = Integer.valueOf(7);
    static final Integer UPDATE_VERSION_2 = Integer.valueOf(9);
    static final Integer UPDATE_VERSION_NEGATIVE = Integer.valueOf(-1);

    static final Date CREATED_ON = getDate("2003-01-02 13-45");
    static final Date CREATED_ON_2 = getDate("2001-11-21 07-11");

    static final Date DELETED_ON = getDate("2005-05-07 15-31");
    static final Date DELETED_ON_2 = getDate("2007-03-18 21-56");
    static final Date DELETED_ON_BEFORE_ANYTHING = getDate("1980-02-05 23-21");

    void setInsertVersion(Integer insertVersion);

    void setUpdateVersion(Integer updateVersion);

    void setCreatedOn(Date createdOn);
    
    void setDeletedOnField(Date deletedOn);

}
