package hr.fer.zemris.vhdllab.view.explorer;

import hr.fer.zemris.vhdllab.applets.texteditor.ViewVhdlEditorMetadata;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceListener;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializationListener;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.lf5.viewer.categoryexplorer.TreeModelAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandManager;
import org.springframework.richclient.command.support.GlobalCommandIds;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.tree.FocusableTreeCellRenderer;

public class ProjectExplorerView extends AbstractView implements
        WorkspaceInitializationListener, WorkspaceListener {

    private static final String PREFERENCES_HIERARCHY_MODE = "project.explorere.hierarchy.mode";
    public static final int HIERARCHY_X_USES_Y = 0;
    public static final int HIERARCHY_X_USED_BY_Y = 1;
    public static final int HIERARCHY_FLAT = 2;

    protected final ActionCommand openCommand = new OpenCommand();
    protected final ActionCommand deleteCommand = new DeleteCommand();
    protected final ActionCommand compileCommand = new CompileCommand();
    protected final ActionCommand simulateCommand = new SimulateCommand();
    protected final ActionCommand viewVhdlCommand = new ViewVhdlCommand();
    protected final ActionCommand hierarchyXUsesYCommand = new HierarchyXUsesYCommand();
    protected final ActionCommand hierarchyXUsedByYCommand = new HierarchyXUsedByYCommand();
    protected final ActionCommand hierarchyFlatCommand = new HierarchyFlatCommand();

    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    protected WorkspaceManager workspaceManager;
    @Autowired
    protected SimulationManager simulationManager;
    @Autowired
    protected MetadataExtractionService metadataExtractionService;
    @Autowired
    protected EditorManagerFactory editorManagerFactory;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    protected JTree tree;

    protected JPopupMenu popupMenu;

    private Integer hierarchyMode;

    @Override
    protected JComponent createControl() {
        CommandManager commandManager = getActiveWindow().getCommandManager();
        commandManager.createCommandGroup("hierarchyMenu",
                new String[] { hierarchyXUsesYCommand.getId(),
                        hierarchyXUsedByYCommand.getId(),
                        hierarchyFlatCommand.getId() });
        CommandGroup commandGroup = commandManager.createCommandGroup(
                "treePopupMenu", new String[] { "newMenu", "separator",
                        openCommand.getId(), "separator",
                        compileCommand.getId(), simulateCommand.getId(),
                        viewVhdlCommand.getId(), "separator",
                        deleteCommand.getId(), "separator", "hierarchyMenu" });
        popupMenu = commandGroup.createPopupMenu();

        root = new DefaultMutableTreeNode("vhdllab-root");
        model = new DefaultTreeModel(root);
        model.addTreeModelListener(new TreeModelAdapter() {
            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                Object[] children = e.getChildren();
                if (children.length > 0) {
                    Object child = children[children.length - 1];
                    TreePath path = e.getTreePath().pathByAddingChild(child);
                    tree.setSelectionPath(path);
                }
            }
        });
        tree = new JTree(model);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        // tree is expanded manually in OpenFileOrShowPopupMenuListener
        tree.setToggleClickCount(0);
        tree.setCellRenderer(new WorkspaceCellRenderer());
        tree.addMouseListener(new OpenFileOrShowPopupMenuListener());
        tree.addTreeSelectionListener(new CommandGuard());
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);

        JPanel hierarchyPanel = new JPanel();
        hierarchyPanel.add(createHierarchyButton(hierarchyXUsesYCommand));
        hierarchyPanel.add(createHierarchyButton(hierarchyXUsedByYCommand));
        hierarchyPanel.add(createHierarchyButton(hierarchyFlatCommand));
        JPanel borderHierarchyPanel = new JPanel(new BorderLayout());
        borderHierarchyPanel.add(hierarchyPanel, BorderLayout.WEST);

        JPanel control = new JPanel(new BorderLayout());
        control.add(borderHierarchyPanel, BorderLayout.NORTH);
        control.add(new JScrollPane(tree), BorderLayout.CENTER);

        return control;
    }

    private AbstractButton createHierarchyButton(ActionCommand command) {
        AbstractButton button = command.createButton();
        button.setText(null);
        String iconKey = BeanUtil.beanName(command.getClass()) + ".bigicon";
        button.setIcon(getIconSource().getIcon(iconKey));
        return button;
    }

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
        CommandManager commandManager = getActiveWindow().getCommandManager();
        commandManager.registerCommand(openCommand);
        commandManager.registerCommand(compileCommand);
        commandManager.registerCommand(simulateCommand);
        commandManager.registerCommand(viewVhdlCommand);
        commandManager.registerCommand(deleteCommand);
        commandManager.registerCommand(hierarchyXUsesYCommand);
        commandManager.registerCommand(hierarchyXUsedByYCommand);
        commandManager.registerCommand(hierarchyFlatCommand);
        context.register(deleteCommand.getId(), deleteCommand);
    }

    @Override
    public void initialize(Workspace workspace) {
        for (Project project : workspace.getProjects()) {
            MutableTreeNode projectNode = addProject(project);
            Hierarchy hierarchy = workspace.getHierarchy(project);
            if (hierarchy != null) {
                addFiles(hierarchy, null, projectNode);
            }
        }
        model.reload();
    }

    @Override
    public void projectCreated(Project project) {
        addProject(project);
    }

    @Override
    public void projectDeleted(Project project) {
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
                    .getChildAt(i);
            if (project.equals(node.getUserObject())) {
                root.remove(i);
                model.nodesWereRemoved(root, new int[] { i },
                        new Object[] { node });
                break;
            }
        }
    }

    private void updateHierarchy(DefaultMutableTreeNode parentNode,
            Hierarchy hierarchy, HierarchyNode hierarchyNode) {
        Map<Object, DefaultMutableTreeNode> map = new HashMap<Object, DefaultMutableTreeNode>(
                parentNode.getChildCount());
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            DefaultMutableTreeNode c = (DefaultMutableTreeNode) parentNode
                    .getChildAt(i);
            map.put(c.getUserObject(), c);
        }

        Iterator<HierarchyNode> iterator = getHierarchyIterator(hierarchy,
                hierarchyNode);
        while (iterator.hasNext()) {
            HierarchyNode next = iterator.next();
            File file = next.getFile();
            DefaultMutableTreeNode nextParentNode;
            if (map.containsKey(file)) {
                nextParentNode = map.remove(file);
            } else {
                nextParentNode = (DefaultMutableTreeNode) insertNode(
                        parentNode, file);
            }
            updateHierarchy(nextParentNode, hierarchy, next);
        }

        int[] childIndices = new int[map.size()];
        Object[] removedChildren = new Object[map.size()];
        int i = 0;
        for (DefaultMutableTreeNode n : map.values()) {
            int index = parentNode.getIndex(n);
            childIndices[i] = index;
            removedChildren[i] = n;
            i++;
            parentNode.remove(n);
        }
        model.nodesWereRemoved(parentNode, childIndices, removedChildren);
    }

    @Override
    public void fileCreated(FileReport report) {
        Hierarchy hierarchy = report.getHierarchy();
        DefaultMutableTreeNode treeNode = getNodeFor(hierarchy.getProject());
        updateHierarchy(treeNode, hierarchy, null);
    }

    @Override
    public void fileSaved(FileReport report) {
        fileCreated(report);
    }

    @Override
    public void fileDeleted(FileReport report) {
        fileCreated(report);
    }

    protected void refereshProjectExplorer() {
        root.removeAllChildren();
        initialize(workspaceManager.getWorkspace());
    }

    public Integer getHierarchyMode() {
        if (hierarchyMode == null) {
            Preferences preferences = Preferences
                    .userNodeForPackage(ProjectExplorerView.class);
            hierarchyMode = preferences.getInt(PREFERENCES_HIERARCHY_MODE,
                    HIERARCHY_X_USES_Y);
        }
        if (hierarchyMode != HIERARCHY_X_USES_Y
                && hierarchyMode != HIERARCHY_X_USED_BY_Y
                && hierarchyMode != HIERARCHY_FLAT) {
            hierarchyMode = HIERARCHY_X_USES_Y;
        }
        return hierarchyMode;
    }

    public void setHierarchyMode(Integer newHierarchyMode) {
        Integer oldHierarchyMode = this.hierarchyMode;
        this.hierarchyMode = newHierarchyMode;
        if (!ObjectUtils.equals(this.hierarchyMode, oldHierarchyMode)) {
            refereshProjectExplorer();
        }
    }

    @SuppressWarnings("unchecked")
    private Iterator<HierarchyNode> getHierarchyIterator(Hierarchy hierarchy,
            HierarchyNode node) {
        Integer mode = getHierarchyMode();
        switch (mode) {
        case 0:
            return hierarchy.iteratorXUsesYHierarchy(node);
        case 1:
            return hierarchy.iteratorXUsedByYHierarchy(node);
        case 2:
            if (node == null) {
                return hierarchy.iteratorFlatHierarchy();
            }
            return IteratorUtils.emptyIterator();
        default:
            throw new IllegalStateException(mode.toString());
        }
    }

    private void addFiles(Hierarchy hierarchy, HierarchyNode hierarchyNode,
            MutableTreeNode parentNode) {
        Iterator<HierarchyNode> iterator = getHierarchyIterator(hierarchy,
                hierarchyNode);
        while (iterator.hasNext()) {
            HierarchyNode next = iterator.next();
            MutableTreeNode parentFileNode = addFile(parentNode, next.getFile());
            addFiles(hierarchy, next, parentFileNode);
        }
    }

    private MutableTreeNode addFile(MutableTreeNode projectNode, File file) {
        return insertNode(projectNode, file);
    }

    private MutableTreeNode addProject(Project project) {
        MutableTreeNode projectNode = insertNode(root, project);
        tree.requestFocusInWindow();
        tree.setSelectionPath(new TreePath(new Object[] { root, projectNode }));
        return projectNode;
    }

    private MutableTreeNode insertNode(MutableTreeNode parentNode,
            Object childObject) {
        MutableTreeNode childNode = new DefaultMutableTreeNode(childObject);
        model.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
        return childNode;
    }

    protected Project getSelectedProject() {
        DefaultMutableTreeNode node = getLastSelectedNode();
        return (Project) node.getUserObject();
    }

    protected File getSelectedFile() {
        DefaultMutableTreeNode node = getLastSelectedNode();
        File hierarchyFile = (File) node.getUserObject();
        File loadedFile = mapper.getFile(hierarchyFile);
        loadedFile.setProject(getProjectForSelectedFile());
        return loadedFile;
    }

    private DefaultMutableTreeNode getNodeFor(Project project) {
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) root
                    .getChildAt(i);
            if (project.equals(projectNode.getUserObject())) {
                return projectNode;
            }
        }
        return null;
    }

    private Project getProjectForSelectedFile() {
        DefaultMutableTreeNode node = getLastSelectedNode();
        TreeNode[] path = node.getPath();
        DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) path[1];
        return (Project) projectNode.getUserObject();
    }

    protected DefaultMutableTreeNode getLastSelectedNode() {
        TreePath path = tree.getSelectionPath();
        if (path == null) {
            return null;
        }
        return (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    protected boolean lastSelectedNodeIsProject() {
        return isProject(getLastSelectedNode());
    }

    protected boolean lastSelectedNodeIsFile() {
        return isFile(getLastSelectedNode());
    }

    protected boolean isProject(DefaultMutableTreeNode node) {
        if (node == null) {
            return false;
        }
        return node.getUserObject() instanceof Project;
    }

    protected boolean isFile(DefaultMutableTreeNode node) {
        if (node == null) {
            return false;
        }
        return node.getUserObject() instanceof File;
    }

    public class OpenCommand extends ActionCommand {
        public OpenCommand() {
            super(BeanUtil.beanName(OpenCommand.class));
            setEnabled(false);
        }

        @Override
        protected void doExecuteCommand() {
            File file = getSelectedFile();
            editorManagerFactory.get(file).open();
        }
    }

    public class CompileCommand extends ActionCommand {
        public CompileCommand() {
            super(BeanUtil.beanName(CompileCommand.class));
            setEnabled(false);
        }

        @Override
        protected void doExecuteCommand() {
            File file = getSelectedFile();
            simulationManager.compile(file);
        }
    }

    public class SimulateCommand extends ActionCommand {
        public SimulateCommand() {
            super(BeanUtil.beanName(SimulateCommand.class));
            setEnabled(false);
        }

        @Override
        protected void doExecuteCommand() {
            File file = getSelectedFile();
            simulationManager.simulate(file);
        }
    }

    public class ViewVhdlCommand extends ActionCommand {
        public ViewVhdlCommand() {
            super(BeanUtil.beanName(ViewVhdlCommand.class));
            setEnabled(false);
        }

        @Override
        protected void doExecuteCommand() {
            File file = getSelectedFile();
            String vhdl = metadataExtractionService.generateVhdl(file.getId())
                    .getData();
            File viewVhdlFile = new File(file.getName(), FileType.SOURCE, vhdl);
            viewVhdlFile.setProject(file.getProject());
            editorManagerFactory.get(
                    new EditorIdentifier(new ViewVhdlEditorMetadata(),
                            viewVhdlFile)).open();
        }
    }

    public class DeleteCommand extends ActionCommand {

        private static final String DELETE_PROJECT_TITLE = "deleteProjectDialog.title";
        private static final String DELETE_PROJECT_MESSAGE = "deleteProjectDialog.message";
        private static final String DELETE_FILE_TITLE = "deleteFileDialog.title";
        private static final String DELETE_FILE_MESSAGE = "deleteFileDialog.message";
        private static final String DELETE_COMMAND_ID = "deleteResourceCommand";

        public DeleteCommand() {
            super(GlobalCommandIds.DELETE);
            setEnabled(false);
        }

        @SuppressWarnings("synthetic-access")
        @Override
        protected void doExecuteCommand() {
            if (lastSelectedNodeIsProject()) {
                final Project project = getSelectedProject();
                String title = getMessage(DELETE_PROJECT_TITLE);
                String message = getMessage(DELETE_PROJECT_MESSAGE,
                        new Object[] { project.getName() });
                new ConfirmationDialog(title, message) {
                    @Override
                    protected String getFinishCommandId() {
                        return DELETE_COMMAND_ID;
                    }

                    @Override
                    protected String getCancelCommandId() {
                        return DEFAULT_CANCEL_COMMAND_ID;
                    }

                    @Override
                    protected void onConfirm() {
                        workspaceManager.delete(project);
                    }
                }.showDialog();
            } else if (lastSelectedNodeIsFile()) {
                final File file = getSelectedFile();
                String title = getMessage(DELETE_FILE_TITLE);
                String message = getMessage(DELETE_FILE_MESSAGE, new Object[] {
                        file.getName(), file.getProject().getName() });
                new ConfirmationDialog(title, message) {
                    @Override
                    protected String getFinishCommandId() {
                        return DELETE_COMMAND_ID;
                    }

                    @Override
                    protected String getCancelCommandId() {
                        return DEFAULT_CANCEL_COMMAND_ID;
                    }

                    @Override
                    protected void onConfirm() {
                        workspaceManager.delete(file);
                    }
                }.showDialog();
            }
        }
    }

    public class HierarchyXUsesYCommand extends ActionCommand {
        public HierarchyXUsesYCommand() {
            super(BeanUtil.beanName(HierarchyXUsesYCommand.class));
        }

        @Override
        protected void doExecuteCommand() {
            setHierarchyMode(HIERARCHY_X_USES_Y);
        }
    }

    public class HierarchyXUsedByYCommand extends ActionCommand {
        public HierarchyXUsedByYCommand() {
            super(BeanUtil.beanName(HierarchyXUsedByYCommand.class));
        }

        @Override
        protected void doExecuteCommand() {
            setHierarchyMode(HIERARCHY_X_USED_BY_Y);
        }
    }

    public class HierarchyFlatCommand extends ActionCommand {
        public HierarchyFlatCommand() {
            super(BeanUtil.beanName(HierarchyFlatCommand.class));
        }

        @Override
        protected void doExecuteCommand() {
            setHierarchyMode(HIERARCHY_FLAT);
        }
    }

    protected class CommandGuard implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            openCommand.setEnabled(lastSelectedNodeIsFile());
            compileCommand.setEnabled(lastSelectedNodeIsFile()
                    && getSelectedFileType().isCompilable());
            simulateCommand.setEnabled(lastSelectedNodeIsFile()
                    && getSelectedFileType().isSimulatable());
            viewVhdlCommand.setEnabled(lastSelectedNodeIsFile()
                    && getSelectedFileType().canViewVhdl());
            deleteCommand
                    .setEnabled(!(lastSelectedNodeIsFile() && getSelectedFileType()
                            .equals(FileType.PREDEFINED)));
        }

        private FileType getSelectedFileType() {
            return getSelectedFile().getType();
        }
    }

    protected class OpenFileOrShowPopupMenuListener extends MouseAdapter {
        @SuppressWarnings("synthetic-access")
        @Override
        public void mouseClicked(MouseEvent e) {
            getActiveWindow().getPage().setActiveComponent(
                    ProjectExplorerView.this);
            if (e.getButton() == MouseEvent.BUTTON3) {
                TreePath path = tree.getClosestPathForLocation(e.getX(), e
                        .getY());
                tree.setSelectionPath(path);
                popupMenu.show(tree, e.getX(), e.getY());
            } else if (e.getButton() == MouseEvent.BUTTON1
                    && e.getClickCount() == 2) {
                if (openCommand.isEnabled()) {
                    openCommand.execute();
                } else {
                    TreePath path = tree.getClosestPathForLocation(e.getX(), e
                            .getY());
                    tree.setSelectionPath(path);
                    if (tree.isExpanded(path)) {
                        tree.collapsePath(path);
                    } else {
                        tree.expandPath(path);
                    }
                }
            }
        }
    }

    protected class WorkspaceCellRenderer extends FocusableTreeCellRenderer {

        private static final long serialVersionUID = 1L;

        private static final String PROJECT_ICON = "project.icon";
        private static final String SOURCE_ICON = "source.icon";
        private static final String SCHEMA_ICON = "schema.icon";
        private static final String AUTOMATON_ICON = "automaton.icon";
        private static final String TESTBENCH_ICON = "testbench.icon";
        private static final String PREDEFINED_ICON = "predefined.icon";

        @SuppressWarnings("synthetic-access")
        @Override
        public Component getTreeCellRendererComponent(
                @SuppressWarnings("hiding") JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                @SuppressWarnings("hiding") boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            String stringValue;

            if (isProject(node)) {
                Project project = (Project) node.getUserObject();
                stringValue = project.getName();
                setIcon(getIconSource().getIcon(PROJECT_ICON));
            } else if (isFile(node)) {
                File file = (File) node.getUserObject();
                stringValue = file.getName();
                switch (file.getType()) {
                case SOURCE:
                    setIcon(getIconSource().getIcon(SOURCE_ICON));
                    break;
                case SCHEMA:
                    setIcon(getIconSource().getIcon(SCHEMA_ICON));
                    break;
                case AUTOMATON:
                    setIcon(getIconSource().getIcon(AUTOMATON_ICON));
                    break;
                case TESTBENCH:
                    setIcon(getIconSource().getIcon(TESTBENCH_ICON));
                    break;
                case PREDEFINED:
                    setIcon(getIconSource().getIcon(PREDEFINED_ICON));
                    break;
                default:
                    throw new IllegalStateException(file.getType().toString());
                }
            } else {
                stringValue = node.getUserObject().toString();
            }

            return super.getTreeCellRendererComponent(tree, stringValue, sel,
                    expanded, leaf, row, hasFocus);
        }

        @Override
        public void setIcon(Icon icon) {
            setLeafIcon(icon);
            setOpenIcon(icon);
            setClosedIcon(icon);
            super.setIcon(icon);
        }
    }

}
