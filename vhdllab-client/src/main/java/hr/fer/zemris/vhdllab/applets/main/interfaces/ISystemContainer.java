package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;

/**
 * A system container is a core of vhdllab and contains important methods about
 * the system. It is designed to be a mediator between a component module (an
 * editor for example) and a system or another module. Since modules can't
 * communicate directly they can use some of system container's methods to
 * establish communication. System container is also a way of getting required
 * resource from a server (that is actually provided in {@link IResourceManager}
 * interface but system container providers resource manager interface).
 * 
 * @author Miro Bezjak
 */
public interface ISystemContainer {

    /* RESOURCE MANIPULATION METHODS */

    /**
     * Opens a dialog for creating new project. This method will return
     * <code>true</code> if specified project was successfully created or
     * <code>false</code> otherwise.
     */
    void createNewProjectInstance();

    /**
     * Opens a dialog for creating new resource. This method will return
     * <code>true</code> if specified resource was successfully created or
     * <code>false</code> otherwise.
     * 
     * @param type
     *            a type of a file to create
     * @return <code>true</code> if specified resource was successfully created;
     *         <code>false</code> otherwise
     * @throws NullPointerException
     *             if <code>type</code> is <code>null</code>
     */
    boolean createNewFileInstance(FileType type);

    /* MANAGER GETTER METHODS */

    /**
     * Returns a resource manager for manipulating resources. Return value will
     * never be <code>null</code>.
     * 
     * @return a resource manager
     */
    IResourceManager getResourceManager();

    FileIdentifier showCompilationRunDialog();

    FileIdentifier showSimulationRunDialog();

}
