package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.applets.editor.automat.entityTable.EntityTable;
import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaEntity;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.awt.Component;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class DefaultWizard implements IWizard {

	/* static fields */
	private static final int MARGIN_OFFSET = Constants.GRID_SIZE * 2;
	
	
	/* private fields */
	private ISystemContainer container;
	
	
	public DefaultWizard() {
	}

	public FileContent getInitialFileContent(Component parent,
			String projectName) {
		String[] options = new String[] {"OK", "Cancel"};
		int optionType = JOptionPane.OK_CANCEL_OPTION;
		int messageType = JOptionPane.PLAIN_MESSAGE;
		EntityTable table = new EntityTable();
		table.setProjectContainer(container);
		table.init();
		int option = JOptionPane.showOptionDialog(parent, table, "New Schema", optionType, messageType, null, options, options[0]);
		if(option == JOptionPane.OK_OPTION) {
			if(projectName == null) return null;
			CircuitInterface ci = table.getCircuitInterface();
			try {
				if(container.getResourceManager().existsFile(projectName, ci.getEntityName())) {
					SystemLog.instance().addSystemMessage(ci.getEntityName() + " already exists!", MessageType.INFORMATION);
				}
			} catch (UniformAppletException e) {
				SystemLog.instance().addSystemMessage("Internal error!", MessageType.INFORMATION);
				return null;
			}
			
			ISchemaInfo info = new SchemaInfo();

			try {
				info.getEntity().getParameters().setValue(SchemaEntity.KEY_NAME, new Caseless(ci.getEntityName()));
			} catch (Exception e) {
				e.printStackTrace();
				SystemLog.instance().addSystemMessage("Internal error!", MessageType.INFORMATION);
				return null;
			}
			
			int ly = MARGIN_OFFSET, ry = MARGIN_OFFSET;
			ISchemaComponentCollection components = info.getComponents();
			for(Port p : ci.getPorts()) {
				InOutSchemaComponent inout = new InOutSchemaComponent(p);
				try {
					if (p.getDirection().isIN()) {
						components.addComponent(MARGIN_OFFSET, ly, inout);
						ly += inout.getHeight() + MARGIN_OFFSET;
					} else { // else isOUT
						components.addComponent(Constants.DEFAULT_SCHEMA_WIDTH, ry, inout);
						ry += inout.getHeight() + MARGIN_OFFSET;
					}
				} catch (Exception e) {
					e.printStackTrace();
					SystemLog.instance().addSystemMessage("Internal error!", MessageType.INFORMATION);
					return null;
				}
			}
			
			SchemaSerializer ss = new SchemaSerializer();
			StringWriter writer = new StringWriter(1000 + 1000 * ci.getPorts().size());
			try {
				ss.serializeSchema(writer, info);
			} catch (IOException e) {
				e.printStackTrace();
				SystemLog.instance().addSystemMessage("Internal error!", MessageType.INFORMATION);
				return null;
			}
			return new FileContent(projectName, ci.getEntityName(), writer.toString());
		} else return null;
	}

	public void setSystemContainer(ISystemContainer container) {
		this.container = container;
	}

}
