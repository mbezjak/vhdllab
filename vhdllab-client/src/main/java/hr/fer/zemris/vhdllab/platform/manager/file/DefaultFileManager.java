package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.api.workspace.FileReport;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.FileService;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultFileManager extends AbstractEventPublisher<FileListener>
        implements FileManager {

    private static final String FILE_CREATED_MESSAGE = "notification.file.created";
    private static final String FILE_SAVED_MESSAGE = "notification.file.saved";
    private static final String FILE_DELETED_MESSAGE = "notification.file.deleted";

    @Autowired
    private FileService service;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Resource(name = "standaloneLocalizationSource")
    private LocalizationSource localizationSource;

    public DefaultFileManager() {
        super(FileListener.class);
    }

    @Override
    public void create(FileInfo file) throws FileAlreadyExistsException {
        checkIfNull(file);
        if (workspaceManager.exist(file)) {
            throw new FileAlreadyExistsException(file.toString());
        }
        FileReport report = service.save(file);
        fireFileCreated(report);
        openEditor(report.getFile());
        ProjectInfo project = mapper
                .getProject(report.getFile().getProjectId());
        log(report, project, FILE_CREATED_MESSAGE);
    }

    @Override
    public void save(FileInfo file) {
        checkIfNull(file);
        FileReport report = service.save(file);
        fireFileSaved(report);
        ProjectInfo project = mapper
                .getProject(report.getFile().getProjectId());
        log(report, project, FILE_SAVED_MESSAGE);
    }

    @Override
    public void delete(FileInfo file) {
        checkIfNull(file);
        if (!file.getType().equals(FileType.PREDEFINED)) {
            closeEditor(file);
            ProjectInfo project = mapper.getProject(file.getProjectId());
            FileReport report = service.delete(file);
            fireFileDeleted(report);
            log(report, project, FILE_DELETED_MESSAGE);
        }
    }

    private void fireFileCreated(FileReport report) {
        for (FileListener l : getListeners()) {
            l.fileCreated(report);
        }
    }

    private void fireFileSaved(FileReport report) {
        for (FileListener l : getListeners()) {
            l.fileSaved(report);
        }
    }

    private void fireFileDeleted(FileReport report) {
        for (FileListener l : getListeners()) {
            l.fileDeleted(report);
        }
    }

    private void openEditor(FileInfo file) {
        editorManagerFactory.get(file).open();
    }

    private void closeEditor(FileInfo file) {
        EditorManager em = editorManagerFactory.get(file);
        if (em.isOpened()) {
            em.close(false);
        }
    }

    private void log(FileReport report, ProjectInfo project, String code) {
        logger.info(localizationSource.getMessage(code, new Object[] {
                report.getFile().getName(), project.getName() }));
    }

    private void checkIfNull(FileInfo file) {
        Validate.notNull(file, "File can't be null");
    }

}
