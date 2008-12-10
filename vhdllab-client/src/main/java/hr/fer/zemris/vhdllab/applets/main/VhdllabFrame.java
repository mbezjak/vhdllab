package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.component.about.About;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.StatusBar;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.ComponentIdentifierFactory;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfiguration;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfigurationParser;
import hr.fer.zemris.vhdllab.applets.main.constant.ComponentTypes;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemError;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogAdapter;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownManager;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;
import hr.fer.zemris.vhdllab.utilities.StringUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.context.ApplicationContext;

public final class VhdllabFrame extends JFrame implements IComponentProvider,
        PreferenceChangeListener {

    private static final long serialVersionUID = 1L;

    ISystemContainer systemContainer;
    private ApplicationContext context;
    private ShutdownManager shutdownManager;

    private Map<ComponentPlacement, JTabbedPane> tabbedContainers;
    private IStatusBar statusBar;

    private JSplitPane projectExplorerSplitPane;
    private JSplitPane viewSplitPane;
    private JSplitPane sideBarSplitPane;

    private JPanel centerPanel;
    private JComponent normalCenterPanel;
    private Container parentOfMaximizedComponent = null;

    IComponentContainer componentContainer;
    IComponentStorage componentStorage;
    IEditorManager editorManager;
    IViewManager viewManager;

    ICommunicator communicator;

    /*
     * (non-Javadoc)
     * 
     * @see java.applet.Applet#init()
     */
    public void init() {
        shutdownManager = (ShutdownManager) context.getBean("shutdownManager");
        initBasicGUI();
        setPaneSize();
        ResourceBundle bundle = ResourceBundleProvider
                .getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
        String text = bundle.getString(LanguageConstants.STATUSBAR_INIT_START);
        SystemLog.instance().addSystemMessage(text, MessageType.INFORMATION);
        componentContainer = new DefaultComponentContainer(this);
        text = bundle.getString(LanguageConstants.STATUSBAR_INIT_COMMUNICATOR);
        SystemLog.instance().addSystemMessage(text, MessageType.INFORMATION);
        communicator = (ICommunicator) context.getBean("communicator");
        ResourceBundleProvider.init();
        componentStorage = new DefaultComponentStorage(componentContainer);

        text = bundle.getString(LanguageConstants.STATUSBAR_INIT_DONE);
        SystemLog.instance().addSystemMessage(text, MessageType.SUCCESSFUL);
        text = bundle.getString(LanguageConstants.STATUSBAR_INIT_GUI);
        SystemLog.instance().addSystemMessage(text, MessageType.INFORMATION);
        initGUI();
        int duration = 15000;
        ToolTipManager.sharedInstance().setDismissDelay(duration);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPaneSize();
            }
        });
        text = bundle.getString(LanguageConstants.STATUSBAR_INIT_DONE);
        SystemLog.instance().addSystemMessage(text, MessageType.SUCCESSFUL);
        text = bundle.getString(LanguageConstants.STATUSBAR_INIT_SYSTEM);
        SystemLog.instance().addSystemMessage(text, MessageType.INFORMATION);

        DefaultSystemContainer systemContainer = (DefaultSystemContainer) context
                .getBean("defaultSystemContainer");
        systemContainer.setComponentProvider(this);
        systemContainer.setParentFrame(JOptionPane
                .getFrameForComponent(VhdllabFrame.this));
        this.systemContainer = systemContainer;
        ComponentConfiguration conf;
        try {
            conf = ComponentConfigurationParser.getConfiguration();
        } catch (UniformAppletException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        editorManager = new DefaultEditorManager(componentStorage, conf,
                systemContainer);
        viewManager = new DefaultViewManager(componentStorage, conf,
                systemContainer);
        ComponentIdentifierFactory.setComponentConfiguration(conf);
        ComponentIdentifierFactory.setContainer(systemContainer);
        systemContainer.setComponentStorage(componentStorage);
        systemContainer.setEditorManager(editorManager);
        systemContainer.setViewManager(viewManager);

        Preferences preferences = Preferences
                .userNodeForPackage(VhdllabFrame.class);
        preferences.addPreferenceChangeListener(this);

        SystemLog.instance().addSystemLogListener(new SystemLogAdapter() {
            @Override
            public void errorMessageAdded(SystemError message) {
                JOptionPane.showMessageDialog(VhdllabFrame.this,
                        "Error occurred: " + message.getCause().getMessage(),
                        "Error occurred", JOptionPane.ERROR_MESSAGE);
            }
        });

        text = bundle.getString(LanguageConstants.STATUSBAR_INIT_DONE);
        SystemLog.instance().addSystemMessage(text, MessageType.SUCCESSFUL);
        text = bundle.getString(LanguageConstants.STATUSBAR_INIT_LOAD_COMPLETE);
        SystemLog.instance().addSystemMessage(text, MessageType.SUCCESSFUL);
        setPaneSize();
    }

    void exitWithConfirmation() {
        shutdownManager.shutdownWithConfirmation();
    }

    void initGUI() {
        JMenuBar menuBar = new PrivateMenuBar().setupMainMenu();
        this.setJMenuBar(menuBar);

        PrivateMenuBar menubar = new PrivateMenuBar();
        for (ComponentPlacement p : ComponentPlacement.values()) {
            JTabbedPane pane = getTabbedPane(p);
            pane.setComponentPopupMenu(menubar
                    .setupPopupMenuForComponents(pane));
        }

        // toolBar = new PrivateToolBar().setup();
        // add(toolBar, BorderLayout.NORTH);
    }

    private JPanel setupStatusBar() {
        StatusBar statusBar = new StatusBar();
        JPanel statusBarPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(statusBar, BorderLayout.CENTER);
        statusBarPanel.setPreferredSize(new Dimension(0, 24));
        this.statusBar = statusBar;
        return statusBarPanel;
    }

    void initBasicGUI() {
        initTabbedContainers();
        centerPanel = new JPanel(new BorderLayout());
        normalCenterPanel = setupCenterPanel();
        centerPanel.add(normalCenterPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(setupStatusBar(), BorderLayout.SOUTH);
    }

    private void initTabbedContainers() {
        ComponentPlacement[] placements = ComponentPlacement.values();
        tabbedContainers = new HashMap<ComponentPlacement, JTabbedPane>(
                placements.length);

        ChangeListener changeListener = new TabbedPaneChangeListener();
        FocusListener focusListener = new TabbedPaneFocusListener();
        MouseListener mouseListener = new TabbedPaneMouseListener();
        ContainerListener containerListener = new TabbedPaneContainerListener();

        for (ComponentPlacement p : placements) {
            JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP,
                    JTabbedPane.SCROLL_TAB_LAYOUT);
            pane.addChangeListener(changeListener);
            pane.addFocusListener(focusListener);
            pane.addMouseListener(mouseListener);
            pane.addContainerListener(containerListener);
            tabbedContainers.put(p, pane);
        }
    }

    private JComponent setupCenterPanel() {
        sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                getTabbedPane(ComponentPlacement.CENTER), null);
        // sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        // centerTabbedPane, rightTabbedPane);
        projectExplorerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                getTabbedPane(ComponentPlacement.LEFT), sideBarSplitPane);
        viewSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                projectExplorerSplitPane,
                getTabbedPane(ComponentPlacement.BOTTOM));

        return viewSplitPane;
    }

    /**
     * Private class for creating menu bar. Created menu bar will be localized.
     * 
     * @author Miro Bezjak
     */
    private class PrivateMenuBar {

        ResourceBundle bundle;

        /**
         * Constructor.
         */
        public PrivateMenuBar() {
            this.bundle = ResourceBundleProvider
                    .getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
        }

        public JPopupMenu setupPopupMenuForComponents(final JTabbedPane pane) {
            final JPopupMenu menuBar = new JPopupMenu();
            JMenuItem menuItem;
            String key;

            // Save editor
            key = LanguageConstants.MENU_FILE_SAVE;
            menuItem = new JMenuItem(bundle.getString(key));
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        JComponent c = (JComponent) pane.getSelectedComponent();
                        if (c == null) {
                            return;
                        }
                        ComponentGroup group = componentContainer
                                .getComponentGroup(c);
                        if (group.equals(ComponentGroup.EDITOR)) {
                            systemContainer.getEditorManager()
                                    .saveEditorExplicitly((IEditor) c);
                        }
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menuBar.add(menuItem);

            // Save all editors
            key = LanguageConstants.MENU_FILE_SAVE_ALL;
            menuItem = new JMenuItem(bundle.getString(key));
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        List<IEditor> editorsToSave = new ArrayList<IEditor>(
                                pane.getTabCount());
                        for (int i = 0; i < pane.getTabCount(); i++) {
                            JComponent c = (JComponent) pane.getComponentAt(i);
                            ComponentGroup group = componentContainer
                                    .getComponentGroup(c);
                            if (group.equals(ComponentGroup.EDITOR)) {
                                editorsToSave.add((IEditor) c);
                            }

                        }
                        systemContainer.getEditorManager().saveEditors(
                                editorsToSave);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menuBar.add(menuItem);
            menuBar.addSeparator();

            // Close editor
            key = LanguageConstants.MENU_FILE_CLOSE;
            menuItem = new JMenuItem(bundle.getString(key));
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        JComponent selectedComponent = (JComponent) pane
                                .getSelectedComponent();
                        if (selectedComponent == null) {
                            return;
                        }
                        ComponentGroup group = componentContainer
                                .getComponentGroup(selectedComponent);
                        if (group.equals(ComponentGroup.EDITOR)) {
                            systemContainer.getEditorManager().closeEditor(
                                    (IEditor) selectedComponent);
                        } else {
                            componentStorage.remove(selectedComponent);
                        }
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menuBar.add(menuItem);

            // Close other editors
            key = LanguageConstants.MENU_FILE_CLOSE_OTHER;
            menuItem = new JMenuItem(bundle.getString(key));
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        int index = pane.getSelectedIndex();
                        if (index == -1) {
                            return;
                        }
                        List<IEditor> editorsToClose = new ArrayList<IEditor>(
                                pane.getTabCount());
                        List<JComponent> componentsToClose = new ArrayList<JComponent>(
                                pane.getTabCount());
                        for (int i = 0; i < pane.getTabCount(); i++) {
                            if (i == index) {
                                continue;
                            }
                            JComponent c = (JComponent) pane.getComponentAt(i);
                            ComponentGroup group = componentContainer
                                    .getComponentGroup(c);
                            if (group.equals(ComponentGroup.EDITOR)) {
                                editorsToClose.add((IEditor) c);
                            } else {
                                componentsToClose.add(c);
                            }
                        }
                        systemContainer.getEditorManager().closeEditors(
                                editorsToClose);
                        for (JComponent c : componentsToClose) {
                            componentStorage.remove(c);
                        }
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menuBar.add(menuItem);

            // Close all editors
            key = LanguageConstants.MENU_FILE_CLOSE_ALL;
            menuItem = new JMenuItem(bundle.getString(key));
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        List<IEditor> editorsToClose = new ArrayList<IEditor>(
                                pane.getTabCount());
                        List<JComponent> componentsToClose = new ArrayList<JComponent>(
                                pane.getTabCount());
                        for (int i = 0; i < pane.getTabCount(); i++) {
                            JComponent c = (JComponent) pane.getComponentAt(i);
                            ComponentGroup group = componentContainer
                                    .getComponentGroup(c);
                            if (group.equals(ComponentGroup.EDITOR)) {
                                editorsToClose.add((IEditor) c);
                            } else {
                                componentsToClose.add(c);
                            }
                        }
                        systemContainer.getEditorManager().closeEditors(
                                editorsToClose);
                        for (JComponent c : componentsToClose) {
                            componentStorage.remove(c);
                        }
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menuBar.add(menuItem);

            return menuBar;
        }

        /**
         * Creates and instantiates main menu bar.
         * 
         * @return created menu bar
         */
        public JMenuBar setupMainMenu() {

            JMenuBar menuBar = new JMenuBar();
            JMenu menu;
            JMenu submenu;
            JMenuItem menuItem;
            String key;

            // File menu
            key = LanguageConstants.MENU_FILE;
            menu = new JMenu(bundle.getString(key));
            setCommonMenuAttributes(menu, key);

            // New sub menu
            key = LanguageConstants.MENU_FILE_NEW;
            submenu = new JMenu(bundle.getString(key));
            setCommonMenuAttributes(submenu, key);

            // New Project menu item
            key = LanguageConstants.MENU_FILE_NEW_PROJECT;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.createNewProjectInstance();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);
            submenu.addSeparator();

            // New VHDL Source menu item
            key = LanguageConstants.MENU_FILE_NEW_VHDL_SOURCE;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.createNewFileInstance(FileType.SOURCE);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);

            // New Testbench menu item
            key = LanguageConstants.MENU_FILE_NEW_TESTBENCH;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer
                                .createNewFileInstance(FileType.TESTBENCH);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);

            // New Schema menu item
            key = LanguageConstants.MENU_FILE_NEW_SCHEMA;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.createNewFileInstance(FileType.SCHEMA);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);

            // New Automat menu item
            key = LanguageConstants.MENU_FILE_NEW_AUTOMAT;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer
                                .createNewFileInstance(FileType.AUTOMATON);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);

            menu.add(submenu);

            // Open menu item
            /*
             * key = LanguageConstants.MENU_FILE_OPEN; menuItem = new
             * JMenuItem(bundle.getString(key));
             * setCommonMenuAttributes(menuItem, key); menu.add(menuItem);
             */
            menu.addSeparator();

            // Save menu item
            key = LanguageConstants.MENU_FILE_SAVE;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        IEditor editor = editorManager.getSelectedEditor();
                        if (editor == null) {
                            return;
                        }
                        systemContainer.getEditorManager()
                                .saveEditorExplicitly(editor);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // Save All menu item
            key = LanguageConstants.MENU_FILE_SAVE_ALL;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.getEditorManager().saveAllEditors();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);
            menu.addSeparator();

            // Close menu item
            key = LanguageConstants.MENU_FILE_CLOSE;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        IEditor editor = editorManager.getSelectedEditor();
                        if (editor == null) {
                            return;
                        }
                        systemContainer.getEditorManager().closeEditor(editor);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // Close All menu item
            key = LanguageConstants.MENU_FILE_CLOSE_ALL;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.getEditorManager().closeAllEditors();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);
            menu.addSeparator();

            // Exit menu item
            key = LanguageConstants.MENU_FILE_EXIT;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        exitWithConfirmation();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                        System.exit(1);
                    }
                }
            });
            menu.add(menuItem);

            menuBar.add(menu);

            // Edit menu
            key = LanguageConstants.MENU_EDIT;
            menu = new JMenu(bundle.getString(key));
            setCommonMenuAttributes(menu, key);
            menuBar.add(menu);

            // Undo menu
            key = LanguageConstants.MENU_EDIT_UNDO;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        IEditor editor = editorManager.getSelectedEditor();
                        if (editor == null) {
                            return;
                        }
                        editor.undo();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // Undo menu
            key = LanguageConstants.MENU_EDIT_REDO;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        IEditor editor = editorManager.getSelectedEditor();
                        if (editor == null) {
                            return;
                        }
                        editor.redo();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // View menu
            key = LanguageConstants.MENU_VIEW;
            menu = new JMenu(bundle.getString(key));
            setCommonMenuAttributes(menu, key);

            // Maximize active window
            key = LanguageConstants.MENU_VIEW_MAXIMIZE_ACTIVE_WINDOW;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        JComponent component = componentContainer
                                .getSelectedComponent();
                        if (component != null) {
                            ComponentPlacement placement = componentContainer
                                    .getComponentPlacement(component);
                            maximizeComponent(getTabbedPane(placement));
                        }
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);
            menu.addSeparator();

            // Show View sub menu
            key = LanguageConstants.MENU_VIEW_SHOW_VIEW;
            submenu = new JMenu(bundle.getString(key));
            setCommonMenuAttributes(submenu, key);

            // Show Compilation Errors menu item
            key = LanguageConstants.MENU_VIEW_SHOW_VIEW_COMPILATION_ERRORS;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String viewType = ComponentTypes.VIEW_COMPILATION_ERRORS;
                    try {
                        IComponentIdentifier<?> identifier = ComponentIdentifierFactory
                                .createViewIdentifier(viewType);
                        systemContainer.getViewManager().openView(identifier);
                    } catch (RuntimeException ex) {
                        String text = bundle
                                .getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
                        String viewTitle = bundle
                                .getString(LanguageConstants.TITLE_FOR
                                        + viewType);
                        text = PlaceholderUtil.replacePlaceholders(text,
                                new String[] { viewTitle });
                        SystemLog.instance().addSystemMessage(text,
                                MessageType.ERROR);
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);

            // Show Simulation Errors menu item
            key = LanguageConstants.MENU_VIEW_SHOW_VIEW_SIMULATION_ERRORS;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String viewType = ComponentTypes.VIEW_SIMULATION_ERRORS;
                    try {
                        IComponentIdentifier<?> identifier = ComponentIdentifierFactory
                                .createViewIdentifier(viewType);
                        systemContainer.getViewManager().openView(identifier);
                    } catch (RuntimeException ex) {
                        String text = bundle
                                .getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
                        String viewTitle = bundle
                                .getString(LanguageConstants.TITLE_FOR
                                        + viewType);
                        text = PlaceholderUtil.replacePlaceholders(text,
                                new String[] { viewTitle });
                        SystemLog.instance().addSystemMessage(text,
                                MessageType.ERROR);
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);

            // Show Status History menu item
            key = LanguageConstants.MENU_VIEW_SHOW_VIEW_STATUS_HISTORY;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String viewType = ComponentTypes.VIEW_STATUS_HISTORY;
                    try {
                        IComponentIdentifier<?> identifier = ComponentIdentifierFactory
                                .createViewIdentifier(viewType);
                        systemContainer.getViewManager().openView(identifier);
                    } catch (RuntimeException ex) {
                        String text = bundle
                                .getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
                        String viewTitle = bundle
                                .getString(LanguageConstants.TITLE_FOR
                                        + viewType);
                        text = PlaceholderUtil.replacePlaceholders(text,
                                new String[] { viewTitle });
                        SystemLog.instance().addSystemMessage(text,
                                MessageType.ERROR);
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            submenu.add(menuItem);

            // Show Project Explorer
            key = LanguageConstants.MENU_VIEW_SHOW_VIEW_PROJECT_EXPLORER;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.getViewManager().openProjectExplorer();
                    } catch (RuntimeException ex) {
                        String text = bundle
                                .getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
                        String viewTitle = bundle
                                .getString(LanguageConstants.TITLE_FOR
                                        + ComponentTypes.VIEW_PROJECT_EXPLORER);
                        text = PlaceholderUtil.replacePlaceholders(text,
                                new String[] { viewTitle });
                        SystemLog.instance().addSystemMessage(text,
                                MessageType.ERROR);
                        SystemLog.instance().addErrorMessage(ex);
                    }

                }
            });
            submenu.add(menuItem);

            menu.add(submenu);

            menuBar.add(menu);

            // Tool menu
            key = LanguageConstants.MENU_TOOLS;
            menu = new JMenu(bundle.getString(key));
            setCommonMenuAttributes(menu, key);

            // Compile with dialog
            key = LanguageConstants.MENU_TOOLS_COMPILE_WITH_DIALOG;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.compileWithDialog();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // Compile active editor
            key = LanguageConstants.MENU_TOOLS_COMPILE_ACTIVE;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.compile(editorManager
                                .getSelectedEditor());
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // Compile history
            key = LanguageConstants.MENU_TOOLS_COMPILE_HISTORY;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.compileLastHistoryResult();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);
            menu.addSeparator();

            // Simulate with dialog
            key = LanguageConstants.MENU_TOOLS_SIMULATE_WITH_DIALOG;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.simulateWithDialog();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // Simulate active editor
            key = LanguageConstants.MENU_TOOLS_SIMULATE_ACTIVE;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.simulate(editorManager
                                .getSelectedEditor());
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // Simulate history
            key = LanguageConstants.MENU_TOOLS_SIMULATE_HISTORY;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.simulateLastHistoryResult();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);
            menu.addSeparator();

            // View VHDL code
            key = LanguageConstants.MENU_TOOLS_VIEW_VHDL_CODE;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        IEditor editor = editorManager.getSelectedEditor();
                        if (editor == null) {
                            return;
                        }
                        systemContainer.getEditorManager().viewVHDLCode(editor);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            // View Preferences
            key = LanguageConstants.MENU_TOOLS_VIEW_PREFERENCES;
            menuItem = new JMenuItem(bundle.getString(key));
            setCommonMenuAttributes(menuItem, key);
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        systemContainer.getEditorManager().openPreferences();
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            if (ApplicationContextHolder.getContext().isDevelopment()) {
                menu.addSeparator();
                menu.add(menuItem);
            }

            menuBar.add(menu);

            // Help menu
            key = LanguageConstants.MENU_HELP;
            menu = new JMenu(bundle.getString(key));
            key = LanguageConstants.MENU_HELP_ABOUT;
            menuItem = new JMenuItem(bundle.getString(key));
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        About.instance().setVisible(true);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);
            setCommonMenuAttributes(menu, key);
            menuBar.add(menu);

            // Development menu
            // TODO development menu only temporary solution
            menu = new JMenu("Development");
            menuItem = new JMenuItem("Show view Error History");
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        IComponentIdentifier<?> identifier = ComponentIdentifierFactory
                                .createViewIdentifier(ComponentTypes.VIEW_ERROR_HISTORY);
                        systemContainer.getViewManager().openView(identifier);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            menuItem = new JMenuItem("Create new error");
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        int error = 1 / 0;
                        System.out.println(error);
                    } catch (RuntimeException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);

            menu.addSeparator();
            menuItem = new JMenuItem("Show new dialog");
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        JOptionPane.showMessageDialog(VhdllabFrame.this,
                                "is it modal?");
                    } catch (HeadlessException ex) {
                        SystemLog.instance().addErrorMessage(ex);
                    }
                }
            });
            menu.add(menuItem);
            // TODO END TEMP SOLUTION

            if (ApplicationContextHolder.getContext().isDevelopment()) {
                menuBar.add(menu);
            }

            return menuBar;
        }

        /**
         * Sets mnemonic, accelerator and tooltip for a given menu. If keys for
         * mnemonic, accelerator or tooltip (or all of them) does not exists
         * then they will simply be ignored.
         * 
         * @param menu
         *            a menu where to set mnemonic and accelerator
         * @param key
         *            a key containing menu's name
         */
        private void setCommonMenuAttributes(JMenuItem menu, String key) {
            /**
             * For locating mnemonic, accelerator or tooltip of a
             * <code>menu</code> this method will simply append appropriate
             * string to <code>key</code>. Information regarding such strings
             * that will be appended can be found here:
             * <ul>
             * <li>LanguageConstants.MNEMONIC_APPEND</li>
             * <li>LanguageConstants.ACCELERATOR_APPEND</li>
             * <li>LanguageConstants.TOOLTIP_APPEND</li>
             * </ul>
             */

            // Set mnemonic
            try {
                String temp = bundle.getString(key
                        + LanguageConstants.MNEMONIC_APPEND);
                temp = temp.trim();
                if (temp.length() == 1 && StringUtil.isAlphaNumeric(temp)) {
                    menu.setMnemonic(temp.codePointAt(0));
                }
            } catch (RuntimeException e) {
            }

            // Set accelerator
            try {
                String temp = bundle.getString(key
                        + LanguageConstants.ACCELERATOR_APPEND);
                String[] keys = temp.split("[+]");
                if (keys.length != 0) {
                    boolean functionKey = false;
                    int mask = 0;
                    int keyCode = 0;
                    for (String k : keys) {
                        k = k.trim();
                        if (k.equalsIgnoreCase("ctrl"))
                            mask += KeyEvent.CTRL_DOWN_MASK;
                        else if (k.equalsIgnoreCase("alt"))
                            mask += KeyEvent.ALT_DOWN_MASK;
                        else if (k.equalsIgnoreCase("shift"))
                            mask += KeyEvent.SHIFT_DOWN_MASK;
                        else if (k.equalsIgnoreCase("func"))
                            functionKey = true;
                        else if (functionKey && k.length() <= 2) {
                            if (!StringUtil.isNumeric(k))
                                throw new IllegalArgumentException();
                            keyCode = KeyEvent.VK_F1 - 1 + Integer.parseInt(k);
                            functionKey = false;
                        } else if (k.length() == 1) {
                            if (!StringUtil.isAlphaNumeric(k))
                                throw new IllegalArgumentException();
                            keyCode = k.toUpperCase().codePointAt(0);
                        }
                    }
                    menu.setAccelerator(KeyStroke.getKeyStroke(keyCode, mask));
                }
            } catch (RuntimeException e) {
            }

            // Set tooltip
            try {
                String text = bundle.getString(key
                        + LanguageConstants.TOOLTIP_APPEND);
                text = text.trim();
                menu.setToolTipText(text);
            } catch (RuntimeException e) {
            }
        }
    }

    @Override
    public IStatusBar getStatusBar() {
        return statusBar;
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
        String name = event.getKey();
        Preferences pref = event.getNode();
        if (name
                .equalsIgnoreCase(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH)) {
            double size = pref.getDouble(name, 0.15);
            projectExplorerSplitPane
                    .setDividerLocation((int) (projectExplorerSplitPane
                            .getWidth() * size));
        } else if (name
                .equalsIgnoreCase(UserFileConstants.SYSTEM_SIDEBAR_WIDTH)) {
            double size = pref.getDouble(name, 0.75);
            sideBarSplitPane.setDividerLocation((int) (sideBarSplitPane
                    .getWidth() * size));
        } else if (name.equalsIgnoreCase(UserFileConstants.SYSTEM_VIEW_HEIGHT)) {
            double size = pref.getDouble(name, 0.75);
            viewSplitPane
                    .setDividerLocation((int) (viewSplitPane.getHeight() * size));
        } else if (name
                .equalsIgnoreCase(UserFileConstants.SYSTEM_TOOLTIP_DURATION)) {
            int duration = pref.getInt(
                    UserFileConstants.SYSTEM_TOOLTIP_DURATION, 15000);
            ToolTipManager.sharedInstance().setDismissDelay(duration);
        }
    }

    void setPaneSize() {
        validate();
        double size;

        try {
            Preferences pref = Preferences.userNodeForPackage(VhdllabFrame.class);
            size = pref.getDouble(
                    UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH, 0.15);
            projectExplorerSplitPane
                    .setDividerLocation((int) (projectExplorerSplitPane
                            .getWidth() * size));

            size = pref.getDouble(UserFileConstants.SYSTEM_SIDEBAR_WIDTH, 0.75);
            sideBarSplitPane.setDividerLocation((int) (sideBarSplitPane
                    .getWidth() * size));

            size = pref.getDouble(UserFileConstants.SYSTEM_VIEW_HEIGHT, 0.75);
            viewSplitPane
                    .setDividerLocation((int) (viewSplitPane.getHeight() * size));
        } catch (RuntimeException e) {
            projectExplorerSplitPane
                    .setDividerLocation((int) (projectExplorerSplitPane
                            .getWidth() * 0.15));
            sideBarSplitPane.setDividerLocation((int) (sideBarSplitPane
                    .getWidth() * 0.75));
            viewSplitPane
                    .setDividerLocation((int) (viewSplitPane.getHeight() * 0.75));
        }
    }

    private void storePaneSize() {
        Preferences pref = Preferences.userNodeForPackage(VhdllabFrame.class);
        DecimalFormat formatter = new DecimalFormat("0.##");
        double size = projectExplorerSplitPane.getDividerLocation() * 1.0
                / projectExplorerSplitPane.getWidth();
        String property = formatter.format(size);
        pref.put(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH, property);

        size = sideBarSplitPane.getDividerLocation() * 1.0
                / sideBarSplitPane.getWidth();
        property = formatter.format(size);
        pref.put(UserFileConstants.SYSTEM_SIDEBAR_WIDTH, property);

        size = viewSplitPane.getDividerLocation() * 1.0
                / viewSplitPane.getHeight();
        property = formatter.format(size);
        pref.put(UserFileConstants.SYSTEM_VIEW_HEIGHT, property);
    }

    void maximizeComponent(Component component) {
        if (component == null)
            return;
        if (isMaximized(component)) {
            centerPanel.remove(component);
            parentOfMaximizedComponent.add(component);
            centerPanel.add(normalCenterPanel, BorderLayout.CENTER);
            parentOfMaximizedComponent = null;
            setPaneSize();
        } else {
            storePaneSize();
            centerPanel.remove(normalCenterPanel);
            parentOfMaximizedComponent = component.getParent();
            centerPanel.add(component, BorderLayout.CENTER);
        }
        centerPanel.repaint();
        centerPanel.validate();
        component.requestFocusInWindow();
    }

    boolean isMaximized(Component component) {
        return parentOfMaximizedComponent != null;
    }

    public JTabbedPane getTabbedPane(ComponentPlacement placement) {
        return tabbedContainers.get(placement);
    }

    class TabbedPaneChangeListener implements ChangeListener {

        /*
         * (non-Javadoc)
         * 
         * @seejavax.swing.event.ChangeListener#stateChanged(javax.swing.event.
         * ChangeEvent)
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            Component component = pane.getSelectedComponent();
            if (component == null) {
                return;
            }
            componentContainer.setSelectedComponent((JComponent) component);
        }

    }

    class TabbedPaneFocusListener extends FocusAdapter {

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.FocusAdapter#focusGained(java.awt.event.FocusEvent)
         */
        @Override
        public void focusGained(FocusEvent e) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            Component component = pane.getSelectedComponent();
            if (component == null) {
                return;
            }
            component.requestFocusInWindow();
        }

    }

    class TabbedPaneMouseListener extends MouseAdapter {

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            if (e.getClickCount() == 2 && pane.getTabCount() != 0) {
                maximizeComponent(pane);
            } else if (e.getClickCount() == 1) {
                Component component = pane.getSelectedComponent();
                componentContainer.setSelectedComponent((JComponent) component);
            }
        }

    }

    class TabbedPaneContainerListener extends ContainerAdapter {

        /*
         * (non-Javadoc)
         * 
         * @seejava.awt.event.ContainerAdapter#componentRemoved(java.awt.event.
         * ContainerEvent)
         */
        @Override
        public void componentRemoved(ContainerEvent e) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            if (pane.getTabCount() == 0 && isMaximized(pane)) {
                maximizeComponent(pane);
            }
        }

    }

    private class DividerHorizontalComponentListener extends ComponentAdapter {

        private final DecimalFormat formatter = new DecimalFormat("0.##");
        private final JSplitPane pane;
        private final String propertyKey;

        public DividerHorizontalComponentListener(JSplitPane pane,
                String propertyKey) {
            if (pane == null) {
                throw new NullPointerException("Split pane cant be null");
            }
            if (propertyKey == null) {
                throw new NullPointerException("Property key cant be null");
            }
            this.pane = pane;
            this.propertyKey = propertyKey;
        }

        /*
         * (non-Javadoc)
         * 
         * @seejava.awt.event.ComponentAdapter#componentMoved(java.awt.event.
         * ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            int dividerLocation = pane.getDividerLocation();
            if (dividerLocation < 0) {
                return;
            }
            double ratio = getDividerRatio(dividerLocation, pane);
            String property = formatter.format(ratio);
            Preferences preferences = Preferences
                    .userNodeForPackage(VhdllabFrame.class);
            preferences.put(propertyKey, property);
        }

        protected double getDividerRatio(double dividerLocation, JSplitPane pane) {
            return dividerLocation / pane.getWidth();
        }

    }

    class DividerVerticalComponentListener extends
            DividerHorizontalComponentListener {

        public DividerVerticalComponentListener(JSplitPane pane,
                String propertyKey) {
            super(pane, propertyKey);
        }

        /*
         * (non-Javadoc)
         * 
         * @seehr.fer.zemris.vhdllab.applets.main.MainApplet.
         * DividerHorizontalComponentListener#getDividerLocation(double,
         * javax.swing.JSplitPane)
         */
        @Override
        protected double getDividerRatio(double dividerLocation, JSplitPane pane) {
            return dividerLocation / pane.getHeight();
        }

    }

    private class SplitPaneHorizontalComponentListener extends ComponentAdapter {

        private final String propertyKey;
        private final double def;

        public SplitPaneHorizontalComponentListener(String propertyKey,
                double def) {
            if (propertyKey == null) {
                throw new NullPointerException("Property key cant be null");
            }
            this.propertyKey = propertyKey;
            this.def = def;
        }

        /*
         * (non-Javadoc)
         * 
         * @seejava.awt.event.ComponentAdapter#componentResized(java.awt.event.
         * ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            JSplitPane pane = (JSplitPane) e.getSource();
            Preferences preferences = Preferences
                    .userNodeForPackage(VhdllabFrame.class);
            double ratio = preferences.getDouble(propertyKey, def);
            int dividerLocation = getDividerLocation(ratio, pane);
            pane.setDividerLocation(dividerLocation);
        }

        protected int getDividerLocation(double ratio, JSplitPane pane) {
            return (int) (pane.getWidth() * ratio);
        }

    }

    class SplitPaneVerticalComponentListener extends
            SplitPaneHorizontalComponentListener {

        public SplitPaneVerticalComponentListener(String propertyKey, double def) {
            super(propertyKey, def);
        }

        /*
         * (non-Javadoc)
         * 
         * @seehr.fer.zemris.vhdllab.applets.main.MainApplet.
         * SplitPaneHorizontalComponentListener#getDividerLocation(double,
         * javax.swing.JSplitPane)
         */
        @Override
        protected int getDividerLocation(double ratio, JSplitPane pane) {
            return (int) (pane.getHeight() * ratio);
        }

    }

    public VhdllabFrame(ApplicationContext context) {
        this.context = context;
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitWithConfirmation();
            }
        });

        setTitle("VHDLLab");
        setPreferredSize(new Dimension(900, 700));
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        URL resource = getClass().getClassLoader().getResource(
                "icons/vhdllab_main_16.png");
        setIconImage(new ImageIcon(resource).getImage());
        init();
    }

}
