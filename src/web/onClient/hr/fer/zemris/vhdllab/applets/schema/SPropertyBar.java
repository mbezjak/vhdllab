package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainFrame;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class SPropertyBar extends JToolBar {
	private SPropertyPanel panel = null;
	private JScrollPane scrpan = null;
	private String currentSelectedInstance = null;
	private SchemaMainFrame parentFrame = null;

	public SPropertyBar(SchemaMainFrame mfr) {
		super("Property Bar");
		parentFrame = mfr;
		
		this.setLayout(new BorderLayout());
		
		panel = new SPropertyPanel(null, this);
		scrpan = new JScrollPane(panel);
		scrpan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(scrpan, BorderLayout.NORTH);
	}
	
	public void generatePropertiesAndSetAsSelected(AbstractSchemaComponent comp) {
		if (comp == null) {
			currentSelectedInstance = null;
			panel.removeAll();
			return;
		}
		currentSelectedInstance = comp.getComponentInstanceName();
		panel.setLinkToComponent(comp);
		parentFrame.requestFocus();
	}
	
	public String getSelectedComponentInstanceName() {
		return currentSelectedInstance;
	}
	
	public void showNoProperties() {
		panel.removeAll();
		currentSelectedInstance = null;
		repaint();
	}
	
	public void handlePropertyUpdate() {
		if (currentSelectedInstance != null) {
			parentFrame.handleComponentPropertyChanged(currentSelectedInstance);
		}
	}
	
	public void handleNameUpdate(String oldName, String newName) {
		parentFrame.handleComponentNameChanged(oldName, newName);
	}
	
}
