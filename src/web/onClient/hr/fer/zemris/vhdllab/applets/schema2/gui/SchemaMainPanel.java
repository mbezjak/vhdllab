package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.SchemaException;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.SchemaCanvas;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.CPToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent.ComponentToAddToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.schema2.model.SchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.model.UserComponent;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.utilities.FileUtil;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class SchemaMainPanel extends JPanel implements IEditor {

	private SoftReference<DefaultWizard> wizardSoftRef;

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
			modified = true;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6643347269051956602L;

	
	
	/* static fields */

	
	/* model private fields */
	private ISchemaCore core;
	private ISchemaController controller;

	/* GUI private fields */
	private SchemaCanvas canvas;
	private CanvasToolbar canTool;
	private ILocalGuiController localGUIController;
	private CPToolbar componentPropertyToolbar;
	private ComponentToAddToolbar componentToAddToolbar;
	private JPanel rightPanel;

	/* IEditor private fields */
	private ProjectContainer projectContainer;
	private FileContent filecontent;
	private boolean readonly, saveable, modified;
//	private double dividerLocation;
	private double constantRightPanelWidth;
	private JSplitPane splitPane;

	
	
	/* ctors */

	public SchemaMainPanel() {
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
		constantRightPanelWidth = 140;

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

		// try {
		// predefined = projectContainer
		// .getPredefinedFileContent(Constants.PREDEFINED_FILENAME);
		// } catch (UniformAppletException e) {
		// throw new SchemaException(
		// "Could not open predefined component file.", e);
		// }

		// init default prototypes
		predefined = FileUtil.readFile(PredefinedComponentsParser.class
				.getResourceAsStream(Constants.PREDEFINED_FILENAME));
		core.initPrototypes(predefined);
		
		// init user component prototypes
		initUserPrototypes();
	}
	
	private void initUserPrototypes() {
		if (projectContainer == null || filecontent == null) return;
		
		String projectname = filecontent.getProjectName();
		String thisname = filecontent.getFileName();
		List<String> circuitnames = null;
		try {
			circuitnames = projectContainer.getAllCircuits(projectname);
			if (circuitnames == null) throw new NullPointerException("getAllCircuits(...) returned null.");
		} catch (Exception e) {
			throw new SchemaException("Could not fetch circuits in project '" + projectname + "'.", e);
		}
		
		ISchemaInfo info = controller.getSchemaInfo();
		Hierarchy hierarchy;
		try {
			hierarchy = projectContainer.extractHierarchy(projectname);
		} catch (UniformAppletException e1) {
			throw new SchemaException("Cannot extract hierarchy for project '" + projectname + "'.", e1);
		}
		
		for (String name : circuitnames) {
			// do not put prototypes for the modelled component or for components that depend on this component
			if (thisname.equals(name) || hierarchy.getChildrenForParent(name).contains(thisname)) continue;
			
			// get circuit interface for the component
			CircuitInterface circint;
			try {
				circint = projectContainer.getCircuitInterfaceFor(projectname, name);
			} catch (UniformAppletException e) {
				throw new SchemaException("Could not fetch circuit interface for circuit '" +
						name + "' in project '" + projectname + "'.", e);
			}
			
			// add component to prototypes
			try {
				info.getPrototyper().addPrototype(new UserComponent(circint));
			} catch (DuplicateKeyException e) {
				throw new SchemaException("Duplicate component name '" + circint.getEntityName() + "'.");
			}
		}
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		componentPropertyToolbar = new CPToolbar(localGUIController, controller);
		componentToAddToolbar = new ComponentToAddToolbar(controller,
				localGUIController);

		controller.addListener(EPropertyChange.CANVAS_CHANGE, canvas);
		controller.addListener(EPropertyChange.PROTOTYPES_CHANGE,
				componentToAddToolbar);

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

		JToolBar componentPropertyToolbarTB = new JToolBar("Property");
		componentPropertyToolbarTB.add(componentPropertyToolbar);
		JScrollPane componentPropertyToolbarScrollPane = new JScrollPane(
				componentPropertyToolbarTB);

		JToolBar componentToAddToolbarTB = new JToolBar("Components");
		componentToAddToolbarTB.add(componentToAddToolbar);
		JScrollPane componentToAddToolbarScrollPane = new JScrollPane(
				componentToAddToolbarTB);

		rightPanel = new JPanel(new GridLayout(2, 1));
		rightPanel.add(componentPropertyToolbarScrollPane);
		rightPanel.add(componentToAddToolbarScrollPane);

		/* init canvas */
		JScrollPane pane = new JScrollPane(canvas);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canTool, BorderLayout.NORTH);
		panel.add(pane, BorderLayout.CENTER);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel,
				rightPanel);
		// splitPane.setDividerLocation((int) (0.8 * getWidth()));
		splitPane.setOneTouchExpandable(true);
//		final BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI())
//				.getDivider();
//		divider.addComponentListener(new ComponentAdapter() {
//			@Override
//			public void componentMoved(ComponentEvent e) {
//				int width = splitPane.getWidth();
//				if (width <= 0) {
//					return;
//				}
//				setSplitPaneDivider((double) splitPane.getDividerLocation() / width);
//				System.out.println("divider location =" + dividerLocation);
//				System.out.println("split pane width =" + width);
//				System.out.println("split pane divider size =" + splitPane.getDividerSize());
//			}
//		});
		rightPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO
				//Insets ins = rightPanel.getInsets();
				//constantRightPanelWidth = splitPane.getRightComponent().getWidth();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
//				int divider = getSplitPaneDivider();
//				if (divider <= 0) {
//					return;
//				}
				int width = getWidth();
				if (width <= 0) return;
				splitPane.setDividerLocation(1 - constantRightPanelWidth / width);
			}
		});
		this.add(splitPane, BorderLayout.CENTER);
	}

//	private void setSplitPaneDivider(double location) {
//		this.dividerLocation = location;
//	}
//
//	private int getSplitPaneDivider() {
//		return (int) (dividerLocation * splitPane.getWidth());
//	}

	private void resetSchema() {
		core.reset();
		controller.clearCommandCache();
	}

	public ISchemaController getController() {
		return controller;
	}

	/* IEditor methods */

	public void dispose() {
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
		DefaultWizard dw = null;
		if (wizardSoftRef != null)
			dw = wizardSoftRef.get();
		if (dw == null) {
			dw = new DefaultWizard();
			wizardSoftRef = new SoftReference<DefaultWizard>(dw);
		}
		return dw;
	}

	public void highlightLine(int line) {
		// do absolutely nothing - this is not a text editor.
	}

	public void init() {
		initStatic();
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
		System.out.println("File content set.");
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
		System.out.println("Project container set.");
		projectContainer = container;
	}

	public void setReadOnly(boolean flag) {
		readonly = flag;
	}

	public void setSavable(boolean flag) {
		saveable = flag;
	}

}
