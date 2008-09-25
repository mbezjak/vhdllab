/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main.componentIdentifier;

import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfiguration;
import hr.fer.zemris.vhdllab.applets.main.conf.EditorProperties;
import hr.fer.zemris.vhdllab.applets.main.constant.ComponentTypes;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * Defines methods that allows easy creation of various component identifiers.
 * 
 * @author Miro Bezjak
 */
public class ComponentIdentifierFactory {

	private static ISystemContainer container;

	private static ComponentConfiguration conf;

	/**
	 * Dont let anyone instantiate this class.
	 */
	private ComponentIdentifierFactory() {
	}

	/**
	 * Setter for a system container.
	 * 
	 * @param container
	 *            a system container
	 * @throws NullPointerException
	 *             if <code>container</code> is <code>null</code>
	 */
	public static void setContainer(ISystemContainer container) {
		if (container == null) {
			throw new NullPointerException("System container cant be null");
		}
		ComponentIdentifierFactory.container = container;
	}

	/**
	 * Setter for a component configuration.
	 * 
	 * @param conf
	 *            a component configuration
	 * @throws NullPointerException
	 *             if <code>conf</code> is <code>null</code>
	 */
	public static void setComponentConfiguration(ComponentConfiguration conf) {
		if (conf == null) {
			throw new NullPointerException(
					"Component configuration cant be null");
		}
		ComponentIdentifierFactory.conf = conf;
	}

	/**
	 * Creates a file editor identifier based on specified resource.
	 * 
	 * @param projectName
	 *            a project that contains a file
	 * @param fileName
	 *            a file for whom to create identifier
	 * @return an editor identifier
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public static IComponentIdentifier<FileIdentifier> createFileEditorIdentifier(
	        Caseless projectName, Caseless fileName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		FileIdentifier file = new FileIdentifier(projectName, fileName);
		return createFileEditorIdentifier(file);
	}

	/**
	 * Creates a file editor identifier based on specified resource.
	 * 
	 * @param file
	 *            a file for whom to create identifier.
	 * @return an editor identifier
	 * @throws NullPointerException
	 *             if <code>file</code> is <code>null</code>
	 */
	public static IComponentIdentifier<FileIdentifier> createFileEditorIdentifier(
			FileIdentifier file) {
		if (file == null) {
			throw new NullPointerException("File identifier cant be null");
		}
		FileType fileType = container.getResourceManager().getFileType(
				file.getProjectName(), file.getFileName());
		EditorProperties ep = conf.getEditorPropertiesByFileType(fileType);
		return new EditorIdentifier<FileIdentifier>(ep.getId(), file);
	}

	/**
	 * Creates a simulation editor identifier based on specified resource.
	 * 
	 * @param file
	 *            a file for whom to create identifier.
	 * @return an editor identifier
	 * @throws NullPointerException
	 *             if <code>file</code> is <code>null</code>
	 */
	public static IComponentIdentifier<FileIdentifier> createSimulationEditorIdentifier(
			FileIdentifier file) {
		if (file == null) {
			throw new NullPointerException("File identifier cant be null");
		}
		return new EditorIdentifier<FileIdentifier>(
				ComponentTypes.EDITOR_SIMULATION, file);
	}

	/**
	 * Creates a view vhdl editor identifier based on specified resource.
	 * 
	 * @param projectName
	 *            a project that contains a file
	 * @param fileName
	 *            a file for whom to create identifier
	 * @return an editor identifier
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public static IComponentIdentifier<FileIdentifier> createViewVHDLIdentifier(
	        Caseless projectName, Caseless fileName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		FileIdentifier file = new FileIdentifier(projectName, fileName);
		return createViewVHDLIdentifier(file);
	}

	/**
	 * Creates a view vhdl identifier based on specified resource.
	 * 
	 * @param file
	 *            a file for whom to create identifier.
	 * @return an editor identifier
	 * @throws NullPointerException
	 *             if <code>file</code> is <code>null</code>
	 */
	public static IComponentIdentifier<FileIdentifier> createViewVHDLIdentifier(
			FileIdentifier file) {
		if (file == null) {
			throw new NullPointerException("File identifier cant be null");
		}
		return new EditorIdentifier<FileIdentifier>(
				ComponentTypes.EDITOR_VIEW_VHDL, file);
	}

	/**
	 * Creates a preferences editor identifier. Return value does not have an
	 * instance modifier.
	 * 
	 * @return a preferences editor identifier
	 */
	public static IComponentIdentifier<?> createPreferencesIdentifier() {
		return new EditorIdentifier<Object>(ComponentTypes.EDITOR_PREFERENCES);
	}

	/**
	 * Creates a view identifier. Return value does not have an instance
	 * modifier.
	 * 
	 * @param type
	 *            a view type
	 * @return a project explorer view identifier
	 */
	public static IComponentIdentifier<?> createViewIdentifier(String type) {
		return new ViewIdentifier<Object>(type);
	}

	/**
	 * Creates a project explorer view identifier. Return value does not have an
	 * instance modifier.
	 * 
	 * @return a project explorer view identifier
	 */
	public static IComponentIdentifier<?> createProjectExplorerIdentifier() {
		return new ViewIdentifier<Object>(ComponentTypes.VIEW_PROJECT_EXPLORER);
	}

}
