package hr.fer.zemris.vhdllab.entity.stub;

import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME_DIFFERENT;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;

public class FileStub2 extends File {

    private static final long serialVersionUID = 1L;

    public FileStub2() {
        setId(456);
        setVersion(654);
        setName(NAME_DIFFERENT);
        setType(FileType.AUTOMATON);
        setData("different data for this stub");
    }

}
