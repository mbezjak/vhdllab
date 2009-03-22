package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

public class ProjectFormObject {

    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public static Project asProjectInfo(String name) {
        Caseless userId = ApplicationContextHolder.getContext().getUserId();
        return new Project(userId, new Caseless(name));
    }

}
