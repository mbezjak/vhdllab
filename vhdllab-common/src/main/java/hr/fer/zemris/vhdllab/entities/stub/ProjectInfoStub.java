package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfoStub extends ProjectInfo implements IOwnableStub {

    private static final long serialVersionUID = 1L;

    public ProjectInfoStub() {
        super(USER_ID, NAME);
        setId(ID);
        setVersion(VERSION);
    }

    public ProjectInfoStub(ProjectInfo project) {
        super(project);
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

}
