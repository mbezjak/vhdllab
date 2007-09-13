package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceAdapter;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.AbstractEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.SchemaException;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.SchemaCanvas;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.CPToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent.TabbedCTAddToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.InvalidateObsoleteUserComponents;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.RebuildPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class SchemaMainPanel extends AbstractEditor {

	// {
	// try {
	// // javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
	// // .getSystemLookAndFeelClassName());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private class ModificationListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			setModified(true);
		}
	}

	/* static fields */
	private static final long serialVersionUID = -6643347269051956602L;

	/* private fields */
	private SoftReference<DefaultWizard> wizardSoftRef;

	/* model private fields */
	private ISchemaCore core;
	private ISchemaController controller;

	/* GUI private fields */
	private SchemaCanvas canvas;
	private CanvasToolbar canTool;
	private ILocalGuiController localGUIController;
	private CPToolbar componentPropertyToolbar;
	private TabbedCTAddToolbar componentToAddToolbar;

	/* IEditor private fields */
	private VetoableResourceListener appletListener;

	/**
	 * Right panel divider width
	 */
	private double rightPanelWidth;

	/**
	 * Glavni split pane koji dijeli canvas od toolbara
	 */
	private JSplitPane verticalSplitPane;
	/**
	 * desni split pane koji dijeli property toolbar i componentToAdd toolbar
	 */
	private JSplitPane horizontalSplitPane;

	protected double rightPanelDividerPosition;

	protected boolean resizingIsOver = false;

	/* ctors */

	public SchemaMainPanel() {
		initStatic();
	}

	/* methods */

	/**
	 * Initializes fields with initial value and assumes that the project
	 * container IS NOT SET. Therefore, this is called from a ctor.
	 */
	private void initStatic() {
		core = new SchemaCore();
		controller = new LocalController();
		canvas = new SchemaCanvas();
		localGUIController = new CanvasToolbarLocalGUIController();

		rightPanelWidth = 200;
		rightPanelDividerPosition = .5;

		controller.registerCore(core);
		controller.addListener(EPropertyChange.ANY_CHANGE,
				new ModificationListener());
	}

	/**
	 * Performs all initialization assuming that a project container HAS BEEN
	 * SET. Therefore, this is not called from a ctor.
	 */
	private void initDynamic() {
		// init prototype components
		initPrototypes();

		// init gui
		initGUI();
	}

	private void initPrototypes() {
		InputStream predefined;

		// init default prototypes
		predefined = PredefinedComponentsParser.class
				.getResourceAsStream(Constants.PREDEFINED_FILENAME);

		// init user component prototypes
		List<CircuitInterface> usercis = getUserPrototypeList();

		// send EmptyCommand to alert listeners
		controller.send(new RebuildPrototypeCollection(predefined, usercis));
		if (usercis != null) {
			controller.send(new InvalidateObsoleteUserComponents(usercis));
		}
	}

	private List<CircuitInterface> getUserPrototypeList() {
		if (container == null || content == null)
			return null;
		System.out.println("Initializing user prototypes.");

		String projectname = content.getProjectName();
		String thisname = content.getFileName();
		List<String> circuitnames = null;
		try {
			circuitnames = resourceManager.getAllCircuits(
					projectname);
			if (circuitnames == null)
				throw new NullPointerException(
						"getAllCircuits(...) returned null.");
		} catch (Exception e) {
			throw new SchemaException("Could not fetch circuits in project '"
					+ projectname + "'.", e);
		}

		Hierarchy hierarchy;
		try {
			hierarchy = resourceManager.extractHierarchy(
					projectname);
		} catch (UniformAppletException e1) {
			throw new SchemaException("Cannot extract hierarchy for project '"
					+ projectname + "'.", e1);
		}

		List<CircuitInterface> usercircuits = new ArrayList<CircuitInterface>();
		for (String name : circuitnames) {
			// do not put prototypes for the modelled component or for
			// components that depend on this component
			if (thisname.equals(name)
					|| hierarchy.getDescendantsForParent(name).contains(
							thisname))
				continue;

			// get circuit interface for the component
			CircuitInterface circint;
			try {
				circint = resourceManager
						.getCircuitInterfaceFor(projectname, name);
			} catch (UniformAppletException e) {
				throw new SchemaException(
						"Could not fetch circuit interface for circuit '"
								+ name + "' in project '" + projectname + "'.",
						e);
			}

			usercircuits.add(circint);
		}

		return usercircuits;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		componentPropertyToolbar = new CPToolbar(localGUIController, controller);
		componentToAddToolbar = new TabbedCTAddToolbar(controller,
				localGUIController);

		controller.addListener(EPropertyChange.CANVAS_CHANGE, canvas);
		controller.addListener(EPropertyChange.PROTOTYPES_CHANGE,
				componentToAddToolbar);
		controller.addListener(EPropertyChange.PROPERTY_CHANGE,
				componentPropertyToolbar);

		// component selection toolbar
		localGUIController.addListener(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE,
				componentToAddToolbar);
		// canvas toolbar
		localGUIController.addListener(canvas);
		// property toolbar
		localGUIController.addListener(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_SELECTION,
				componentPropertyToolbar);

		canvas.registerLocalController(localGUIController);
		canvas.registerSchemaController(controller);

		canTool = new CanvasToolbar(null);
		canTool.registerController(localGUIController);
		localGUIController.addListener(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE, canTool);

		JScrollPane componentPropertyToolbarScrollPane = new JScrollPane(
				componentPropertyToolbar);
		JScrollPane componentToAddToolbarScrollPane = new JScrollPane(
				componentToAddToolbar);

		horizontalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				componentPropertyToolbarScrollPane,
				componentToAddToolbarScrollPane);

		/* init canvas */
		JScrollPane pane = new JScrollPane(canvas);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canTool, BorderLayout.NORTH);
		panel.add(pane, BorderLayout.CENTER);

		verticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel,
				horizontalSplitPane);

		horizontalSplitPane.setOneTouchExpandable(true);
		verticalSplitPane.setOneTouchExpandable(true);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				int width = getWidth();
				int height = getHeight();

				if (width <= 0) {
					return;
				}
				verticalSplitPane
						.setDividerLocation(1
								- rightPanelWidth
								/ ((width >= rightPanelWidth) ? width
										: rightPanelWidth));

				if (height <= 0) {
					return;
				}
				horizontalSplitPane
						.setDividerLocation(rightPanelDividerPosition);

				resizingIsOver = true;
			}
		});

		this.add(verticalSplitPane, BorderLayout.CENTER);
	}

	private void resetSchema() {
		core.reset();
		controller.clearCommandCache();
	}

	public ISchemaController getController() {
		return controller;
	}

	/* IEditor methods */

	@Override
	public void dispose() {
		resourceManager.removeVetoableResourceListener(appletListener);
		super.dispose();
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

	public IWizard getWizard() {
		DefaultWizard dw = null;
		if (wizardSoftRef != null)
			dw = wizardSoftRef.get();
		if (dw == null) {
			dw = new DefaultWizard();
			wizardSoftRef = new SoftReference<DefaultWizard>(dw);
		}
		return dw;
	}

	@Override
	public void init() {
		super.init();
		initDynamic();
	}

	@Override
	public void setFileContent(FileContent content) {
		super.setFileContent(content);
		// System.out.println("File content set.");
		resetSchema();
		if (content != null) {
			SchemaDeserializer sd = new SchemaDeserializer();
			StringReader stread = new StringReader(content.getContent());

			core.setSchemaInfo(sd.deserializeSchema(stread));
		}

		initPrototypes();

		appletListener = new VetoableResourceAdapter() {
			@Override
			public void resourceSaved(String projectName, String fileName) {
				if (fileName.equals(SchemaMainPanel.this.content.getFileName())) return;
				
				boolean oldmodifiedstatus = SchemaMainPanel.this.isModified();
				
				List<CircuitInterface> usercis = getUserPrototypeList();
				controller.send(new RebuildPrototypeCollection(null, usercis));
				controller.send(new InvalidateObsoleteUserComponents(usercis));
				
				SchemaMainPanel.this.setModified(oldmodifiedstatus);
			}
		};
		resourceManager.addVetoableResourceListener(appletListener);
	}

}
