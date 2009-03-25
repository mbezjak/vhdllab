package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.CommandExecutorException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.LocalController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaCore;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.AddUpdatePredefinedPrototype;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.DeleteComponentCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.DeleteWireCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.InvalidateObsoleteUserComponents;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.RebuildPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.RemovePrototype;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.SchemaCanvas;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.CPToolbar;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent.TabbedCTAddToolbar;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditor;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceAdapter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceListener;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

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
    private WorkspaceListener appletListener;

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
        File thisfile = getFile();
        if (thisfile == null)
            return null;
        System.out.println("Initializing user prototypes.");

        Project project = thisfile.getProject();
        List<File> circuitnames = new ArrayList<File>();
        for (File info : container.getWorkspaceManager().getFilesForProject(
                project)) {
            if (info.getType().isCircuit()) {
                circuitnames.add(info);
            }
        }

        Hierarchy hierarchy = container.getWorkspaceManager().getHierarchy(
                project);

        List<CircuitInterface> usercircuits = new ArrayList<CircuitInterface>();
        for (File circuit : circuitnames) {
            // do not put prototypes for the modelled component or for
            // components that depend on this component
            if (thisfile.equals(circuit)
                    || hierarchy.fileHasDependency(circuit, thisfile))
                continue;

            // get circuit interface for the component
            CircuitInterface circint = container.getMetadataExtractionService()
                    .extractCircuitInterface(circuit.getId());

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

        // #########Added by Delac: key listener for delete and escape
        // action#########
        this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
                "delete_key_action");
        this.getActionMap().put("delete_key_action", new AbstractAction() {
            private static final long serialVersionUID = 1844240025875439799L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (localGUIController.getSelectedType() == CanvasToolbarLocalGUIController.TYPE_WIRE) {
                    if (SchemaCanvas.doDeleteWire(SchemaMainPanel.this)) {
                        Caseless sel = localGUIController
                                .getSelectedComponent();
                        localGUIController
                                .setSelectedComponent(
                                        sel,
                                        CanvasToolbarLocalGUIController.TYPE_NOTHING_SELECTED);
                        ICommand delete = new DeleteWireCommand(sel);
                        controller.send(delete);
                    }
                } else if (localGUIController.getSelectedType() == CanvasToolbarLocalGUIController.TYPE_COMPONENT) {
                    Caseless sel = localGUIController.getSelectedComponent();
                    localGUIController
                            .setSelectedComponent(
                                    sel,
                                    CanvasToolbarLocalGUIController.TYPE_NOTHING_SELECTED);
                    ICommand delete = new DeleteComponentCommand(sel);
                    controller.send(delete);
                }
            }
        });
        this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "escape_key_action");
        this.getActionMap().put("escape_key_action", new AbstractAction() {
            private static final long serialVersionUID = 1844240025875439799L;

            @Override
            public void actionPerformed(ActionEvent e) {
                localGUIController.setState(ECanvasState.MOVE_STATE);
            }
        });
        // #############

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
    protected void doDispose() {
        container.getWorkspaceManager().removeListener(appletListener);
    }

    @Override
    public void undo() {
        try {
            if (controller.canUndo()) {
                controller.undo();
            }
        } catch (CommandExecutorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void redo() {
        try {
            if (controller.canRedo()) {
                controller.redo();
            }
        } catch (CommandExecutorException e) {
            e.printStackTrace();
        }
    }

    @Override
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

    public boolean isProtoInEditor(Caseless cmpname) {
        boolean isused = false;

        ISchemaInfo nfo = controller.getSchemaInfo();
        for (Entry<Caseless, ISchemaComponent> ntry : nfo.getPrototypes()
                .entrySet()) {
            if (ntry.getKey().equals(cmpname)) {
                isused = true;
                break;
            }
        }

        return isused;
    }

    public boolean isPlacedInEditor(String fileName) {
        boolean isplaced = false;

        for (PlacedComponent plc : controller.getSchemaInfo().getComponents()) {
            CircuitInterface plcci = plc.comp.getCircuitInterface();
            if (plcci.isName(fileName)) {
                isplaced = true;
                break;
            }
        }

        return isplaced;
    }

    private boolean hasCircuitInterfaceChanged(String fileName,
            CircuitInterface ci) {
        for (PlacedComponent plc : controller.getSchemaInfo().getComponents()) {
            CircuitInterface plcci = plc.comp.getCircuitInterface();
            if (plcci.isName(fileName)) {
                if (!ci.equals(plcci))
                    return true;
            }
        }

        return false;
    }

    public boolean shouldBeAdded(FileReport report) {
        Hierarchy hierarchy = report.getHierarchy();

        File thisfile = getFile();
        System.out.println("This = " + thisfile.getName() + "; other = "
                + report.getFile().getName());
        if (thisfile.equals(report.getFile())
                || hierarchy.fileHasDependency(report.getFile(), thisfile)) {
            System.out.println("Other should NOT be added to this.");
            return false;
        }
        System.out.println("Other should be added to this.");
        return true;
    }

    @Override
    protected void doInitWithoutData() {
        initDynamic();
    }

    @Override
    protected void doInitWithData(File f) {
        resetSchema();
        if (f != null) {
            SchemaDeserializer sd = new SchemaDeserializer();
            StringReader stread = new StringReader(f.getData());

            core.setSchemaInfo(sd.deserializeSchema(stread));
        }

        initPrototypes();

        appletListener = new WorkspaceAdapter() {

            @Override
            public void fileSaved(FileReport report) {
                File file = report.getFile();

                // don't do anything if this editor was saved
                if (file.getName().equals(
                        SchemaMainPanel.this.getFile().getName()))
                    return;

                // don't add a non circuit
                if (!file.getType().isCircuit())
                    return;

                // shouldn't be added to prototypes
                if (!shouldBeAdded(report)) {
                    // then remove it, if it's used in schema
                    Caseless cfn = new Caseless(file.getName().toString());
                    if (isProtoInEditor(cfn)) {
                        boolean oldmodifiedstatus = SchemaMainPanel.this
                                .isModified();
                        controller.send(new RemovePrototype(cfn));
                        if (isPlacedInEditor(file.getName())) {
                            controller
                                    .send(new InvalidateObsoleteUserComponents(
                                            controller.getSchemaInfo()
                                                    .getPrototyper()));
                        } else {
                            // only prototype changes were made
                            SchemaMainPanel.this.setModified(oldmodifiedstatus);
                        }
                    }
                    return;
                }

                // must be added to prototype collection
                CircuitInterface ci = container.getMetadataExtractionService()
                        .extractCircuitInterface(file.getId());

                boolean oldmodifiedstatus = SchemaMainPanel.this.isModified();
                boolean isplaced = isPlacedInEditor(file.getName());
                controller.send(new AddUpdatePredefinedPrototype(ci));
                if (isplaced) {
                    // now check if circuit interface has changed
                    if (hasCircuitInterfaceChanged(file.getName(), ci)) {
                        // only perform invalidation if circuit interface has
                        // changed
                        controller.send(new InvalidateObsoleteUserComponents(
                                controller.getSchemaInfo().getPrototyper()));
                    } else {
                        // changes were made only to the prototype collection
                        SchemaMainPanel.this.setModified(oldmodifiedstatus);
                    }
                } else {
                    // changes were made only to the prototype collection
                    SchemaMainPanel.this.setModified(oldmodifiedstatus);
                }
            }

            @Override
            public void fileCreated(FileReport report) {
                File file = report.getFile();
                // don't add a non-circuit
                if (!file.getType().isCircuit())
                    return;

                // check hierarchy to see if this should be added
                if (!shouldBeAdded(report))
                    return;

                // we must add - fetch circuit interface
                CircuitInterface ci = container.getMetadataExtractionService()
                        .extractCircuitInterface(file.getId());

                boolean oldmodifiedstatus = SchemaMainPanel.this.isModified();
                controller.send(new AddUpdatePredefinedPrototype(ci));
                SchemaMainPanel.this.setModified(oldmodifiedstatus);
            }

            @Override
            public void fileDeleted(FileReport report) {
                File file = report.getFile();
                String fileName = file.getName();
                // don't bother with non-circuits
                if (!file.getType().isCircuit())
                    return;

                // check if it was used in editor at all
                Caseless cfn = new Caseless(fileName);
                if (!isProtoInEditor(cfn))
                    return;

                // it was used in editor, thus, it must be removed
                boolean oldmodifiedstatus = SchemaMainPanel.this.isModified();
                controller.send(new RemovePrototype(cfn));
                if (isPlacedInEditor(fileName)) {
                    controller.send(new InvalidateObsoleteUserComponents(
                            controller.getSchemaInfo().getPrototyper()));
                } else {
                    // only changes to prototypes were made
                    SchemaMainPanel.this.setModified(oldmodifiedstatus);
                }
            }

            // @Override
            // public void resourceDeleted(String projectName, String fileName)
            // {
            // /* check if the deleted resource was used in schema */
            // boolean isused = false;
            //
            // for (PlacedComponent plc :
            // controller.getSchemaInfo().getComponents()) {
            // CircuitInterface plcci = plc.comp.getCircuitInterface();
            // if (plcci.getEntityName().equalsIgnoreCase(fileName)) {
            // isused = true;
            // break;
            // }
            // }
            //				
            // /* perform changes */
            // if (isused) {
            // boolean oldmodifiedstatus = isModified();
            //					
            // List<CircuitInterface> usercis = getUserPrototypeList();
            // controller.send(new RebuildPrototypeCollection(null, usercis));
            // controller.send(new InvalidateObsoleteUserComponents(usercis));
            //					
            // SchemaMainPanel.this.setModified(oldmodifiedstatus);
            // }
            // }

        };
        container.getWorkspaceManager().addListener(appletListener);
    }

}
