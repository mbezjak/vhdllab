package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.LibraryDao;
import hr.fer.zemris.vhdllab.dao.LibraryFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.entities.LibraryFileInfo;
import hr.fer.zemris.vhdllab.service.LibraryFileService;
import hr.fer.zemris.vhdllab.service.init.clientlogs.ClientLogsLibraryInitializer;
import hr.fer.zemris.vhdllab.service.init.predefined.PredefinedLibraryInitializer;

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
    public void saveClientLog(String data) {
        Library errorsLibrary = libraryDao
                .findByName(ClientLogsLibraryInitializer.LIBRARY_NAME);
        String name = "by user '" + UserHolder.getUser().toString() + "' at "
                + new Date().toString();
        LibraryFile error = new LibraryFile(new Caseless(name), data);
        errorsLibrary.addFile(error);
        dao.save(error);
    }

    @Override
    public List<LibraryFileInfo> getPredefinedFiles() {
        Library predefinedLibrary = getPredefinedLibrary();
        List<LibraryFileInfo> infoFiles = new ArrayList<LibraryFileInfo>(
                predefinedLibrary.getFiles().size());
        for (LibraryFile file : predefinedLibrary.getFiles()) {
            infoFiles.add(wrapToInfoObject(file));
        }
        return infoFiles;
    }

    @Override
    public LibraryFileInfo findPredefinedByName(Caseless name) {
        Library predefinedLibrary = getPredefinedLibrary();
        LibraryFile file = dao.findByName(predefinedLibrary.getId(), name);
        return wrapToInfoObject(file);
    }

    private Library getPredefinedLibrary() {
        return libraryDao.findByName(PredefinedLibraryInitializer.LIBRARY_NAME);
    }

    private LibraryFileInfo wrapToInfoObject(LibraryFile file) {
        return file == null ? null : new LibraryFileInfo(file, file
                .getLibrary().getId());
    }
}
