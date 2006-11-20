package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.vhdllab.applets.main.dummy.ProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.dummy.SideBar;
import hr.fer.zemris.vhdllab.applets.main.dummy.StatusBar;
import hr.fer.zemris.vhdllab.applets.main.dummy.StatusExplorer;
import hr.fer.zemris.vhdllab.applets.main.dummy.Writer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainter;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 * 
 * 
 * @author Miro Bezjak
 */
public class MainApplet
		extends JApplet
		implements ProjectContainter {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private Cache cache;
	private MethodInvoker invoker;
	private AjaxMediator ajax;
	private ResourceBundle bundle;
	
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private StatusBar statusBar;
	
	private ProjectExplorer projectExplorer;
	private Writer writer;
	private StatusExplorer statusExplorer;
	private SideBar sideBar;
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		super.init();
		ajax = new DefaultAjaxMediator(this);
		invoker = new DefaultMethodInvoker(ajax);
		
		//**********************
		ServerInitData server = new ServerInitData();
		server.init(this);
		server.writeGlobalFiles();
		//**********************
		
		cache = new Cache(invoker);
		bundle = CachedResourceBundles.getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN,
				LanguageConstants.LANGUAGE_EN, null);
		initGUI();
		
		//**********************
		server.writeServerInitData();
		//**********************
	
		List<String> projects = cache.findProjects();
		for(String projectName : projects) {
			projectExplorer.addProject(projectName);
			List<String> files = cache.findFilesByProject(projectName);
			for(String fileName : files) {
				projectExplorer.addFile(projectName, fileName);
			}
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
		cache = null;
		invoker = null;
		//ajax.initiateAbort();
		ajax = null;
		this.setJMenuBar(null);
		this.getContentPane().removeAll();
		this.repaint();
		super.destroy();
	}
	
	private void initGUI() {
		JPanel topContainerPanel = new JPanel(new BorderLayout());
		menuBar = new PrivateMenuBar(bundle).setup();
		this.setJMenuBar( menuBar );
		
		toolBar = new PrivateToolBar(bundle).setup();
		topContainerPanel.add(toolBar, BorderLayout.NORTH);
		topContainerPanel.add(setupStatusBar(), BorderLayout.SOUTH);
		topContainerPanel.add(setupCenterPanel(), BorderLayout.CENTER);
		
		this.add(topContainerPanel, BorderLayout.CENTER);
	}
	
	private JPanel setupStatusBar() {
		statusBar = new StatusBar();
		JPanel statusBarPanel = new JPanel(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);
		statusBarPanel.setPreferredSize(new Dimension(0, 24));

		return statusBarPanel;
	}
	
	private JPanel setupCenterPanel() {
		projectExplorer = new ProjectExplorer();
		projectExplorer.setProjectContainer(this);
		JPanel projectExplorerPanel = new JPanel(new BorderLayout());
		projectExplorerPanel.add(projectExplorer, BorderLayout.CENTER);
		projectExplorerPanel.setPreferredSize(new Dimension(this.getWidth()/3, 0));
		
		JPanel writerPanel = new JPanel(new BorderLayout());
		writer = new Writer();
		writerPanel.add(writer, BorderLayout.CENTER);
		
		JPanel statusExplorerPanel = new JPanel(new BorderLayout());
		statusExplorer = new StatusExplorer();
		statusExplorerPanel.add(statusExplorer, BorderLayout.CENTER);
		statusExplorerPanel.setPreferredSize(new Dimension(0, this.getHeight()/3));
		
		JPanel sideBarPanel = new JPanel(new BorderLayout());
		sideBar = new SideBar();
		sideBarPanel.add(sideBar, BorderLayout.CENTER);
		sideBarPanel.setPreferredSize(new Dimension(this.getWidth()/3, 0));
		
		JSplitPane sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, writerPanel, sideBarPanel);
		JSplitPane projectExporerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorerPanel, sideBarSplitPane);
		JSplitPane statusExplorerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectExporerSplitPane, statusExplorerPanel);
		
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
		
		/**
		 * Creates and instantiates menu bar.
		 * 
		 * @return created menu bar
		 */
		public JMenuBar setup() {

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
			
			// New File menu item
			key = LanguageConstants.MENU_FILE_NEW_FILE;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createNewFileInstance(File.FT_VHDLSOURCE);
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
					saveFileInstance(File.FT_VHDLSOURCE);
				}
			});
			menu.add(menuItem);

			// Save All menu item
			key = LanguageConstants.MENU_FILE_SAVE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menu.add(menuItem);
			menu.addSeparator();
			
			// Close menu item
			key = LanguageConstants.MENU_FILE_CLOSE;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menu.add(menuItem);

			// Close All menu item
			key = LanguageConstants.MENU_FILE_CLOSE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
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
				if(temp.length() == 1) {
					menu.setMnemonic(temp.codePointAt(0));
				}
			} catch (RuntimeException e) {}
			
			// Set accelerator
			try {
				String temp = bundle.getString(key + LanguageConstants.ACCELERATOR_APPEND);
				String[] keys = temp.split("[+]");
				if(keys.length != 0) {
					int mask = 0;
					int keyCode = 0;
					for(String k : keys) {
						k = k.trim();
						if(k.equals("ctrl")) mask += KeyEvent.CTRL_DOWN_MASK;
						else if(k.equals("alt")) mask += KeyEvent.ALT_DOWN_MASK;
						else if(k.equals("shift")) mask += KeyEvent.SHIFT_DOWN_MASK;
						else if(k.length() == 1) keyCode = k.toUpperCase().codePointAt(0);
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
	
	
	public List<String> getAllCircuits() {
		// TODO Auto-generated method stub
		return null;
	}

	public CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) {
		return cache.getCircuitInterfaceFor(projectName, fileName);
	}

	public String getOptions() {
		return cache.getOptions();
	}

	public ResourceBundle getResourceBundle() {
		return bundle;
	}

	public void openFile(String projectName, String fileName) {
		String content = cache.loadFileContent(projectName, fileName);
		FileContent fileContent = new FileContent(projectName, fileName, content);
		writer.setFileContent(fileContent);
	}
	
	private void createNewFileInstance(String type) {
		if(type.equals(File.FT_VHDLSOURCE)) {
			writer.setupWizard();
			FileContent content = writer.getInitialFileContent();
			String projectName = content.getProjectName();
			String fileName = content.getFileName();
			cache.createFile(projectName, fileName, type);
			projectExplorer.addFile(projectName, fileName);
		} else return;
	}
	
	private void saveFileInstance(String type) {
		if(type.equals(File.FT_VHDLSOURCE)) {
			if(!writer.isModified()) return;
			String fileName = writer.getFileName();
			String projectName = writer.getProjectName();
			String content = writer.getData();
			cache.saveFile(projectName, fileName, content);
		} else return;
	}


	private class ServerInitData {
		
		public void init(Applet applet) {
			applet.setSize(new Dimension(1000,650));
		}
		
		public void writeGlobalFiles() {
			try {
				Long fileId = invoker.createGlobalFile("applet", GlobalFile.GFT_APPLET);
				invoker.saveGlobalFile(fileId, "a=2a\nb=22");
				fileId = invoker.createGlobalFile("tema", GlobalFile.GFT_THEME);
				invoker.saveGlobalFile(fileId, "neka = random\ntema=za testiranje");
			} catch (AjaxException e) {
				e.printStackTrace();
			}
		}
		
		public void writeServerInitData() {
			try {
				long start = System.currentTimeMillis();
				Long projectId = invoker.createProject("Project1", Long.valueOf(0));
				Long fileId = invoker.createFile(projectId, "File1", File.FT_VHDLSOURCE);
				invoker.saveFile(fileId, "simple content");
				fileId = invoker.createFile(projectId, "File2", File.FT_VHDLSOURCE);
				invoker.saveFile(fileId, "some file content that should be displayed in writer");
				
				
				FileContent content = new FileContent(invoker.loadProjectName(projectId), invoker.loadFileName(fileId), invoker.loadFileContent(fileId));
				writer.setFileContent(content);
				
				long end = System.currentTimeMillis();
				content = new FileContent("", "", writer.getData()+(start-end)+"\nLoaded Options:\n"+cache.getOptions());
				writer.setFileContent(content);
			} catch (AjaxException e) {
				e.printStackTrace();
			}
		}
	}
}