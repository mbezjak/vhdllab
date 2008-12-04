package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao dao;
    @Autowired
    private ProjectDao projectDao;
    @Resource(name = "circuitInterfaceExtractionService")
    private CircuitInterfaceExtractor extractor;

    @Override
    public FileInfo save(FileInfo file) {
        File entity;
        if(file.getId() == null) {
            // creating new file
            entity = new File(file.getType(), file.getName(), file.getData());
            Project project = projectDao.load(file.getProjectId());
            project.addFile(entity);
        } else {
            entity = wrapToEntity(file);
        }
        if (entity.getType().equals(FileType.SOURCE)) {
            CircuitInterface ci = extractor.extract(file);
            if(!ci.getName().equalsIgnoreCase(file.getName().toString())) {
                throw new IllegalStateException("Resource "
                        + entity.getName()
                        + " must have only one entity with the same name.");
            }
        }
        dao.save(entity);
        return wrapToInfoObject(entity);
    }

    @Override
    public void delete(FileInfo file) {
        dao.delete(wrapToEntity(file));
    }

    @Override
    public List<FileInfo> findByProject(Integer projectId) {
        Project project = projectDao.load(projectId);
        List<FileInfo> infoFiles = new ArrayList<FileInfo>(project.getFiles()
                .size());
        for (File file : project.getFiles()) {
            infoFiles.add(wrapToInfoObject(file));
        }
        return infoFiles;
    }

    @Override
    public FileInfo findByName(Integer projectId, Caseless name) {
        return wrapToInfoObject(dao.findByName(projectId, name));
    }

    private File wrapToEntity(FileInfo info) {
        File entity = dao.load(info.getId());
        entity.setData(info.getData());
        return entity;
    }

    private FileInfo wrapToInfoObject(File file) {
        return file == null ? null : new FileInfo(file, file.getProject()
                .getId());
    }
}
