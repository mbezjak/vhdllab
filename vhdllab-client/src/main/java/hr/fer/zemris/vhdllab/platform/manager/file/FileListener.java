package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.api.workspace.FileSaveReport;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;

import java.util.EventListener;

@AutoPublished(publisher = DefaultFileManager.class)
public interface FileListener extends EventListener {

    void fileCreated(FileSaveReport report);

    void fileSaved(FileSaveReport report);
    
    void fileDeleted(FileInfo file);

}
