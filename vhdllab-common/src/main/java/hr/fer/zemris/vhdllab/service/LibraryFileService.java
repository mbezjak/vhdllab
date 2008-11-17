package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.LibraryFileInfo;

import java.util.List;

public interface LibraryFileService {
    
    void saveClientLog(String data);
    
    List<LibraryFileInfo> getPredefinedFiles();
    
    LibraryFileInfo findPredefinedByName(Caseless name);

}
