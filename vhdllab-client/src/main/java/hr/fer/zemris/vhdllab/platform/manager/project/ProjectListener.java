package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;

import java.util.EventListener;

@AutoPublished(publisher = DefaultProjectManager.class)
public interface ProjectListener extends EventListener {

    void projectCreated(ProjectInfo project);

    void projectDeleted(ProjectInfo project);

}
