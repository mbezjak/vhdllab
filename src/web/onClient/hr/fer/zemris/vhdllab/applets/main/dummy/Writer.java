package hr.fer.zemris.vhdllab.applets.main.dummy;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Writer extends JPanel implements IEditor, IWizard {

	private static final long serialVersionUID = 5853551043423675268L;
	private JTextArea text;
	private boolean savable;
	private boolean readonly;
	private boolean modifiedFlag = true;
	
	private ProjectContainer container;
	private FileContent content;
	
	public void init() {
		text = new JTextArea("This is writer area!", 25,50);
		JScrollPane scroll = new JScrollPane(text);
		this.setLayout(new BorderLayout());
		this.add(scroll, BorderLayout.CENTER);
		this.setBackground(Color.RED);
		text.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if(modifiedFlag) return;
				modifiedFlag = true;
				container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
			}
			public void removeUpdate(DocumentEvent e) {
				if(modifiedFlag) return;
				modifiedFlag = true;
				container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
			}
			public void insertUpdate(DocumentEvent e) {
				if(modifiedFlag) return;
				modifiedFlag = true;
				container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
			}
		});
	}

	public String getData() {
		modifiedFlag = false;
		return text.getText();
	}

	public IWizard getWizard() {
		return this;
	}

	public boolean isModified() {
		return modifiedFlag;
	}

	public void setFileContent(FileContent fContent) {
		this.content = fContent;
		text.setText(content.getContent());
		modifiedFlag = false;
		container.resetEditorTitle(modifiedFlag, content.getProjectName(),content.getFileName());
	}

	public void setProjectContainer(ProjectContainer pContainer) {
		this.container = pContainer;
	}

	public FileContent getInitialFileContent(Component parent) {
		try {
			String projectName = container.getActiveProject();
			String fileName;
			do {
				fileName = JOptionPane.showInputDialog(parent, "Enter file name:");
			} while(container.existsFile(projectName, fileName));
			
			String data = "new file named '" + fileName + "' that belongs to '" +projectName +"' was created in: " + getCurrentDateAndTime();
			FileContent initialContent = new FileContent(projectName, fileName, data);
			return initialContent;
		} catch (UniformAppletException e) {
			e.printStackTrace();
		}
		return null;
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

	public void highlightLine(int line) {}

	public void setReadOnly(boolean flag) {
		this.readonly = flag;
	}

	public void setSaveable(boolean flag) {
		this.savable = flag;
	}

	public boolean isReadOnly() {
		return readonly;
	}

	public boolean isSavable() {
		return savable;
	}

	public void setSavable(boolean flag) {
		savable = flag;
	}
}