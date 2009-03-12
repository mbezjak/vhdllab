package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.util.BeanUtils;

public class FileStub extends File {

    private static final long serialVersionUID = 1L;

    public FileStub() {
        BeanUtils.copyProperties(this, new FileInfoStub());
    }

}
