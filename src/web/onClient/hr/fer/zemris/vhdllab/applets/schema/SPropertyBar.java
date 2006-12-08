package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.BorderLayout;

import javax.swing.JToolBar;

public class SPropertyBar extends JToolBar {
	private SPropertyPanel panel = null;

	public SPropertyBar() {
		super("Component Property Bar");
		this.setLayout(new BorderLayout());
		panel = new SPropertyPanel(null);
		this.add(panel, BorderLayout.NORTH);
	}
	
	public SPropertyPanel getPropertyPanel() {
		return panel;
	}
	
}
