package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entity.File;

import java.util.List;

public interface PredefinedFilesDao {

    List<File> getPredefinedFiles();

    File findByName(String name);

}
