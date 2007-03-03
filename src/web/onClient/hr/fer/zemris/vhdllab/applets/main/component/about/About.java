package hr.fer.zemris.vhdllab.applets.main.component.about;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class About {
	
	private JOptionPane aboutContent;
	private JDialog dialog;
	private Content content;
	private JScrollPane scroll;
	
	public About(Component parent) {
		this.content = new Content();
		this.scroll = new JScrollPane(content);
		this.scroll.setPreferredSize(new Dimension(600, 600));
		aboutContent = new JOptionPane(scroll, JOptionPane.INFORMATION_MESSAGE);
		this.dialog = this.aboutContent.createDialog(parent, "About");
	}
	
	public void setVisible(boolean isVisible) {
		this.dialog.setVisible(isVisible);
	}
}