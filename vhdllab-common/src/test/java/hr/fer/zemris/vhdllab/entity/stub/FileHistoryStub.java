package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.FileHistory;
import hr.fer.zemris.vhdllab.util.BeanUtils;

public class FileHistoryStub extends FileHistory {

    private static final long serialVersionUID = 1L;

    public static final Integer PROJECT_ID = 258;
    public static final Integer PROJECT_ID_DIFFERENT = 852;

    public FileHistoryStub() {
        BeanUtils.copyProperties(this, new FileInfoStub());
        setProjectId(PROJECT_ID);
        setHistory(new HistoryStub());
    }
}
