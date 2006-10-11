package hr.fer.zemris.vhdllab.main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProjectExplorer extends JPanel {

	private static final long serialVersionUID = 4932799790563214089L;

	public ProjectExplorer() {
		JLabel label = new JLabel("This is project explorer area!");
		this.add(label,BorderLayout.CENTER);
		this.setBackground(Color.BLACK);
	}
}