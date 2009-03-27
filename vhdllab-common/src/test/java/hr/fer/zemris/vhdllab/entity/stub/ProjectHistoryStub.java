package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.ProjectHistory;

import org.apache.commons.beanutils.BeanUtils;

public class ProjectHistoryStub extends ProjectHistory {

    private static final long serialVersionUID = 1L;

    public ProjectHistoryStub() throws Exception {
        BeanUtils.copyProperties(this, new OwnedEntityStub());
        setHistory(new HistoryStub());
    }

}
