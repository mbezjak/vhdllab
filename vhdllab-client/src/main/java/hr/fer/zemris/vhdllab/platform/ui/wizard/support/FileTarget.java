package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.File;

import org.hibernate.validator.NotNull;

public class FileTarget {

    @NotNull
    private File targetFile;

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File file) {
        this.targetFile = file;
    }

}
