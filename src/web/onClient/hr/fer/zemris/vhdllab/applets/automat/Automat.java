package hr.fer.zemris.vhdllab.applets.automat;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
		final JToggleButton normal=new JToggleButton(ic);
		normal.setActionCommand("Normal");
		normal.setToolTipText("Super Mario mode!!!");
		ic=new ImageIcon("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/StartStateMode.png");
		JToggleButton pocStanje=new JToggleButton(ic);
		pocStanje.setActionCommand("pocStanje");
		pocStanje.setToolTipText("Pocetno stanje");
		

		
		
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
		
		pocStanje.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("pocStanje"))adrw.setStanjeRada(6);
			}
		});
		
		JToolBar tulbar=new JToolBar();
		final ButtonGroup grupa=new ButtonGroup();
		grupa.add(normal);
		grupa.add(dodajNoviSignal);
		grupa.add(dodajNoviPrijelaz);
		grupa.add(brisi);
		grupa.add(pocStanje);
		grupa.setSelected(normal.getModel(),true);
		tulbar.add(normal);
		tulbar.add(dodajNoviSignal);
		tulbar.add(dodajNoviPrijelaz);
		tulbar.add(brisi);
		tulbar.add(pocStanje);
		
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



	public void setupWizard() {
		// TODO Auto-generated method stub
		
	}



	public FileContent getInitialFileContent() {
		// TODO Auto-generated method stub
		return null;
	}



	public void setReadOnly(boolean flag) {
		// TODO Auto-generated method stub
		
	}



	public void setSaveable(boolean flag) {
		// TODO Auto-generated method stub
		
	}



	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}



	public boolean isSavable() {
		// TODO Auto-generated method stub
		return false;
	}



	public void setSavable(boolean flag) {
		// TODO Auto-generated method stub
		
	}



	

}
