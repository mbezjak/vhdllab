package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.vhdllab.applets.main.components.dummy.SideBar;
import hr.fer.zemris.vhdllab.applets.main.components.projectexplorer.ProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.components.statusbar.StatusBar;
import hr.fer.zemris.vhdllab.applets.main.constants.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.applets.main.constants.ViewTypes;
import hr.fer.zemris.vhdllab.applets.main.dialogs.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.dialogs.SaveDialog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.preferences.Preferences;
import hr.fer.zemris.vhdllab.preferences.SingleOption;
import hr.fer.zemris.vhdllab.string.StringUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;
import hr.fer.zemris.vhdllab.vhdl.model.StringFormat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JApplet;
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

/**
 * Main applet that is container for all other modules. This applet also defines
 * methods that other modules use for communication. Two modules can not
 * communicate directly! Only through main applet that acts as a mediator. All
 * methods available to other modules are defined in <code>ProjectContainer</code>
 * interface.
 * 
 * @author Miro Bezjak
 * @see ProjectContainer
 */
public class MainApplet
		extends JApplet
		implements ProjectContainer {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private Communicator communicator;
	private ResourceBundle bundle;
	
	private JTabbedPane editorPane;
	private JTabbedPane viewPane;
	private IProjectExplorer projectExplorer;
	private SideBar sideBar;
	private IStatusBar statusBar;
	private JToolBar toolBar;

	private JPanel projectExplorerPanel;
	private JPanel sideBarPanel;
	
	private JSplitPane projectExplorerSplitPane;
	private JSplitPane viewSplitPane;
	private JSplitPane sideBarSplitPane;

	private JPanel centerPanel;
	private JPanel normalCenterPanel;
	private Container parentOfMaximizedComponent = null;
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		super.init();
		String userId = this.getParameter("userId");
		if(userId==null) {
			// TODO following should be removed when security is implemented!
			// We must not enter this! If we do, applet should refuse to run!
			// Until then:
			userId = "uid:id-not-set";
			// future implementation when security is in place
			// throw new SecurityException();
		}
		AjaxMediator ajax = new DefaultAjaxMediator(this);
		MethodInvoker invoker = new DefaultMethodInvoker(ajax);
		
		//**********************
		ServerInitData server = new ServerInitData();
		server.writeGlobalFiles();
		//**********************
		
		communicator = new Communicator(invoker, userId);
		bundle = getResourceBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
		
		initGUI();
		this.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				setPaneSize();
			}
			public void componentShown(ComponentEvent e) {}
			
		});

		
		//**********************
		server.writeServerInitData();
		//**********************
		
		refreshWorkspace();
		
		/*try {
			List<String> projects = communicator.getAllProjects();
			for(String projectName : projects) {
				projectExplorer.addProject(projectName);
				List<String> files = communicator.findFilesByProject(projectName);
				for(String fileName : files) {
					projectExplorer.addFile(projectName, fileName);
				}
			}
			String text = bundle.getString(LanguageConstants.STATUSBAR_LOAD_COMPLETE);
			echoStatusText(text);
		} catch (UniformAppletException e) {
			String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_LOAD_WORKSPACE);
			echoStatusText(text);
		}*/

	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#start()
	 */
	@Override
	public void start() {
		super.start();
		setPaneSize();
	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#stop()
	 */
	@Override
	public void stop() {
		super.stop();
	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#destroy()
	 */
	@Override
	public void destroy() {
		saveAllEditors();
		//closeAllEditors();
		try {
			communicator.cleanUp();
		} catch (UniformAppletException ignored) {}
		communicator = null;
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
		ProjectExplorer projectExplorer = new ProjectExplorer();
		this.projectExplorer = projectExplorer;
		projectExplorer.setProjectContainer(this);
		//JPanel projectExplorerPanel = new JPanel(new BorderLayout());
		projectExplorerPanel = new JPanel(new BorderLayout());
		projectExplorerPanel.add(projectExplorer, BorderLayout.CENTER);
		
		editorPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		editorPane.setComponentPopupMenu(new PrivateMenuBar(bundle).setupPopupMenuForEditors());
		editorPane.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					maximizeComponent(editorPane);
				}
				editorPane.getSelectedComponent().requestFocusInWindow();
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		editorPane.addContainerListener(new ContainerListener() {
			public void componentAdded(ContainerEvent e) {}
			public void componentRemoved(ContainerEvent e) {
				if(editorPane.getTabCount() == 0 && isMaximized(editorPane)) {
					maximizeComponent(editorPane);
				}
			}
		});
		
		viewPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		viewPane.setComponentPopupMenu(new PrivateMenuBar(bundle).setupPopupMenuForViews());
		viewPane.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					maximizeComponent(viewPane);
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		viewPane.addContainerListener(new ContainerListener() {
			public void componentAdded(ContainerEvent e) {
				/*if(viewPane.getTabCount() == 1) {
					Component[] components = viewSplitPane.getComponents();
					if(components != null) {
						boolean contains = false;
						for(Component c : components) {
							if(c == viewPane) {
								contains = true;
								break;
							}
						}
						if(!contains) {
							viewSplitPane.add(viewPane);
							// TODO postavit velicnu iz preference-a
						}
					}
				}*/
			}
			public void componentRemoved(ContainerEvent e) {
				/*if(viewPane.getTabCount() == 0) {
					viewSplitPane.remove(viewPane);
				}*/
			}
		});
		
		sideBarPanel = new JPanel(new BorderLayout());
		sideBar = new SideBar();
		sideBarPanel.add(sideBar, BorderLayout.CENTER);
		
		sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPane, null);
		projectExplorerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorerPanel, sideBarSplitPane);
		viewSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectExplorerSplitPane, viewPane);
		
		JPanel centerComponentsPanel = new JPanel(new BorderLayout());
		centerComponentsPanel.add(viewSplitPane);
		
		return centerComponentsPanel;
		
	}
	
	/**
	 * Stop all internet traffic and destroy application.
	 */
	protected void exitApplication() {
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
		 * @param bundle {@link ResourceBundle} that contains localized information about menus
		 * 		that will be created.
		 */
		public PrivateMenuBar(ResourceBundle bundle) {
			this.bundle = bundle;
		}
		
		public JPopupMenu setupPopupMenuForEditors() {
			JPopupMenu menuBar = new JPopupMenu();
			JMenuItem menuItem;
			String key;

			// Save editor
			key = LanguageConstants.MENU_FILE_SAVE;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveEditor((IEditor)editorPane.getSelectedComponent());
				}
			});
			menuBar.add(menuItem);
			
			// Save all editors
			key = LanguageConstants.MENU_FILE_SAVE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveAllEditors();
				}
			});
			menuBar.add(menuItem);
			menuBar.addSeparator();
			
			// Close editor
			key = LanguageConstants.MENU_FILE_CLOSE;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeEditor((IEditor)editorPane.getSelectedComponent(), true);
				}
			});
			menuBar.add(menuItem);

			// Close other editors
			key = LanguageConstants.MENU_FILE_CLOSE_OTHER;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllButThisEditor((IEditor)editorPane.getSelectedComponent(), true);
				}
			});
			menuBar.add(menuItem);
			
			// Close all editors
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllEditors(true);
				}
			});
			menuBar.add(menuItem);

			return menuBar;
		}
		
		public JPopupMenu setupPopupMenuForViews() {
			JPopupMenu menuBar = new JPopupMenu();
			JMenuItem menuItem;
			String key;

			// Close view
			key = LanguageConstants.MENU_FILE_CLOSE;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeView((IView)viewPane.getSelectedComponent());
				}
			});
			menuBar.add(menuItem);

			// Close other views
			key = LanguageConstants.MENU_FILE_CLOSE_OTHER;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllButThisView((IView)viewPane.getSelectedComponent());
				}
			});
			menuBar.add(menuItem);
			
			// Close all views
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllViews();
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
						createNewProjectInstance();
					} catch (UniformAppletException ex) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						ex.printStackTrace(pw);
						throw new NullPointerException(sw.toString());
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
						createNewFileInstance(FileTypes.FT_VHDL_SOURCE);
					} catch (UniformAppletException e1) {}
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
						createNewFileInstance(FileTypes.FT_VHDL_TB);
					} catch (UniformAppletException ex) {}
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
						createNewFileInstance(FileTypes.FT_VHDL_STRUCT_SCHEMA);
					} catch (UniformAppletException e1) {}
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
						createNewFileInstance(FileTypes.FT_VHDL_AUTOMAT);
					} catch (UniformAppletException ex) {}
				}
			});
			submenu.add(menuItem);
			
			menu.add(submenu);
			
			// Open menu item
			/*key = LanguageConstants.MENU_FILE_OPEN;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menu.add(menuItem);*/
			menu.addSeparator();
			
			// Save menu item
			key = LanguageConstants.MENU_FILE_SAVE;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveEditor((IEditor)editorPane.getSelectedComponent());
				}
			});
			menu.add(menuItem);

			// Save All menu item
			key = LanguageConstants.MENU_FILE_SAVE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveAllEditors();
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
					closeEditor((IEditor)editorPane.getSelectedComponent(), true);
				}
			});
			menu.add(menuItem);

			// Close All menu item
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setCommonMenuAttributes(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllEditors(true);
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
					/*if(editorPane.isFocusOwner()) {
						maximizeComponent(editorPane);
					} else if(editorPane.isFocusCycleRoot()) {
						maximizeComponent(viewPane);
					}*/
					if(editorPane.getTabCount() != 0) {
						maximizeComponent(editorPane);
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
					String viewType = ViewTypes.VT_COMPILATION_ERRORS;
					try {
						openView(viewType);
					} catch (Exception ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
						String viewTitle = bundle.getString(LanguageConstants.VIEW_TITLE_FOR + viewType);
						text = Utilities.replacePlaceholders(text, new String[] {viewTitle});
						echoStatusText(text);
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
					String viewType = ViewTypes.VT_SIMULATION_ERRORS;
					try {
						openView(viewType);
					} catch (Exception ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_OPEN_VIEW);
						String viewTitle = bundle.getString(LanguageConstants.VIEW_TITLE_FOR + viewType);
						text = Utilities.replacePlaceholders(text, new String[] {viewTitle});
						echoStatusText(text);
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
						compileWithDialog();
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_COMPILE);
						echoStatusText(text);
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
						compile((IEditor)editorPane.getSelectedComponent());
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_COMPILE);
						echoStatusText(text);
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
						compileLastHistoryResult();
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_COMPILE);
						echoStatusText(text);
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
						simulateWithDialog();
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_SIMULATE);
						echoStatusText(text);
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
						simulate((IEditor)editorPane.getSelectedComponent());
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_SIMULATE);
						echoStatusText(text);
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
						simulateLastHistoryResult();
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_SIMULATE);
						echoStatusText(text);
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
						viewVHDLCode((IEditor)editorPane.getSelectedComponent());
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE);
						echoStatusText(text);
					}
				}
			});
			menu.add(menuItem);
			
			
			menuBar.add(menu);

			// Help menu
			key = LanguageConstants.MENU_HELP;
			menu = new JMenu(bundle.getString(key));
			setCommonMenuAttributes(menu, key);
			menuBar.add(menu);
			
			return menuBar;
		}

		/**
		 * Sets mnemonic, accelerator and tooltip for a given menu. If keys for mnemonic,
		 * accelerator or tooltip (or all of them) does not exists then they will simply
		 * be ignored.
		 * 
		 * @param menu a menu where to set mnemonic and accelerator
		 * @param key a key containing menu's name 
		 */
		private void setCommonMenuAttributes(JMenuItem menu, String key) {
			/**
			 * For locating mnemonic, accelerator or tooltip of a <code>menu</code> this
			 * method will simply append appropriate string to <code>key</code>.
			 * Information regarding such strings that will be appended can be found here:
			 * <ul>
			 * <li>LanguageConstants.MNEMONIC_APPEND</li>
			 * <li>LanguageConstants.ACCELERATOR_APPEND</li>
			 * <li>LanguageConstants.TOOLTIP_APPEND</li>
			 * </ul>
			 */
			
			// Set mnemonic
			try {
				String temp = bundle.getString(key + LanguageConstants.MNEMONIC_APPEND);
				temp = temp.trim();
				if(temp.length() == 1 && StringUtil.isAlphaNumeric(temp)) {
					menu.setMnemonic(temp.codePointAt(0));
				}
			} catch (RuntimeException e) {}
			
			// Set accelerator
			try {
				String temp = bundle.getString(key + LanguageConstants.ACCELERATOR_APPEND);
				String[] keys = temp.split("[+]");
				if(keys.length != 0) {
					boolean functionKey = false;
					int mask = 0;
					int keyCode = 0;
					for(String k : keys) {
						k = k.trim();
						if(k.equalsIgnoreCase("ctrl")) mask += KeyEvent.CTRL_DOWN_MASK;
						else if(k.equalsIgnoreCase("alt")) mask += KeyEvent.ALT_DOWN_MASK;
						else if(k.equalsIgnoreCase("shift")) mask += KeyEvent.SHIFT_DOWN_MASK;
						else if(k.equalsIgnoreCase("func")) functionKey = true;
						else if(functionKey && k.length() <= 2) {
							if(!StringUtil.isNumeric(k)) throw new IllegalArgumentException();
							keyCode = KeyEvent.VK_F1 - 1 + Integer.parseInt(k);
							functionKey = false;
						}
						else if(k.length() == 1) {
							if(!StringUtil.isAlphaNumeric(k)) throw new IllegalArgumentException();
							keyCode = k.toUpperCase().codePointAt(0);								
						}
					}
					menu.setAccelerator(KeyStroke.getKeyStroke(keyCode, mask));
				}
			} catch (RuntimeException e) {}
			
			// Set tooltip
			try {
				String text = bundle.getString(key + LanguageConstants.TOOLTIP_APPEND);
				text = text.trim();
				menu.setToolTipText(text);
			} catch (RuntimeException e) {}
		}
	}

	/**
	 * Private class for creating tool bar. Created toll bar will be localized.
	 * 
	 * @author Miro Bezjak
	 */
	private class PrivateToolBar {
		
		private ResourceBundle bundle;

		/**
		 * Constructor.
		 * 
		 * @param bundle {@link ResourceBundle} that contains information about menus
		 * 		that will be created.
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
	
	private void compileWithDialog() throws UniformAppletException {
		String title = bundle.getString(LanguageConstants.DIALOG_RUN_COMPILATION_TITLE);
		String listTitle = bundle.getString(LanguageConstants.DIALOG_RUN_COMPILATION_LIST_TITLE);
		FileIdentifier file = showRunDialog(title, listTitle, RunDialog.COMPILATION_TYPE);
		if(file == null) return;
		String projectName = file.getProjectName();
		String fileName = file.getFileName();
		compile(projectName, fileName);
	}
	
	private void compile(IEditor editor) throws UniformAppletException {
		if(editor == null) return;
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		if(isCircuit(projectName, fileName)) {
			compile(projectName, fileName);
		}
	}
	
	private void compileLastHistoryResult() throws UniformAppletException {
		if(communicator.compilationHistoryIsEmpty()) {
			compileWithDialog();
		} else {
			FileIdentifier file = communicator.getLastCompilationHistoryTarget();
			if(file == null) {
				compileWithDialog();
			} else {
				compile(file.getProjectName(), file.getFileName());
			}
		}
	}
	
	public void compile(String projectName, String fileName) throws UniformAppletException {
		if(isCircuit(projectName, fileName)) {
			List<IEditor> openedEditors = getEditorsThatHave(projectName);
			String title = bundle.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_TITLE);
			String message = bundle.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE);
			boolean shouldContinue = saveResourcesWithSaveDialog(openedEditors, title, message);
			if(shouldContinue) {
				CompilationResult result = communicator.compile(projectName, fileName);
				IView view = openView(ViewTypes.VT_COMPILATION_ERRORS);
				view.setData(result);
			}
		}
	}
	
	private void simulateWithDialog() throws UniformAppletException {
		String title = bundle.getString(LanguageConstants.DIALOG_RUN_SIMULATION_TITLE);
		String listTitle = bundle.getString(LanguageConstants.DIALOG_RUN_SIMULATION_LIST_TITLE);
		FileIdentifier file = showRunDialog(title, listTitle, RunDialog.SIMULATION_TYPE);
		if(file == null) return;
		String projectName = file.getProjectName();
		String fileName = file.getFileName();
		simulate(projectName, fileName);
	}
	
	private void simulate(IEditor editor) throws UniformAppletException {
		if(editor == null) return;
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		if(isTestbench(projectName, fileName)) {
			simulate(projectName, fileName);
		}
	}
	
	private void simulateLastHistoryResult() throws UniformAppletException {
		if(communicator.simulationHistoryIsEmpty()) {
			simulateWithDialog();
		} else {
			FileIdentifier file = communicator.getLastSimulationHistoryTarget();
			if(file == null) {
				simulateWithDialog();
			} else {
				simulate(file.getProjectName(), file.getFileName());
			}
		}
	}
	
	public void simulate(String projectName, String fileName) throws UniformAppletException {
		if(isTestbench(projectName, fileName)) {
			List<IEditor> openedEditors = getEditorsThatHave(projectName);
			String title = bundle.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_TITLE);
			String message = bundle.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE);
			boolean shouldContinue = saveResourcesWithSaveDialog(openedEditors, title, message);
			if(shouldContinue) {
				SimulationResult result = communicator.runSimulation(projectName, fileName);
				IView view = openView(ViewTypes.VT_SIMULATION_ERRORS);
				view.setData(result);
				if(result.getWaveform() != null) {
					String simulationName = fileName + ".sim";
					openEditor(projectName, simulationName, result.getWaveform(), FileTypes.FT_VHDL_SIMULATION, false, true);
				}
			}
		}
	}
	
	public List<String> getAllCircuits(String projectName) throws UniformAppletException {
		List<String> fileNames = communicator.findFilesByProject(projectName);
		List<String> circuits = new ArrayList<String>();
		for(String name : fileNames) {
			if(isCircuit(projectName, name)) {
				circuits.add(name);
			}
		}
		return circuits;
	}
	
	public List<String> getAllTestbenches(String projectName) throws UniformAppletException {
		List<String> fileNames = communicator.findFilesByProject(projectName);
		List<String> testbenches = new ArrayList<String>();
		for(String name : fileNames) {
			if(isTestbench(projectName, name)) {
				testbenches.add(name);
			}
		}
		return testbenches;
	}
	
	private boolean isCircuit(String projectName, String fileName) throws UniformAppletException {
		List<String> fileTypes = communicator.getFileTypes();
		fileTypes.remove(FileTypes.FT_VHDL_TB);
		fileTypes.remove(FileTypes.FT_VHDL_SIMULATION);
		String type = getFileType(projectName, fileName);
		return fileTypes.contains(type);
	}
	
	private boolean isTestbench(String projectName, String fileName) throws UniformAppletException {
		String type = getFileType(projectName, fileName);
		return type.equals(FileTypes.FT_VHDL_TB);
	}
	
	private boolean isSimulation(String projectName, String fileName) throws UniformAppletException {
		String type = getFileType(projectName, fileName);
		return type.equals(FileTypes.FT_VHDL_SIMULATION);
	}
	
	public String getFileType(String projectName, String fileName) throws UniformAppletException {
		return communicator.loadFileType(projectName, fileName);
	}
	
	public void viewVHDLCode(String projectName, String fileName) throws UniformAppletException {
		// TODO ovo provjerit dal radi dobro
		if(isCircuit(projectName, fileName) || 
				isTestbench(projectName, fileName)) {
			String vhdl = communicator.generateVHDL(projectName, fileName);
			openEditor(projectName, "vhdl:"+fileName, vhdl, FileTypes.FT_VHDL_SOURCE, false, true);
		}
	}
	
	public void viewVHDLCode(IEditor editor) throws UniformAppletException {
		if(editor == null) return;
		viewVHDLCode(editor.getProjectName(), editor.getFileName());
	}
	
	public Hierarchy extractHierarchy(String projectName) throws UniformAppletException {
		/*Hierarchy h = communicator.extractHierarchy(projectName);
		JOptionPane.showMessageDialog(this, h.toString());
		return h;*/
		return communicator.extractHierarchy(projectName);
	}

	public CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) throws UniformAppletException {
		// TODO tu mozda sejvat file
		// pitanje je dal s dialogom ili ne?
		IEditor editor = getEditor(projectName, fileName);
		if(editor != null) {
			saveEditor(editor);
		}
		return communicator.getCircuitInterfaceFor(projectName, fileName);
	}

	public Preferences getPreferences(String type) throws UniformAppletException {
		return communicator.getPreferences(type);
	}
	
	public void savePreferences(Preferences pref) throws UniformAppletException {
		communicator.savePreferences(pref);
	}

	public ResourceBundle getResourceBundle(String baseName) {
		String language = null;
		String country = null;
		try {
			Preferences preferences = communicator.getPreferences(FileTypes.FT_COMMON);
			SingleOption option = preferences.getOption(UserFileConstants.COMMON_LANGUAGE);
			if(option != null) {
				language = option.getChosenValue(); 
			}
			option = preferences.getOption(UserFileConstants.COMMON_COUNTRY);
			if(option != null) {
				country = option.getChosenValue(); 
			}
		} catch (UniformAppletException ignored) {}
		if(language == null) language = "en";
		
		ResourceBundle bundle = CachedResourceBundles.getBundle(baseName, language, country);
		/* This might happen only during developing/testing due to error. It
		 * should never happen once application is deployed
		 */
		if(bundle == null) throw new NullPointerException("Bundle can not be null.");
		return bundle;
	}
	
	public String getSelectedProject() {
		return projectExplorer.getSelectedProject();
	}
	
	public List<String> getAllProjects() {
		return projectExplorer.getAllProjects();
	}
	
	public void echoStatusText(String text) {
		statusBar.setText(text);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer#isCorrectEntityName(java.lang.String)
	 */
	public boolean isCorrectEntityName(String name) {
		return StringFormat.isCorrectEntityName(name);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer#isCorrectProjectName(java.lang.String)
	 */
	public boolean isCorrectProjectName(String name) {
		return StringFormat.isCorrectProjectName(name);
	}
	
	private int indexOfView(String type) {
		for(int i = 0; i < viewPane.getTabCount(); i++) {
			IView view = (IView) viewPane.getComponentAt(i);
			String viewPaneComponentType = communicator.getViewType(view);
			if(viewPaneComponentType.equals(type)) {
				return i;
			}
		}
		return -1;
	}
	
	private int indexOfView(IView view) {
		if(view == null) return -1;
		String type = communicator.getViewType(view);
		return indexOfView(type);
	}
	
	private int indexOfEditor(String projectName, String fileName) {
		for(int i = 0; i < editorPane.getTabCount(); i++) {
			IEditor editor = (IEditor) editorPane.getComponentAt(i);
			if(projectName.equals(editor.getProjectName()) &&
					fileName.equalsIgnoreCase(editor.getFileName())) {
				return i;
			}
		}
		return -1;
	}
	
	private int indexOfEditor(IEditor editor) {
		if(editor == null) return -1;
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		return indexOfEditor(projectName, fileName);
	}
	
	private List<IView> getAllOpenedViews() {
		List<IView> openedViews = new ArrayList<IView>();
		for(int i = 0; i < viewPane.getTabCount(); i++) {
			IView view = (IView) viewPane.getComponentAt(i);
			openedViews.add(view);
		}
		return openedViews;
	}
	
	private List<IEditor> getAllOpenedEditors() {
		List<IEditor> openedEditors = new ArrayList<IEditor>();
		for(int i = 0; i < editorPane.getTabCount(); i++) {
			IEditor editor = (IEditor) editorPane.getComponentAt(i);
			openedEditors.add(editor);
		}
		return openedEditors;
	}
	
	public IView getView(String type) {
		int index = indexOfView(type);
		if(index == -1) {
			openView(type);
			index = indexOfView(type);
		}
		return (IView)viewPane.getComponentAt(index);
	}
	
	public IView openView(String type) {
		int index = indexOfView(type);
		if(index == -1) {
			// Initialization of an editor
			IView view = communicator.getView(type);
			view.setProjectContainer(this);
			// End of initialization

			String title = bundle.getString(LanguageConstants.VIEW_TITLE_FOR + type);
			Component component = viewPane.add(title, (Component)view);
			index = viewPane.indexOfComponent(component);
		}
		viewPane.setSelectedIndex(index);
		return getView(type);
	}

	
	public void openEditor(String projectName, String fileName, boolean isSavable, boolean isReadOnly) throws UniformAppletException {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) {
			String content = communicator.loadFileContent(projectName, fileName);
			FileContent fileContent = new FileContent(projectName, fileName, content);
			String type = getFileType(projectName, fileName);

			index = openEditorImpl(fileContent, type, isSavable, isReadOnly);
		}
		editorPane.setSelectedIndex(index);
	}
	
	private void openEditor(String projectName, String fileName, String content, String type, boolean isSavable, boolean isReadOnly) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if(content == null) {
			throw new NullPointerException("Content can not be null.");
		}
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) {
			FileContent fileContent = new FileContent(projectName, fileName, content);

			index = openEditorImpl(fileContent, type, isSavable, isReadOnly);
		} else {
			IEditor editor = (IEditor) editorPane.getComponentAt(index);
			FileContent fileContent = new FileContent(projectName, fileName, content);
			editor.setFileContent(fileContent);
		}
		editorPane.setSelectedIndex(index);
	}
	
	private int openEditorImpl(FileContent fileContent, String type, boolean isSavable, boolean isReadOnly)  {
		// Initialization of an editor
		IEditor editor = communicator.getEditor(type);
		editor.setProjectContainer(this);
		editor.init();
		editor.setSavable(isSavable);
		editor.setReadOnly(isReadOnly);
		editor.setFileContent(fileContent);
		// End of initialization
		
		String projectName = fileContent.getProjectName();
		String fileName = fileContent.getFileName();
		String title = createEditorTitle(projectName, fileName);
		Component component = editorPane.add(title, (Component)editor);
		int index = editorPane.indexOfComponent(component);
		String toolTipText = createEditorToolTip(projectName, fileName);
		editorPane.setToolTipTextAt(index, toolTipText);
		return index;
	}
	
	private String createEditorTitle(String projectName, String fileName) {
		return fileName + "/" + projectName;
	}
	
	private String createEditorToolTip(String projectName, String fileName)	{
		StringBuilder toolTipText = new StringBuilder(20 + projectName.length() + fileName.length());
		toolTipText.append(projectName).append("/").append(fileName);
		return toolTipText.toString();
	}
	
	private void refreshWorkspace() {
		// TODO to treba jos do kraja napravit, zajedno s refreshProject i refreshFile
		List<String> openedProjects = projectExplorer.getAllProjects();
		for(String p : openedProjects) {
			projectExplorer.removeProject(p);
		}
		
		try {
			List<String> projects = communicator.getAllProjects();
			for(String projectName : projects) {
				projectExplorer.addProject(projectName);
			}
			
			String text = bundle.getString(LanguageConstants.STATUSBAR_LOAD_COMPLETE);
			echoStatusText(text);
		} catch (UniformAppletException e) {
			String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_LOAD_WORKSPACE);
			echoStatusText(text);
		}
	}
	
	private void refreshProject(String projectName) throws UniformAppletException {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		projectExplorer.removeProject(projectName);
		if(communicator.existsProject(projectName)) {
			projectExplorer.addProject(projectName);
		}
	}
	
	public void resetEditorTitle(boolean contentChanged, String projectName, String fileName) {
		String title = createEditorTitle(projectName, fileName);
		if(contentChanged) {
			title = "*" + title;
		}
		int index = indexOfEditor(projectName, fileName);
		if(index != -1)  {
			editorPane.setTitleAt(index, title);
		}
	}
	
	public boolean existsFile(String projectName, String fileName) throws UniformAppletException {
		return communicator.existsFile(projectName, fileName);
	}
	
	public boolean existsProject(String projectName) throws UniformAppletException {
		return communicator.existsProject(projectName);
	}
	
	public void deleteFile(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if(fileIsOpened(projectName, fileName)) {
			IEditor editor = getOpenedEditor(projectName, fileName);
			closeEditor(editor, false);
		}
		projectExplorer.removeFile(projectName, fileName);
		communicator.deleteFile(projectName, fileName);
	}
	
	public void deleteProject(String projectName) throws UniformAppletException {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		for(String fileName : communicator.findFilesByProject(projectName)) {
			if(fileIsOpened(projectName, fileName)) {
				IEditor editor = getOpenedEditor(projectName, fileName);
				closeEditor(editor, false);
			}
		}
		projectExplorer.removeProject(projectName);
		communicator.deleteProject(projectName);
	}

	
	public void createNewFileInstance(String type) throws UniformAppletException {
		// Initialization of a wizard
		IWizard wizard = communicator.getEditor(type).getWizard();
		if(wizard == null) throw new NullPointerException("No wizard for type: " + type);
		wizard.setProjectContainer(this);
		FileContent content = wizard.getInitialFileContent(this);
		// End of initialization
		
		if(content == null) return;
		String projectName = content.getProjectName();
		String fileName = content.getFileName();
		String data = content.getContent();
		boolean exists = communicator.existsFile(projectName, fileName);
		if(exists) {
			String text = bundle.getString(LanguageConstants.STATUSBAR_EXISTS_FILE);
			text = Utilities.replacePlaceholders(text, new String[] {fileName});
			echoStatusText(text);
			return;
		}
		communicator.createFile(projectName, fileName, type);
		communicator.saveFile(projectName, fileName, data);
		projectExplorer.addFile(projectName, fileName);
		String text = bundle.getString(LanguageConstants.STATUSBAR_FILE_CREATED);
		text = Utilities.replacePlaceholders(text, new String[] {fileName});
		echoStatusText(text);
		openEditor(projectName, fileName, true, false);
	}
	
	public void createNewProjectInstance() throws UniformAppletException {
		String title = bundle.getString(LanguageConstants.DIALOG_CREATE_NEW_PROJECT_TITLE);
		String message = bundle.getString(LanguageConstants.DIALOG_CREATE_NEW_PROJECT_MESSAGE);
		String projectName = showCreateProjectDialog(title, message);
		if(projectName == null) return;
		boolean exists = communicator.existsProject(projectName);
		if(exists) {
			// projectName is never null here
			String text = bundle.getString(LanguageConstants.STATUSBAR_EXISTS_PROJECT);
			text = Utilities.replacePlaceholders(text, new String[] {projectName});
			echoStatusText(text);
			return;
		}
		communicator.createProject(projectName);
		projectExplorer.addProject(projectName);
		String text = bundle.getString(LanguageConstants.STATUSBAR_PROJECT_CREATED);
		text = Utilities.replacePlaceholders(text, new String[] {projectName});
		echoStatusText(text);
	}
	
	public IEditor getEditor(String projectName, String fileName) throws UniformAppletException {
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) {
			openEditor(projectName, fileName, true, false);
			index = indexOfEditor(projectName, fileName);
		}
		return (IEditor)editorPane.getComponentAt(index);
	}
	
	private List<IEditor> getEditorsThatHave(String projectName) {
		List<IEditor> openedEditors = getAllOpenedEditors();
		List<IEditor> editorsThatHaveProject = new ArrayList<IEditor>();
		for(IEditor e : openedEditors) {
			String editorsProjectName = e.getProjectName();
			if(editorsProjectName.equals(projectName)) {
				editorsThatHaveProject.add(e);
			}
		}
		return editorsThatHaveProject;
	}
	
	private IEditor getOpenedEditor(String projectName, String fileName) {
		int index = indexOfEditor(projectName, fileName);
		return (IEditor)editorPane.getComponentAt(index);
	}
	
	private void setPaneSize() {
		try {
			Preferences preferences = communicator.getPreferences(FileTypes.FT_APPLET);
			validate();
			SingleOption o;
			double size;
			
			o = preferences.getOption(UserFileConstants.APPLET_PROJECT_EXPLORER_WIDTH);
			size = Double.parseDouble(o.getChosenValue());
			projectExplorerSplitPane.setDividerLocation((int)(projectExplorerSplitPane.getWidth() * size));
			
			o = preferences.getOption(UserFileConstants.APPLET_SIDEBAR_WIDTH);
			size = Double.parseDouble(o.getChosenValue());
			sideBarSplitPane.setDividerLocation((int)(sideBarSplitPane.getWidth() * size));
			
			o = preferences.getOption(UserFileConstants.APPLET_VIEW_HEIGHT);
			size = Double.parseDouble(o.getChosenValue());
			viewSplitPane.setDividerLocation((int)(viewSplitPane.getHeight() * size));
		} catch (UniformAppletException e) {
			projectExplorerSplitPane.setDividerLocation((int)(projectExplorerSplitPane.getWidth() * 0.15));
			sideBarSplitPane.setDividerLocation((int)(sideBarSplitPane.getWidth() * 0.75));
			viewSplitPane.setDividerLocation((int)(sideBarSplitPane.getWidth() * 0.75));
		}
	}
	
	private void storePaneSize() {
		try {
			String type = FileTypes.FT_APPLET;
			Preferences preferences = communicator.getPreferences(type);
			
			SingleOption o = preferences.getOption(UserFileConstants.APPLET_PROJECT_EXPLORER_WIDTH);
			double size = projectExplorerSplitPane.getDividerLocation() * 1.0 / projectExplorerSplitPane.getWidth(); 
			o.setChosenValue(String.valueOf(size));
			
			o = preferences.getOption(UserFileConstants.APPLET_SIDEBAR_WIDTH);
			size = sideBarSplitPane.getDividerLocation() * 1.0 / sideBarSplitPane.getWidth(); 
			o.setChosenValue(String.valueOf(size));

			o = preferences.getOption(UserFileConstants.APPLET_VIEW_HEIGHT);
			size = viewSplitPane.getDividerLocation() * 1.0 / viewSplitPane.getHeight();
			o.setChosenValue(String.valueOf(size));
			
			savePreferences(preferences);
		} catch (UniformAppletException e) {}
	}
	
	private void maximizeComponent(Component component) {
		if(component == null) return;
		if(isMaximized(component)) {
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
	
	private void saveEditor(IEditor editor) {
		if(editor == null) return;
		List<IEditor> editorsToSave = new ArrayList<IEditor>(1);
		editorsToSave.add(editor);
		saveEditors(editorsToSave);
	}
	
	private void saveAllEditors() {
		List<IEditor> openedEditors = getAllOpenedEditors();
		saveEditors(openedEditors);
	}
	
	private void saveEditors(List<IEditor> editorsToSave) {
		if(editorsToSave == null || editorsToSave.isEmpty()) return;
		List<IEditor> savedEditors = new ArrayList<IEditor>();
		List<String> projects = new ArrayList<String>();
		for(IEditor editor : editorsToSave) {
			if(editor.isSavable() && editor.isModified()) {
				String fileName = editor.getFileName();
				String projectName = editor.getProjectName();
				String content = editor.getData();
				if(content == null) continue;
				savedEditors.add(editor);
				if(!projects.contains(projectName)) {
					projects.add(projectName);
				}
				try {
					communicator.saveFile(projectName, fileName, content);
					resetEditorTitle(false, projectName, fileName);
					String text = bundle.getString(LanguageConstants.STATUSBAR_FILE_SAVED);
					text = Utilities.replacePlaceholders(text, new String[] {fileName});
					echoStatusText(text);
				} catch (UniformAppletException e) {
					String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_SAVE_FILE);
					text = Utilities.replacePlaceholders(text, new String[] {fileName});
					echoStatusText(text);
				}
			}
		}
		
		for(String projectName : projects) {
			projectExplorer.refreshProject(projectName);
		}
		String text = bundle.getString(LanguageConstants.STATUSBAR_FILE_SAVED_ALL);
		String numberOfFiles = String.valueOf(savedEditors.size());
		text = Utilities.replacePlaceholders(text, new String[] {numberOfFiles});
		echoStatusText(text);
	}
	
	private boolean fileIsOpened(IEditor editor) {
		if(editor == null) return false;
		return indexOfEditor(editor) != -1;
	}
	
	private boolean fileIsOpened(String projectName, String fileName) {
		if(projectName == null || fileName == null) return false;
		return indexOfEditor(projectName, fileName) != -1;
	}
	
	private List<IEditor> pickOpenedEditors(List<IEditor> editors) {
		List<IEditor> openedEditors = new ArrayList<IEditor>();
		for(IEditor e : editors) {
			if(fileIsOpened(e)) {
				openedEditors.add(e);
			}
		}
		return openedEditors;
	}
	
	private void closeEditor(IEditor editor, boolean showDialog) {
		if(editor == null) return;
		List<IEditor> editorsToClose = new ArrayList<IEditor>(1);
		editorsToClose.add(editor);
		closeEditors(editorsToClose, showDialog);
	}
	
	private void closeAllEditors(boolean showDialog) {
		List<IEditor> openedEditors = getAllOpenedEditors();
		closeEditors(openedEditors, showDialog);
	}
	
	private void closeAllButThisEditor(IEditor editorToKeepOpened, boolean showDialog) {
		if(editorToKeepOpened == null) return;
		List<IEditor> openedEditors = getAllOpenedEditors();
		openedEditors.remove(editorToKeepOpened);
		closeEditors(openedEditors, showDialog);
	}
	
	private void closeEditors(List<IEditor> editorsToClose, boolean showDialog) {
		if(editorsToClose == null) return;
		String title = bundle.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_TITLE);
		String message = bundle.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_MESSAGE);
		editorsToClose = pickOpenedEditors(editorsToClose);
		boolean shouldContinue;
		if(showDialog) {
			shouldContinue = saveResourcesWithSaveDialog(editorsToClose, title, message);
		} else {
			shouldContinue = true;
		}
		if(shouldContinue) {
			for(IEditor editor : editorsToClose) {
				// Clean up of an editor
				editor.cleanUp();
				// End of clean up
				
				int index = indexOfEditor(editor);
				editorPane.remove(index);
			}
		}
	}
	
	private void closeView(IView view) {
		if(view == null) return;
		List<IView> viewsToClose = new ArrayList<IView>(1);
		viewsToClose.add(view);
		closeViews(viewsToClose);
	}
	
	private void closeAllViews() {
		List<IView> openedViews = getAllOpenedViews();
		closeViews(openedViews);
	}
	
	private void closeAllButThisView(IView viewToKeepOpened) {
		if(viewToKeepOpened == null) return;
		List<IView> openedViews = getAllOpenedViews();
		openedViews.remove(viewToKeepOpened);
		closeViews(openedViews);
	}
	
	private void closeViews(List<IView> viewsToClose) {
		if(viewsToClose == null) return;
		for(IView view : viewsToClose) {
			int index = indexOfView(view);
			viewPane.remove(index);
		}
	}
	
	private boolean saveResourcesWithSaveDialog(List<IEditor> openedEditors, String title, String message) {
		if(openedEditors == null) return false;
		// create a list of savable and modified editors
		List<IEditor> notSavedEditors = new ArrayList<IEditor>();
		for(IEditor editor : openedEditors) {
			if(editor.isSavable() && editor.isModified()) {
				notSavedEditors.add(editor);
			}
		}
		
		if(!notSavedEditors.isEmpty()) {
			// look in preference and see if there is a need to show save dialog (user might have
			// checked a "always save resources" checkbox)
			boolean shouldAutoSave;
			try {
				Preferences pref = getPreferences(FileTypes.FT_APPLET);
				SingleOption singleOption = pref.getOption(UserFileConstants.APPLET_SAVE_DIALOG_ALWAYS_SAVE_RESOURCES);
				String selected = singleOption.getChosenValue();
				shouldAutoSave = Boolean.parseBoolean(selected);
			} catch (UniformAppletException e) {
				shouldAutoSave = false;
			}
			
			List<IEditor> editorsToSave = notSavedEditors;
			if(!shouldAutoSave) {
				List<FileIdentifier> filesToSave = showSaveDialog(title, message, notSavedEditors);
				if(filesToSave == null) return false;
				
				// If size of files returned by save dialog equals those of not saved editors
				// then a list of files are entirely equal to a list of not saved editors and
				// no transformation is required.
				if(filesToSave.size() != editorsToSave.size()) {
					// transform FileIdentifiers to editors
					editorsToSave = new ArrayList<IEditor>();
					for(FileIdentifier file : filesToSave) {
						String projectName = file.getProjectName();
						String fileName = file.getFileName();
						IEditor e = getOpenedEditor(projectName, fileName);
						editorsToSave.add(e);
					}
				}
			}
			
			saveEditors(editorsToSave);
		}
		return true;
	}
	
	private List<FileIdentifier> showSaveDialog(String title, String message, List<IEditor> editorsToBeSaved) {
		if(editorsToBeSaved.isEmpty()) return Collections.emptyList();
		String selectAll = bundle.getString(LanguageConstants.DIALOG_BUTTON_SELECT_ALL);
		String deselectAll = bundle.getString(LanguageConstants.DIALOG_BUTTON_DESELECT_ALL);
		String ok = bundle.getString(LanguageConstants.DIALOG_BUTTON_OK);
		String cancel = bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
		String alwaysSave = bundle.getString(LanguageConstants.DIALOG_SAVE_CHECKBOX_ALWAYS_SAVE_RESOURCES);
		
		SaveDialog dialog = new SaveDialog(this, true);
		dialog.setTitle(title);
		dialog.setText(message);
		dialog.setOKButtonText(ok);
		dialog.setCancelButtonText(cancel);
		dialog.setSelectAllButtonText(selectAll);
		dialog.setDeselectAllButtonText(deselectAll);
		dialog.setAlwaysSaveCheckBoxText(alwaysSave);
		for(IEditor editor : editorsToBeSaved) {
			dialog.addItem(true, editor.getProjectName(), editor.getFileName());
		}
		dialog.startDialog();
		// control locked until user clicks on OK, CANCEL or CLOSE button
		
		boolean shouldAutoSave = dialog.shouldAlwaysSaveResources();
		if(shouldAutoSave) {
			try {
				Preferences pref = getPreferences(FileTypes.FT_APPLET);
				SingleOption singleOption = pref.getOption(UserFileConstants.APPLET_SAVE_DIALOG_ALWAYS_SAVE_RESOURCES);
				singleOption.setChosenValue(String.valueOf(shouldAutoSave));
				savePreferences(pref);
			} catch (UniformAppletException e) {}
		}
		int option = dialog.getOption();
		if(option != SaveDialog.OK_OPTION) return null;
		else return dialog.getSelectedResources();
	}
	
	private FileIdentifier showRunDialog(String title, String listTitle, int dialogType) {
		String ok = bundle.getString(LanguageConstants.DIALOG_BUTTON_OK);
		String cancel = bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
		String currentProjectTitle = bundle.getString(LanguageConstants.DIALOG_RUN_CURRENT_PROJECT_TITLE);
		String changeCurrentProjectButton = bundle.getString(LanguageConstants.DIALOG_RUN_CHANGE_CURRENT_PROJECT_BUTTON);
		String projectName = getSelectedProject();
		String currentProjectLabel;
		if(projectName == null) {
			currentProjectLabel = bundle.getString(LanguageConstants.DIALOG_RUN_ACTIVE_PROJECT_LABEL_NO_ACTIVE_PROJECT);
		} else {
			currentProjectLabel = bundle.getString(LanguageConstants.DIALOG_RUN_CURRENT_PROJECT_LABEL);
			currentProjectLabel = Utilities.replacePlaceholders(currentProjectLabel, new String[] {projectName});
		}
		
		RunDialog dialog = new RunDialog(this, true, this, dialogType);
		dialog.setTitle(title);
		dialog.setCurrentProjectTitle(currentProjectTitle);
		dialog.setChangeProjectButtonText(changeCurrentProjectButton);
		dialog.setCurrentProjectText(currentProjectLabel);
		dialog.setListTitle(listTitle);
		dialog.setOKButtonText(ok);
		dialog.setCancelButtonText(cancel);
		dialog.startDialog();
		// control locked until user clicks on OK, CANCEL or CLOSE button
		
		return dialog.getSelectedFile();
	}
	
	private String showCreateProjectDialog(String title, String message) {
		String ok = bundle.getString(LanguageConstants.DIALOG_BUTTON_OK);
		String cancel = bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
		Object[] options = new Object[] {ok, cancel};
		
		//String projectName = (String) JOptionPane.showInputDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
		String projectName = (String) JOptionPane.showInputDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION);
		/*try {
			if(projectName != null && communicator.existsProject(projectName)) {
				return null;
			}
		} catch (UniformAppletException e) {
		}*/
		return projectName;
	}
	
	/**
	 * A collection of utility methods for MainApplet.
	 *  
	 * @author Miro Bezjak
	 */
	private static class Utilities {
		
		/**
		 * Constructs FileIdentifiers out of <code>editors</code>.
		 * @param editors editors to construct FileIdentifiers from
		 * @return FileIdentifiers constructed out of <code>editors</code>
		 */
		public static List<FileIdentifier> convertEditorsToFileIdentifers(List<IEditor> editors) {
			if(editors == null) return null;
			List<FileIdentifier> files = new ArrayList<FileIdentifier>();
			for(IEditor e : editors) {
				String projectName = e.getProjectName();
				String fileName = e.getFileName();
				FileIdentifier f = new FileIdentifier(projectName, fileName);
				files.add(f);
			}
			return files;
		}
		
		/**
		 * Replace placeholders in <code>message</code> with <code>replacements</code>
		 * string. Look at <code>Client_Main_ApplicationResources_en.properties</code>
		 * file to learn what placeholders are.
		 * @param message a message from where to replace placeholders
		 * @param replacements an array of string to replace placeholders 
		 * @return modified message
		 */
		public static String replacePlaceholders(String message, String[] replacements) {
			if(replacements == null) return message;
			String replaced = message;
			int i = 0; // placeholders starts with 0
			for(String s : replacements) {
				replaced = replaced.replace("{" + i + "}", s);
				i++;
			}
			return replaced;
		}
		
	}

	private class ServerInitData {
		
		private static final String userId = "uid:id-not-set";
		
		public void writeGlobalFiles() {
			try {
				AjaxMediator ajax = new DefaultAjaxMediator(MainApplet.this);
				MethodInvoker invoker = new DefaultMethodInvoker(ajax);
				
				Long fileId = invoker.createUserFile(userId, FileTypes.FT_COMMON);
				Preferences preferences = new Preferences();
				List<String> values = new ArrayList<String>();
				values.add("en");
				values.add("hr");
				SingleOption o = new SingleOption(UserFileConstants.COMMON_LANGUAGE, "Language", "String", values, "en", "en");
				preferences.setOption(o);
				invoker.saveUserFile(fileId, preferences.serialize());
				
				preferences = new Preferences();
				fileId = invoker.createUserFile(userId, FileTypes.FT_APPLET);
				o = new SingleOption(UserFileConstants.APPLET_PROJECT_EXPLORER_WIDTH, "PE width", "Double", null, "0.15", "0.15");
				preferences.setOption(o);
				
				o = new SingleOption(UserFileConstants.APPLET_SIDEBAR_WIDTH, "Sidebar width", "Double", null, "0.75", "0.75");
				preferences.setOption(o);

				o = new SingleOption(UserFileConstants.APPLET_VIEW_HEIGHT, "View height", "Double", null, "0.75", "0.75");
				preferences.setOption(o);
				
				values = new ArrayList<String>();
				values.add("true");
				values.add("false");
				o = new SingleOption(UserFileConstants.APPLET_SAVE_DIALOG_ALWAYS_SAVE_RESOURCES, "Always save resources", "Boolean", values, "false", "false");
				preferences.setOption(o);
				
				invoker.saveUserFile(fileId, preferences.serialize());
			} catch (UniformAppletException e) {
				e.printStackTrace();
			}
		}
		
		public void writeServerInitData() {
			try {
				final String projectName1 = "Project1";
				final String fileName1 = "File1";
				final String fileType1 = FileTypes.FT_VHDL_SOURCE;
				final String fileContent1 = "simple content";
				final String fileName2 = "mux41";
				final String fileType2 = FileTypes.FT_VHDL_SOURCE;
				final String fileContent2 = "library IEEE;" + "\n" +
											"use IEEE.STD_LOGIC_1164.ALL;" + "\n" + 
											"\n" +
											"entity mux41 is" + "\n" +
											"port ( e :in std_logic;" + "\n" +
											"d:in std_logic_vector (3 downto 0);" + "\n" +
											"sel :in std_logic_vector (1 downto 0);" + "\n" +
											"z :out std_logic);" + "\n" +
											//");" + "\n" +
											"end mux41;" + "\n" +
											"" + "\n" +
											"architecture Behavioral of mux41 is" + "\n" +
											"" + "\n" +
											"begin" + "\n" +
											"process(d,e,sel)" + "\n" +
											"begin" + "\n" +
											"if (e = '1')then" + "\n" + 
											"case sel is" + "\n" +
											"when  \"00\" => z <= d(0);" + "\n" +
											"when  \"01\" => z <= d(1);" + "\n" +
											"when  \"10\" => z <= d(2);" + "\n" +
											"when  \"11\" => z <= d(3);" + "\n" +
											"when others => z <='0';" + "\n" +
											"end case;" + "\n" +
											"else z<='0';" + "\n" +
											"end if;" + "\n" +
											"end process;" + "\n" +
											"end Behavioral;";
				
				final String fileName3 = "mux41_tb";
				final String fileType3 = FileTypes.FT_VHDL_TB;
				final String fileContent3 = "<file>mux41</file>" + "\n" + 
											"<measureUnit>ns</measureUnit>" + "\n" +
											"<duration>700</duration>" + "\n" +
											"<signal name=\"E\" type=\"scalar\">(0,1)</signal>" + "\n" + 
											"<signal name=\"D\" type=\"vector\" rangeFrom=\"3\" rangeTo=\"0\">(0,0000)(20,1000)(30,1100)(40,1110)(50,1111)(60,0111)(65,0101)(70,0001)(90,0000)(95,0010)(120,1110)(135,1100)(150,0100)(155,0001)(180,0000)(190,1010)(230,0010)(235,0110)(245,0100)(255,0101)(265,1101)(295,1001)(315,1000)(325,1010)(330,0010)(340,0110)(360,1111)(375,1101)(380,1001)(385,1000)</signal>" + "\n" + 
											"<signal name=\"SEL\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"0\">(0,00)(25,10)(35,11)(70,10)(85,00)(130,01)(165,11)(195,10)(200,00)(250,01)(260,00)(285,10)(310,11)(320,10)(350,00)(360,01)(385,00)(395,10)(410,00)(415,01)(430,00)</signal>" + "\n";
				
				final String fileName4 = "Automat1";
				final String fileType4 = FileTypes.FT_VHDL_AUTOMAT;
				final String fileContent4 = "<Automat>" + "\n" +
	"<Podatci_Sklopa>" + "\n" +
		"<Ime>Automat1</Ime>" + "\n" +
		"<Tip>Moore</Tip>" + "\n" +
		"<Interfac>a in std_logic" + "\n" +
"b out std_logic" + "\n" +
		"</Interfac>" + "\n" +
		"<Pocetno_Stanje>A</Pocetno_Stanje>" + "\n" +
		"<Reset>1</Reset>" + "\n" + 
		"<Clock>falling_edge</Clock>" + "\n" +
	"</Podatci_Sklopa>" + "\n" +

	"<Stanje>" + "\n" +
		"<Ime>A</Ime>" + "\n" +
		"<Izlaz>0</Izlaz>" + "\n" +
		"<Ox>30</Ox>" + "\n" +
		"<Oy>30</Oy>" + "\n" +
	"</Stanje>" + "\n" +

	"<Stanje>" + "\n" +
		"<Ime>B</Ime>" + "\n" +
		"<Izlaz>1</Izlaz>" + "\n" +
		"<Ox>100</Ox>" + "\n" +
		"<Oy>100</Oy>" + "\n" +
	"</Stanje>" + "\n" +

	"<Stanje>" + "\n" +
		"<Ime>C</Ime>" + "\n" +
		"<Izlaz>1</Izlaz>" + "\n" +
		"<Ox>30</Ox>" + "\n" +
		"<Oy>100</Oy>" + "\n" +
	"</Stanje>" + "\n" +

	"<Prijelaz>" + "\n" +
		"<Iz>A</Iz>" + "\n" +
		"<U>B</U>" + "\n" +
		"<Pobuda>1</Pobuda>" + "\n" +
	"</Prijelaz>" + "\n" +

	"<Prijelaz>" + "\n" +
		"<Iz>A</Iz>" + "\n" +
		"<U>C</U>" + "\n" +
		"<Pobuda>0</Pobuda>" + "\n" +
	"</Prijelaz>" + "\n" +

	"<Prijelaz>" + "\n" +
		"<Iz>B</Iz>" + "\n" +
		"<U>A</U>" + "\n" +
		"<Pobuda>1</Pobuda>" + "\n" +
	"</Prijelaz>" + "\n" +

	"<Prijelaz>" + "\n" +
		"<Iz>B</Iz>" + "\n" +
		"<U>C</U>" + "\n" +
		"<Pobuda>0</Pobuda>" + "\n" +
	"</Prijelaz>" + "\n" +

	"<Prijelaz>" + "\n" +
		"<Iz>C</Iz>" + "\n" +
		"<U>C</U>" + "\n" +
		"<Pobuda>0</Pobuda>" + "\n" +
	"</Prijelaz>" + "\n" +

	"<Prijelaz>" + "\n" +
		"<Iz>C</Iz>" + "\n" +
		"<U>B</U>" + "\n" +
		"<Pobuda>1</Pobuda>" + "\n" +
	"</Prijelaz>" + "\n" +
"</Automat>";
				
				long start = System.currentTimeMillis();
				IEditor editor = communicator.getEditor(FileTypes.FT_VHDL_SOURCE);
				editor.setProjectContainer(MainApplet.this);
				editor.init();
				
				if(!communicator.existsProject(projectName1)) {
					communicator.createProject(projectName1);
				}
				if(!communicator.existsFile(projectName1,fileName1)) {
					communicator.createFile(projectName1, fileName1, fileType1);
					communicator.saveFile(projectName1, fileName1, fileContent1);
				}
				
				if(!communicator.existsFile(projectName1,fileName2)) {
					communicator.createFile(projectName1, fileName2, fileType2);
					communicator.saveFile(projectName1, fileName2, fileContent2);
				}

				if(!communicator.existsFile(projectName1,fileName3)) {
					communicator.createFile(projectName1, fileName3, fileType3);
					communicator.saveFile(projectName1, fileName3, fileContent3);
				}
				
				if(!communicator.existsFile(projectName1,fileName4)) {
					communicator.createFile(projectName1, fileName4, fileType4);
					communicator.saveFile(projectName1, fileName4, fileContent4);
				}
				
				long end = System.currentTimeMillis();
				
				String infoData = editor.getData()+(start-end)+"ms\nLoaded Preferences:\n"+communicator.getPreferences(FileTypes.FT_COMMON).serialize();
				infoData = infoData.replace("&lt;", "<");
				infoData = infoData.replace("&gt;", ">");
				communicator.saveFile(projectName1, fileName1, infoData);

				openEditor(projectName1, fileName1, true, false);
				openEditor(projectName1, fileName2, true, false);
				openEditor(projectName1, fileName3, true, false);
				openEditor(projectName1, fileName4, true, false);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}