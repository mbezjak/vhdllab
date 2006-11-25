package hr.fer.zemris.vhdllab.applets.main.dummy;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Writer extends JPanel implements IEditor, IWizard {

	private static final long serialVersionUID = 5853551043423675268L;
	private JTextArea text;
	private boolean change;
	
	private ProjectContainer container;
	private FileContent content;
	
	public Writer() {
		text = new JTextArea("This is writer area!", 25,50);
		text.addKeyListener(new KeyListener() {
		
			public void keyTyped(KeyEvent e) {
				change = true;
			}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		
		});
		JScrollPane scroll = new JScrollPane(text);
		this.add(scroll, BorderLayout.CENTER);
		this.setBackground(Color.RED);
		
	}

	public String getData() {
		change = false;
		return text.getText();
	}

	public IWizard getWizard() {
		return this;
	}

	public boolean isModified() {
		return change;
	}

	public void setFileContent(FileContent fContent) {
		this.content = fContent;
		text.setText(content.getContent());
	}

	public void setProjectContainer(ProjectContainer pContainer) {
		this.container = pContainer;
	}

	public FileContent getInitialFileContent() {
		return content;
	}
	
	public void setupWizard() {
		String projectName;
		String fileName;
		do {
			projectName = JOptionPane.showInputDialog("Enter project name:");
		} while(!container.existsProject(projectName));
		
		do {
			fileName = JOptionPane.showInputDialog("Enter file name:");
		} while(container.existsFile(projectName, fileName));

		String data = "new file named '" + fileName + "' that belongs to '" +projectName +"' was created in: " + getCurrentDateAndTime();
		text.setText(data);
		content = new FileContent(projectName, fileName, data);
	}

	public String getFileName() {
		return content.getFileName();
	}

	public String getProjectName() {
		return content.getProjectName();
	}
	
	private String getCurrentDateAndTime() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault());
		return sdf.format(cal.getTime());
	}

}