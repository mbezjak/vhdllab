package hr.fer.zemris.vhdllab.applets.editor.automat;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private ISystemContainer container;
	private String projectName="default project";
	private String fileName;
	private ResourceBundle bundle=null;
	
	public Automat() {
		super();	
	}

	
	/**
	 * Metoda koja kreira GUI automata uz nekoliko predpostavki.
	 * Poziva se metoda AutoDrawer.setMinXY te se pri tom eksperimentalno
	 * zakljucuje da se velicina umanjuje za 3 sto nemora biti tako.
	 *
	 *
	 */
	private void createGUI() {
			
			adrw=new AutoDrawer();

			if(bundle!=null) adrw.setResourceBundle(container,bundle);
			
			Icon ic=new ImageIcon(getClass().getResource("AddMode1.png"));
			final JToggleButton dodajNoviSignal=new JToggleButton(ic);
			dodajNoviSignal.setActionCommand("Dodaj stanje");
			dodajNoviSignal.setToolTipText(bundle.getString(LanguageConstants.BUTTON_ADDSTATE));
			ic=new ImageIcon(getClass().getResource("AddMode2.png"));
			final JToggleButton dodajNoviPrijelaz=new JToggleButton(ic);
			dodajNoviPrijelaz.setActionCommand("Dodaj prijelaz");
			dodajNoviPrijelaz.setToolTipText(bundle.getString(LanguageConstants.BUTTON_ADDTRANSITION));
			ic=new ImageIcon(getClass().getResource("DeleteMode.png"));
			final JToggleButton brisi=new JToggleButton(ic);
			brisi.setActionCommand("Brisi");
			brisi.setToolTipText(bundle.getString(LanguageConstants.BUTTON_DELETE));
			ic=new ImageIcon(getClass().getResource("EditMode.png"));
			final JToggleButton normal=new JToggleButton(ic);
			normal.setActionCommand("Normal");
			normal.setToolTipText(bundle.getString(LanguageConstants.BUTTON_NORMAL));
			ic=new ImageIcon(getClass().getResource("StartStateMode.png"));
			final JToggleButton pocStanje=new JToggleButton(ic);
			pocStanje.setActionCommand("pocStanje");
			pocStanje.setToolTipText(bundle.getString(LanguageConstants.BUTTON_SETINITIAL));
			final JButton podatci=new JButton("Podatci o automatu");
			podatci.setToolTipText(bundle.getString(LanguageConstants.BUTTON_DATA));
		

		
		
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
			
			podatci.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					adrw.dataChange();
				}
				
			});
	
			final JToolBar tulbar=new JToolBar();
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
			tulbar.add(podatci);
			
			JScrollPane p=new JScrollPane(adrw);
			this.setLayout(new BorderLayout());
			this.add(p,BorderLayout.CENTER);
			this.add(tulbar,BorderLayout.NORTH);
			
			//predpostavke za -3:
			adrw.setMinXY(Automat.this.getWidth()-3,
					Automat.this.getHeight()-tulbar.getHeight()-3);
			
			this.addComponentListener(new ComponentListener(){

				public void componentResized(ComponentEvent arg0) {
					adrw.setMinXY(Automat.this.getWidth()-3,
							Automat.this.getHeight()-tulbar.getHeight()-3);
				}

				public void componentMoved(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void componentShown(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void componentHidden(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
		    KeyboardFocusManager.getCurrentKeyboardFocusManager()
		     .addKeyEventDispatcher(new KeyEventDispatcher(){
		        public boolean dispatchKeyEvent(KeyEvent e){
		          if(e.getID() == KeyEvent.KEY_RELEASED)
		          {
		            if(e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
		            	adrw.setStanjeRada(1);
		            	normal.setSelected(true);
		            	dodajNoviPrijelaz.setSelected(false);
		            	dodajNoviSignal.setSelected(false);
		            	brisi.setSelected(false);
		            	pocStanje.setSelected(false);
		            	podatci.setSelected(false);
		            }
		          }
		          return false;}});
	}


//*************************************************************************
	
	public void setFileContent(FileContent fc) {
		fileName = fc.getFileName();
		projectName = fc.getProjectName();
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



	public void setSystemContainer(ISystemContainer container) {
		this.container=container;
		if(container!=null){
			projectName=container.getSelectedProject();
			bundle=container.getResourceBundle("Client_Automat_ApplicationResources");
		}else
			bundle=CachedResourceBundles.getBundle("Client_Automat_ApplicationResources","en");
	}

	public IWizard getWizard() {
		return this;
	}


	public FileContent getInitialFileContent(Component parent, String projectName) {
		AUTPodatci pod=new AUTPodatci(parent,container,bundle);
		//TODO OSTAVI TO: projectName = pContainer.getSelectedProject();
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
	
	public void dispose() {}

	


}
