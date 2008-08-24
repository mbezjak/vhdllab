package hr.fer.zemris.vhdllab.applets.editor.tb3;

import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.AbstractEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class TestbenchEditor extends AbstractEditor implements IWizard {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizard getWizard() {
		return this;
	}

	@Override
	public void init() {
		super.init();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFileContent(FileContent content) {
		super.setFileContent(content);
		// TODO Auto-generated method stub
		setModified(false);
	}

	@Override
	public FileContent getInitialFileContent(Component parent,
			String projectName) {
		RunDialog dialog = new RunDialog(parent, true, container, RunDialog.COMPILATION_TYPE);
		dialog.setChangeProjectButtonText("change");
		dialog.setTitle("Select a file for which to create testbench");
		dialog.setListTitle("Select a file for which to create testbench");
		dialog.startDialog();
		if(dialog.getOption() != RunDialog.OK_OPTION) return null;
		FileIdentifier file = dialog.getSelectedFile();
		if(!projectName.equalsIgnoreCase(file.getProjectName())) {
			SystemLog.instance().addSystemMessage("Cant create testbench for file outside of '"+projectName+"'", MessageType.INFORMATION);
			return null;
		}
		// Ovo gore ostaviti
		// Ovo dolje zamijeniti
		// inicijalno ime testbencha
//		String initialTestbenchName = file.getFileName() + "_tb";
		
		// kako dobit ciruit interface
//		CircuitInterface ci;
//		try {
//			ci = container.getResourceManager().getCircuitInterfaceFor(file.getProjectName(), file.getFileName());
//		} catch (UniformAppletException e) {
//			e.printStackTrace();
//			return null;
//		}
		String testbench = JOptionPane.showInputDialog(parent, "Enter a name of a testbench");
		if(testbench == null) return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<file>").append(file.getFileName()).append("</file>");
		return new FileContent(projectName, testbench, sb.toString());
	}

}
