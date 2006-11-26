package hr.fer.zemris.vhdllab.applets.automat;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
/**
 * 
 * @author ddelac
 *
 */
public class Automat extends JPanel implements IEditor,IWizard  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2093412659859056334L;
	
	/**
	 * AutoDrawer je JPanel na koji se crta automat
	 */
	AutoDrawer adrw=null;
	
	public Automat() {
		super();		
	}

	
	
	private void createGUI(String podatci) {
		
		adrw=new AutoDrawer(podatci);
		Icon ic=new ImageIcon("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/AddMode1.png");
		JToggleButton dodajNoviSignal=new JToggleButton(ic);
		dodajNoviSignal.setActionCommand("Dodaj stanje");
		dodajNoviSignal.setToolTipText("Dodaj stanje");
		ic=new ImageIcon("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/AddMode2.png");
		JToggleButton dodajNoviPrijelaz=new JToggleButton(ic);
		dodajNoviPrijelaz.setActionCommand("Dodaj prijelaz");
		dodajNoviPrijelaz.setToolTipText("Dodaj prijelaz");
		ic=new ImageIcon("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/DeleteMode.png");
		JToggleButton brisi=new JToggleButton(ic);
		brisi.setActionCommand("Brisi");
		brisi.setToolTipText("Brisi");
		ic=new ImageIcon("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/EditMode.png");
		JToggleButton normal=new JToggleButton(ic);
		normal.setActionCommand("Normal");
		normal.setToolTipText("Super Mario mode!!!");
		
		normal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Normal"))adrw.setStanjeRada(1);
			}
			
		});
		
		dodajNoviSignal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Dodaj stanje"))adrw.setStanjeRada(2);
			}
			
		});
		
		dodajNoviPrijelaz.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Dodaj prijelaz"))adrw.setStanjeRada(3);
			}
			
		});
		
		brisi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Brisi"))adrw.setStanjeRada(5);
			}
		});
		
		JToolBar tulbar=new JToolBar();
		ButtonGroup grupa=new ButtonGroup();
		grupa.add(normal);
		grupa.add(dodajNoviSignal);
		grupa.add(dodajNoviPrijelaz);
		grupa.add(brisi);
		grupa.setSelected(normal.getModel(),true);
		tulbar.add(normal);
		tulbar.add(dodajNoviSignal);
		tulbar.add(dodajNoviPrijelaz);
		tulbar.add(brisi);
		
		this.setLayout(new BorderLayout());
		this.add(adrw,BorderLayout.CENTER);
		this.add(tulbar,BorderLayout.NORTH);
			
	}


//	TODO ---------------IMPLEMENTIRAJ KASNIJE-------------------------------
	
	public void setFileContent(FileContent fc) {
		createGUI(fc.getContent());
		
	}



	public String getData() {
		return adrw.getData();
	}



	public String getProjectName() {
		// TODO Auto-generated method stub
		return null;
	}



	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}



	public boolean isModified() {
		// TODO Auto-generated method stub
		return false;
	}



	public void highlightLine(int line) {
		// TODO Auto-generated method stub
		
	}



	public void setProjectContainer(ProjectContainer pContainer) {
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
