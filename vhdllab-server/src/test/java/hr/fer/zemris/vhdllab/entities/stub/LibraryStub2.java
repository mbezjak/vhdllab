package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.Library;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryStub2 extends LibraryStub {

    private static final long serialVersionUID = 1L;

    public LibraryStub2() {
        super();
        setId(ID_2);
        setVersion(VERSION_2);
        setName(NAME_2);
    }

    public LibraryStub2(Library library) {
        super(library);
    }

}
