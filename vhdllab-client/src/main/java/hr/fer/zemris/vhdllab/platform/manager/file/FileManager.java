package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

public interface FileManager extends EventPublisher<FileListener> {

    void create(File file) throws FileAlreadyExistsException;

    void save(File file);

    void delete(File file);

}
