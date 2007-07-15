package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.dummies.DummyWizard;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.SchemaException;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.SchemaCanvas;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.CPToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent.ComponentToAddToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class SchemaMainPanel extends JPanel implements IEditor {

	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
					.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	/* model private fields */
	private ISchemaCore core;
	private ISchemaController controller;

	/* GUI private fields */
	private SchemaCanvas canvas;
	private CanvasToolbar canTool;
	private ILocalGuiController localGUIController;
	private CPToolbar componentPropertyToolbar;
	private ComponentToAddToolbar componentToAddToolbar;

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
		canvas = new SchemaCanvas();
		localGUIController = new CanvasToolbarLocalGUIController();
		projectContainer = null;
		filecontent = null;
		readonly = false;
		saveable = false;
		modified = false;

		controller.registerCore(core);
		controller.addListener(EPropertyChange.ANY_CHANGE,
				new ModificationListener());
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
			predefined = projectContainer
					.getPredefinedFileContent(PREDEFINED_FILE_NAME);
		} catch (UniformAppletException e) {
			throw new SchemaException(
					"Could not open predefined component file.", e);
		}

		core.initPrototypes(predefined);
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		componentPropertyToolbar = new CPToolbar(localGUIController, controller);
		componentToAddToolbar = new ComponentToAddToolbar(controller,
				localGUIController);

		controller.addListener(EPropertyChange.CANVAS_CHANGE, canvas);
		controller.addListener(EPropertyChange.PROTOTYPES_CHANGE,
				componentToAddToolbar);

		// canvas toolbar
		localGUIController.addListener(canvas);
		// property toolbar
		localGUIController.addListener(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_SELECTION,
				componentPropertyToolbar);
		// component selection toolbar
		localGUIController.addListener(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE,
				componentToAddToolbar);

		canvas.registerLocalController(localGUIController);
		canvas.registerSchemaController(controller);

		canTool = new CanvasToolbar(null);
		canTool.registerController(localGUIController);
		localGUIController.addListener(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE, canTool);

		JToolBar componentPropertyToolbarTB = new JToolBar("Property");
		componentPropertyToolbarTB.add(componentPropertyToolbar);
		JScrollPane componentPropertyToolbarScrollPane = new JScrollPane(
				componentPropertyToolbarTB);

		JToolBar componentToAddToolbarTB = new JToolBar("Components");
		componentToAddToolbarTB.add(componentToAddToolbar);
		JScrollPane componentToAddToolbarScrollPane = new JScrollPane(
				componentToAddToolbarTB);

		JPanel rightPanel = new JPanel(new GridLayout(2, 1));
		rightPanel.add(componentPropertyToolbarScrollPane);
		rightPanel.add(componentToAddToolbarScrollPane);

		/* init canvas */
		JScrollPane pane = new JScrollPane(canvas);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canTool, BorderLayout.NORTH);
		panel.add(pane, BorderLayout.CENTER);
		this.add(panel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.EAST);
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
		if (filecontent != null)
			return filecontent.getFileName();
		return null;
	}

	public String getProjectName() {
		if (filecontent != null)
			return filecontent.getProjectName();
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
		initPrototypes();
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
