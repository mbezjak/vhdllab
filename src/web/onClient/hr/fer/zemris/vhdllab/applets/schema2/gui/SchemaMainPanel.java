package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedConf;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyWizard;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.SchemaException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.DefaultSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JPanel;




public class SchemaMainPanel extends JPanel implements IEditor {
	
	private class ModificationListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			modified = true;
		}
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6643347269051956602L;
	
	
	
	/* static fields */
	private static final String PREDEFINED_FILE_NAME = "predefined.xml";
	
	
	/* private fields */
	private ISchemaCore core;
	private ISchemaController controller;
	
	/* IEditor private fields */
	private ProjectContainer projectContainer;
	private FileContent filecontent;
	private boolean readonly, saveable, modified;
	
	
	
	/* ctors */
	
	public SchemaMainPanel() {
		initStatic();
	}

	
	
	
	
	/* methods */

	private void initStatic() {
		core = new SchemaCore();
		controller = new LocalController();
		projectContainer = null;
		filecontent = null;
		readonly = false;
		saveable = false;
		modified = false;
		
		controller.registerCore(core);
		controller.addListener(EPropertyChange.ANY_CHANGE, new ModificationListener());
	}
	
	private void initDynamic() {
		// init prototype components
		initPrototypes();
		
		// init gui
		initGUI();
	}
	
	private void initPrototypes() {
		String predefined;
		
		try {
			predefined = projectContainer.getPredefinedFileContent(PREDEFINED_FILE_NAME);
		} catch (UniformAppletException e) {
			throw new SchemaException("Could not open predefined component file.", e);
		}
		
		PredefinedComponentsParser predefparser = new PredefinedComponentsParser(predefined);
		PredefinedConf predefconf = predefparser.getConfiguration();
		
		for (PredefinedComponent pc : predefconf.getComponents()) {
			try {
				core.getSchemaInfo().getPrototyper().addPrototype(new DefaultSchemaComponent(pc));
			} catch (DuplicateKeyException e) {
				throw new SchemaException("Duplicate component.", e);
			}
		}
	}
	
	private void initGUI() {
	}
	
	private void resetSchema() {
		core.reset();
		controller.clearCommandCache();
	}
	
	public ISchemaController getController() {
		return controller;
	}



	
	
	/* IEditor methods */

	public void cleanUp() {
		resetSchema();
	}

	public String getData() {
		SchemaSerializer ss = new SchemaSerializer();
		StringWriter writer = new StringWriter(500);
		
		try {
			ss.serializeSchema(writer, core.getSchemaInfo());
		} catch (IOException e) {
			return "<schemaInfo></schemaInfo>";
		}
		
		return writer.toString();
	}

	public String getFileName() {
		if (filecontent != null) return filecontent.getFileName();
		return null;
	}

	public String getProjectName() {
		if (filecontent != null) return filecontent.getProjectName();
		return null;
	}

	public IWizard getWizard() {
		// TODO: create real wiz
		return new DummyWizard();
	}

	public void highlightLine(int line) {
		// do absolutely nothing - this is not a text editor.
	}

	public void init() {
		initDynamic();
	}

	public boolean isModified() {
		return modified;
	}

	public boolean isReadOnly() {
		return readonly;
	}

	public boolean isSavable() {
		return saveable;
	}

	public void setFileContent(FileContent content) {
		filecontent = content;
		resetSchema();
		if (filecontent != null) {
			SchemaDeserializer sd = new SchemaDeserializer();
			StringReader stread = new StringReader(filecontent.getContent());
			
			core.setSchemaInfo(sd.deserializeSchema(stread));
		}
	}

	public void setProjectContainer(ProjectContainer container) {
		projectContainer = container;
	}
	
	public void setReadOnly(boolean flag) {
		readonly = flag;
	}
	
	public void setSavable(boolean flag) {
		saveable = flag;
	}
	
	
	
	
	
}






















