package hr.fer.zemris.vhdllab.applets.texteditor;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TextEditorTest extends JFrame {
	TextEditor editor= null; 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TextEditorTest() throws HeadlessException {
		super();
		createGUI();
	}

	private void createGUI() {
		this.getContentPane().setLayout(new BorderLayout());
		 editor = new TextEditor();
		
 
		 
		editor.init();
		editor.setSavable(true);
		editor.setReadOnly(true);
		 
		
		JPanel panel =  new JPanel();
	
		panel.add(editor);
		this.add(panel);

	}

	 

 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final TextEditorTest edi = new TextEditorTest();
		edi.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		edi.pack();
		edi.setVisible(true);
		edi.addWindowListener(new WindowListener() {
		
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
		
			}
		
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
		
			}
		
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
		
			}
		
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
		
			}
		
			public void windowClosed(WindowEvent e) {
				edi.editor.cleanUp();
				
			}
		
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
		
			}
		
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
		
			}
		
		});

	}

}
