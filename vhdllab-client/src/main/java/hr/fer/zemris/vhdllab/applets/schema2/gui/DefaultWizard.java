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
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.awt.Component;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class DefaultWizard implements Wizard {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(DefaultWizard.class);

    /* static fields */
    private static final int MARGIN_OFFSET = Constants.GRID_SIZE * 2;

    /* private fields */
    private PlatformContainer container;

    public DefaultWizard() {
    }

    public File getInitialFileContent(Component parent,
            hr.fer.zemris.vhdllab.entities.Caseless projectName) {
        String[] options = new String[] { "OK", "Cancel" };
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.PLAIN_MESSAGE;
        EntityTable table = new EntityTable();

        int option = 0;
        boolean flag = false;

        do {
            flag = false;
            option = JOptionPane.showOptionDialog(parent, table, "New Schema",
                    optionType, messageType, null, options, options[0]);
            if (!table.isDataCorrect() && option == JOptionPane.OK_OPTION) {
                flag = true;
                JOptionPane.showMessageDialog(parent, "Bad input!");
            }
        } while (flag);

        if (option == JOptionPane.OK_OPTION) {
            if (projectName == null)
                return null;
            CircuitInterface ci = table.getCircuitInterface();
            File file = container.getMapper().getFile(
                    new FileIdentifier(projectName,
                            new hr.fer.zemris.vhdllab.entities.Caseless(ci
                                    .getName())));
            if (file != null) {
                LOG.info(ci.getName() + " already exists!");
            }

            ISchemaInfo info = new SchemaInfo();

            try {
                Caseless cname = new Caseless(ci.getName());
                info.getEntity().getParameters().setValue(
                        SchemaEntity.KEY_NAME, cname);
                HashSet<Object> allowed = new HashSet<Object>();
                allowed.add(cname);
                info.getEntity().getParameters().getParameter(
                        SchemaEntity.KEY_NAME).getConstraint()
                        .setPossibleValues(allowed);
            } catch (Exception e) {
                LOG.error("Unexpected error", e);
                return null;
            }

            int ly = MARGIN_OFFSET, ry = MARGIN_OFFSET;
            ISchemaComponentCollection components = info.getComponents();
            for (Port p : ci.getPorts()) {
                InOutSchemaComponent inout = new InOutSchemaComponent(p);
                try {
                    if (p.isIN()) {
                        components.addComponent(MARGIN_OFFSET, ly, inout);
                        ly += inout.getHeight() + MARGIN_OFFSET;
                    } else { // else isOUT
                        components.addComponent(Constants.DEFAULT_SCHEMA_WIDTH,
                                ry, inout);
                        ry += inout.getHeight() + MARGIN_OFFSET;
                    }
                } catch (Exception e) {
                    LOG.error("Unexpected error", e);
                    return null;
                }
            }

            SchemaSerializer ss = new SchemaSerializer();
            StringWriter writer = new StringWriter(1000 + 1000 * ci.getPorts()
                    .size());
            try {
                ss.serializeSchema(writer, info);
            } catch (IOException e) {
                LOG.error("Unexpected error", e);
                return null;
            }
            Project project = container.getMapper().getProject(new ProjectIdentifier(projectName));
            return new File(FileType.SCHEMA, new hr.fer.zemris.vhdllab.entities.Caseless(ci.getName()), writer.toString(), project.getId());
        } else
            return null;
    }

    public void setContainer(PlatformContainer container) {
        this.container = container;
    }

}
