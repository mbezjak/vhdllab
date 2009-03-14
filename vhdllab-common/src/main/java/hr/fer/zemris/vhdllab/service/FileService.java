package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.workspace.FileReport;
import hr.fer.zemris.vhdllab.entity.File;

public interface FileService {

    FileReport save(File file);

    FileReport delete(File file);

}
