package hr.fer.zemris.vhdllab.applets.automat;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
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
	
	private static boolean isReadOnly=false;
	private static boolean isSavable=false;
	private ProjectContainer pContainer;
	private String projectName="default project";
	private String fileName;
	private ResourceBundle bundle=null;
	
	public Automat() {
		super();	
	}

	
	
	private void createGUI() {
			
			adrw=new AutoDrawer();
			Icon ic=new ImageIcon(getClass().getResource("AddMode1.png"));
			JToggleButton dodajNoviSignal=new JToggleButton(ic);
			dodajNoviSignal.setActionCommand("Dodaj stanje");
			dodajNoviSignal.setToolTipText("Add state");
			ic=new ImageIcon(getClass().getResource("AddMode2.png"));
			JToggleButton dodajNoviPrijelaz=new JToggleButton(ic);
			dodajNoviPrijelaz.setActionCommand("Dodaj prijelaz");
			dodajNoviPrijelaz.setToolTipText("Add transition");
			ic=new ImageIcon(getClass().getResource("DeleteMode.png"));
			JToggleButton brisi=new JToggleButton(ic);
			brisi.setActionCommand("Brisi");
			brisi.setToolTipText("Delete");
			ic=new ImageIcon(getClass().getResource("EditMode.png"));
			final JToggleButton normal=new JToggleButton(ic);
			normal.setActionCommand("Normal");
			normal.setToolTipText("Editor mode");
			ic=new ImageIcon(getClass().getResource("StartStateMode.png"));
			JToggleButton pocStanje=new JToggleButton(ic);
			pocStanje.setActionCommand("pocStanje");
			pocStanje.setToolTipText("Set initial state");
		

		
		
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


//*************************************************************************
	
	public void setFileContent(FileContent fc) {
		if(adrw!=null)adrw.setData(fc.getContent());
	}



	public String getData() {
		adrw.isModified=false;
		return adrw.getData();
	}



	public String getProjectName() {
		return projectName;
	}



	public String getFileName() {
		return fileName;
	}



	public boolean isModified() {
		return adrw.isModified;
	}



	public void highlightLine(int line) {
		
	}



	public void setProjectContainer(ProjectContainer pContainer) {
		this.pContainer=pContainer;
		projectName=pContainer.getActiveProject();
		try {
			bundle=pContainer.getResourceBundle("Client_Automat_ApplicationResource");
		} catch (UniformAppletException e) {
			e.printStackTrace();
		}
		if(bundle!=null) adrw.setResourceBundle(bundle);
	}



	public IWizard getWizard() {
		return this;
	}


	public FileContent getInitialFileContent(Component parent) {
		AUTPodatci pod=new AUTPodatci((JComponent)parent);
		String gen=null;
		if(pod.ime!=null){
			LinkedList<Stanje> stanja=new LinkedList<Stanje>();
			HashSet<Prijelaz> prijelazi=new HashSet<Prijelaz>();
			gen=new CodeGenerator().generateInternalCode(pod,prijelazi,stanja);
		} else return null;
		return new FileContent(projectName,pod.ime,gen);
	}



	public void setReadOnly(boolean flag) {
		isReadOnly=flag;
		
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}



	public boolean isSavable() {
		return isSavable;
	}



	public void setSavable(boolean flag) {
		isSavable=flag;
	}



	public void init() {
		createGUI();
		
	}




}
