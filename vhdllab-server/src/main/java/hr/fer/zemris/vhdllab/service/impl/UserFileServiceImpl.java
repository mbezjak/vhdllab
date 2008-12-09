package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.UserFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.service.UserFileService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileDao dao;

    @Override
    public void save(List<UserFileInfo> files) {
        for (UserFileInfo info : files) {
            UserFile file;
            if (info.getId() == null) {
                // creating new user file
                file = new UserFile(info.getUserId(), info.getName(), info
                        .getData());
            } else {
                file = wrapToEntity(info);
            }
            dao.save(file);
        }
    }

    @Override
    public List<UserFileInfo> findByUser() {
        Caseless userId = UserHolder.getUser();
        List<UserFile> userFiles = dao.findByUser(userId);
        List<UserFileInfo> infoFiles = new ArrayList<UserFileInfo>(userFiles
                .size());
        for (UserFile file : userFiles) {
            infoFiles.add(wrapToInfoObject(file));
        }
        return infoFiles;
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
