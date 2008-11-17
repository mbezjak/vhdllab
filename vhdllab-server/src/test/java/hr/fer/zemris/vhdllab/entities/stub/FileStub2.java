package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.File;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileStub2 extends FileStub {

    private static final long serialVersionUID = 1L;

    public FileStub2() {
        super();
        setId(ID_2);
        setVersion(VERSION_2);
        setName(NAME_2);
        setData(DATA_2);
        setType(TYPE_2);
    }

    public FileStub2(File file) {
        super(file);
    }

}
