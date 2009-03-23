package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

import java.awt.Component;

/**
 * Interface defining methods that Wizards should have. Every editor must also
 * implement this interface and thereby creating a component (most likely
 * dialog) that will be displayed to user to create new file instance. Every
 * wizard must have an empty public default constructor!
 * <p>
 * Note that not every editor can create file instances. For example: Simulator
 * (editor used to display simulation results) may not create new file instances
 * because simulation result can not be created out of nothing nor can it be
 * created on client application. For that it needs one of VHDL simulators that
 * are run on server.
 * <p>
 * Therefor if editor can not create new file instances then method defined by
 * this interface may be empty (methods will do nothing). However all editors
 * (including those that can not create new file instances) must implement this
 * interface.
 * 
 * @author Miro Bezjak
 */
public interface Wizard {

	/**
	 * Sets a project container so that this wizard may communicate with other
	 * components in main applet or gain any information that project container
	 * offers.
	 * 
	 * @param container
	 *            a project container
	 * @see hr.fer.zemris.vhdllab.platform.manager.editor.WizardManager
	 */
	void setContainer(PlatformContainer container);

	/**
	 * Returns an initial <code>FileContent</code> where initial means file
	 * content created as a result of what user selected/wrote in a wizard.
	 * <p>
	 * For example: Initial file content of VHDL source editor may be project
	 * and file name chosen by user, along with written entity block of vhdl
	 * code. Other editors will (most likely) not have vhdl code written in file
	 * content, but rather an intern format that they know how to display. Note
	 * that name of entity block in VHDL file must be the same as returned
	 * <code>FileContent</code>'s file name because <code>Server</code>
	 * dictates this rule!
	 * <p>
	 * This method should only display a wizard! Not actual editors to display
	 * returning file content (do not invoke method
	 * {@link WizardManager#openEditor(Caseless, Caseless, boolean, boolean)}).
	 * <p>
	 * Returned value may be <code>null</code>. In that case it indicates
	 * either editor does not have a wizard or user clicked on a
	 * <code>cancel</code> button and does not want to create new file
	 * instance.
	 * 
	 * @param parent
	 *            a parent component used to enable modal dialog
	 * @param projectName
	 *            a name of a project that must be a part of returning
	 *            <code>FileContent</code>; this indicates in which project
	 *            should created file be placed
	 * @return an initial <code>FileContent</code>
	 */
	File getInitialFileContent(Component parent, Project project);

}
