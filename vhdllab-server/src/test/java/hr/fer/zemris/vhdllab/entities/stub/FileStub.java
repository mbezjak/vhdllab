package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileStub extends File implements IFileResourceStub {

    private static final long serialVersionUID = 1L;

    public FileStub() {
        super(TYPE, NAME, DATA);
        setId(ID);
        setVersion(VERSION);
    }

    public FileStub(File file) {
        super(file);
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
