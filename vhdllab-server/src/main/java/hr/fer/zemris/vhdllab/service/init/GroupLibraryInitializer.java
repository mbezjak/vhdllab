package hr.fer.zemris.vhdllab.service.init;

/**
 * Executes a group of {@link ProjectInitializer}s set by
 * {@link #setInitializers(ProjectInitializer[])}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class GroupLibraryInitializer implements ProjectInitializer {

    /**
     * Initializers that will be executed.
     */
    private ProjectInitializer[] initializers;

    /**
     * Sets initializers to execute
     * 
     * @param initializers
     *            initializers to execute
     */
    public void setInitializers(ProjectInitializer[] initializers) {
        this.initializers = initializers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.service.init.LibraryInitializer#initLibrary()
     */
    @Override
    public void initProject() {
        for (ProjectInitializer initializer : initializers) {
            initializer.initProject();
        }
    }

}
