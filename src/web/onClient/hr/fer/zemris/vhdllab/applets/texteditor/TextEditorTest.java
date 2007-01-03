package hr.fer.zemris.vhdllab.applets.texteditor;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TextEditorTest extends JFrame {
	
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
		TextEditor editor = new TextEditor();
		
 
		 
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
		TextEditorTest edi = new TextEditorTest();
		edi.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		edi.pack();
		edi.setVisible(true);
		

	}

}
