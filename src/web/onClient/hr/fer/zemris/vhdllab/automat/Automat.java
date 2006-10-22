package hr.fer.zemris.vhdllab.automat;

import hr.fer.zemris.vhdllab.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.main.interfaces.ProjectContainter;

import javax.swing.JPanel;
/**
 * 
 * @author ddelac
 *
 */
public class Automat extends JPanel implements IEditor, IWizard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2093412659859056334L;

	public void setFileContent(FileContent fContent) {
		// TODO Auto-generated method stub
		
	}

	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isModified() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setProjectContainer(ProjectContainter pContainer) {
		// TODO Auto-generated method stub
		
	}

	public IWizard getWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	public FileContent getInitialFileContent() {
		// TODO Auto-generated method stub
		return null;
	}

}
