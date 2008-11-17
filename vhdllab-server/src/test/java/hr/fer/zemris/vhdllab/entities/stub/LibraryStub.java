package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import java.util.Set;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryStub extends Library implements IEntityObjectStub {

    private static final long serialVersionUID = 1L;

    public LibraryStub() {
        super(NAME);
        setId(ID);
        setVersion(VERSION);
    }

    public LibraryStub(Library library) {
        super(library);
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

    public void setFiles(Set<LibraryFile> files) {
        setField(this, "files", files);
    }

}
