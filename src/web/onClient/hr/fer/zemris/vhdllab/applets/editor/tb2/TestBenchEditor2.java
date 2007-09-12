package hr.fer.zemris.vhdllab.applets.editor.tb2;

import hr.fer.zemris.vhdllab.applets.main.event.EditorListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/**
 * New version of TestBench editor. Provides easy-to-use graphical user interfac for creating testbench
 * for VHDL files.
 * @author ddelac
 *
 */
public class TestBenchEditor2 extends JPanel implements IEditor, IWizard {

	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 3290741436480467180L;
	
	private EditorPanel edPan;
	
	
	public TestBenchEditor2() {
		super();
	}
	
		
	/**
	 * Creates graphical user interface for TestBench editor. 
	 *
	 */
	private void createGUI() {
		//TODO: finish this
		
		JToolBar toolbar=new JToolBar();
		//Create buttons for toolbar
		JLabel lab=new JLabel("Toolbar");
		toolbar.add(lab);
		
		//Add Canvas panel
		
		
		this.setLayout(new BorderLayout());
		this.add(toolbar, BorderLayout.NORTH);
		
		edPan=new EditorPanel();
		JScrollPane pane=new JScrollPane(edPan);
		this.add(pane,BorderLayout.CENTER);
	}










	//************************INTERFACE METHODS**********************************************
	//TODO:implement later :)
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProjectName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IWizard getWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	public void highlightLine(int line) {
		// TODO Auto-generated method stub

	}

	public void init() {
		createGUI();
	}

	public boolean isModified() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSavable() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFileContent(FileContent content) {
		edPan.setContent(content.getContent());
	}

	public void setSystemContainer(ISystemContainer container) {
		// TODO Auto-generated method stub

	}

	public void setReadOnly(boolean flag) {
		// TODO Auto-generated method stub

	}

	public void setSavable(boolean flag) {
		// TODO Auto-generated method stub

	}

	public FileContent getInitialFileContent(Component parent,
			String projectName) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#addEditorListener(hr.fer.zemris.vhdllab.applets.main.event.EditorListener)
	 */
	@Override
	public void addEditorListener(EditorListener l) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#getEditorListeners()
	 */
	@Override
	public EditorListener[] getEditorListeners() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#removeAllEditorListeners()
	 */
	@Override
	public void removeAllEditorListeners() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#removeEditorListener(hr.fer.zemris.vhdllab.applets.main.event.EditorListener)
	 */
	@Override
	public void removeEditorListener(EditorListener l) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#setModified(boolean)
	 */
	@Override
	public boolean setModified(boolean flag) {
		// TODO Auto-generated method stub
		return false;
	}

}
