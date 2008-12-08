package hr.fer.zemris.vhdllab.platform.workspace;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.workspace.model.ProjectIdentifier;

public interface IdentifierToInfoObjectMapper {

    ProjectInfo getProject(ProjectIdentifier project);

    FileInfo getFile(FileIdentifier file);

    ProjectIdentifier asIdentifier(ProjectInfo project);

    FileIdentifier asIdentifier(ProjectInfo project, FileInfo file);

}
