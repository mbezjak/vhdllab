package hr.fer.zemris.vhdllab.applets.main.interfaces;


import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.preferences.Preferences;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.List;
import java.util.ResourceBundle;

public interface ProjectContainer {
	
	List<String> getAllCircuits(String projectName) throws UniformAppletException;
	CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) throws UniformAppletException;
	Preferences getPreferences(String type) throws UniformAppletException;
	void openEditor(String projectName, String fileName, boolean isSavable, boolean isReadOnly) throws UniformAppletException;
	boolean existsFile(String projectName, String fileName) throws UniformAppletException;
	boolean existsProject(String projectName) throws UniformAppletException;
	IEditor getEditor(String projectName, String fileName) throws UniformAppletException;
	void compile(String projectName, String fileName) throws UniformAppletException;
	void simulate(String projectName, String fileName) throws UniformAppletException;
	
	/**
	 * Gets a resource bundle for the given base name and user's locale.
	 * @param baseName the base name of the resource bundle, a fully qualified class name
	 * @return a resource bundle for the given base name and user's locale.
	 * @throws UniformAppletException if any error occurred
	 */
	ResourceBundle getResourceBundle(String baseName) throws UniformAppletException;
	
	/**
	 * Reset editor title to indicate to user that editor's contents are changed or not
	 * (depending on a <code>contentChanged</code> flag). Both project name and file name
	 * identifies an editor whose title should be reset.
	 * @param contentChanged a flag that indicates if editor's contents are changed or not
	 * @param projectName a name of a project
	 * @param fileName a name of a file
	 */
	void resetEditorTitle(boolean contentChanged, String projectName, String fileName);
	
	/**
	 * Returns a current active project. An active project is a project which user is currently
	 * working on. Note that there is a possibility that user does not have an active project.
	 * Such case is, for example, when user does not have any projects at all. If there is no
	 * active project this method will return <code>null</code>.
	 * @return a current active project or <code>null</code> if there is no active project.
	 */
	String getActiveProject();
	
}