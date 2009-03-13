package hr.fer.zemris.vhdllab.util;

import hr.fer.zemris.vhdllab.entity.ProjectType;

import org.apache.commons.lang.Validate;

public abstract class ProjectUtils {

    public static String createProjectName(ProjectType type) {
        Validate.notNull(type, "Project type can't be null");
        Validate.isTrue(type.equals(ProjectType.USER),
                "Can't generate name for project type: ", type);
        return type.toString();
    }

}
