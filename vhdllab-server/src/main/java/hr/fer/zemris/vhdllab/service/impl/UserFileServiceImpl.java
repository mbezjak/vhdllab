package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.LibraryDao;
import hr.fer.zemris.vhdllab.dao.UserFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.preferences.global.PreferencesParser;
import hr.fer.zemris.vhdllab.preferences.global.Property;
import hr.fer.zemris.vhdllab.service.UserFileService;
import hr.fer.zemris.vhdllab.service.init.preferences.PreferencesLibraryInitializer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileDao dao;
    @Autowired
    private LibraryDao libraryDao;

    @Override
    public void save(List<UserFileInfo> files) {
        for (UserFileInfo info : files) {
            dao.save(wrapToEntity(info));
        }
    }

    @Override
    public List<UserFileInfo> findByUser(Caseless userId) {
        List<UserFile> userFiles = dao.findByUser(userId);
        Library lib = libraryDao
                .findByName(PreferencesLibraryInitializer.LIBRARY_NAME);
        for (LibraryFile globalFile : lib.getFiles()) {
            boolean found = false;
            for (UserFile userFile : userFiles) {
                if (userFile.getName().equals(globalFile.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                UserFile uf = synchronizeUserFile(globalFile, userId);
                userFiles.add(uf);
            }
        }
        List<UserFileInfo> infoFiles = new ArrayList<UserFileInfo>(userFiles
                .size());
        for (UserFile file : userFiles) {
            infoFiles.add(wrapToInfoObject(file));
        }
        return infoFiles;
    }

    private UserFile synchronizeUserFile(LibraryFile file, Caseless userId) {
        Property property = PreferencesParser.parseProperty(file.getData());
        String data = property.getData().getDefaultValue();
        UserFile uf = new UserFile(userId, file.getName(), data);
        dao.save(uf);
        return uf;
    }

    private UserFile wrapToEntity(UserFileInfo info) {
        UserFile entity = dao.load(info.getId());
        entity.setData(info.getData());
        return entity;
    }

    private UserFileInfo wrapToInfoObject(UserFile file) {
        return file == null ? null : new UserFileInfo(file);
    }
}
