package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.vhdllab.applets.main.dummy.ProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.dummy.SideBar;
import hr.fer.zemris.vhdllab.applets.main.dummy.StatusBar;
import hr.fer.zemris.vhdllab.applets.main.dummy.StatusExplorer;
import hr.fer.zemris.vhdllab.applets.main.dummy.ToolBar;
import hr.fer.zemris.vhdllab.applets.main.dummy.Writer;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class MainApplet
		extends JApplet
		 {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private AjaxMediator ajax;
	private ResourceBundle bundle;
	
	private JMenuBar menuBar;
	private ToolBar toolBar;
	private StatusBar statusBar;
	
	private ProjectExplorer projectExplorer;
	private Writer writer;
	private StatusExplorer statusExplorer;
	private SideBar sideBar;
	
	@Override
	public void init() {
		super.init();
		ajax = new DefaultAjaxMediator(this);
		bundle = CachedResourceBundles.getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN,
				LanguageConstants.LANGUAGE_EN, null);
		initGUI();
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void stop() {
		super.stop();
	}
	
	@Override
	public void destroy() {
		//ajax.initiateAbort();
		ajax = null;
		this.getContentPane().removeAll();
		this.repaint();
		super.destroy();
	}
	
	private void initGUI() {
		JPanel topContainerPanel = new JPanel(new BorderLayout());
		this.setJMenuBar( setupMenubar() );
		
		topContainerPanel.add(setupToolBar(), BorderLayout.NORTH);
		topContainerPanel.add(setupStatusBar(), BorderLayout.SOUTH);
		topContainerPanel.add(setupCenterPanel(), BorderLayout.CENTER);
		
		this.add(topContainerPanel, BorderLayout.CENTER);
	}
	
	
	private JMenuBar setupMenubar() {
		String temp;
		JMenu menu, submenu;
		JMenuItem menuItem;
		menuBar = new JMenuBar();
		
		// File menu
		menu = new JMenu(bundle.getString(LanguageConstants.MENU_FILE));
		temp = bundle.getString(LanguageConstants.MENU_FILE_MNEMONIC);
		if(temp.length() == 1) {
			menu.setMnemonic(temp.codePointAt(0));
		}

		// New sub menu
		submenu = new JMenu(bundle.getString(LanguageConstants.MENU_ITEM_NEW));
		temp = bundle.getString(LanguageConstants.MENU_ITEM_NEW_MNEMONIC);
		if(temp.length() == 1) {
			submenu.setMnemonic(temp.codePointAt(0));
		}

		// New Project menu item
		menuItem = new JMenuItem(bundle.getString(LanguageConstants.MENU_ITEM_NEW_PROJECT));
		temp = bundle.getString(LanguageConstants.MENU_ITEM_NEW_PROJECT_MNEMONIC);
		if(temp.length() == 1) {
			menuItem.setMnemonic(temp.codePointAt(0));
		}
		submenu.add(menuItem);
		
		submenu.addSeparator();
		
		// New File menu item
		menuItem = new JMenuItem(bundle.getString(LanguageConstants.MENU_ITEM_NEW_FILE));
		temp = bundle.getString(LanguageConstants.MENU_ITEM_NEW_FILE_MNEMONIC);
		if(temp.length() == 1) {
			menuItem.setMnemonic(temp.codePointAt(0));
		}
		submenu.add(menuItem);
		
		// New Testbench menu item
		menuItem = new JMenuItem(bundle.getString(LanguageConstants.MENU_ITEM_NEW_TESTBENCH));
		temp = bundle.getString(LanguageConstants.MENU_ITEM_NEW_TESTBENCH_MNEMONIC);
		if(temp.length() == 1) {
			menuItem.setMnemonic(temp.codePointAt(0));
		}
		submenu.add(menuItem);
		
		// New Schema menu item
		menuItem = new JMenuItem(bundle.getString(LanguageConstants.MENU_ITEM_NEW_SCHEMA));
		temp = bundle.getString(LanguageConstants.MENU_ITEM_NEW_SCHEMA_MNEMONIC);
		if(temp.length() == 1) {
			menuItem.setMnemonic(temp.codePointAt(0));
		}
		submenu.add(menuItem);
		
		// New Automat menu item
		menuItem = new JMenuItem(bundle.getString(LanguageConstants.MENU_ITEM_NEW_AUTOMAT));
		temp = bundle.getString(LanguageConstants.MENU_ITEM_NEW_AUTOMAT_MNEMONIC);
		if(temp.length() == 1) {
			menuItem.setMnemonic(temp.codePointAt(0));
		}
		submenu.add(menuItem);
		
		menu.add(submenu);
		
		
		// Exit menu item
		menuItem = new JMenuItem(bundle.getString(LanguageConstants.MENU_ITEM_EXIT));
		temp = bundle.getString(LanguageConstants.MENU_ITEM_EXIT_MNEMONIC);
		if(temp.length() == 1) {
			menuItem.setMnemonic(temp.codePointAt(0));
		}
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitApplication();
			}
		});
		menu.add(menuItem);
		
		menuBar.add(menu);
		
		// Edit menu
		menu = new JMenu(bundle.getString(LanguageConstants.MENU_EDIT));
		temp = bundle.getString(LanguageConstants.MENU_EDIT_MNEMONIC);
		if(temp.length() == 1) {
			menu.setMnemonic(temp.codePointAt(0));
		}

		menuBar.add(menu);
		
		// View menu
		menu = new JMenu(bundle.getString(LanguageConstants.MENU_VIEW));
		temp = bundle.getString(LanguageConstants.MENU_VIEW_MNEMONIC);
		if(temp.length() == 1) {
			menu.setMnemonic(temp.codePointAt(0));
		}
		
		menuBar.add(menu);

		// Tool menu
		menu = new JMenu(bundle.getString(LanguageConstants.MENU_TOOL));
		temp = bundle.getString(LanguageConstants.MENU_TOOL_MNEMONIC);
		if(temp.length() == 1) {
			menu.setMnemonic(temp.codePointAt(0));
		}

		menuBar.add(menu);
		
		// Help menu
		menu = new JMenu(bundle.getString(LanguageConstants.MENU_HELP));
		temp = bundle.getString(LanguageConstants.MENU_HELP_MNEMONIC);
		if(temp.length() == 1) {
			menu.setMnemonic(temp.codePointAt(0));
		}

		menuBar.add(menu);
		
		return menuBar;
	}

	private JPanel setupToolBar() {
		toolBar = new ToolBar();
		JPanel toolBarPanel = new JPanel(new BorderLayout());
		toolBarPanel.add(toolBar, BorderLayout.CENTER);
		toolBarPanel.setPreferredSize(new Dimension(0, this.getHeight()/8));
		
		return toolBarPanel;
	}

	private JPanel setupStatusBar() {
		statusBar = new StatusBar();
		JPanel statusBarPanel = new JPanel(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);
		statusBarPanel.setPreferredSize(new Dimension(0, this.getHeight()/8));

		return statusBarPanel;
	}
	
	private JPanel setupCenterPanel() {
		projectExplorer = new ProjectExplorer();
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
	
	protected void exitApplication() {
		stop();
		destroy();
	}
}