package hr.fer.zemris.vhdllab.entities.stub;

import hr.fer.zemris.vhdllab.entities.Project;

/**
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectStub2 extends ProjectStub {

    private static final long serialVersionUID = 1L;

    public ProjectStub2() {
        super();
        setId(ID_2);
        setVersion(VERSION_2);
        setUserId(USER_ID_2);
        setName(NAME_2);
    }

    public ProjectStub2(Project project) {
        super(project);
    }

}
