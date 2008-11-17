package hr.fer.zemris.vhdllab.service.init.clientlogs;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.service.init.AbstractLibraryInitializer;

/**
 * Initializes client logs library.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ClientLogsLibraryInitializer extends AbstractLibraryInitializer {

    /**
     * A name of client logs library.
     */
    public static final Caseless LIBRARY_NAME = new Caseless("client_logs");

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.service.init.LibraryInitializer#initLibrary()
     */
    @Override
    public void initLibrary() {
        persistLibrary(LIBRARY_NAME);
    }

}
