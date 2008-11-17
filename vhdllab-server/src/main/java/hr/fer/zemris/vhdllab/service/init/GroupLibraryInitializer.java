package hr.fer.zemris.vhdllab.service.init;

/**
 * Executes a group of {@link LibraryInitializer}s set by
 * {@link #setInitializers(LibraryInitializer[])}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class GroupLibraryInitializer implements LibraryInitializer {

    /**
     * Initializers that will be executed.
     */
    private LibraryInitializer[] initializers;

    /**
     * Sets initializers to execute
     * 
     * @param initializers
     *            initializers to execute
     */
    public void setInitializers(LibraryInitializer[] initializers) {
        this.initializers = initializers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.service.init.LibraryInitializer#initLibrary()
     */
    @Override
    public void initLibrary() {
        for (LibraryInitializer initializer : initializers) {
            initializer.initLibrary();
        }
    }

}
