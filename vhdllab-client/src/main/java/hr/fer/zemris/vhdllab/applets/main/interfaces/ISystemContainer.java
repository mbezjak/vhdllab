package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.entities.FileType;

public interface ISystemContainer {

    void createNewProjectInstance();

    boolean createNewFileInstance(FileType type);

}
