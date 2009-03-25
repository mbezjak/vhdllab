package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.File;

import org.apache.commons.beanutils.BeanUtils;

public class FileStub extends File {

    private static final long serialVersionUID = 1L;

    public FileStub() throws Exception {
        BeanUtils.copyProperties(this, new FileInfoStub());
    }

}
