package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.util.List;
import java.util.ResourceBundle;

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
	 * listener <code>l</code> is <code>null</code>, no exception is thrown
	 * and no action is performed.
	 * 
	 * @param l
	 *            a listener to remove
	 */
	void removeVetoableResourceListener(VetoableResourceListener l);

	/**
	 * Removes all vetoable resource listener.
	 */
	void removeAllVetoableResourceListeners();

	/**
	 * Returns an array of all the vetoable resource listeners registered on
	 * this resource manager.
	 * 
	 * @return all of this resource manager's
	 *         <code>VetoableResourceListener</code>s or an empty array if no
	 *         vetoable resource listeners are currently registered
	 */
	VetoableResourceListener[] getVetoableResourceListeners();

	/* RESOURCE MANIPULATION METHODS */

	/**
	 * Creates a new project.
	 * 
	 * @param projectName
	 *            a name of a project to create
	 * @return <code>true</code> if specified project was successfully
	 *         created; <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>projectName</code> is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	boolean createNewProject(String projectName) throws UniformAppletException;

	/**
	 * Creates a new resource.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a file to create
	 * @param type
	 *            a file type
	 * @return <code>true</code> if specified resource was successfully
	 *         created; <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	boolean createNewResource(String projectName, String fileName, String type)
			throws UniformAppletException;

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
	 * @return <code>true</code> if specified resource was successfully
	 *         created; <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if either parameter (except for <code>data</code>) is
	 *             <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	boolean createNewResource(String projectName, String fileName, String type,
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
	void deleteFile(String projectName, String fileName)
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
	void deleteProject(String projectName) throws UniformAppletException;

	/**
	 * Returns <code>true</code> if a file with <code>fileName</code>
	 * already exists in a specified project or <code>false</code> otherwise.
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
	boolean existsFile(String projectName, String fileName)
			throws UniformAppletException;

	/**
	 * Returns <code>true</code> if a project with <code>projectName</code>
	 * already exists or <code>false</code> otherwise.
	 * 
	 * @param projectName
	 *            a project name to check
	 * @return <code>true</code> if a project with <code>projectName</code>
	 *         already exists; <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>projectName</code> is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	boolean existsProject(String projectName) throws UniformAppletException;

	/**
	 * Returns all projects for a user. Return value will never be
	 * <code>null</code> although it can be empty list.
	 * 
	 * @return all projects
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	List<String> getAllProjects() throws UniformAppletException;

	/**
	 * Returns all files in specified project. Return value will never be
	 * <code>null</code> although it can be empty list.
	 * 
	 * @param projectName
	 *            a name of a project to extract files from
	 * @return all files in specified project
	 * @throws NullPointerException
	 *             if <code>projectName</code> is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	List<String> getFilesFor(String projectName) throws UniformAppletException;

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
	 * @see #isCircuit(String, String)
	 */
	List<String> getAllCircuits(String projectName)
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
	 * @see #isTestbench(String, String)
	 */
	List<String> getAllTestbenches(String projectName)
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
	CircuitInterface getCircuitInterfaceFor(String projectName, String fileName)
			throws UniformAppletException;

	/**
	 * Generates a vhdl code for specified resource. Return value will never be
	 * <code>null</code>.
	 * 
	 * @param projectName
	 *            a project name that contains a file
	 * @param fileName
	 *            a file name of a file to generate vhdl code from
	 * @return a vhdl code for specified resource
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	String generateVHDL(String projectName, String fileName)
			throws UniformAppletException;

	/**
	 * Returns a content of a specified file. Return value will never be
	 * <code>null</code>.
	 * 
	 * @param projectName
	 *            a project name that contains a file
	 * @param fileName
	 *            a name of a file whose content to return
	 * @return a content of a specified file
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	String getFileContent(String projectName, String fileName)
			throws UniformAppletException;

	/**
	 * Return a content of a specified predefined file. Return value will never
	 * be null.
	 * 
	 * @param fileName
	 *            a name of predefined file
	 * @return a content of a specified predefined file
	 * @throws NullPointerException
	 *             if <code>fileName</code> is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	String getPredefinedFileContent(String fileName)
			throws UniformAppletException;

	/**
	 * Saves a file content.
	 * 
	 * @param projectName
	 *            a project name that contains a file
	 * @param fileName
	 *            a name of a file to save
	 * @param content
	 *            a file content to save
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	void saveFile(String projectName, String fileName, String content)
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
	String getFileType(String projectName, String fileName);

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
	Hierarchy extractHierarchy(String projectName)
			throws UniformAppletException;

	/* COMPILATION METHOD */

	/**
	 * Compiles a specified resource. Return value can be <code>null</code> if
	 * someone vetoed compilation.
	 * 
	 * @param projectName
	 *            a resource's project name
	 * @param fileName
	 *            a resource's file name
	 * @return a compilation result
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	CompilationResult compile(String projectName, String fileName)
			throws UniformAppletException;

	/* SIMULATION METHOD */

	/**
	 * Simulates a specified resource. Return value can be <code>null</code>
	 * if someone vetoed simulation.
	 * 
	 * @param projectName
	 *            a resource's project name
	 * @param fileName
	 *            a resource's file name
	 * @return a simulation result
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	SimulationResult simulate(String projectName, String fileName)
			throws UniformAppletException;

	/**
	 * Returns a user preferences. Return value will never be <code>null</code>.
	 * 
	 * @return a user preferences.
	 */
	IUserPreferences getPreferences();

	/**
	 * Gets a resource bundle for the given base name and user's locale.
	 * Returned value will never be <code>null</code>.
	 * 
	 * @param baseName
	 *            the base name of the resource bundle, a fully qualified class
	 *            name (or a name before language suffix)
	 * @return a resource bundle for the given base name and user's locale.
	 * @throws IllegalArgumentException
	 *             if no bundle for such <code>baseName</code> can be found
	 */
	ResourceBundle getResourceBundle(String baseName);

	/* IS-SOMETHING METHODS */

	/**
	 * Returns <code>true</code> if specified file is a circuit (i.e.
	 * represents a component in a vhdl) or <code>false</code> otherwise.
	 * 
	 * @param projectName
	 *            project name that contains a file
	 * @param fileName
	 *            a name of a file
	 * @return <code>true</code> if specified file is a circuit (i.e.
	 *         represents a component in a vhdl); <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean isCircuit(String projectName, String fileName);

	/**
	 * Returns <code>true</code> if specified file is a testbench (i.e. a
	 * component in vhdl with no ports, input nor output) or <code>false</code>
	 * otherwise.
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
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean isTestbench(String projectName, String fileName);

	/**
	 * Returns <code>true</code> if specified file is a simulation (i.e. a
	 * simulation result of a vhdl simulator) or <code>false</code> otherwise.
	 * 
	 * @param projectName
	 *            project name that contains a file
	 * @param fileName
	 *            a name of a file
	 * @return <code>true</code> if specified file is a simulation (i.e. a
	 *         simulation result of a vhdl simulator); <code>false</code>
	 *         otherwise
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean isSimulation(String projectName, String fileName);

	/**
	 * Returns <code>true</code> if specified file is a compilable (i.e. is a
	 * component in vhdl) or <code>false</code> otherwise.
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
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean isCompilable(String projectName, String fileName);

	/**
	 * Returns <code>true</code> if specified file is a simulatable (i.e. is a
	 * testbench in vhdl) or <code>false</code> otherwise.
	 * 
	 * @param projectName
	 *            project name that contains a file
	 * @param fileName
	 *            a name of a file
	 * @return <code>true</code> if specified file is a simulatable (i.e. is a
	 *         testbench in vhdl); <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean isSimulatable(String projectName, String fileName);

	/**
	 * Ignore case and check if <code>name</code> is a correct entity name.
	 * Correct entity name is a string with the following format:
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
	 * @throws NullPointerException
	 *             is <code>name</code> is <code>null</code>.
	 */
	boolean isCorrectEntityName(String name);

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
	 * @throws NullPointerException
	 *             is <code>name</code> is <code>null</code>.
	 */
	boolean isCorrectFileName(String name);

	/**
	 * Ignore case and check if <code>name</code> is a correct project name.
	 * Correct project name is a string with the following format:
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
	 * @throws NullPointerException
	 *             is <code>name</code> is <code>null</code>.
	 */
	boolean isCorrectProjectName(String name);

}
