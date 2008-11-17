package hr.fer.zemris.vhdllab.service.init.preferences;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.service.init.AbstractLibraryInitializer;

/**
 * Initializes preferences files library.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class PreferencesLibraryInitializer extends AbstractLibraryInitializer {

    /**
     * A name of a preferences library.
     */
    public static final Caseless LIBRARY_NAME = new Caseless("preferences");

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.service.init.LibraryInitializer#initLibrary()
     */
    @Override
    public void initLibrary() {
        initLibrary(LIBRARY_NAME);
    }

}
