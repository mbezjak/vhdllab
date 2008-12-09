package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.api.workspace.FileSaveReport;
import hr.fer.zemris.vhdllab.entities.FileInfo;

public abstract class FileAdapter implements FileListener {

    @Override
    public void fileCreated(FileSaveReport report) {
    }

    @Override
    public void fileSaved(FileSaveReport report) {
    }

    @Override
    public void fileDeleted(FileInfo file) {
    }

}
