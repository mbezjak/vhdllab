package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

public interface IdentifierToInfoObjectMapper {

    Project getProject(String name);

    Project getProject(Integer projectId);

    File getFile(String projectName, String fileName);

}
