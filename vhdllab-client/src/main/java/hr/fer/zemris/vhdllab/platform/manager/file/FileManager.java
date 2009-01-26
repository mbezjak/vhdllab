package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

public interface FileManager extends EventPublisher<FileListener> {

    void create(FileInfo file) throws FileAlreadyExistsException;

    void save(FileInfo file);

    void delete(FileInfo file);

}
