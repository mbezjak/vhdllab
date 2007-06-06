package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;

import javax.swing.JPanel;




public class SchemaMainPanel extends JPanel {
	
	private ISchemaCore core;
	private ISchemaController controller;
	
	
	public SchemaMainPanel() {
		init();
	}


	private void init() {
		core = new SchemaCore();
		controller = new LocalController();
		
		core.registerController(controller);
		controller.registerCore(core);
	}
	
	public ISchemaController getController() {
		return controller;
	}
	
}






















