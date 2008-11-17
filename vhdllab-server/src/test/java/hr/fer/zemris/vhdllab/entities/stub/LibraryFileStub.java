package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryFileStub extends LibraryFile implements ILibraryFileInfoStub {

    private static final long serialVersionUID = 1L;

    public LibraryFileStub() {
        super(NAME, DATA);
        setId(ID);
        setVersion(VERSION);
    }

    public LibraryFileStub(LibraryFile file) {
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
    public void setLibraryId(Integer libraryId) {
        setField(this, "libraryId", libraryId);
    }

}
