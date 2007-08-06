package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageType;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;

import java.util.ResourceBundle;

/**
 * A system container is a core of vhdllab and contains important methods about
 * the system. It is designed to be a mediator between a component module (an
 * editor for example) and a system or another module. Since modules can't
 * communicate directly they can use some of system container's methods to
 * establish communication. System container is also a way of getting required
 * resource from a server (that is actually provided in
 * {@link IResourceManager} interface but system container providers resource
 * manager interface).
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
	 * @see #compile(FileIdentifier)
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
	 * @see #compile(FileIdentifier)
	 * @see #compile(IEditor)
	 * @see #compile(String, String)
	 */
	boolean compileLastHistoryResult();

	/**
	 * Compiles specified resource. If <code>file</code> is <code>null</code>
	 * this method will not throw an exception and will simply return
	 * <code>false</code>.
	 * <p>
	 * Note that some resource is not compilable (simulation for example)
	 * therefor this method will not compile such resource. To check if a
	 * resource is compilable invoke
	 * {@link IResourceManager#isCompilable(String, String)} method.
	 * </p>
	 * 
	 * @param file
	 *            a resource to compile
	 * @return <code>true</code> if resource has been compiled;
	 *         <code>false</code> otherwise
	 * @see #compileWithDialog()
	 * @see #compileLastHistoryResult()
	 * @see #compile(IEditor)
	 * @see #compile(String, String)
	 */
	boolean compile(FileIdentifier file);

	/**
	 * Compiles a resource in an editor and returns <code>true</code> if
	 * resource has been compiled or <code>false</code> otherwise. If
	 * <code>editor</code> is <code>null</code> this method will not throw
	 * an exception and will simply return <code>false</code>.
	 * <p>
	 * Note that some resource is not compilable (simulation for example)
	 * therefor this method will not compile such resource. To check if a
	 * resource is compilable invoke
	 * {@link IResourceManager#isCompilable(String, String)} method.
	 * </p>
	 * 
	 * @param editor
	 *            an editor containing resource to compile
	 * @return <code>true</code> if resource has been compiled;
	 *         <code>false</code> otherwise
	 * @see #compileWithDialog()
	 * @see #compileLastHistoryResult()
	 * @see #compile(FileIdentifier)
	 * @see #compile(String, String)
	 */
	boolean compile(IEditor editor);

	/**
	 * Compiles a specified resource. If any error occurs this method will
	 * simply return <code>false</code>.
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
	 * @see #compile(FileIdentifier)
	 * @see #compile(IEditor)
	 */
	boolean compile(String projectName, String fileName);

	/* SIMULATE RESOURCE METHODS */

	/**
	 * Shows a dialog to a user so he can select a resource that he wants
	 * simulated. A user can cancel a dialog and in that case this method will
	 * return <code>false</code>.
	 * 
	 * @return <code>true</code> if a resource has been simulated; false
	 *         otherwise
	 * @see #simulateLastHistoryResult()
	 * @see #simulate(FileIdentifier)
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
	 * @see #simulate(FileIdentifier)
	 * @see #simulate(IEditor)
	 * @see #simulate(String, String)
	 */
	boolean simulateLastHistoryResult();

	/**
	 * Simulates specified resource. If <code>file</code> is <code>null</code>
	 * this method will not throw an exception and will simply return
	 * <code>false</code>.
	 * <p>
	 * Note that some resource is not simulatable (vhdl source for example)
	 * therefor this method will not simulate such resource. To check if a
	 * resource is simulatable invoke
	 * {@link IResourceManager#isSimulatable(String, String)} method.
	 * </p>
	 * 
	 * @param file
	 *            a resource to simulate
	 * @return <code>true</code> if resource has been simulated;
	 *         <code>false</code> otherwise
	 * @see #simulateWithDialog()
	 * @see #simulateLastHistoryResult()
	 * @see #simulate(IEditor)
	 * @see #simulate(String, String)
	 */
	boolean simulate(FileIdentifier file);

	/**
	 * Simulates a resource in an editor and returns <code>true</code> if
	 * resource has been simulated or <code>false</code> otherwise. If
	 * <code>editor</code> is <code>null</code> this method will not throw
	 * an exception and will simply return <code>false</code>.
	 * <p>
	 * Note that some resource is not simulatable (vhdl source for example)
	 * therefor this method will not simulate such resource. To check if a
	 * resource is simulatable invoke
	 * {@link IResourceManager#isSimulatable(String, String)} method.
	 * </p>
	 * 
	 * @param editor
	 *            an editor containing resource to simulate
	 * @return <code>true</code> if resource has been simulated;
	 *         <code>false</code> otherwise
	 * @see #simulateWithDialog()
	 * @see #simulateLastHistoryResult()
	 * @see #simulate(FileIdentifier)
	 * @see #simulate(String, String)
	 */
	boolean simulate(IEditor editor);

	/**
	 * Simulates a specified resource. If any error occurs this method will
	 * simply return <code>false</code>.
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
	 * @see #simulate(FileIdentifier)
	 * @see #simulate(IEditor)
	 */
	boolean simulate(String projectName, String fileName);

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
	 * @param type
	 *            a type of a file to create
	 * @return <code>true</code> if specified resource was successfully
	 *         created; <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>type</code> is <code>null</code>
	 */
	boolean createNewFileInstance(String type);

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

	/* PREFERENCES AND RESOURCE BUNDLE METHODS */

	/**
	 * Returns a system log. Return value will never be <code>null</code>.
	 * 
	 * @return a system log
	 */
	ISystemLog getSystemLog();

	/**
	 * Returns a resource manager for manipulating resources. Return value
	 * will never be <code>null</code>.
	 * 
	 * @return a resource manager
	 */
	IResourceManager getResourceManager();

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
	void echoStatusText(String text, MessageType type);

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
	 * @return an editor presenting vhdl code or <code>null</code> if any
	 *         error occurs
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	IEditor viewVHDLCode(String projectName, String fileName);

	/**
	 * TODO PENDING REMOVAL
	 */
	IEditor getEditor(FileIdentifier resource);

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