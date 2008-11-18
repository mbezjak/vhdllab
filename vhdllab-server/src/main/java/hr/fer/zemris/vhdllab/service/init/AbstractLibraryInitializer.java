package hr.fer.zemris.vhdllab.service.init;

import hr.fer.zemris.vhdllab.dao.LibraryDao;
import hr.fer.zemris.vhdllab.dao.LibraryFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Provides a way to initialize a library.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public abstract class AbstractLibraryInitializer implements LibraryInitializer {

    @Autowired
    private LibraryDao libraryDao;
    @Autowired
    private LibraryFileDao libraryFileDao;

    /**
     * Names of library files to initialize, if any. Files must be located in
     * the same package as implementor of this class.
     */
    private String[] fileNames;

    /**
     * Setter for fileNames. Files with specified names must be located in the
     * same package as class extending this class.
     * 
     * @param fileNames
     *            names of a file to initialize
     */
    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    /**
     * Initializes specified library and provided files (through
     * {@link #setFileNames(String[])} method), if any.
     * 
     * @param name
     *            a name of a library
     */
    protected void initLibrary(Caseless name) {
        Library library = persistLibrary(name);
        if (fileNames != null) {
            for (String fileName : fileNames) {
                persistLibraryFile(library, fileName);
            }
        }
    }

    /**
     * Creates a library if it doesn't exist already.
     * 
     * @param name
     *            a name of a library to persist
     * @return created or existing specified library
     */
    protected Library persistLibrary(Caseless name) {
        Library predefinedLibrary = libraryDao.findByName(name);
        if (predefinedLibrary == null) {
            predefinedLibrary = new Library(name);
            libraryDao.save(predefinedLibrary);
        }
        return predefinedLibrary;
    }

    /**
     * Creates a library file if it doesn't exist already.
     * 
     * @param library
     *            a library to add a file to
     * @param name
     *            a name of a library file
     */
    private void persistLibraryFile(Library library, String name) {
        String data = readFile(name);
        Caseless predefinedName = new Caseless(name);
        LibraryFile predefinedFile = libraryFileDao.findByName(library.getId(),
                predefinedName);
        if (predefinedFile == null) {
            predefinedFile = new LibraryFile(predefinedName, data);
            library.addFile(predefinedFile);
            libraryFileDao.save(predefinedFile);
        }
    }

    /**
     * Reads a content of specified file. File must be located in the same
     * package as instantiated class.
     * 
     * @param name
     *            a name of a file
     * @return content of a specified file
     */
    private String readFile(String name) {
        InputStream is = getClass().getResourceAsStream(name);
        return FileUtil.readFile(is);
    }

}
