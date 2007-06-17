package hr.fer.zemris.vhdllab.applets.schema2.gui;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;

import javax.swing.JPanel;




public class SchemaMainPanel extends JPanel implements IEditor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6643347269051956602L;
	
	
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
		
		controller.registerCore(core);
	}
	
	private void initDynamic() {
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
		// TODO Auto-generated method stub
		return null;
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






















