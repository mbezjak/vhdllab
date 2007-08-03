package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum;
import hr.fer.zemris.vhdllab.applets.main.constant.ComponentTypes;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.util.List;
import java.util.ResourceBundle;

/**
 * A system container is a core of vhdllab and contains important methods about
 * the system. It is designed to be a mediator between a component modul (an
 * editor for example) and a system or another module. Since modules can't
 * communicate directly they can use some of system container's methods to
 * establish communication. System container is also a way of getting required
 * resource from a server.
 * 
 * @author Miro Bezjak
 */
public interface ISystemContainer {

	/* COMPILE RESOURCE METHODS */

	/**
	 * Shows a dialog to a user so he can select a resource that he wants
	 * compiled. A user can cancel a dialog and in that case this method will
	 * return <code>false</code>.
	 * 
	 * @return <code>true</code> if a resource has been compiled; false
	 *         otherwise
	 * @see #compileLastHistoryResult()
	 * @see #compile(IEditor)
	 * @see #compile(String, String)
	 */
	boolean compileWithDialog();

	/**
	 * Compiles a last recently compiled resource. This method will return
	 * <code>true</code> if resource has been compiled or <code>false</code>
	 * otherwise. If no resource has been compiled then a dialog will present to
	 * a user to select a resource to compile (like in
	 * {@link #compileWithDialog()} method).
	 * 
	 * @return <code>true</code> if resource has been compiled;
	 *         <code>false</code> otherwise
	 * @see #compileWithDialog()
	 * @see #compile(IEditor)
	 * @see #compile(String, String)
	 * @see #getLastCompilationHistoryTarget()
	 */
	boolean compileLastHistoryResult();

	/**
	 * Compiles a resource in an editor and returns <code>true</code> if
	 * resource has been compiled or <code>false</code> otherwise. If
	 * <code>editor</code> is <code>null</code> this method will not throw
	 * an exception and will simply return <code>false</code>.
	 * <p>
	 * Note that some resource is not compilable (simulation for example)
	 * therefor this method will not compile such resource. To check if a
	 * resource is compilable invoke {@link #isCompilable(String, String)}
	 * method.
	 * </p>
	 * 
	 * @param editor
	 *            an editor containing resource to compile
	 * @return <code>true</code> if resource has been compiled;
	 *         <code>false</code> otherwise
	 * @see #compileWithDialog()
	 * @see #compileLastHistoryResult()
	 * @see #compile(String, String)
	 */
	boolean compile(IEditor editor);

	/**
	 * Compiles a specified resource. Aside from actually compiling specified
	 * resource this method (including any other compile methods in system
	 * container) will to the following:
	 * <ul>
	 * <li>Before resource is compiled this method will check to see if any
	 * editor associated with specified resource is opened. If it is it will
	 * invoke {@link #saveResourcesWithDialog(List, String, String)}</li>
	 * <li>If any exceptional condition occurs then this method will report it
	 * by invoking {@link #echoStatusText(String, MessageEnum)} method</li>
	 * <li>Once resource has been compiled user will be notified by opening a
	 * view containing compilation result</li>
	 * </ul>
	 * <p>
	 * If either parameter is <code>null</code> this method will not throw an
	 * exception and will simply return <code>false</code>.
	 * </p>
	 * 
	 * @param projectName
	 *            a resource's project name
	 * @param fileName
	 *            a resource's file name
	 * @return <code>true</code> if resource has been compiled;
	 *         <code>false</code> otherwise
	 * @see #compileWithDialog()
	 * @see #compileLastHistoryResult()
	 * @see #compile(IEditor)
	 */
	boolean compile(String projectName, String fileName);

	/**
	 * Returns a last recently compiled resource of <code>null</code> if no
	 * such resource exists.
	 * 
	 * @return a last recently compiled resource of <code>null</code> if no
	 *         such resource exists
	 * @see #compileLastHistoryResult()
	 */
	FileIdentifier getLastCompilationHistoryTarget();

	/* SIMULATE RESOURCE METHODS */

	/**
	 * Shows a dialog to a user so he can select a resource that he wants
	 * simulated. A user can cancel a dialog and in that case this method will
	 * return <code>false</code>.
	 * 
	 * @return <code>true</code> if a resource has been simulated; false
	 *         otherwise
	 * @see #simulateLastHistoryResult()
	 * @see #simulate(IEditor)
	 * @see #simulate(String, String)
	 */
	boolean simulateWithDialog();

	/**
	 * Simulates a last recently simulated resource. This method will return
	 * <code>true</code> if resource has been simulated or <code>false</code>
	 * otherwise. If no resource has been simulated then a dialog will present
	 * to a user to select a resource to simulate (like in
	 * {@link #simulateWithDialog()} method).
	 * 
	 * @return <code>true</code> if resource has been simulated;
	 *         <code>false</code> otherwise
	 * @see #simulateWithDialog()
	 * @see #simulate(IEditor)
	 * @see #simulate(String, String)
	 * @see #getLastSimulationHistoryTarget()
	 */
	boolean simulateLastHistoryResult();

	/**
	 * Simulates a resource in an editor and returns <code>true</code> if
	 * resource has been simulated or <code>false</code> otherwise. If
	 * <code>editor</code> is <code>null</code> this method will not throw
	 * an exception and will simply return <code>false</code>.
	 * <p>
	 * Note that some resource is not simulatable (vhdl source for example)
	 * therefor this method will not simulate such resource. To check if a
	 * resource is simulatable invoke {@link #isSimulatable(String, String)}
	 * method.
	 * </p>
	 * 
	 * @param editor
	 *            an editor containing resource to simulate
	 * @return <code>true</code> if resource has been simulated;
	 *         <code>false</code> otherwise
	 * @see #simulateWithDialog()
	 * @see #simulateLastHistoryResult()
	 * @see #simulate(String, String)
	 */
	boolean simulate(IEditor editor);

	/**
	 * Simulates a specified resource. Aside from actually simulating specified
	 * resource this method (including any other simulate methods in system
	 * container) will to the following:
	 * <ul>
	 * <li>Before resource is simulated this method will check to see if any
	 * editor associated with specified resource is opened. If it is it will
	 * invoke {@link #saveResourcesWithDialog(List, String, String)}</li>
	 * <li>If any exceptional condition occurs then this method will report it
	 * by invoking {@link #echoStatusText(String, MessageEnum)} method</li>
	 * <li>Once resource has been simulated user will be notified by opening a
	 * view containing simulation result</li>
	 * <li>If a simulation finished successfully an editor will open containing
	 * simulation data</li>
	 * </ul>
	 * <p>
	 * If either parameter is <code>null</code> this method will not throw an
	 * exception and will simply return <code>false</code>.
	 * </p>
	 * 
	 * @param projectName
	 *            a resource's project name
	 * @param fileName
	 *            a resource's file name
	 * @return <code>true</code> if resource has been simulated;
	 *         <code>false</code> otherwise
	 * @see #simulateWithDialog()
	 * @see #simulateLastHistoryResult()
	 * @see #simulate(IEditor)
	 */
	boolean simulate(String projectName, String fileName);

	/**
	 * Returns a last recently simulated resource of <code>null</code> if no
	 * such resource exists.
	 * 
	 * @return a last recently simulated resource of <code>null</code> if no
	 *         such resource exists
	 * @see #simulateLastHistoryResult()
	 */
	FileIdentifier getLastSimulationHistoryTarget();

	/* RESOURCE MANIPULATION METHODS */

	/**
	 * Opens a dialog for creating new project. This method will return
	 * <code>true</code> if specified project was successfully created or
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if specified project was successfully
	 *         created; <code>false</code> otherwise
	 */
	boolean createNewProjectInstance();

	/**
	 * Opens a dialog for creating new resource. This method will return
	 * <code>true</code> if specified resource was successfully created or
	 * <code>false</code> otherwise.
	 * 
	 * @param id
	 *            a component identifier (in {@link ComponentTypes}) to choose
	 *            an editor (and his wizard)
	 * @return <code>true</code> if specified resource was successfully
	 *         created; <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 * @see ComponentTypes
	 */
	boolean createNewFileInstance(String id);

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
	 * will never be null.
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
	 * Returns a file type of a specified file. Return value can be
	 * <code>null</code> and in that case it indicates an error has occured.
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
	 * Returns an extracted hierachy for a specified project. Return value will
	 * never be null.
	 * 
	 * @param projectName
	 *            a name of a project to extract hierachy from
	 * @return an extracted hierachy for a specified project
	 * @throws NullPointerException
	 *             if <code>projectName</code> is <code>null</code>
	 * @throws UniformAppletException
	 *             if exceptional condition occurs (for example server is not
	 *             responding)
	 */
	Hierarchy extractHierarchy(String projectName)
			throws UniformAppletException;

	/**
	 * Returns a currently selected project in a project explorer or
	 * <code>null</code> if no project is selected.
	 * 
	 * @return a currently selected project in a project explorer or
	 *         <code>null</code> if no project is selected
	 */
	String getSelectedProject();

	/**
	 * Returns a currently selected file in a project explorer or
	 * <code>null</code> if no file is selected.
	 * 
	 * @return a currently selected file in a project explorer or
	 *         <code>null</code> if no file is selected
	 */
	FileIdentifier getSelectedFile();

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
	 * <li>it must contain only alpha (only letters of english alphabet),
	 * numeric (digits 0 to 9) or underscore (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underscore character
	 * <li>it must not contain a tandem of underscore characters
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>name</code> is a correct name;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             is <code>name</code> is <code>null</code>.
	 */
	boolean isCorrectEntityName(String name);

	/**
	 * Ignore case and check if <code>name</code> is a correct project name.
	 * Correct project name is a string with the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet),
	 * numeric (digits 0 to 9) or underscore (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underscore character
	 * <li>it must not contain a tandem of underscore characters
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>name</code> is a correct name;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             is <code>name</code> is <code>null</code>.
	 */
	boolean isCorrectProjectName(String name);

	/* PREFERENCES AND RESOURCE BUNDLE METHODS */

	/**
	 * Returns a user preferences. Return value will never be <code>null</code>.
	 * 
	 * @return a user preferences.
	 */
	IUserPreferences getPreferences();

	/**
	 * Returns a data from a property as string of <code>null</code> if
	 * exceptional condition occurs. This is an alias method for
	 * <code>getPreferences().getPropety(name)</code>, however this method
	 * does not throw {@link PropertyAccessException}, instead if such
	 * exception occurs this method will simply return <code>null</code>.
	 * 
	 * @param name
	 *            a name of a property to return
	 * @return a data of a property or <code>null</code> is error occurred
	 */
	String getProperty(String name);

	/**
	 * Set a property. This is an alias method for
	 * <code>getPreferences().setPropety(name, data)</code>, however this
	 * method does not throw {@link PropertyAccessException}, instead if such
	 * exception occurs this method will simply report a problem.
	 * 
	 * @param name
	 *            a name of a property to set
	 * @param data
	 *            a data to set
	 */
	void setProperty(String name, String data);

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

	/* COMPONENT PROVIDER METHODS */

	/**
	 * Returns a component provider. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return a component provider.
	 */
	IComponentProvider getComponentProvider();

	/**
	 * Returns a status bar. Return value will never be <code>null</code>.
	 * This is an alias method for
	 * <code>getComponentProvider().getStatusBar()</code>.
	 * 
	 * @return a status bar
	 */
	IStatusBar getStatusBar();

	/**
	 * Echos a text to status bar. This is an alias method for
	 * <code>getStatusBar().setMessage(String, MessageEnum)</code>.
	 * 
	 * @param text
	 *            a text to set
	 * @param type
	 *            a type of a message
	 */
	void echoStatusText(String text, MessageEnum type);

	/* EDITOR MANIPULATION METHODS */

	/**
	 * Opens a preferences editor.
	 */
	void openPreferences();

	/**
	 * Opens a text editor where a vhdl code for a specified file can be view.
	 * 
	 * @param editor
	 *            an editor containing a file for whom to view vhdl code
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 */
	void viewVHDLCode(IEditor editor);

	/**
	 * Opens a text editor where a vhdl code for a specified file can be view.
	 * 
	 * @param projectName
	 *            a project name that contains a file
	 * @param fileName
	 *            a name of a file for whom to view vhdl code
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	void viewVHDLCode(String projectName, String fileName);

	/**
	 * TODO PENDING REMOVAL
	 */
	IEditor getEditor(String projectName, String fileName);

	/**
	 * TODO PENDING REMOVAL!
	 */
	void openEditor(String projectName, String fileName, boolean savable,
			boolean readOnly);

	/**
	 * TODO PENDING REMOVAL!
	 */
	void openEditor(String projectName, String fileName, String content,
			String type, boolean savable, boolean readOnly);

	/**
	 * TODO PENDING REMOVAL!
	 */
	IView getView(String type);

	/**
	 * TODO PENDING REMOVAL!
	 */
	void resetEditorTitle(boolean contentChanged, String projectName,
			String fileName);

	/**
	 * Opens a project explorer.
	 */
	void openProjectExplorer();

	void saveEditor(IEditor editor);

	void saveAllEditors();

	void closeEditor(IEditor editor, boolean showDialog);

	void closeAllButThisEditor(IEditor editorToKeepOpened, boolean showDialog);

	void closeAllEditors(boolean showDialog);

	void closeView(IView view);

	IView openView(String type);

	void refreshWorkspace();
}