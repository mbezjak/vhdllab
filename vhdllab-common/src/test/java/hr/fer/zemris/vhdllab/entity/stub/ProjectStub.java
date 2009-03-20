package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.util.BeanUtil;

public class ProjectStub extends Project {

    private static final long serialVersionUID = 1L;

    public ProjectStub() {
        BeanUtil.copyProperties(this, new ProjectInfoStub());
        addFile(new FileStub());
    }

}
