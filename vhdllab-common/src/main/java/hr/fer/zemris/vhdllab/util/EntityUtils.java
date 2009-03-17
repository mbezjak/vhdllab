package hr.fer.zemris.vhdllab.util;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

public abstract class EntityUtils {

    public static File lightweightClone(File file) {
        File clone = new File(file);
        clone.setProject(null);
        clone.setData(null);
        return clone;
    }

    public static Project lightweightClone(Project project) {
        Project clone = new Project(project);
        clone.setFiles(null);
        return clone;
    }

    public static boolean isLightweight(File file) {
        return file.getProject() == null && file.getData() == null;
    }

}
