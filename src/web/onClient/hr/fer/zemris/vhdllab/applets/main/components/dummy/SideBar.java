package hr.fer.zemris.vhdllab.applets.main.components.dummy;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SideBar extends JPanel {

	private static final long serialVersionUID = -2426335850507706828L;
	
	public SideBar() {
		JLabel label = new JLabel("This is shortcut explorer area!");
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.PINK);
	}
}
