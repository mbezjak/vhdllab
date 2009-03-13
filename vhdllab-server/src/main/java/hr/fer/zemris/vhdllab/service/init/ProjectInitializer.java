package hr.fer.zemris.vhdllab.service.init;

/**
 * Initializes a project needed by vhdllab server.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface ProjectInitializer {

    /**
     * Initializes a project by persisting it to vhdllab database.
     */
    void initProject();

}
