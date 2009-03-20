package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.FileInfo;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.util.BeanUtil;

public class FileInfoStub extends FileInfo {

    private static final long serialVersionUID = 1L;

    public static final FileType TYPE = FileType.SOURCE;
    public static final String DATA = "a data of a file";

    public FileInfoStub() {
        BeanUtil.copyProperties(this, new NamedEntityStub());
        setType(TYPE);
        setData(DATA);
    }

}
