package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.PredefinedFilesDao;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.ProjectService;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.filetype.source.SourceMetadataExtractionService;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao fileDao;
    @Autowired
    private PredefinedFilesDao predefinedFilesDao;
    @Autowired
    private ProjectService projectService;

    @Override
    public FileReport save(File file) {
        if (file.getType().equals(FileType.SOURCE)) {
            CircuitInterface ci = new SourceMetadataExtractionService()
                    .extractCircuitInterface(file);
            if (!ci.getName().equalsIgnoreCase(file.getName().toString())) {
                throw new IllegalStateException("Resource " + file.getName()
                        + " must have only one entity with the same name.");
            }
        }

        File saved;
        if (file.isNew()) {
            fileDao.persist(file);
            saved = file;
        } else {
            saved = fileDao.merge(file);
        }
        return getReport(saved);
    }

    @Override
    public FileReport delete(Integer fileId) {
        File file = fileDao.load(fileId);
        fileDao.delete(file);
        return getReport(file);
    }

    @Override
    public File findByName(Integer projectId, String name) {
        File file = fileDao.findByName(projectId, name);
        if (file == null) {
            file = predefinedFilesDao.findByName(name);
        }
        return new File(file);
    }

    private FileReport getReport(File file) {
        Hierarchy hierarchy = projectService.extractHierarchy(file.getProject()
                .getId());
        return new FileReport(file, hierarchy);
    }

}
