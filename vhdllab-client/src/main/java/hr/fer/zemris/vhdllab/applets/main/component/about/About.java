package hr.fer.zemris.vhdllab.applets.main.component.about;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class About {
	
	private static final About INSTANCE = new About();
	
	private JOptionPane aboutContent;
	private JDialog dialog;
	private Content content;
	private JScrollPane scroll;
	
	public static About instance() {
		return INSTANCE;
	}
	
	private About() {
		this.content = new Content();
		this.scroll = new JScrollPane(content);
		this.scroll.setPreferredSize(new Dimension(600, 600));
		aboutContent = new JOptionPane(scroll, JOptionPane.INFORMATION_MESSAGE);
		this.dialog = this.aboutContent.createDialog(ApplicationContextHolder.getContext().getFrame(), "About");
	}
	
	public void setVisible(boolean isVisible) {
		this.dialog.setVisible(isVisible);
	}
}