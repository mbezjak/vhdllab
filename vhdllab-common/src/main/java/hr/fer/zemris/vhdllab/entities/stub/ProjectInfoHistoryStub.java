package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.History;
import hr.fer.zemris.vhdllab.entities.ProjectInfoHistory;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfoHistoryStub extends ProjectInfoHistory implements
        IProjectInfoHistoryStub {

    private static final long serialVersionUID = 1L;

    public ProjectInfoHistoryStub() {
        super(new ProjectInfoStub(), new HistoryStub());
        setId(ID);
        setVersion(VERSION);
    }

    public ProjectInfoHistoryStub(ProjectInfoHistory history) {
        super(history);
    }

    @Override
    public void setId(Integer id) {
        setField(this, "id", id);
    }

    @Override
    public void setName(Caseless name) {
        setField(this, "name", name);
    }

    @Override
    public void setVersion(Integer version) {
        setField(this, "version", version);
    }

    @Override
    public void setUserId(Caseless userId) {
        setField(this, "userId", userId);
    }

    @Override
    public void setHistory(History history) {
        setField(this, "history", history);
    }

}
