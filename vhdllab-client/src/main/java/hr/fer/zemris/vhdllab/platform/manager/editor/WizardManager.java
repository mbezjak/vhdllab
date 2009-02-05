package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entities.FileType;

public interface WizardManager {

    void createNewProjectInstance();

    boolean createNewFileInstance(FileType type);

}
