package hr.fer.zemris.vhdllab.applets.editor.tb3;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageType;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TestbenchEditor extends JPanel implements IEditor, IWizard {
	
	private static final long serialVersionUID = -1637316576029494858L;
	
	private ISystemContainer container;
	private boolean readOnly;
	private boolean savable;
	private boolean modified;
	private FileContent content;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		return content.getFileName();
	}

	@Override
	public String getProjectName() {
		return content.getProjectName();
	}

	@Override
	public IWizard getWizard() {
		return this;
	}

	@Override
	public void highlightLine(int line) {
	}

	@Override
	public void init() {
		modified = false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isModified() {
		return modified;
	}
	
	private void setModified(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public boolean isSavable() {
		return savable;
	}

	@Override
	public void setFileContent(FileContent content) {
		this.content = content;
		// TODO Auto-generated method stub
	}

	@Override
	public void setSystemContainer(ISystemContainer container) {
		this.container = container;
	}

	@Override
	public void setReadOnly(boolean flag) {
		this.readOnly = flag;
	}

	@Override
	public void setSavable(boolean flag) {
		savable = flag;
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
			container.echoStatusText("Cant create testbench for file outside of '"+projectName+"'", MessageType.INFORMATION);
			return null;
		}
		// Ovo gore ostaviti
		// Ovo dolje zamijeniti
		// inicijalno ime testbencha
//		String initialTestbenchName = file.getFileName() + "_tb";
		
		// kako dobit ciruit interface
//		CircuitInterface ci;
//		try {
//			ci = container.getCircuitInterfaceFor(file.getProjectName(), file.getFileName());
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
