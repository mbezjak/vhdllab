package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entity.File;

import java.util.Set;

public interface PredefinedFilesDao {

    Set<File> getPredefinedFiles();

    File findByName(String name);

}
