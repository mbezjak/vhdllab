package hr.fer.zemris.vhdllab.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.AjaxOpListener;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.ajax.shared.XMLUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JPanel;

public class MainApplet extends JApplet implements AjaxOpListener {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private AjaxMediator ajax;
	
	private final int PROJECT_EXPLORER_WIDTH = 5;
	private final int STATUS_EXPLORER_HEIGHT = 5;
	private final int SHORTCUT_EXPLORER_WIDTH = 5;
	
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
		JPanel projectExplorerPanel = new JPanel(new BorderLayout());
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorerPanel.add(projectExplorer, BorderLayout.CENTER);
		projectExplorerPanel.setPreferredSize(new Dimension(this.getWidth()/PROJECT_EXPLORER_WIDTH,0));
		
		JPanel writerPanel = new JPanel(new BorderLayout());
		Writer writer = new Writer();
		writerPanel.add(writer, BorderLayout.CENTER);
		
		JPanel statusExplorerPanel = new JPanel(new BorderLayout());
		StatusExplorer statusExplorer = new StatusExplorer();
		statusExplorerPanel.add(statusExplorer, BorderLayout.CENTER);
		statusExplorerPanel.setPreferredSize(new Dimension(0,this.getHeight()/STATUS_EXPLORER_HEIGHT));
		
		JPanel shortcutExplorerPanel = new JPanel(new BorderLayout());
		ShortcutExplorer shortcutExplorer = new ShortcutExplorer();
		shortcutExplorerPanel.add(shortcutExplorer, BorderLayout.CENTER);
		shortcutExplorerPanel.setPreferredSize(new Dimension(this.getWidth()/SHORTCUT_EXPLORER_WIDTH,0));
		
		JPanel centerComponentsPanel = new JPanel(new BorderLayout());
		centerComponentsPanel.add(projectExplorerPanel, BorderLayout.WEST);
		centerComponentsPanel.add(writerPanel, BorderLayout.CENTER);
		centerComponentsPanel.add(statusExplorerPanel, BorderLayout.SOUTH);
		centerComponentsPanel.add(shortcutExplorerPanel, BorderLayout.EAST);
		
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