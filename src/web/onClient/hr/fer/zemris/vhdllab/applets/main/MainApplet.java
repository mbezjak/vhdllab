package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.vhdllab.applets.main.dummy.ProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.dummy.SideBar;
import hr.fer.zemris.vhdllab.applets.main.dummy.StatusExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.statusbar.StatusBar;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	private StatusBar statusBar;
	private JTabbedPane editorPane;
	
	private ProjectExplorer projectExplorer;
	private StatusExplorer statusExplorer;
	private SideBar sideBar;
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		super.init();
		AjaxMediator ajax = new DefaultAjaxMediator(this);
		MethodInvoker invoker = new DefaultMethodInvoker(ajax);
		
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
		closeAllEditors();
		cache = null;
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
		
		editorPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		
		JPanel statusExplorerPanel = new JPanel(new BorderLayout());
		statusExplorer = new StatusExplorer();
		statusExplorerPanel.add(statusExplorer, BorderLayout.CENTER);
		statusExplorerPanel.setPreferredSize(new Dimension(0, this.getHeight()/3));
		
		JPanel sideBarPanel = new JPanel(new BorderLayout());
		sideBar = new SideBar();
		sideBarPanel.add(sideBar, BorderLayout.CENTER);
		sideBarPanel.setPreferredSize(new Dimension(this.getWidth()/3, 0));
		
		JSplitPane sideBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPane, sideBarPanel);
		JSplitPane projectExporerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorerPanel, sideBarSplitPane);
		JSplitPane statusExplorerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectExporerSplitPane, statusExplorerPanel);
		
		//******************
		sideBarSplitPane.setDividerLocation(650);
		projectExporerSplitPane.setDividerLocation(110);
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
					saveFileInstance(editorPane.getSelectedComponent());
				}
			});
			menu.add(menuItem);

			// Save All menu item
			key = LanguageConstants.MENU_FILE_SAVE_ALL;
			menuItem = new JMenuItem(bundle.getString(key));
			setMnemonicAndAccelerator(menuItem, key);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveAllFileInstances();
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
					closeEditor(editorPane.getSelectedComponent());
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
	
	
	public List<String> getAllCircuits(String projectName) {
		List<String> fileNames = cache.findFilesByProject(projectName);
		List<String> circuits = new ArrayList<String>();
		for(String name : fileNames) {
			String type = cache.loadFileType(projectName, name);
			if(type.equals(File.FT_VHDLSOURCE)) {
				circuits.add(name);
			}
		}
		return circuits;
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
	
	public void openFile(String projectName, String fileName) {
		String content = cache.loadFileContent(projectName, fileName);
		FileContent fileContent = new FileContent(projectName, fileName, content);
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) {
			String type = cache.loadFileType(projectName, fileName);
			IEditor editor = cache.getEditor(type);
			editor.setProjectContainer(this);
			editor.setFileContent(fileContent);
			Component component = editorPane.add(fileName, (JPanel)editor);
			int i = editorPane.indexOfComponent(component);
			String toolTipText = projectName + "/" + fileName;
			editorPane.setToolTipTextAt(i, toolTipText);
			index = editorPane.indexOfTab(fileName);
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
	
	public boolean existsFile(String projectName, String fileName) {
		return cache.existsFile(projectName, fileName);
	}
	
	public boolean existsProject(String projectName) {
		return cache.existsProject(projectName);
	}
	
	private void createNewFileInstance(String type) {
		IWizard wizard = cache.getWizard(type);
		wizard.setProjectContainer(this);
		FileContent content = wizard.getInitialFileContent();
		if(content == null) return;
		String projectName = content.getProjectName();
		String fileName = content.getFileName();
		String data = content.getContent();
		cache.createFile(projectName, fileName, type);
		cache.saveFile(projectName, fileName, data);
		projectExplorer.addFile(projectName, fileName);
		openFile(projectName, fileName);
	}
	
	public IEditor getEditor(String projectName, String fileName) {
		int index = indexOfEditor(projectName, fileName);
		return (IEditor)editorPane.getComponentAt(index);
	}
	
	private void saveFileInstance(Component component) {
		if(component == null) return;
		IEditor editor = (IEditor) component;
		String fileName = editor.getFileName();
		String projectName = editor.getProjectName();
		String content = editor.getData();
		cache.saveFile(projectName, fileName, content);
		resetEditorTitle(false, projectName, fileName);
	}

	private void saveAllFileInstances() {
		for(int i = 0; i < editorPane.getTabCount(); i++) {
			saveFileInstance(editorPane.getComponentAt(i));
		}
	}
	
	private void closeEditor(Component component) {
		IEditor editor = (IEditor) component;
		if(editor.isModified()) {
			String yes = bundle.getString(LanguageConstants.DIALOG_BUTTON_YES);
			String no = bundle.getString(LanguageConstants.DIALOG_BUTTON_NO);
			String cancel = bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
			Object[] options = {yes, no, cancel};
			String title = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_SINGLE_RESOURCE_TITLE);
			String message = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_SINGLE_RESOURCE_MESSAGE);
			message = message.replace("{0}", editor.getFileName());
			
			int option = JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if(option == JOptionPane.YES_OPTION) {
				String projectName = editor.getProjectName();
				String fileName = editor.getFileName();
				String data = editor.getData();
				cache.saveFile(projectName, fileName, data);
			} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		editorPane.remove(component);
	}
	
	private void closeAllEditors() {
		List<IEditor> notSavedEditors = new ArrayList<IEditor>();
		StringBuilder messageRepresentation = new StringBuilder(50);
		for(int i = 0; i < editorPane.getTabCount(); i++) {
			IEditor editor = (IEditor)editorPane.getComponentAt(i);
			if(editor.isModified()) {
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
			String yes = bundle.getString(LanguageConstants.DIALOG_BUTTON_YES);
			String no = bundle.getString(LanguageConstants.DIALOG_BUTTON_NO);
			String cancel = bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
			Object[] options = {yes, no, cancel};
			String title = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_TITLE);
			String message = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_MESSAGE);
			message = message.replace("{0}", messageRepresentation.toString());
	
			int option = JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			if(option == JOptionPane.YES_OPTION) {
				for(IEditor editor : notSavedEditors) {
					String projectName = editor.getProjectName();
					String fileName = editor.getFileName();
					String data = editor.getData();
					cache.saveFile(projectName, fileName, data);
				}
			} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		editorPane.removeAll();
	}
	
	private void closeAllButThisEditors(Component component) {
		List<Component> toBeClosedComponents = new ArrayList<Component>();
		List<IEditor> notSavedEditors = new ArrayList<IEditor>();
		StringBuilder messageRepresentation = new StringBuilder(50);
		for(int i = 0; i < editorPane.getTabCount(); i++) {
			Component c = editorPane.getComponentAt(i);
			if(!c.equals(component)) {
				IEditor editor = (IEditor)c;
				if(editor.isModified()) {
					notSavedEditors.add(editor);
					messageRepresentation.append(editor.getFileName()).append(" [")
						.append(editor.getProjectName()).append("]\n");
				}
				toBeClosedComponents.add(c);
			}
			
		}
		int lenght = messageRepresentation.length();
		if(lenght != 0) {
			messageRepresentation.replace(lenght-1, lenght, "");
		}
		
		if(notSavedEditors.size() != 0) {
			String yes = bundle.getString(LanguageConstants.DIALOG_BUTTON_YES);
			String no = bundle.getString(LanguageConstants.DIALOG_BUTTON_NO);
			String cancel = bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
			Object[] options = {yes, no, cancel};
			String title = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_TITLE);
			String message = bundle.getString(LanguageConstants.DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_MESSAGE);
			message = message.replace("{0}", messageRepresentation.toString());
	
			int option = JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			if(option == JOptionPane.YES_OPTION) {
				for(IEditor editor : notSavedEditors) {
					String projectName = editor.getProjectName();
					String fileName = editor.getFileName();
					String data = editor.getData();
					cache.saveFile(projectName, fileName, data);
				}
			} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		for(Component c : toBeClosedComponents) {
			closeEditor(c);
		}
	}

	private class ServerInitData {
		
		public void init(Applet applet) {
			applet.setSize(new Dimension(1000,650));
		}
		
		public void writeGlobalFiles() {
			try {
				AjaxMediator ajax = new DefaultAjaxMediator(MainApplet.this);
				MethodInvoker invoker = new DefaultMethodInvoker(ajax);
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
				final String projectName1 = "Project1";
				final String fileName1 = "File1";
				final String fileType1 = File.FT_VHDLSOURCE;
				final String fileContent1 = "simple content";
				final String fileName2 = "File2";
				final String fileType2 = File.FT_VHDLSOURCE;
				final String fileContent2 = "some file content that should be displayed in writer";
				
				long start = System.currentTimeMillis();
				IEditor editor = cache.getEditor("vhdl_source");
				
				cache.createProject(projectName1);
				cache.createFile(projectName1, fileName1, fileType1);
				cache.saveFile(projectName1, fileName1, fileContent1);
				
				cache.createFile(projectName1, fileName2, fileType2);
				cache.saveFile(projectName1, fileName2, fileContent2);
				
				long end = System.currentTimeMillis();
				
				String infoData = editor.getData()+(start-end)+"ms\nLoaded Options:\n"+cache.getOptions();
				cache.saveFile(projectName1, fileName1, infoData);

				openFile(projectName1, fileName2);
				openFile(projectName1, fileName1);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}