package hr.fer.zemris.vhdllab.main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusExplorer extends JPanel {

	private static final long serialVersionUID = -7253178039704067567L;
	
	public StatusExplorer() {
		JLabel label = new JLabel("This is status explorer area!");
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.BLUE);
	}
}
