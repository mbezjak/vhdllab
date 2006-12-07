package hr.fer.zemris.vhdllab.applets.schema;

import javax.swing.JToolBar;

public class SPropertyBar extends JToolBar {
	private SPropertyPanel panel = null;

	public SPropertyBar() {
		super("Component Property Bar");
		panel = new SPropertyPanel(null);
		this.add(panel);
	}
	
	public SPropertyPanel getPropertyPanel() {
		return panel;
	}
	
}
