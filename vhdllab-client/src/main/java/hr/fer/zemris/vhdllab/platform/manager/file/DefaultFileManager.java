package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.api.workspace.FileSaveReport;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.FileService;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultFileManager extends AbstractEventPublisher<FileListener>
        implements FileManager {

    @Autowired
    private FileService service;
    @Autowired
    private WorkspaceManager workspaceManager;

    public DefaultFileManager() {
        super(FileListener.class);
    }

    @Override
    public void create(FileInfo file) throws FileAlreadyExistsException {
        checkIfNull(file);
        if(workspaceManager.exist(file)) {
            throw new FileAlreadyExistsException(file.toString());
        }
        FileSaveReport report = service.save(file);
        fireFileCreated(report);
    }

    @Override
    public void save(FileInfo file) {
        checkIfNull(file);
        FileSaveReport report = service.save(file);
        fireFileSaved(report);
    }

    @Override
    public void delete(FileInfo file) {
        checkIfNull(file);
        service.delete(file);
        fireFileDeleted(file);
    }

    private void fireFileCreated(FileSaveReport report) {
        for (FileListener l : getListeners()) {
            l.fileCreated(report);
        }
    }

    private void fireFileSaved(FileSaveReport report) {
        for (FileListener l : getListeners()) {
            l.fileSaved(report);
        }
    }

    private void fireFileDeleted(FileInfo file) {
        for (FileListener l : getListeners()) {
            l.fileDeleted(file);
        }
    }

    private void checkIfNull(FileInfo file) {
        Validate.notNull(file, "File can't be null");
    }

}
