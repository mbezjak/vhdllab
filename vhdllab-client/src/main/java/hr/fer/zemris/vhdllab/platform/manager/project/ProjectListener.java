package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;

import java.util.EventListener;

@AutoPublished(publisher = DefaultProjectManager.class)
public interface ProjectListener extends EventListener {

    void projectCreated(Project project);

    void projectDeleted(Project project);

}
