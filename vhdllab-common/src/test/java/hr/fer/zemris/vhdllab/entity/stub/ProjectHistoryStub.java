package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.ProjectHistory;
import hr.fer.zemris.vhdllab.util.BeanUtil;

public class ProjectHistoryStub extends ProjectHistory {

    private static final long serialVersionUID = 1L;

    public ProjectHistoryStub() {
        BeanUtil.copyProperties(this, new ProjectInfoStub());
        setHistory(new HistoryStub());
    }

}
