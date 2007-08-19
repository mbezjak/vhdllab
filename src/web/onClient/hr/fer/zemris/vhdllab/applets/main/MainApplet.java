package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.vhdllab.applets.main.component.about.About;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageType;
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
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;
import hr.fer.zemris.vhdllab.preferences.PropertyListener;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;
import hr.fer.zemris.vhdllab.utilities.StringUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * Main applet is a container for all other modules. This is where all the GUI
 * is placed and shown to a user.
 * 
 * @author Miro Bezjak
 * @see ISystemContainer
 */
public class MainApplet extends JApplet implements IComponentContainer,
		IComponentProvider, PropertyListener {

	private static final long serialVersionUID = 4037604752375048576L;

	private ISystemContainer systemContainer;
	private ResourceBundle bundle;

	/* COMPONENT CONTAINER PRIVATE FIELDS */
	private Map<JComponent, ComponentInformation> components;
	private Map<ComponentGroup, JComponent> selectedComponentsByGroup;
	private JComponent selectedComponent;

	private JTabbedPane centerTabbedPane;
	private JTabbedPane bottomTabbedPane;
	private JTabbedPane leftTabbedPane;
	private JTabbedPane rightTabbedPane;
	private IStatusBar statusBar;
	private JToolBar toolBar;

	private JSplitPane projectExplorerSplitPane;
	private JSplitPane viewSplitPane;
	private JSplitPane sideBarSplitPane;

	private JPanel centerPanel;
	private JPanel normalCenterPanel;
	private Container parentOfMaximizedComponent = null;

	private About about;

	private IComponentStorage componentStorage;
	private IEditorManager editorManager;
	private IViewManager viewManager;

	private Communicator communicator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		super.init();
		String userId = this.getParameter("userId");
		if (userId == null) {
			// TODO following should be removed when security is implemented!
			// We must not enter this! If we do, applet should refuse to run!
			// Until then:
			userId = "uid:id-not-set";
			// future implementation when security is in place
			// throw new SecurityException();
		}
		components = new HashMap<JComponent, ComponentInformation>();
		selectedComponentsByGroup = new HashMap<ComponentGroup, JComponent>();

		IResourceManager resourceManager;
		ISystemLog systemLog;
		try {
			AjaxMediator ajax = new DefaultAjaxMediator(this);
			Initiator initiator = new AjaxInitiator(ajax);
			MethodInvoker invoker = new DefaultMethodInvoker(initiator);
			communicator = new Communicator(invoker, userId);
			communicator.init();
			resourceManager = new DefaultResourceManager(communicator);
			systemLog = new DefaultSystemLog();
			componentStorage = new DefaultComponentStorage(this);
			bundle = resourceManager
					.getResourceBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
		} catch (Exception e) {
			// TODO ovo se treba maknut kad MainApplet vise nece bit u
			// development fazi
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JOptionPane.showMessageDialog(this, sw.toString());
			return;
		}

		initGUI();
		int duration;
		try {
			duration = resourceManager.getPreferences().getPropertyAsInteger(
					UserFileConstants.SYSTEM_TOOLTIP_DURATION).intValue();
		} catch (PropertyAccessException e) {
			duration = 15000;
		}
		ToolTipManager.sharedInstance().setDismissDelay(duration);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setPaneSize();
			}
		});

		DefaultSystemContainer systemContainer = new DefaultSystemContainer(
				resourceManager, systemLog, this, JOptionPane
						.getFrameForComponent(this));
		this.systemContainer = systemContainer;
		ComponentConfiguration conf;
		try {
			conf = ComponentConfigurationParser.getConfiguration();
		} catch (UniformAppletException e) {
			e.printStackTrace();
			return;
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
		try {
			systemContainer.init();
		} catch (UniformAppletException e) {
			e.printStackTrace();
			return;
		}

		getPreferences().addPropertyListener(this,
				UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH);
		getPreferences().addPropertyListener(this,
				UserFileConstants.SYSTEM_SIDEBAR_WIDTH);
		getPreferences().addPropertyListener(this,
				UserFileConstants.SYSTEM_VIEW_HEIGHT);
		getPreferences().addPropertyListener(this,
				UserFileConstants.SYSTEM_TOOLTIP_DURATION);

		// FIXME ovo mozda spretnije rijesit
		IComponentIdentifier<?> stViewIdentifier = ComponentIdentifierFactory
				.createViewIdentifier(ComponentTypes.VIEW_STATUS_HISTORY);
		systemContainer.getViewManager().openView(stViewIdentifier);
		systemContainer.getViewManager().openProjectExplorer();

		String text = bundle
				.getString(LanguageConstants.STATUSBAR_LOAD_COMPLETE);
		systemContainer.echoStatusText(text, MessageType.SUCCESSFUL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#start()
	 */
	@Override
	public void start() {
		super.start();
		setPaneSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#stop()
	 */
	@Override
	public void stop() {
		super.stop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#destroy()
	 */
	@Override
	public void destroy() {
		try {
			((DefaultSystemContainer) systemContainer).dispose();
			communicator.dispose();
		} catch (UniformAppletException e) {
			e.printStackTrace();
		}
		this.setJMenuBar(null);
		this.getContentPane().removeAll();
		this.repaint();
		super.destroy();
	}

	private void initGUI() {
		JPanel topContainerPanel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(new BorderLayout());
		JMenuBar menuBar = new PrivateMenuBar(bundle).setupMainMenu();
		this.setJMenuBar(menuBar);

		toolBar = new PrivateToolBar(bundle).setup();
		topContainerPanel.add(toolBar, BorderLayout.NORTH);
		topContainerPanel.add(setupStatusBar(), BorderLayout.SOUTH);
		normalCenterPanel = setupCenterPanel();
		centerPanel.add(normalCenterPanel, BorderLayout.CENTER);
		topContainerPanel.add(centerPanel, BorderLayout.CENTER);

		this.add(topContainerPanel, BorderLayout.CENTER);
	}

	private JPanel setupStatusBar() {
		StatusBar statusBar = new StatusBar();
		JPanel statusBarPanel = new JPanel(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);
		statusBarPanel.setPreferredSize(new Dimension(0, 24));
		this.statusBar = statusBar;
		return statusBarPanel;
	}

	private JPanel setupCenterPanel() {
		about = new About(this);

		centerTabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		bottomTabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		leftTabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		rightTabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);

		centerTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Component component = centerTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});
		centerTabbedPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Component component = centerTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});
		bottomTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Component component = bottomTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});
		bottomTabbedPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Component component = bottomTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});
		leftTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Component component = leftTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});
		leftTabbedPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Component component = leftTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});
		rightTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Component component = rightTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});
		rightTabbedPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Component component = rightTabbedPane.getSelectedComponent();
				if (component == null) {
					return;
				}
				activate((JComponent) component);
			}
		});

		PrivateMenuBar menubar = new PrivateMenuBar(bundle);
		centerTabbedPane.setComponentPopupMenu(menubar
				.setupPopupMenuForComponents(centerTabbedPane));
		bottomTabbedPane.setComponentPopupMenu(menubar
				.setupPopupMenuForComponents(bottomTabbedPane));
		leftTabbedPane.setComponentPopupMenu(menubar
				.setupPopupMenuForComponents(leftTabbedPane));
		rightTabbedPane.setComponentPopupMenu(menubar
				.setupPopupMenuForComponents(rightTabbedPane));

		centerTabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2
						&& centerTabbedPane.getTabCount() != 0) {
					maximizeComponent(centerTabbedPane);
				}
				Component component = centerTabbedPane.getSelectedComponent();
				if (component != null) {
					component.requestFocusInWindow();
				}
			}
		});
		centerTabbedPane.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentRemoved(ContainerEvent e) {
				if (centerTabbedPane.getTabCount() == 0
						&& isMaximized(centerTabbedPane)) {
					maximizeComponent(centerTabbedPane);
				}
			}
		});

		bottomTabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2
						&& bottomTabbedPane.getTabCount() != 0) {
					maximizeComponent(bottomTabbedPane);
				}
				Component component = bottomTabbedPane.getSelectedComponent();
				if (component != null) {
					component.requestFocusInWindow();
				}
			}
		});
		bottomTabbedPane.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentRemoved(ContainerEvent e) {
				if (bottomTabbedPane.getTabCount() == 0
						&& isMaximized(bottomTabbedPane)) {
					maximizeComponent(bottomTabbedPane);
				}
			}
		});

		leftTabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && leftTabbedPane.getTabCount() != 0) {
					maximizeComponent(leftTabbedPane);
				}
				Component component = leftTabbedPane.getSelectedComponent();
				if (component != null) {
					component.requestFocusInWindow();
				}
			}
		});
		leftTabbedPane.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentRemoved(ContainerEvent e) {
				if (leftTabbedPane.getTabCount() == 0
						&& isMaximized(leftTabbedPane)) {
					maximizeComponent(leftTabbedPane);
				}
			}
		});

		rightTabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2
						&& rightTabbedPane.getTabCount() != 0) {
					maximizeComponent(rightTabbedPane);
				}
				Component component = rightTabbedPane.getSelectedComponent();
				if (component != null) {
					component.requestFocusInWindow();
				}
			}
		});
		rightTabbedPane.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentRemoved(ContainerEvent e) {
				if (rightTabbedPane.getTabCount() == 0
						&& isMaximized(rightTabbedPane)) {
					maximizeComponent(rightTabbedPane);
				}
			}
		});

		sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				centerTabbedPane, null);
		// sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		// centerTabbedPane, rightTabbedPane);
		projectExplorerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftTabbedPane, sideBarSplitPane);
		viewSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				projectExplorerSplitPane, bottomTabbedPane);

		JPanel centerComponentsPanel = new JPanel(new BorderLayout());
		centerComponentsPanel.add(viewSplitPane);

		BasicSplitPaneDivider divider;
		divider = ((BasicSplitPaneUI) projectExplorerSplitPane.getUI())
				.getDivider();
		divider.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				int dividerLocation = projectExplorerSplitPane
						.getDividerLocation();
				if (dividerLocation < 0) {
					return;
				}
				double size = dividerLocation * 1.0
						/ projectExplorerSplitPane.getWidth();
				DecimalFormat formatter = new DecimalFormat("0.##");
				String property = formatter.format(size);
				setProperty(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH,
						property);
			}
		});

		divider = ((BasicSplitPaneUI) sideBarSplitPane.getUI()).getDivider();
		divider.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				int dividerLocation = sideBarSplitPane.getDividerLocation();
				if (dividerLocation < 0) {
					return;
				}
				double size = dividerLocation * 1.0
						/ sideBarSplitPane.getWidth();
				DecimalFormat formatter = new DecimalFormat("0.##");
				String property = formatter.format(size);
				setProperty(UserFileConstants.SYSTEM_SIDEBAR_WIDTH, property);
			}
		});

		divider = ((BasicSplitPaneUI) viewSplitPane.getUI()).getDivider();
		divider.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				int dividerLocation = viewSplitPane.getDividerLocation();
				if (dividerLocation < 0) {
					return;
				}
				double size = dividerLocation * 1.0 / viewSplitPane.getHeight();
				DecimalFormat formatter = new DecimalFormat("0.##");
				String property = formatter.format(size);
				setProperty(UserFileConstants.SYSTEM_VIEW_HEIGHT, property);
			}
		});

		return centerComponentsPanel;

	}

	/**
	 * Stop all internet traffic and destroy application.
	 */
	private void exitApplication() {
		stop();
		destroy();
	}

	/**
	 * Private class for creating menu bar. Created menu bar will be localized.
	 * 
	 * @author Miro Bezjak
	 */
	private class PrivateMenuBar {

		private ResourceBundle bundle;

		/**
		 * Constructor.
		 * 
		 * @param bundle
		 *            {@link ResourceBundle} that contains localized information
		 *            about menus that will be created.
		 */
		public PrivateMenuBar(ResourceBundle bundle) {
			this.bundle = bundle;
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
					JComponent c = (JComponent) pane.getSelectedComponent();
					if (c == null) {
						return;
					}
					ComponentGroup group = getComponentGroup(c);
					if (group.equals(ComponentGroup.EDITOR)) {
						systemContainer.getEditorManager().saveEditor(
								(IEditor) c);
					}
				}
			});
			menuBar.add(menuItem);

			// Save all editors
			key = LanguageConstants.MENU_FILE_SAVE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<IEditor> editorsToSave = new ArrayList<IEditor>(pane
							.getTabCount());
					for (int i = 0; i < pane.getTabCount(); i++) {
						JComponent c = (JComponent) pane.getComponentAt(i);
						ComponentGroup group = getComponentGroup(c);
						if (group.equals(ComponentGroup.EDITOR)) {
							editorsToSave.add((IEditor) c);
						}

					}
					systemContainer.getEditorManager().saveEditors(
							editorsToSave);
				}
			});
			menuBar.add(menuItem);
			menuBar.addSeparator();

			// Close editor
			key = LanguageConstants.MENU_FILE_CLOSE;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComponent selectedComponent = (JComponent) pane
							.getSelectedComponent();
					if (selectedComponent == null) {
						return;
					}
					ComponentGroup group = getComponentGroup(selectedComponent);
					if (group.equals(ComponentGroup.EDITOR)) {
						systemContainer.getEditorManager().closeEditor(
								(IEditor) selectedComponent);
					} else {
						componentStorage.remove(selectedComponent);
					}
				}
			});
			menuBar.add(menuItem);

			// Close other editors
			key = LanguageConstants.MENU_FILE_CLOSE_OTHER;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = pane.getSelectedIndex();
					if (index == -1) {
						return;
					}
					List<IEditor> editorsToClose = new ArrayList<IEditor>(pane
							.getTabCount());
					List<JComponent> componentsToClose = new ArrayList<JComponent>(
							pane.getTabCount());
					for (int i = 0; i < pane.getTabCount(); i++) {
						if (i == index) {
							continue;
						}
						JComponent c = (JComponent) pane.getComponentAt(i);
						ComponentGroup group = getComponentGroup(c);
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
				}
			});
			menuBar.add(menuItem);

			// Close all editors
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<IEditor> editorsToClose = new ArrayList<IEditor>(pane
							.getTabCount());
					List<JComponent> componentsToClose = new ArrayList<JComponent>(
							pane.getTabCount());
					for (int i = 0; i < pane.getTabCount(); i++) {
						JComponent c = (JComponent) pane.getComponentAt(i);
						ComponentGroup group = getComponentGroup(c);
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
					systemContainer.createNewProjectInstance();
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
					systemContainer
							.createNewFileInstance(FileTypes.FT_VHDL_SOURCE);
				}
			});
			submenu.add(menuItem);

			// New Testbench menu item
			key = LanguageConstants.MENU_FILE_NEW_TESTBENCH;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.createNewFileInstance(FileTypes.FT_VHDL_TB);
				}
			});
			submenu.add(menuItem);

			// New Schema menu item
			key = LanguageConstants.MENU_FILE_NEW_SCHEMA;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer
							.createNewFileInstance(FileTypes.FT_VHDL_STRUCT_SCHEMA);
				}
			});
			submenu.add(menuItem);

			// New Automat menu item
			key = LanguageConstants.MENU_FILE_NEW_AUTOMAT;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer
							.createNewFileInstance(FileTypes.FT_VHDL_AUTOMAT);
				}
			});
			submenu.add(menuItem);

			// TODO this is only a temporary solution!
			submenu.addSeparator();
			menuItem = new JMenuItem("Property");
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer
							.createNewFileInstance(FileTypes.FT_PREFERENCES);
				}
			});
			submenu.add(menuItem);
			// END TEMP SOLUTION

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
					systemContainer.getEditorManager().saveEditor(
							editorManager.getSelectedEditor());
				}
			});
			menu.add(menuItem);

			// Save All menu item
			key = LanguageConstants.MENU_FILE_SAVE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.getEditorManager().saveAllEditors();
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
					systemContainer.getEditorManager().closeEditor(
							editorManager.getSelectedEditor());
				}
			});
			menu.add(menuItem);

			// Close All menu item
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.getEditorManager().closeAllEditors();
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
					exitApplication();
				}
			});
			menu.add(menuItem);

			menuBar.add(menu);

			// Edit menu
			key = LanguageConstants.MENU_EDIT;
			menu = new JMenu(bundle.getString(key));
			setCommonMenuAttributes(menu, key);
			menuBar.add(menu);

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
					/*
					 * if(editorPane.isFocusOwner()) {
					 * maximizeComponent(editorPane); } else
					 * if(editorPane.isFocusCycleRoot()) {
					 * maximizeComponent(viewPane); }
					 */
					if (getSelectedComponent() != null) {
						ComponentPlacement placement = componentStorage
								.getComponentPlacement(getSelectedComponent());
						maximizeComponent(getTabbedPane(placement));
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
					} catch (Exception ex) {
						String text = bundle
								.getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
						String viewTitle = bundle
								.getString(LanguageConstants.TITLE_FOR
										+ viewType);
						text = PlaceholderUtil.replacePlaceholders(text,
								new String[] { viewTitle });
						systemContainer.echoStatusText(text, MessageType.ERROR);
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
					} catch (Exception ex) {
						String text = bundle
								.getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
						String viewTitle = bundle
								.getString(LanguageConstants.TITLE_FOR
										+ viewType);
						text = PlaceholderUtil.replacePlaceholders(text,
								new String[] { viewTitle });
						systemContainer.echoStatusText(text, MessageType.ERROR);
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
					} catch (Exception ex) {
						String text = bundle
								.getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
						String viewTitle = bundle
								.getString(LanguageConstants.TITLE_FOR
										+ viewType);
						text = PlaceholderUtil.replacePlaceholders(text,
								new String[] { viewTitle });
						systemContainer.echoStatusText(text, MessageType.ERROR);
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
					} catch (Exception ex) {
						String text = bundle
								.getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
						String viewTitle = bundle
								.getString(LanguageConstants.TITLE_FOR
										+ ComponentTypes.VIEW_PROJECT_EXPLORER);
						text = PlaceholderUtil.replacePlaceholders(text,
								new String[] { viewTitle });
						systemContainer.echoStatusText(text, MessageType.ERROR);
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
					systemContainer.compileWithDialog();
				}
			});
			menu.add(menuItem);

			// Compile active editor
			key = LanguageConstants.MENU_TOOLS_COMPILE_ACTIVE;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.compile(editorManager.getSelectedEditor());
				}
			});
			menu.add(menuItem);

			// Compile history
			key = LanguageConstants.MENU_TOOLS_COMPILE_HISTORY;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.compileLastHistoryResult();
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
					systemContainer.simulateWithDialog();
				}
			});
			menu.add(menuItem);

			// Simulate active editor
			key = LanguageConstants.MENU_TOOLS_SIMULATE_ACTIVE;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.simulate(editorManager.getSelectedEditor());
				}
			});
			menu.add(menuItem);

			// Simulate history
			key = LanguageConstants.MENU_TOOLS_SIMULATE_HISTORY;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.simulateLastHistoryResult();
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
					systemContainer.getEditorManager().viewVHDLCode(
							editorManager.getSelectedEditor());
				}
			});
			menu.add(menuItem);
			menu.addSeparator();

			// View Preferences
			key = LanguageConstants.MENU_TOOLS_VIEW_PREFERENCES;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					systemContainer.getEditorManager().openPreferences();
				}
			});
			menu.add(menuItem);

			menuBar.add(menu);

			// Help menu
			key = LanguageConstants.MENU_HELP;
			menu = new JMenu(bundle.getString(key));
			key = LanguageConstants.MENU_HELP_ABOUT;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					about.setVisible(true);
				}
			});
			menu.add(menuItem);
			setCommonMenuAttributes(menu, key);
			menuBar.add(menu);

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

	/**
	 * Private class for creating tool bar. Created tool bar will be localized.
	 * 
	 * @author Miro Bezjak
	 */
	private class PrivateToolBar {

		private ResourceBundle bundle;

		/**
		 * Constructor.
		 * 
		 * @param bundle
		 *            {@link ResourceBundle} that contains information about
		 *            menus that will be created.
		 */
		public PrivateToolBar(ResourceBundle bundle) {
			this.bundle = bundle;
		}

		/**
		 * Creates and instantiates tool bar.
		 * 
		 * @return created tool bar
		 */
		public JToolBar setup() {
			JToolBar toolBar = new JToolBar();
			toolBar.setPreferredSize(new Dimension(0, 24));

			return toolBar;
		}
	}

	@Override
	public IStatusBar getStatusBar() {
		return statusBar;
	}

	@Override
	public void propertyChanged(String name, String oldValue, String newValue) {
		double size;
		try {
			size = getPreferences().getPropertyAsDouble(name);
		} catch (PropertyAccessException e) {
			e.printStackTrace();
			return;
		}
		if (name
				.equalsIgnoreCase(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH)) {
			projectExplorerSplitPane
					.setDividerLocation((int) (projectExplorerSplitPane
							.getWidth() * size));
		} else if (name
				.equalsIgnoreCase(UserFileConstants.SYSTEM_SIDEBAR_WIDTH)) {
			sideBarSplitPane.setDividerLocation((int) (sideBarSplitPane
					.getWidth() * size));
		} else if (name.equalsIgnoreCase(UserFileConstants.SYSTEM_VIEW_HEIGHT)) {
			viewSplitPane
					.setDividerLocation((int) (viewSplitPane.getHeight() * size));
		} else if (name
				.equalsIgnoreCase(UserFileConstants.SYSTEM_TOOLTIP_DURATION)) {
			int duration;
			try {
				duration = getPreferences().getPropertyAsInteger(
						UserFileConstants.SYSTEM_TOOLTIP_DURATION).intValue();
			} catch (PropertyAccessException e) {
				duration = 15000;
			}
			ToolTipManager.sharedInstance().setDismissDelay(duration);
		}
	}

	private IUserPreferences getPreferences() {
		return systemContainer.getPreferences();
	}

	private String getProperty(String name) {
		return systemContainer.getProperty(name);
	}

	private void setProperty(String name, String data) {
		systemContainer.setProperty(name, data);
	}

	private void setPaneSize() {
		try {
			validate();
			double size;

			size = Double
					.parseDouble(getProperty(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH));
			projectExplorerSplitPane
					.setDividerLocation((int) (projectExplorerSplitPane
							.getWidth() * size));

			size = Double
					.parseDouble(getProperty(UserFileConstants.SYSTEM_SIDEBAR_WIDTH));
			sideBarSplitPane.setDividerLocation((int) (sideBarSplitPane
					.getWidth() * size));

			size = Double
					.parseDouble(getProperty(UserFileConstants.SYSTEM_VIEW_HEIGHT));
			viewSplitPane
					.setDividerLocation((int) (viewSplitPane.getHeight() * size));
		} catch (Exception e) {
			projectExplorerSplitPane
					.setDividerLocation((int) (projectExplorerSplitPane
							.getWidth() * 0.15));
			sideBarSplitPane.setDividerLocation((int) (sideBarSplitPane
					.getWidth() * 0.75));
			viewSplitPane
					.setDividerLocation((int) (sideBarSplitPane.getWidth() * 0.75));
		}
	}

	private void storePaneSize() {
		DecimalFormat formatter = new DecimalFormat("0.##");
		double size = projectExplorerSplitPane.getDividerLocation() * 1.0
				/ projectExplorerSplitPane.getWidth();
		String property = formatter.format(size);
		setProperty(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH, property);

		size = sideBarSplitPane.getDividerLocation() * 1.0
				/ sideBarSplitPane.getWidth();
		property = formatter.format(size);
		setProperty(UserFileConstants.SYSTEM_SIDEBAR_WIDTH, property);

		size = viewSplitPane.getDividerLocation() * 1.0
				/ viewSplitPane.getHeight();
		property = formatter.format(size);
		setProperty(UserFileConstants.SYSTEM_VIEW_HEIGHT, property);
	}

	private void maximizeComponent(Component component) {
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
	}

	private boolean isMaximized(Component component) {
		return parentOfMaximizedComponent != null;
	}

	private JTabbedPane getTabbedPane(JComponent component) {
		ComponentPlacement placement = components.get(component).getPlacement();
		return getTabbedPane(placement);
	}

	private JTabbedPane getTabbedPane(ComponentPlacement placement) {
		switch (placement) {
		case CENTER:
			return centerTabbedPane;
		case BOTTOM:
			return bottomTabbedPane;
		case LEFT:
			return leftTabbedPane;
		case RIGHT:
			return rightTabbedPane;
		default:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#addComponent(java.lang.String,
	 *      javax.swing.JComponent,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void addComponent(String title, JComponent component,
			ComponentGroup group, ComponentPlacement placement) {
		components.put(component, new ComponentInformation(group, placement));
		JTabbedPane pane = getTabbedPane(placement);
		pane.add(title, component);
		activate(component);
		addListenersFor(component);
	}

	/**
	 * Sets an icon for a specified component.
	 * 
	 * @param component
	 *            a component to set icon to
	 */
	private void setIcon(JComponent component) {
		ComponentInformation info = components.get(component);
		JTabbedPane pane = getTabbedPane(info.getPlacement());
		Icon componentIcon = ComponentIcon.getNormalIcon(info.getGroup());
		int index = pane.indexOfComponent(component);
		pane.setIconAt(index, componentIcon);
	}

	/**
	 * Sets an active icon for a specified component.
	 * 
	 * @param component
	 *            a component to set icon to
	 */
	private void setActiveIcon(JComponent component) {
		ComponentInformation info = components.get(component);
		JTabbedPane pane = getTabbedPane(info.getPlacement());
		Icon componentIcon = ComponentIcon.getActiveIcon(info.getGroup());
		int index = pane.indexOfComponent(component);
		pane.setIconAt(index, componentIcon);
	}

	private void addListenersFor(JComponent component) {
		addListenersFor(component, component);
	}

	private void addListenersFor(Container container,
			JComponent originalComponent) {
		if (container == null) {
			return;
		}
		for (Component c : container.getComponents()) {
			if (c instanceof Container) {
				addListenersFor((Container) c, originalComponent);
			}
			c.addFocusListener(new MyFocus(originalComponent));
			c.addMouseListener(new MyMouse(originalComponent));
		}
	}

	private void activate(JComponent component) {
		if (component == null) {
			return;
		}
		if (selectedComponent == null || !selectedComponent.equals(component)) {
			selectedComponent = component;
		}
		ComponentGroup group = components.get(component).getGroup();
		JComponent previousComponent = selectedComponentsByGroup.get(group);
		if (previousComponent != null && previousComponent.equals(component)) {
			return;
		}
		deactivate(previousComponent);
		selectedComponentsByGroup.put(group, component);
		selectedComponent = component;
		setActiveIcon(component);
		JTabbedPane pane = getTabbedPane(component);
		pane.setSelectedComponent(component);
	}

	private void deactivate(JComponent component) {
		deactivate(component, true);
	}

	private void deactivate(JComponent component, boolean setIcon) {
		if (component == null) {
			return;
		}
		ComponentInformation info = components.get(component);
		selectedComponentsByGroup.remove(info.getGroup());
		if (selectedComponent != null && selectedComponent.equals(component)) {
			selectedComponent = null;
		}
		if (setIcon) {
			setIcon(component);
		}
	}

	class MyMouse extends MouseAdapter {
		private JComponent c;

		public MyMouse(JComponent c) {
			this.c = c;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			activate(c);
		}
	}

	class MyFocus extends FocusAdapter {
		private JComponent c;

		public MyFocus(JComponent c) {
			this.c = c;
		}

		@Override
		public void focusGained(FocusEvent e) {
			activate(c);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#removeComponent(javax.swing.JComponent)
	 */
	@Override
	public void removeComponent(JComponent component) {
		if (!containsComponent(component)) {
			return;
		}
		deactivate(component, false);
		JTabbedPane pane = getTabbedPane(component);
		components.remove(component);
		pane.remove(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#containsComponent(javax.swing.JComponent)
	 */
	@Override
	public boolean containsComponent(JComponent component) {
		return components.containsKey(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getComponentPlacement(javax.swing.JComponent)
	 */
	@Override
	public ComponentPlacement getComponentPlacement(JComponent component) {
		if (!containsComponent(component)) {
			return null;
		}
		return components.get(component).getPlacement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getComponentPlacement(javax.swing.JComponent)
	 */
	@Override
	public ComponentGroup getComponentGroup(JComponent component) {
		if (!containsComponent(component)) {
			return null;
		}
		return components.get(component).getGroup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#setComponentTitle(javax.swing.JComponent,
	 *      java.lang.String)
	 */
	@Override
	public void setComponentTitle(JComponent component, String title) {
		if (!containsComponent(component)) {
			return;
		}
		JTabbedPane pane = getTabbedPane(component);
		int index = pane.indexOfComponent(component);
		pane.setTitleAt(index, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getComponentTitle(javax.swing.JComponent)
	 */
	@Override
	public String getComponentTitle(JComponent component) {
		if (!containsComponent(component)) {
			return null;
		}
		JTabbedPane pane = getTabbedPane(component);
		int index = pane.indexOfComponent(component);
		return pane.getTitleAt(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#setComponentToolTipText(javax.swing.JComponent,
	 *      java.lang.String)
	 */
	@Override
	public void setComponentToolTipText(JComponent component, String tooltip) {
		if (!containsComponent(component)) {
			return;
		}
		JTabbedPane pane = getTabbedPane(component);
		int index = pane.indexOfComponent(component);
		pane.setToolTipTextAt(index, tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#setSelectedComponent(javax.swing.JComponent)
	 */
	@Override
	public void setSelectedComponent(JComponent component) {
		if (!containsComponent(component)) {
			return;
		}
		activate(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getSelectedComponent()
	 */
	@Override
	public JComponent getSelectedComponent() {
		return selectedComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getSelectedComponent(java.util.Collection)
	 */
	@Override
	public JComponent getSelectedComponent(ComponentGroup group) {
		return selectedComponentsByGroup.get(group);
	}

	/**
	 * Stores a component group and placement.
	 * 
	 * @author Miro Bezjak
	 */
	private static class ComponentInformation {
		private ComponentGroup group;
		private ComponentPlacement placement;

		/**
		 * Constructs a component information using specified parameters.
		 * 
		 * @param group
		 *            a component group
		 * @param placement
		 *            a component placement
		 */
		public ComponentInformation(ComponentGroup group,
				ComponentPlacement placement) {
			super();
			this.group = group;
			this.placement = placement;
		}

		/**
		 * A component group.
		 * 
		 * @return a component group
		 */
		public ComponentGroup getGroup() {
			return group;
		}

		/**
		 * A component placement.
		 * 
		 * @return a component placement
		 */
		public ComponentPlacement getPlacement() {
			return placement;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((group == null) ? 0 : group.hashCode());
			result = prime * result
					+ ((placement == null) ? 0 : placement.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final ComponentInformation other = (ComponentInformation) obj;
			if (group == null) {
				if (other.group != null)
					return false;
			} else if (!group.equals(other.group))
				return false;
			if (placement == null) {
				if (other.placement != null)
					return false;
			} else if (!placement.equals(other.placement))
				return false;
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "group: " + group + " / placement: " + placement;
		}
	}

}
