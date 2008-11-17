package hr.fer.zemris.vhdllab.service.init;

/**
 * Initializes library needed by vhdllab server.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface LibraryInitializer {

    /**
     * Initializes a library by persisting it to vhdllab database.
     */
    void initLibrary();

}
