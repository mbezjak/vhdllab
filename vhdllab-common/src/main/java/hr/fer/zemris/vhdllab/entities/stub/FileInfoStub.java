package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileInfoStub extends FileInfo implements IFileInfoStub {

    private static final long serialVersionUID = 1L;

    public FileInfoStub() {
        super(new FileResourceStub(), PROJECT_ID);
        setId(ID);
        setVersion(VERSION);
    }

    public FileInfoStub(FileInfo file) {
        super(file, true);
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
    public void setType(FileType type) {
        setField(this, "type", type);
    }

    @Override
    public void setProjectId(Integer projectId) {
        setField(this, "projectId", projectId);
    }
}
