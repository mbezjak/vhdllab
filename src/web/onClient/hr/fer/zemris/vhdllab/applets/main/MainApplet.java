package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.vhdllab.applets.main.constants.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.constants.ViewTypes;
import hr.fer.zemris.vhdllab.applets.main.dummy.ProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.dummy.SideBar;
import hr.fer.zemris.vhdllab.applets.main.dummy.StatusExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.FileIdentifier;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.simulations.WaveApplet;
import hr.fer.zemris.vhdllab.applets.statusbar.StatusBar;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.file.options.Options;
import hr.fer.zemris.vhdllab.file.options.SingleOption;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.string.StringUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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
 * 
 * 
 * @author Miro Bezjak
 */
public class MainApplet
		extends JApplet
		implements ProjectContainer {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private Cache cache;
	private ResourceBundle bundle;
	
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private IStatusBar statusBar;
	private JTabbedPane editorPane;
	private JTabbedPane viewPane;
	
	private JPanel centerPanel;
	private JPanel normalCenterPanel;
	
	private ProjectExplorer projectExplorer;
	private StatusExplorer statusExplorer;
	private SideBar sideBar;
	
	private JPanel projectExplorerPanel;
	private JPanel statusExplorerPanel;
	private JPanel sideBarPanel;
	
	private JSplitPane projectExplorerSplitPane;
	private JSplitPane statusExplorerSplitPane;
	private JSplitPane sideBarSplitPane;
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		super.init();
		String userId = this.getParameter("userId");
		if(userId==null) {
			// We must not enter this! If we do, applet should refuse to run!
			// Until then:
			userId = "uid:id-not-set";
		}
		AjaxMediator ajax = new DefaultAjaxMediator(this);
		MethodInvoker invoker = new DefaultMethodInvoker(ajax);
		
		//**********************
		ServerInitData server = new ServerInitData();
		server.init(this);
		server.writeGlobalFiles();
		//**********************
		
		cache = new Cache(invoker, userId);
		String language = null;
		try {
			String data = cache.getUserFile(FileTypes.FT_COMMON);
			Options options = Options.deserialize(data);
			SingleOption commonOptions = options.getOption(UserFileConstants.COMMON_LANGUAGE);
			language = commonOptions.getSelectedValue();
		} catch (UniformAppletException e) {
			statusBar.setText(bundle.getString(LanguageConstants.STATUSBAR_LANGUAGE_SETTING_NOT_FOUND));
			language = "en";
		}
		bundle = CachedResourceBundles.getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN,
				language, null);
		initGUI();
		
		//**********************
		server.writeServerInitData();
		//**********************
		
		try {
			List<String> projects = cache.findProjects();
			for(String projectName : projects) {
				projectExplorer.addProject(projectName);
				List<String> files = cache.findFilesByProject(projectName);
				for(String fileName : files) {
					projectExplorer.addFile(projectName, fileName);
				}
			}
			
			String statusBarText = bundle.getString(LanguageConstants.STATUSBAR_LOAD_COMPLETE);
			statusBar.setText(statusBarText);
		} catch (UniformAppletException e) {
			String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_LOAD_WORKSPACE);
			statusBar.setText(text);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#start()
	 */
	@Override
	public void start() {
		super.start();
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
		closeAllEditors();
		cache = null;
		this.setJMenuBar(null);
		this.getContentPane().removeAll();
		this.repaint();
		super.destroy();
	}
	
	private void initGUI() {
		JPanel topContainerPanel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(new BorderLayout());
		menuBar = new PrivateMenuBar(bundle).setupMainMenu();
		this.setJMenuBar( menuBar );
		
		toolBar = new PrivateToolBar(bundle).setup();
		topContainerPanel.add(toolBar, BorderLayout.NORTH);
		topContainerPanel.add(setupStatusBar(), BorderLayout.SOUTH);
		normalCenterPanel = setupCenterPanel();
		centerPanel.add(normalCenterPanel);
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
		projectExplorer = new ProjectExplorer();
		projectExplorer.setProjectContainer(this);
		//JPanel projectExplorerPanel = new JPanel(new BorderLayout());
		projectExplorerPanel = new JPanel(new BorderLayout());
		projectExplorerPanel.add(projectExplorer, BorderLayout.CENTER);
		projectExplorerPanel.setPreferredSize(new Dimension(this.getWidth()/3, 0));
		
		editorPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		editorPane.setComponentPopupMenu(new PrivateMenuBar(bundle).setupPopupMenuForEditors());
		editorPane.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					maximizeActiveComponent(editorPane);
					/*centerPanel.remove(normalCenterPanel);
					centerPanel.add(editorPane, BorderLayout.CENTER);
					centerPanel.repaint();*/
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
		//JPanel statusExplorerPanel = new JPanel(new BorderLayout());
		statusExplorerPanel = new JPanel(new BorderLayout());
		statusExplorer = new StatusExplorer();
		statusExplorerPanel.add(statusExplorer, BorderLayout.CENTER);
		statusExplorerPanel.setPreferredSize(new Dimension(0, this.getHeight()/3));
		
		//JPanel sideBarPanel = new JPanel(new BorderLayout());
		sideBarPanel = new JPanel(new BorderLayout());
		sideBar = new SideBar();
		sideBarPanel.add(sideBar, BorderLayout.CENTER);
		sideBarPanel.setPreferredSize(new Dimension(this.getWidth()/3, 0));
		
		//JSplitPane sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPane, sideBarPanel);
		//JSplitPane projectExporerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorerPanel, sideBarSplitPane);
		//JSplitPane statusExplorerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectExporerSplitPane, statusExplorerPanel);
		
		sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPane, sideBarPanel);
		projectExplorerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorerPanel, sideBarSplitPane);
		statusExplorerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectExplorerSplitPane, statusExplorerPanel);
		
		//******************
		sideBarSplitPane.setDividerLocation(650);
		projectExplorerSplitPane.setDividerLocation(110);
		statusExplorerSplitPane.setDividerLocation(500);
		
		//******************
		
		JPanel centerComponentsPanel = new JPanel(new BorderLayout());
		centerComponentsPanel.add(statusExplorerSplitPane);
		
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
		 * @param bundle {@link ResourceBundle} that contains information about menus
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
					closeEditor((IEditor)editorPane.getSelectedComponent());
				}
			});
			menuBar.add(menuItem);

			// Close other editors
			key = LanguageConstants.MENU_FILE_CLOSE_OTHER;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllButThisEditor((IEditor)editorPane.getSelectedComponent());
				}
			});
			menuBar.add(menuItem);
			
			// Close all editors
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllEditors();
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
			setMnemonicAndAccelerator(menu, key);
			
			// New sub menu
			key = LanguageConstants.MENU_FILE_NEW;
			submenu = new JMenu(bundle.getString(key));
			setMnemonicAndAccelerator(submenu, key);

			// New Project menu item
			key = LanguageConstants.MENU_FILE_NEW_PROJECT;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			submenu.add(menuItem);
			submenu.addSeparator();
			
			// New VHDL Source menu item
			key = LanguageConstants.MENU_FILE_NEW_VHDL_SOURCE;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						createNewFileInstance(FileTypes.FT_VHDLSOURCE);
					} catch (UniformAppletException e1) {}
				}
			});
			submenu.add(menuItem);
			
			// New Testbench menu item
			key = LanguageConstants.MENU_FILE_NEW_TESTBENCH;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			submenu.add(menuItem);
			
			// New Schema menu item
			key = LanguageConstants.MENU_FILE_NEW_SCHEMA;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			submenu.add(menuItem);
			
			// New Automat menu item
			key = LanguageConstants.MENU_FILE_NEW_AUTOMAT;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			submenu.add(menuItem);
			
			menu.add(submenu);
			
			// Open menu item
			key = LanguageConstants.MENU_FILE_OPEN;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menu.add(menuItem);
			menu.addSeparator();
			
			// Save menu item
			key = LanguageConstants.MENU_FILE_SAVE;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveEditor((IEditor)editorPane.getSelectedComponent());
				}
			});
			menu.add(menuItem);

			// Save All menu item
			key = LanguageConstants.MENU_FILE_SAVE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
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
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeEditor((IEditor)editorPane.getSelectedComponent());
				}
			});
			menu.add(menuItem);

			// Close All menu item
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeAllEditors();
				}
			});
			menu.add(menuItem);
			menu.addSeparator();

			// Exit menu item
			key = LanguageConstants.MENU_FILE_EXIT;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
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
			setMnemonicAndAccelerator(menu, key);
			menuBar.add(menu);
			
			// View menu
			key = LanguageConstants.MENU_VIEW;
			menu = new JMenu(bundle.getString(key));
			setMnemonicAndAccelerator(menu, key);
			menuBar.add(menu);

			// Tool menu
			key = LanguageConstants.MENU_TOOLS;
			menu = new JMenu(bundle.getString(key));
			setMnemonicAndAccelerator(menu, key);
			
			// Compile menu item
			key = LanguageConstants.MENU_TOOLS_COMPILE;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						compileLastHistoryResult();
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_COMPILE);
						statusBar.setText(text);
					}
				}
			});
			menu.add(menuItem);
			
			// Simulate menu item
			key = LanguageConstants.MENU_TOOLS_SIMULATE;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						simulateLastHistoryResult();
					} catch (UniformAppletException ex) {
						String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_SIMULATE);
						statusBar.setText(text);
					}
				}
			});
			menu.add(menuItem);

			
			menuBar.add(menu);

			// Help menu
			key = LanguageConstants.MENU_HELP;
			menu = new JMenu(bundle.getString(key));
			setMnemonicAndAccelerator(menu, key);
			menuBar.add(menu);
			
			return menuBar;
		}

		/**
		 * Sets mnemonic and accelerator for a given menu. If keys for mnemonic
		 * or accelerator (or both) does not exists then they will simply be ignored.
		 * 
		 * @param menu a menu where to set mnemonic and accelerator
		 * @param key a key containing menu's name 
		 */
		private void setMnemonicAndAccelerator(JMenuItem menu, String key) {
			/**
			 * For locating mnemonic or accelerator of a <code>menu</code> this method
			 * will simply append appropriate string to <code>key</code>.
			 * Information regarding such strings that will be appended can be found here:
			 * <ul>
			 * <li>LanguageConstants.MNEMONIC_APPEND</li>
			 * <li>LanguageConstants.ACCELERATOR_APPEND</li>
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
	
	private void compileLastHistoryResult() throws UniformAppletException {
		if(cache.compilationHistoryIsEmpty()) {
			//TODO run compilation wizard
			String projectName = JOptionPane.showInputDialog("In which project is a file?");
			String fileName = JOptionPane.showInputDialog("Which file to simulate?");
			simulate(projectName, fileName);
			
		}
		FileIdentifier file = cache.getLastCompilationHistoryTarget();
		compile(file.getProjectName(), file.getFileName());
	}
	
	public void compile(String projectName, String fileName) throws UniformAppletException {
		List<IEditor> openedEditors = getAllOpenEditors();
		List<IEditor> notSavedEditors = new ArrayList<IEditor>();
		for(IEditor e : openedEditors) {
			if(e.isSavable() && e.isModified() &&
					projectName.equals(e.getProjectName())) {
				notSavedEditors.add(e);
			}
		}
		
		if(notSavedEditors.size() != 0) {
			String title = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_RESOURCES_FOR_COMPILATION_TITLE);
			String message = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE);
			int option = showSaveDialog(title, message);
			
			if(option == JOptionPane.YES_OPTION) {
				saveEditors(notSavedEditors);
			} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		
		CompilationResult result = cache.compile(projectName, fileName);
		IView view = cache.getView(ViewTypes.VT_COMPILATION_ERRORS);
		view.setProjectContainer(this);
		view.setData(result);
		// TODO implement do kraja
	}
	
	private void simulateLastHistoryResult() throws UniformAppletException {
		if(cache.simulationHistoryIsEmpty()) {
			//TODO run simulation wizard
			String projectName = JOptionPane.showInputDialog("In which project is a file?");
			String fileName = JOptionPane.showInputDialog("Which file to simulate?");
			simulate(projectName, fileName);
			
		}
		FileIdentifier file = cache.getLastSimulationHistoryTarget();
		simulate(file.getProjectName(), file.getFileName());
	}
	
	public void simulate(String projectName, String fileName) throws UniformAppletException {
		List<IEditor> openedEditors = getAllOpenEditors();
		List<IEditor> notSavedEditors = new ArrayList<IEditor>();
		for(IEditor e : openedEditors) {
			if(e.isSavable() && e.isModified() &&
					projectName.equals(e.getProjectName())) {
				notSavedEditors.add(e);
			}
		}
		
		if(notSavedEditors.size() != 0) {
			String title = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_RESOURCES_FOR_SIMULATION_TITLE);
			String message = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE);
			int option = showSaveDialog(title, message);
			
			if(option == JOptionPane.YES_OPTION) {
				saveEditors(notSavedEditors);
			} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		
		SimulationResult result = cache.runSimulation(projectName, fileName);
		String simulationName = fileName + ".sim";
		//cache.createFile(projectName, simulationName, FileTypes.FT_SIMULATION);
		//cache.saveFile(projectName, simulationName, result.getWaveform());
		//openEditor(projectName, simulationName, true, false);
		openSimulationEditor(projectName, simulationName, result.getWaveform());
	}
	
	public List<String> getAllCircuits(String projectName) throws UniformAppletException {
		List<String> fileNames = cache.findFilesByProject(projectName);
		List<String> circuits = new ArrayList<String>();
		for(String name : fileNames) {
			String type = cache.loadFileType(projectName, name);
			if(type.equals(FileTypes.FT_VHDLSOURCE)) {
				circuits.add(name);
			}
		}
		return circuits;
	}

	public CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) throws UniformAppletException {
		return cache.getCircuitInterfaceFor(projectName, fileName);
	}

	public String getUserFile(String type) throws UniformAppletException {
		return cache.getUserFile(type);
	}

	public ResourceBundle getResourceBundle() {
		return bundle;
	}
	
	private int indexOfEditor(String projectName, String fileName) {
		for(int i = 0; i < editorPane.getTabCount(); i++) {
			IEditor editor = (IEditor) editorPane.getComponentAt(i);
			if(projectName.equals(editor.getProjectName()) &&
					fileName.equals(editor.getFileName())) {
				return i;
			}
		}
		return -1;
	}
	
	private int indexOfEditor(IEditor editor) {
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		return indexOfEditor(projectName, fileName);
	}
	
	private List<IEditor> getAllOpenEditors() {
		List<IEditor> openedEditors = new ArrayList<IEditor>();
		for(int i = 0; i < editorPane.getTabCount(); i++) {
			IEditor editor = (IEditor) editorPane.getComponentAt(i);
			openedEditors.add(editor);
		}
		return openedEditors;
	}
	
	public void openEditor(String projectName, String fileName, boolean isSavable, boolean isReadOnly) throws UniformAppletException {
		String content = cache.loadFileContent(projectName, fileName);
		FileContent fileContent = new FileContent(projectName, fileName, content);
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) {
			String type = cache.loadFileType(projectName, fileName);
			
			// Initialization of an editor
			IEditor editor = cache.getEditor(type);
			editor.setProjectContainer(this);
			editor.setFileContent(fileContent);
			editor.setSavable(isSavable);
			editor.setReadOnly(isReadOnly);
			// End of initialization
			
			Component component = editorPane.add(fileName, (JPanel)editor);
			index = editorPane.indexOfComponent(component);
			String toolTipText = projectName + "/" + fileName;
			editorPane.setToolTipTextAt(index, toolTipText);
		}
		editorPane.setSelectedIndex(index);
	}
	
	private void openSimulationEditor(String projectName, String fileName, String content) throws UniformAppletException {
		FileContent fileContent = new FileContent(projectName, fileName, content);
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) {
			// Initialization of an editor
			IEditor editor = new WaveApplet();
			editor.setProjectContainer(this);
			editor.setFileContent(fileContent);
			editor.setSavable(false);
			editor.setReadOnly(true);
			// End of initialization
			
			Component component = editorPane.add(fileName, (JPanel)editor);
			index = editorPane.indexOfComponent(component);
			String toolTipText = projectName + "/" + fileName;
			editorPane.setToolTipTextAt(index, toolTipText);
		}
		editorPane.setSelectedIndex(index);
	}
	
	public void resetEditorTitle(boolean contentChanged, String projectName, String fileName) {
		String title = fileName;
		if(contentChanged) {
			title = "*" + title;
		}
		int index = indexOfEditor(projectName, fileName);
		if(index != -1)  {
			editorPane.setTitleAt(index, title);
		}
	}
	
	public boolean existsFile(String projectName, String fileName) throws UniformAppletException {
		return cache.existsFile(projectName, fileName);
	}
	
	public boolean existsProject(String projectName) throws UniformAppletException {
		return cache.existsProject(projectName);
	}
	
	private void createNewFileInstance(String type) throws UniformAppletException {
		// Initialization of a wizard
		IWizard wizard = cache.getEditor(type).getWizard();
		if(wizard == null) throw new NullPointerException("No wizard can not be null.");
		wizard.setProjectContainer(this);
		FileContent content = wizard.getInitialFileContent();
		// End of initialization
		
		if(content == null) return;
		String projectName = content.getProjectName();
		String fileName = content.getFileName();
		String data = content.getContent();
		cache.createFile(projectName, fileName, type);
		cache.saveFile(projectName, fileName, data);
		projectExplorer.addFile(projectName, fileName);
		openEditor(projectName, fileName, true, false);
	}
	
	public IEditor getEditor(String projectName, String fileName) throws UniformAppletException {
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) {
			openEditor(projectName, fileName, true, false);
			index = indexOfEditor(projectName, fileName);
		}
		return (IEditor)editorPane.getComponentAt(index);
	}
	
	private void maximizeActiveComponent(Component component) {
		if(normalCenterPanel.isVisible()) {
			centerPanel.remove(normalCenterPanel);
			centerPanel.add(component, BorderLayout.CENTER);
		} else {
			centerPanel.remove(component);
			centerPanel.add(normalCenterPanel, BorderLayout.CENTER);
		}
		centerPanel.repaint();
	}
	
	private void saveEditor(IEditor editor) {
		if(editor == null) return;
		List<IEditor> editorsToSave = new ArrayList<IEditor>();
		editorsToSave.add(editor);
		saveEditors(editorsToSave);
	}
	
	private void saveEditors(List<IEditor> editorsToSave) {
		if(editorsToSave == null) return;
		for(IEditor editor : editorsToSave) {
			if(editor.isSavable() && editor.isModified()) {
				String fileName = editor.getFileName();
				String projectName = editor.getProjectName();
				String content = editor.getData();
				try {
					cache.saveFile(projectName, fileName, content);
					resetEditorTitle(false, projectName, fileName);
				} catch (UniformAppletException e) {
					String text = bundle.getString(LanguageConstants.STATUSBAR_CANT_SAVE_FILE);
					text = replacePlaceholders(text, new String[] {fileName});
					statusBar.setText(text);
				}
			}
		}
	}

	private void saveAllEditors() {
		List<IEditor> openedEditors = getAllOpenEditors();
		saveEditors(openedEditors);
	}
	
	private void closeEditor(IEditor editor) {
		if(editor == null) return;
		List<IEditor> editorsToClose = new ArrayList<IEditor>();
		editorsToClose.add(editor);
		closeEditors(editorsToClose);
	}
	
	private void closeAllEditors() {
		List<IEditor> openedEditors = getAllOpenEditors();
		closeEditors(openedEditors);
	}
	
	private void closeAllButThisEditor(IEditor editorToKeepOpened) {
		if(editorToKeepOpened == null) return;
		List<IEditor> openedEditors = getAllOpenEditors();
		openedEditors.remove(editorToKeepOpened);
		closeEditors(openedEditors);
	}
	
	private void closeEditors(List<IEditor> editorsToClose) {
		if(editorsToClose == null) return;
		List<IEditor> notSavedEditors = new ArrayList<IEditor>();
		StringBuilder messageRepresentation = new StringBuilder(editorsToClose.size() * 10);
		for(IEditor editor : editorsToClose) {
			if(editor.isSavable() && editor.isModified()) {
				notSavedEditors.add(editor);
				messageRepresentation.append(editor.getFileName()).append(" [")
					.append(editor.getProjectName()).append("]\n");
			}
		}
		int lenght = messageRepresentation.length();
		if(lenght != 0) {
			messageRepresentation.replace(lenght-1, lenght, "");
		}
		
		if(notSavedEditors.size() != 0) {
			String title;
			String message;
			if(notSavedEditors.size() == 1) {
				title = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_SINGLE_RESOURCE_TITLE);
				message = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_SINGLE_RESOURCE_MESSAGE);
			} else {
				title = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_TITLE);
				message = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_MESSAGE);
			}
			message = replacePlaceholders(message, new String[] {messageRepresentation.toString()});
			int option = showSaveDialog(title, message);
			
			if(option == JOptionPane.YES_OPTION) {
				saveEditors(notSavedEditors);
			} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		
		for(IEditor editor : editorsToClose) {
			int index = indexOfEditor(editor);
			editorPane.remove(index);
		}
	}
	
	private int showSaveDialog(String title, String message) {
		String yes = bundle.getString(LanguageConstants.DIALOG_BUTTON_YES);
		String no = bundle.getString(LanguageConstants.DIALOG_BUTTON_NO);
		String cancel = bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
		Object[] options = {yes, no, cancel};
		int option = JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		return option;
	}
	
	private String replacePlaceholders(String message, String[] replacements) {
		if(replacements == null) return message;
		String replaced = message;
		int i = 0;
		for(String s : replacements) {
			replaced = replaced.replace("{" + i + "}", s);
			i++;
		}
		return replaced;
	}

	private class ServerInitData {
		
		public void init(Applet applet) {
			applet.setSize(new Dimension(1000,650));
		}
		
		public void writeGlobalFiles() {
			try {
				AjaxMediator ajax = new DefaultAjaxMediator(MainApplet.this);
				MethodInvoker invoker = new DefaultMethodInvoker(ajax);
				
				Long fileId = invoker.createUserFile("uid:id-not-set", FileTypes.FT_APPLET);
				invoker.saveUserFile(fileId, "a=2a\nb=22");
				fileId = invoker.createUserFile("uid:id-not-set", FileTypes.FT_THEME);
				invoker.saveUserFile(fileId, "neka = random\ntema=za testiranje");
				
				Long fileId2 = invoker.createUserFile("uid:id-not-set", FileTypes.FT_COMMON);
				List<String> values = new ArrayList<String>();
				values.add("en");
				values.add("hr");
				SingleOption o = new SingleOption("localization.language", "Language", "String", values, "en", "en");
				List<SingleOption> options = new ArrayList<SingleOption>();
				options.add(o);
				Options opt = new Options();
				opt.setOption(o);
				invoker.saveUserFile(fileId2, opt.serialize());
			} catch (UniformAppletException e) {
				e.printStackTrace();
			}
		}
		
		public void writeServerInitData() {
			try {
				final String projectName1 = "Project1";
				final String fileName1 = "File1";
				final String fileType1 = FileTypes.FT_VHDLSOURCE;
				final String fileContent1 = "simple content";
				final String fileName2 = "File2";
				final String fileType2 = FileTypes.FT_VHDLSOURCE;
				final String fileContent2 = "some file content that should be displayed in writer";
				
				long start = System.currentTimeMillis();
				IEditor editor = cache.getEditor("vhdl_source");
				
				cache.createProject(projectName1);
				cache.createFile(projectName1, fileName1, fileType1);
				cache.saveFile(projectName1, fileName1, fileContent1);
				
				cache.createFile(projectName1, fileName2, fileType2);
				cache.saveFile(projectName1, fileName2, fileContent2);

				long end = System.currentTimeMillis();
				
				String infoData = editor.getData()+(start-end)+"ms\nLoaded Options:\n"+cache.getUserFile(FileTypes.FT_COMMON);
				cache.saveFile(projectName1, fileName1, infoData);

				openEditor(projectName1, fileName1, true, false);
				openEditor(projectName1, fileName2, true, false);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}