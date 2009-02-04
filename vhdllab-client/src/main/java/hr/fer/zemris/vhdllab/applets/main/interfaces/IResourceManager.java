package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.util.List;

/**
 * Defines methods for resource management (manipulation and data extraction).
 * 
 * @author Miro Bezjak
 */
public interface IResourceManager {

    /* LISTENERS METHODS */

    /**
     * Adds the specified vetoable resource listener to receive resource events
     * from this resource manager. If listener <code>l</code> is
     * <code>null</code>, no exception is thrown and no action is performed.
     * 
     * @param l
     *            a listener to add
     */
    void addVetoableResourceListener(VetoableResourceListener l);

    /**
     * Removes the specified vetoable resource listener so that it no longer
     * receives resource events from this resource manager. This method performs
     * no function, nor does it throw an exception, if the listener specified by
     * the argument was not previously added to this resource manager. If
     * listener <code>l</code> is <code>null</code>, no exception is thrown and
     * no action is performed.
     * 
     * @param l
     *            a listener to remove
     */
    void removeVetoableResourceListener(VetoableResourceListener l);

    /* RESOURCE MANIPULATION METHODS */

    /**
     * Creates a new resource.
     * 
     * @param projectName
     *            a name of a project containing a file
     * @param fileName
     *            a name of a file to create
     * @param type
     *            a file type
     * @param data
     *            an initial data to save
     * @return <code>true</code> if specified resource was successfully created;
     *         <code>false</code> otherwise
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    boolean createNewResource(Caseless projectName, Caseless fileName, FileType type,
            String data) throws UniformAppletException;

    /**
     * Deletes a file from a project.
     * 
     * @param projectName
     *            a project name that contains a file
     * @param fileName
     *            a file name of a file to delete
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    void deleteFile(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    /**
     * Deletes a project.
     * 
     * @param projectName
     *            a name of a project to delete
     * @throws NullPointerException
     *             if <code>projectName</code> is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    void deleteProject(Caseless projectName) throws UniformAppletException;

    /**
     * Returns <code>true</code> if a file with <code>fileName</code> already
     * exists in a specified project or <code>false</code> otherwise.
     * 
     * @param projectName
     *            specified project name
     * @param fileName
     *            a file name to check
     * @return <code>true</code> if a file already exists in a specified
     *         project; <code>false</code> otherwise
     * @throws NullPointerException
     *             if either paramter is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    boolean existsFile(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    /**
     * Returns all circuits in a specified project. Return value will never be
     * <code>null</code> although it can be empty list.
     * 
     * @param projectName
     *            a name of a project to extract circuits from
     * @return all circuits in a specified project
     * @throws NullPointerException
     *             if <code>projectName</code> is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    List<Caseless> getAllCircuits(Caseless projectName)
            throws UniformAppletException;

    /**
     * Returns an extracted hierarchy for a specified project. Return value will
     * never be null.
     * 
     * @param projectName
     *            a name of a project to extract hierarchy from
     * @return an extracted hierarchy for a specified project
     * @throws NullPointerException
     *             if <code>projectName</code> is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    Hierarchy extractHierarchy(Caseless projectName)
            throws UniformAppletException;

}
