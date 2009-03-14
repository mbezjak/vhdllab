package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectType;

public class ProjectStub2 extends Project {

    private static final long serialVersionUID = 1L;

    public ProjectStub2() {
        setId(789);
        setVersion(987);
        setName("project name");
        setUserId("project user id");
        setType(ProjectType.PREFERENCES);
    }

}
