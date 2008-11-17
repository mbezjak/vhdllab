package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.Set;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectStub extends Project implements IOwnableStub {

    private static final long serialVersionUID = 1L;

    public ProjectStub() {
        super(USER_ID, NAME);
        setId(ID);
        setVersion(VERSION);
    }

    public ProjectStub(Project project) {
        super(project);
    }

    @Override
    public void setId(Integer id) {
        setField(this, "id", id);
    }

    @Override
    public void setVersion(Integer version) {
        setField(this, "version", version);
    }

    @Override
    public void setName(Caseless name) {
        setField(this, "name", name);
    }

    @Override
    public void setUserId(Caseless userId) {
        setField(this, "userId", userId);
    }

    public void setFiles(Set<File> files) {
        setField(this, "files", files);
    }

}
