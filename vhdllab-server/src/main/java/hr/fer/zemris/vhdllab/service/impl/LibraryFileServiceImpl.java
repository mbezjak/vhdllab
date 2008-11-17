package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.LibraryDao;
import hr.fer.zemris.vhdllab.dao.LibraryFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.entities.LibraryFileInfo;
import hr.fer.zemris.vhdllab.service.LibraryFileService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class LibraryFileServiceImpl implements LibraryFileService {

    @Autowired
    private LibraryFileDao dao;
    @Autowired
    private LibraryDao libraryDao;

    @Override
    public void saveError(String data) {
        Library errorsLibrary = libraryDao.findByName(new Caseless("errors"));
        LibraryFile error = new LibraryFile(
                new Caseless(new Date().toString()), data);
        errorsLibrary.addFile(error);
        dao.save(error);
    }

    @Override
    public List<LibraryFileInfo> getPredefinedFiles() {
        Library predefinedLibrary = libraryDao.findByName(new Caseless(
                "predefined"));
        List<LibraryFileInfo> infoFiles = new ArrayList<LibraryFileInfo>(
                predefinedLibrary.getFiles().size());
        for (LibraryFile file : predefinedLibrary.getFiles()) {
            infoFiles.add(wrapToInfoObject(file));
        }
        return infoFiles;
    }

    @Override
    public LibraryFileInfo findPredefinedByName(Caseless name) {
        Library predefinedLibrary = libraryDao.findByName(new Caseless(
        "predefined"));
        LibraryFile file = dao.findByName(predefinedLibrary.getId(), name);
        return wrapToInfoObject(file);
    }

    private LibraryFileInfo wrapToInfoObject(LibraryFile file) {
        return file == null ? null : new LibraryFileInfo(file, file
                .getLibrary().getId());
    }
}
