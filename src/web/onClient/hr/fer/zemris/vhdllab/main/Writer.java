package hr.fer.zemris.vhdllab.main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Writer extends JPanel {

	private static final long serialVersionUID = 5853551043423675268L;
	private JTextArea text;
	
	public Writer() {
		text = new JTextArea("This is writer area!");
		this.add(text, BorderLayout.CENTER);
		this.setBackground(Color.RED);
	}
	
}