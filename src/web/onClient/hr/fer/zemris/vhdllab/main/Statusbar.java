package hr.fer.zemris.vhdllab.main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Statusbar extends JPanel {

	private static final long serialVersionUID = 226157592254177077L;
	
	public Statusbar() {
		JLabel label = new JLabel("This is statusbar area!");
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.CYAN);
	}
}
