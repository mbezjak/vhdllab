package hr.fer.zemris.vhdllab.applets.main.component.about;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class Person extends JPanel {
	private static final long serialVersionUID = 6855933562897982575L;

	/** Ime i prezime osobe */
	private String name;

	/** Opis osobe koja ja sudjelovala na projektu. */
	private JTextArea text;

	/** Fotka osobe koja je sudjelovala na projektu */
	private ImageIcon icon;


	public Person(String name, ImageIcon icon, String text) {
		this.name = name;
		this.icon = icon;
		this.text = new JTextArea(13, 35);
		this.text.setLineWrap(true);
		this.text.setWrapStyleWord(true);
		this.text.setBackground(new Color(201, 211, 236));
		this.text.setEditable(false);
		this.text.setText(text);
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		layout.putConstraint(SpringLayout.WEST, this.text, 200, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, this.text, 50, SpringLayout.NORTH, this);
		this.add(this.text);
	}


	@Override
	public Dimension preferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(600, 200);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(new Color(201, 211, 236));
		icon.paintIcon(this, g, 0, 0);
		g.drawString(name, 200, 20);
	}
}