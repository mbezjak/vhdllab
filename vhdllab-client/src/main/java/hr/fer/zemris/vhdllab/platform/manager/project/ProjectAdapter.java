package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entities.ProjectInfo;

public abstract class ProjectAdapter implements ProjectListener {

    @Override
    public void projectCreated(ProjectInfo project) {
    }

    @Override
    public void projectDeleted(ProjectInfo project) {
    }

}
