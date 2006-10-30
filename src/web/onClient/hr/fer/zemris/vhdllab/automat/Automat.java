package hr.fer.zemris.vhdllab.automat;

import hr.fer.zemris.vhdllab.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.main.interfaces.ProjectContainter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	AutoDrawer adrw=null;
	
	public Automat(String podatci) {
		super();
		createGUI(podatci);

		
	}

	
	
	private void createGUI(String podatci) {
		
		adrw=new AutoDrawer(podatci);
		JButton dodajNoviSignal=new JButton("Dodaj novo stanje");
		JButton dodajNoviPrijelaz=new JButton("Dodaj novi prijelaz");
		
		dodajNoviSignal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				adrw.setStanjeRada(2);
			}
			
		});
		
		dodajNoviPrijelaz.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				adrw.setStanjeRada(3);
			}
			
		});
		
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
