package hr.fer.zemris.vhdllab.automat;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import hr.fer.zemris.vhdllab.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.main.interfaces.ProjectContainter;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
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
	
	public Automat(String podatci) {
		super();
		createGUI(podatci);

		
	}

	
	
	private void createGUI(String podatci) {
		
		AutoDrawer adrw=new AutoDrawer(podatci);
		JButton dodajNoviSignal=new JButton("Dodaj novi signal");
		JButton dodajNoviPrijelaz=new JButton("Dodaj novi prijelaz");
		JToolBar tulbar=new JToolBar();
		tulbar.add(dodajNoviSignal);
		tulbar.add(dodajNoviPrijelaz);
		
		this.setLayout(new BorderLayout());
		this.add(adrw,BorderLayout.CENTER);
		this.add(tulbar,BorderLayout.NORTH);
		
	}



	//TODO ---------------IMPLEMENTIRAJ KASNIJE-------------------------------
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
