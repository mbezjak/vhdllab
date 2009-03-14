package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.ProjectInfo;
import hr.fer.zemris.vhdllab.entity.ProjectType;
import hr.fer.zemris.vhdllab.util.BeanUtils;

public class ProjectInfoStub extends ProjectInfo {

    private static final long serialVersionUID = 1L;

    public static final String USER_ID = "user identifier";
    public static final String USER_ID_UPPERCASE = USER_ID.toUpperCase();
    public static final String USER_ID_2 = "another user identifier";
    public static final ProjectType TYPE = ProjectType.USER;
    public static final ProjectType TYPE_2 = ProjectType.PREFERENCES;

    public ProjectInfoStub() {
        BeanUtils.copyProperties(this, new NamedEntityStub());
        setUserId(USER_ID);
        setType(TYPE);
    }

}
