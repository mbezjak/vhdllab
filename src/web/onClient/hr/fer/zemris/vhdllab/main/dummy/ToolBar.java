package hr.fer.zemris.vhdllab.main.dummy;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ToolBar extends JPanel {

	private static final long serialVersionUID = 8622650690807102908L;
	
	public ToolBar() {
		JLabel label = new JLabel("This is toolbar area!");
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.GREEN);
	}
}
