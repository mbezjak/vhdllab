package hr.fer.zemris.vhdllab.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.AjaxOpListener;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.vhdllab.main.dummy.ProjectExplorer;
import hr.fer.zemris.vhdllab.main.dummy.SideBar;
import hr.fer.zemris.vhdllab.main.dummy.StatusBar;
import hr.fer.zemris.vhdllab.main.dummy.StatusExplorer;
import hr.fer.zemris.vhdllab.main.dummy.ToolBar;
import hr.fer.zemris.vhdllab.main.dummy.Writer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class MainApplet
		extends JApplet
		implements AjaxOpListener {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private AjaxMediator ajax;
	
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
		ajax.registerResultListener(this);
		initGUI();
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}
	
	@Override
	public void destroy() {
		//ajax.initiateAbort();
		ajax = null;
		super.destroy();
	}
	
	private void initGUI() {
		setupMenubar();
		setupToolBar();
		setupStatusBar();
		setupMainPanel();
	}
	
	private void setupMenubar() {
		menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);
		
		this.setJMenuBar(menuBar);
	}

	private void setupToolBar() {
		toolBar = new ToolBar();
		JPanel toolBarPanel = new JPanel(new BorderLayout());
		toolBarPanel.add(toolBar, BorderLayout.CENTER);
		
		this.getContentPane().add(toolBarPanel, BorderLayout.NORTH);		
	}

	private void setupStatusBar() {
		statusBar = new StatusBar();
		JPanel statusBarPanel = new JPanel(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		this.getContentPane().add(statusBarPanel, BorderLayout.SOUTH);

	}
	
	private void setupMainPanel() {
		projectExplorer = new ProjectExplorer();
		JPanel projectExplorerPanel = new JPanel(new BorderLayout());
		projectExplorerPanel.add(projectExplorer, BorderLayout.CENTER);
		projectExplorerPanel.setPreferredSize(new Dimension(this.getWidth()/3,0));
		
		JPanel writerPanel = new JPanel(new BorderLayout());
		writer = new Writer();
		writerPanel.add(writer, BorderLayout.CENTER);
		
		JPanel statusExplorerPanel = new JPanel(new BorderLayout());
		statusExplorer = new StatusExplorer();
		statusExplorerPanel.add(statusExplorer, BorderLayout.CENTER);
		statusExplorerPanel.setPreferredSize(new Dimension(0,this.getHeight()/3));
		
		JPanel sideBarPanel = new JPanel(new BorderLayout());
		sideBar = new SideBar();
		sideBarPanel.add(sideBar, BorderLayout.CENTER);
		sideBarPanel.setPreferredSize(new Dimension(this.getWidth()/3,0));
		
		JSplitPane splitBarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, writerPanel, sideBarPanel);
		JSplitPane projectExporerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorerPanel, splitBarSplitPane);
		JSplitPane statusExplorerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectExporerSplitPane, statusExplorerPanel);
		JPanel centerComponentsPanel = new JPanel(new BorderLayout());
		//centerComponentsPanel.add(projectExplorerPanel, BorderLayout.WEST);
		//centerComponentsPanel.add(writerPanel, BorderLayout.CENTER);
		//centerComponentsPanel.add(statusExplorerPanel, BorderLayout.SOUTH);
		//centerComponentsPanel.add(shortcutExplorerPanel, BorderLayout.EAST);
		centerComponentsPanel.add(statusExplorerSplitPane);
		
		this.getContentPane().add(centerComponentsPanel, BorderLayout.CENTER);
		
	}
	
	public void resultReceived(String result, int code) {
		if(code!=200) return;
		//Properties p = XMLUtil.deserializeProperties(result);
	}
	
	public void ajaxCallResultReceived(String result, int code) {
		ajax.fireResultReceived(result,code);
	}
	
}