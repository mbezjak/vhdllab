package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.LibraryFile;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryFileStub2 extends LibraryFileStub {

    private static final long serialVersionUID = 1L;

    public LibraryFileStub2() {
        super();
        setId(ID_2);
        setVersion(VERSION_2);
        setName(NAME_2);
        setData(DATA_2);
    }

    public LibraryFileStub2(LibraryFile file) {
        super(file);
    }

}
