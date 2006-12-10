package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.BorderLayout;
import java.awt.ScrollPane;

import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class SPropertyBar extends JToolBar {
	private SPropertyPanel panel = null;
	private JScrollPane scrpan = null;

	public SPropertyBar() {
		super("Property Bar");
		this.setLayout(new BorderLayout());
		
		panel = new SPropertyPanel(null);
		scrpan = new JScrollPane(panel);
		scrpan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(scrpan, BorderLayout.CENTER);
	}
	
	public SPropertyPanel getPropertyPanel() {
		return panel;
	}
	
}
