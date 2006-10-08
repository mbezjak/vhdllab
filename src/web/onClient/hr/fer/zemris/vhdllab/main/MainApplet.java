package hr.fer.zemris.vhdllab.main;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.AjaxOpListener;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.ajax.shared.XMLUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MainApplet extends JApplet implements AjaxOpListener {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 4037604752375048576L;
	
	private AjaxMediator ajax;
	
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
		ajax.initiateAbort();
		ajax = null;
		super.destroy();
	}
	
	private void initGUI() {
		JPanel projectExplorerPanel = new JPanel(new BorderLayout());
		projectExplorerPanel.add(new ProjectExplorer(), BorderLayout.CENTER);
		projectExplorerPanel.setPreferredSize(new Dimension(this.getWidth()/5,0));

		JPanel writerPanel = new JPanel(new BorderLayout());
		JTextArea ta = new JTextArea();
		writerPanel.add(ta, BorderLayout.CENTER);
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.add(projectExplorerPanel, BorderLayout.CENTER);
		statusPanel.setPreferredSize(new Dimension(0,this.getHeight()/5));
		
		this.getContentPane().add(projectExplorerPanel, BorderLayout.WEST);
		this.getContentPane().add(writerPanel, BorderLayout.CENTER);
		this.getContentPane().add(statusPanel, BorderLayout.SOUTH);

		
		// TODO Auto-generated method stub
	}
	
	public void resultReceived(String result, int code) {
		if(code!=200) return;
		Properties p = XMLUtil.deserializeProperties(result);
	}
	
	public void ajaxCallResultReceived(String result, int code) {
		ajax.fireResultReceived(result,code);
	}
	
}