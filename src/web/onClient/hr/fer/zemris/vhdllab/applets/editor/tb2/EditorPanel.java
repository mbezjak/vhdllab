package hr.fer.zemris.vhdllab.applets.editor.tb2;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel that holds a list of signal names on the west side and a CanvasPanel in Center.
 * @author ddelac
 *
 */
public class EditorPanel extends JPanel {
	
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -2892291077620056982L;
	
	private CanvasPanel panel;
	
	public EditorPanel() {
		super();
		createGUI();
	}

	private void createGUI() {
		this.setLayout(new BorderLayout());
		
		panel=new CanvasPanel();
		this.add(panel,BorderLayout.CENTER);
		
		//TODO:Create List of signals
		this.add(new JLabel("List of signals"),BorderLayout.WEST);
		this.setOpaque(true);
		this.setBackground(Color.BLUE);
	}

	public void setContent(String content) {
		// TODO postavlja se file content za editor!!!
		
	}
	
}
