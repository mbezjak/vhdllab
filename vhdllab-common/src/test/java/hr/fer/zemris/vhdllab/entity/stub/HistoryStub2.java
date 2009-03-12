package hr.fer.zemris.vhdllab.entity.stub;

public class HistoryStub2 extends HistoryStub {

    private static final long serialVersionUID = 1L;

    public HistoryStub2() {
        setInsertVersion(INSERT_VERSION_DIFFERENT);
        setUpdateVersion(UPDATE_VERSION_DIFFERENT);
        setCreatedOn(CREATED_ON_DIFFERENT);
        setDeletedOn(DELETED_ON_DIFFERENT);
    }

}
