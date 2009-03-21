package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

import java.util.EventListener;

@AutoPublished(publisher = DefaultFileManager.class)
public interface FileListener extends EventListener {

    void fileCreated(FileReport report);

    void fileSaved(FileReport report);

    void fileDeleted(FileReport report);

}
