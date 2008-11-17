package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.History;

import java.util.Date;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class HistoryStub extends History implements IHistoryStub {

    private static final long serialVersionUID = 1L;

    public HistoryStub() {
        super(INSERT_VERSION, UPDATE_VERSION);
        setCreatedOn(CREATED_ON);
        setDeletedOn(DELETED_ON);
    }

    @Override
    public void setInsertVersion(Integer insertVersion) {
        setField(this, "insertVersion", insertVersion);
    }

    @Override
    public void setUpdateVersion(Integer updateVersion) {
        setField(this, "updateVersion", updateVersion);
    }

    @Override
    public void setCreatedOn(Date createdOn) {
        setField(this, "createdOn", createdOn);
    }

    @Override
    public void setDeletedOnField(Date deletedOn) {
        setField(this, "deletedOn", deletedOn);
    }

}
