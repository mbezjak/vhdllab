package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
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
     * Returns all projects for a user. Return value will never be
     * <code>null</code> although it can be empty list.
     * 
     * @return all projects
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    List<Caseless> getAllProjects() throws UniformAppletException;

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
     * @see #isCircuit(Caseless, Caseless)
     */
    List<Caseless> getAllCircuits(Caseless projectName)
            throws UniformAppletException;

    /**
     * Returns all testbenches in a specified project. Return value will never
     * be <code>null</code> although it can be empty list.
     * 
     * @param projectName
     *            a name of a project to extract testbenches from
     * @return all testbenches in a specified project
     * @throws NullPointerException
     *             if <code>projectName</code> is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     * @see #isTestbench(Caseless, Caseless)
     */
    List<Caseless> getAllTestbenches(Caseless projectName)
            throws UniformAppletException;

    /**
     * Extracts and returns a circuit interface for specified file. Return value
     * will never be <code>null</code>.
     * 
     * @param projectName
     *            a project name that contains a file
     * @param fileName
     *            a file name of a file to extract circuit interface from
     * @return a circuit interface for specified file
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    CircuitInterface getCircuitInterfaceFor(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    /**
     * Generates a vhdl code for specified resource. Return value will never be
     * <code>null</code>.
     * 
     * @param projectName
     *            a project name that contains a file
     * @param fileName
     *            a file name of a file to generate vhdl code from
     * @return a vhdl generation result for specified resource
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws UniformAppletException
     *             if exceptional condition occurs (for example server is not
     *             responding)
     */
    VHDLGenerationResult generateVHDL(Caseless projectName, Caseless fileName)
            throws UniformAppletException;

    /**
     * Returns a file type of a specified file. Return value can be
     * <code>null</code> and in that case it indicates an error has occurred.
     * 
     * @param projectName
     *            a project name that contains a file
     * @param fileName
     *            a name of a file
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @return a type of a specified file
     */
    FileType getFileType(Caseless projectName, Caseless fileName);

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

    /* IS-SOMETHING METHODS */

    /**
     * Returns <code>true</code> if specified file is a circuit (i.e. represents
     * a component in a vhdl) or <code>false</code> otherwise. If either
     * <code>projectName</code> or <code>fileName</code> is <code>null</code>
     * then this method will return <code>false</code>.
     * 
     * @param projectName
     *            project name that contains a file
     * @param fileName
     *            a name of a file
     * @return <code>true</code> if specified file is a circuit (i.e. represents
     *         a component in a vhdl); <code>false</code> otherwise
     */
    boolean isCircuit(Caseless projectName, Caseless fileName);

    /**
     * Returns <code>true</code> if specified file is a testbench (i.e. a
     * component in vhdl with no ports, input nor output) or <code>false</code>
     * otherwise. If either <code>projectName</code> or <code>fileName</code> is
     * <code>null</code> then this method will return <code>false</code>.
     * <p>
     * Note that any component with no ports is considered a testbench. For
     * example you can create a new vhdl source and leave empty entity block.
     * That file is a testbench!
     * </p>
     * 
     * @param projectName
     *            project name that contains a file
     * @param fileName
     *            a name of a file
     * @return <code>true</code> if specified file is a testbench (i.e. a
     *         component in vhdl with no ports, input nor output);
     *         <code>false</code> otherwise
     */
    boolean isTestbench(Caseless projectName, Caseless fileName);

    /**
     * Returns <code>true</code> if specified file is a compilable (i.e. is a
     * component in vhdl) or <code>false</code> otherwise. If either
     * <code>projectName</code> or <code>fileName</code> is <code>null</code>
     * then this method will return <code>false</code>.
     * <p>
     * Note that predefined files are purposely set to be not compilable even
     * though they are components in vhdl.
     * </p>
     * 
     * @param projectName
     *            project name that contains a file
     * @param fileName
     *            a name of a file
     * @return <code>true</code> if specified file is a compilable (i.e. is a
     *         component in vhdl); <code>false</code> otherwise
     */
    boolean isCompilable(Caseless projectName, Caseless fileName);

    /**
     * Returns <code>true</code> if specified file is a simulatable (i.e. is a
     * testbench in vhdl) or <code>false</code> otherwise. If either
     * <code>projectName</code> or <code>fileName</code> is <code>null</code>
     * then this method will return <code>false</code>.
     * 
     * @param projectName
     *            project name that contains a file
     * @param fileName
     *            a name of a file
     * @return <code>true</code> if specified file is a simulatable (i.e. is a
     *         testbench in vhdl); <code>false</code> otherwise
     */
    boolean isSimulatable(Caseless projectName, Caseless fileName);

    /**
     * Ignore case and check if <code>name</code> is a correct file name.
     * Correct file name is a string with the following format:
     * <ul>
     * <li>it must contain only alpha (only letters of English alphabet),
     * numeric (digits 0 to 9) or underscore (_) characters
     * <li>it must not start with a non-alpha character
     * <li>it must not end with an underscore character
     * <li>it must not contain a tandem of underscore characters
     * <li>it must not be a reserved word (check at
     * hr.fer.zemris.vhdllab.utilities.NotValidVHDLNames.txt)
     * </ul>
     * 
     * @param name
     *            a string that will be checked.
     * @return <code>true</code> if <code>name</code> is a correct name;
     *         <code>false</code> otherwise.
     */
    boolean isCorrectFileName(Caseless name);

}
