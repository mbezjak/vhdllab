package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

public class ProjectFormObject {

    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public static ProjectInfo asProjectInfo(String name) {
        Caseless userId = ApplicationContextHolder.getContext().getUserId();
        return new ProjectInfo(userId, new Caseless(name));
    }

}
