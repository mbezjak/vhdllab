package hr.fer.zemris.vhdllab.applets;

import hr.fer.zemris.ajax.shared.AjaxMediator;
import hr.fer.zemris.ajax.shared.AjaxOpListener;
import hr.fer.zemris.ajax.shared.AjaxToJavaInterface;
import hr.fer.zemris.ajax.shared.DefaultAjaxMediator;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.XMLUtil;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class SimpleEditorApplet extends JApplet implements AjaxOpListener, AjaxToJavaInterface {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 7100909319307233674L;

	private JTextField address;
	private JTextArea text;
	private AjaxStatus aStatus;
	private AjaxMediator ajaxMediator;
	
	@Override
	public void init() {
		super.init();
		ajaxMediator = new DefaultAjaxMediator(this);
		ajaxMediator.registerResultListener(this);
		createGUI();
	}

	private void createGUI() {
		address = new JTextField();
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("File name: "),BorderLayout.WEST);
		p1.add(address,BorderLayout.CENTER);
		JPanel p2 = new JPanel(new GridLayout(1,2));
		JButton loadButton = new JButton("load");
		JButton saveButton = new JButton("save");
		p2.add(loadButton);
		p2.add(saveButton);
		p1.add(p2,BorderLayout.EAST);
		text = new JTextArea();
		
		aStatus = new AjaxStatus(ajaxMediator);
		
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aStatus.loadFile(address.getText());
			}
		});
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aStatus.saveFile(address.getText(),text.getText());
			}
		});
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(p1,BorderLayout.NORTH);
		this.getContentPane().add(new JScrollPane(text),BorderLayout.CENTER);
		this.getContentPane().add(aStatus,BorderLayout.SOUTH);
	}

	public void resultReceived(String result, int code) {
		if(code!=200) {
			return;
		} else {
			Properties p = XMLUtil.deserializeProperties(result);
			String method = p.getProperty("method","");
			if(method.equals("")) return;
			String status = p.getProperty("status");
			String content = p.getProperty(MethodConstants.PROP_FILE_CONTENT,"");
			if(status.equalsIgnoreCase("error")) {
				JOptionPane.showMessageDialog(null,content);
			} else {
				if(method.equalsIgnoreCase(MethodConstants.MTD_LOAD_FILE_CONTENT)) {
					text.setText(content);
				}
			}
		}
	}

	public void ajaxCallResultReceived(String result, int code) {
		ajaxMediator.fireResultReceived(result,code);
	}

	public static class AjaxStatus extends JPanel implements AjaxOpListener {
		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = -4429295300867542739L;

		JButton msgButton;
		JLabel msgLabel;
		AjaxMediator ajaxMediator;
		
		boolean inProgress;
		
		public AjaxStatus(AjaxMediator ajaxMediator) {
			super();
			this.ajaxMediator = ajaxMediator;
			setLayout(new BorderLayout());
			msgButton = new JButton("Abort");
			msgLabel = new JLabel("Ready.");
			add(msgLabel,BorderLayout.CENTER);
			add(msgButton,BorderLayout.EAST);
			
			msgButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					abortOperation();
				}
			});
			
			msgButton.setEnabled(false);
			
			ajaxMediator.registerResultListener(this);
		}
		
		public void saveFile(String fileName, String content) {
			synchronized (this) {
				if(inProgress) {
					throw new IllegalStateException("A call is already in progress!");
				}
				inProgress = true;
			}
			Properties p = new Properties();
			p.setProperty("method",MethodConstants.MTD_SAVE_FILE);
			p.setProperty(MethodConstants.PROP_FILE_ID,fileName);
			p.setProperty(MethodConstants.PROP_FILE_CONTENT,content);
			String msg = XMLUtil.serializeProperties(p);
			msgButton.setEnabled(true);
			msgLabel.setText("Operation initiated...");
			msgLabel.repaint();
			ajaxMediator.initiateAsynchronousCall(msg);
		}


		public void loadFile(String fileName) {
			synchronized (this) {
				if(inProgress) {
					throw new IllegalStateException("A call is already in progress!");
				}
				inProgress = true;
			}
			Properties p = new Properties();
			p.setProperty("method",MethodConstants.MTD_LOAD_FILE_CONTENT);
			p.setProperty(MethodConstants.PROP_FILE_ID,fileName);
			String msg = XMLUtil.serializeProperties(p);
			msgButton.setEnabled(true);
			msgLabel.setText("Operation initiated...");
			msgLabel.repaint();
			ajaxMediator.initiateAsynchronousCall(msg);
		}

		public void abortOperation() {
			msgLabel.setText("Abort initiated...");
			msgLabel.repaint();
			ajaxMediator.initiateAbort();
		}

		public void resultReceived(String result, int code) {
			synchronized (this) {
				inProgress = false;
			}
			msgButton.setEnabled(false);
			if(code!=200) {
				msgLabel.setText("Result arrived. Code is "+code+".");
			} else {
				Properties p = XMLUtil.deserializeProperties(result);
				String method = p.getProperty("method","");
				String status = p.getProperty("status");
				String content = p.getProperty(MethodConstants.STATUS_CONTENT);
				msgLabel.setText("Result arrived. ["+status+"/"+method+"] "+content);
			}
		}
	}
}