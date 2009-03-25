package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.Project;

import org.apache.commons.beanutils.BeanUtils;

public class ProjectStub extends Project {

    private static final long serialVersionUID = 1L;

    public ProjectStub() throws Exception {
        BeanUtils.copyProperties(this, new ProjectInfoStub());
        addFile(new FileStub());
    }

}
