package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

public interface FileService {

    FileReport save(File file);

    FileReport delete(Integer fileId);

    File findByName(Integer projectId, String name);

}
