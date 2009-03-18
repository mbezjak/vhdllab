package hr.fer.zemris.vhdllab.service.impl;

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

    private UserFile wrapToEntity(UserFileInfo info) {
        UserFile entity = dao.load(info.getId());
        entity.setData(info.getData());
        return entity;
    }

}
