package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entity.PreferencesFile;

import java.util.List;

public interface PreferencesFileService {

    void save(List<PreferencesFile> files);

}
