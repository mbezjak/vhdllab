package hr.fer.zemris.vhdllab.applets.main.dummy;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 226157592254177077L;
	
	public StatusBar() {
		JLabel label = new JLabel("This is statusbar area!");
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.CYAN);
	}
}
