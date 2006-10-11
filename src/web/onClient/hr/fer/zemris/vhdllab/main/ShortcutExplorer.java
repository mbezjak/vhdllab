package hr.fer.zemris.vhdllab.main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShortcutExplorer extends JPanel {

	private static final long serialVersionUID = -2426335850507706828L;
	
	public ShortcutExplorer() {
		JLabel label = new JLabel("This is shortcut explorer area!");
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.PINK);
	}
}
