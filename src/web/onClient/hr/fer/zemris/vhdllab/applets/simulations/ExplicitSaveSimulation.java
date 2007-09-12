/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.simulations;

import java.awt.Frame;

import javax.swing.JOptionPane;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IExplicitSave;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.client.core.SystemContext;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.constants.FileTypes;

/**
 * @author Miro Bezjak
 * 
 */
public class ExplicitSaveSimulation implements IExplicitSave {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IExplicitSave#save(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer)
	 */
	@Override
	public void save(IEditor editor, ISystemContainer container)
			throws UniformAppletException {
		Frame owner = SystemContext.getFrameOwner();
		String name = JOptionPane.showInputDialog(owner,
				"Enter a name under which to save this resource:");
		if (name == null || name.equals("")) {
			return;
		}
		IResourceManager rm = container.getResourceManager();
		if (!rm.isCorrectFileName(name)) {
			SystemLog.instance().addSystemMessage("Invalid name!",
					MessageType.INFORMATION);
		}
		String projectName = editor.getProjectName();
		rm.createNewResource(projectName, name, FileTypes.FT_VHDL_SIMULATION,
				editor.getData());
		editor.setModified(false);
	}

}
