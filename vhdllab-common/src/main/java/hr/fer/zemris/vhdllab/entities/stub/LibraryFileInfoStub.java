package hr.fer.zemris.vhdllab.entities.stub;

import static hr.fer.zemris.vhdllab.entities.stub.TestsUtil.setField;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.LibraryFileInfo;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryFileInfoStub extends LibraryFileInfo implements
        ILibraryFileInfoStub {

    private static final long serialVersionUID = 1L;

    public LibraryFileInfoStub() {
        super(new FileResourceStub(), LIBRARY_ID);
        setId(ID);
        setVersion(VERSION);
    }

    public LibraryFileInfoStub(LibraryFileInfo file) {
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
