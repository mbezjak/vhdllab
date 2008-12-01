package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileResource;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileResourceStub extends FileResource implements IFileResourceStub {

    private static final long serialVersionUID = 1L;

    public FileResourceStub() {
        super(TYPE, NAME, DATA);
        setId(ID);
        setVersion(VERSION);
    }

    public FileResourceStub(FileResource resource) {
        super(resource, true);
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

}
