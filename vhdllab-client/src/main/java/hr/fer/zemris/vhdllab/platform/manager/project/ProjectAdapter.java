package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entity.Project;

public abstract class ProjectAdapter implements ProjectListener {

    @Override
    public void projectCreated(Project project) {
    }

    @Override
    public void projectDeleted(Project project) {
    }

}
