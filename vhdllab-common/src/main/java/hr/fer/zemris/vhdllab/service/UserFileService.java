package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;

import java.util.List;

public interface UserFileService {

    void save(List<UserFileInfo> files);

    List<UserFileInfo> findByUser(Caseless userId);

}
