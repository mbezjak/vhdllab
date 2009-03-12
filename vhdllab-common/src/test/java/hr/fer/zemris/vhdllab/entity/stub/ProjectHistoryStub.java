package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.ProjectHistory;
import hr.fer.zemris.vhdllab.util.BeanUtils;

public class ProjectHistoryStub extends ProjectHistory {

    private static final long serialVersionUID = 1L;

    public ProjectHistoryStub() {
        BeanUtils.copyProperties(this, new ProjectInfoStub());
        setHistory(new HistoryStub());
    }

}
