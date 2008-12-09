package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.entities.FileInfo;

public interface FileManager {

    void create(FileInfo file) throws FileAlreadyExistsException;

    void save(FileInfo file);

    void delete(FileInfo file);

}
