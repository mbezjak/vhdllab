package hr.fer.zemris.vhdllab.applets.main.dummy;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Writer extends JPanel implements IEditor, IWizard {

	private static final long serialVersionUID = 5853551043423675268L;
	private JTextArea text;
	private boolean change;
	
	private ProjectContainter container;
	private FileContent content;
	
	public Writer() {
		text = new JTextArea("This is writer area!");
		text.setPreferredSize(new Dimension(100,100));
		text.addKeyListener(new KeyListener() {
		
			public void keyTyped(KeyEvent e) {
				change = true;
			}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		
		});
		this.add(text, BorderLayout.CENTER);
		this.setBackground(Color.RED);
		
	}

	public String getData() {
		change = false;
		return text.getText();
	}

	public IWizard getWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isModified() {
		return change;
	}

	public void setFileContent(FileContent fContent) {
		this.content = fContent;
		text.setText(content.getContent());
	}

	public void setProjectContainer(ProjectContainter pContainer) {
		this.container = pContainer;
	}

	public FileContent getInitialFileContent() {
		return content;
	}
	
	public void setupWizard() {
		String projectName = JOptionPane.showInputDialog("Enter project name:");
		String fileName = JOptionPane.showInputDialog("Enter file name:");
		content = new FileContent(projectName, fileName, "");
	}

}