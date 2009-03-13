package hr.fer.zemris.vhdllab.service.init.predefined;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.service.init.AbstractProjectInitializer;

/**
 * Initializes predefined files library.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class PredefinedLibraryInitializer extends AbstractProjectInitializer {

    /**
     * A name of a predefined library.
     */
    public static final Caseless LIBRARY_NAME = new Caseless("predefined");

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.service.init.LibraryInitializer#initLibrary()
     */
    @Override
    public void initProject() {
        initProject(LIBRARY_NAME);
    }

}
