package hr.fer.zemris.vhdllab.main;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class ProjectExplorer extends JComponent {

	private static final long serialVersionUID = 2074383497057028265L;
	private JLabel label;
	
	public ProjectExplorer() {
		label = new JLabel();
	}
	
	public JLabel getLabel() {
		return label;
	}
}