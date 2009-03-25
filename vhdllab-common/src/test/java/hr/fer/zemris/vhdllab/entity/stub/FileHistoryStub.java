package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.FileHistory;

import org.apache.commons.beanutils.BeanUtils;

public class FileHistoryStub extends FileHistory {

    private static final long serialVersionUID = 1L;

    public static final Integer PROJECT_ID = 258;
    public static final Integer PROJECT_ID_2 = 852;

    public FileHistoryStub() throws Exception {
        BeanUtils.copyProperties(this, new FileInfoStub());
        setProjectId(PROJECT_ID);
        setHistory(new HistoryStub());
    }
}
