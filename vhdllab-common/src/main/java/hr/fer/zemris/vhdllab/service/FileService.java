package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.workspace.FileReport;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;

import java.util.List;

public interface FileService {

    FileReport save(FileInfo file);

    FileReport delete(FileInfo file);

    List<FileInfo> findByProject(Integer projectId);

    FileInfo findByName(Integer projectId, Caseless name);

}
