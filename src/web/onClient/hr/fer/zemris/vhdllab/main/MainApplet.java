package hr.fer.zemris.vhdllab.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.AjaxOpListener;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.ajax.shared.XMLUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.springframework.web.servlet.support.JspAwareRequestContext;

public class MainApplet extends JApplet implements AjaxOpListener {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private AjaxMediator ajax;
	
	private JMenuBar menubar;
	private Toolbar toolbar;
	private Statusbar statusbar;
	
	private ProjectExplorer projectExplorer;
	private Writer writer;
	private StatusExplorer statusExplorer;
	private ShortcutExplorer shortcutExplorer;
	
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
		setupToolbar();
		setupStatusbar();
		setupMainPanel();
	}
	
	private void setupMenubar() {
		menubar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		menubar.add(file);
		
		this.setJMenuBar(menubar);
	}

	private void setupToolbar() {
		toolbar = new Toolbar();
		JPanel toolbarPanel = new JPanel(new BorderLayout());
		toolbarPanel.add(toolbar, BorderLayout.CENTER);
		
		this.getContentPane().add(toolbarPanel, BorderLayout.NORTH);		
	}

	private void setupStatusbar() {
		statusbar = new Statusbar();
		JPanel statusbarPanel = new JPanel(new BorderLayout());
		statusbarPanel.add(statusbar, BorderLayout.CENTER);

		this.getContentPane().add(statusbarPanel, BorderLayout.SOUTH);

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
		
		JPanel shortcutExplorerPanel = new JPanel(new BorderLayout());
		shortcutExplorer = new ShortcutExplorer();
		shortcutExplorerPanel.add(shortcutExplorer, BorderLayout.CENTER);
		shortcutExplorerPanel.setPreferredSize(new Dimension(this.getWidth()/3,0));
		
		JSplitPane shortcutExplorerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, writerPanel, shortcutExplorerPanel);
		JSplitPane projectExporerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorerPanel, shortcutExplorerSplitPane);
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
		Properties p = XMLUtil.deserializeProperties(result);
	}
	
	public void ajaxCallResultReceived(String result, int code) {
		ajax.fireResultReceived(result,code);
	}
	
}