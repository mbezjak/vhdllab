package hr.fer.zemris.vhdllab.platform.manager.file;

import hr.fer.zemris.vhdllab.service.workspace.FileReport;

public abstract class FileAdapter implements FileListener {

    @Override
    public void fileCreated(FileReport report) {
    }

    @Override
    public void fileSaved(FileReport report) {
    }

    @Override
    public void fileDeleted(FileReport report) {
    }

}
