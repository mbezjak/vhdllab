package hr.fer.zemris.vhdllab.applets.main.dummy;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ProjectExplorer extends JPanel implements IExplorer {

	private static final long serialVersionUID = 4932799790563214089L;
	private ProjectContainer container;
	private JList list;
	private DefaultListModel model;
	private String projectName;

	public ProjectExplorer() {
		model = new DefaultListModel();
		list = new JList(model);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setPreferredSize(new Dimension(100,100));
		//this.add(list,BorderLayout.CENTER);
		list.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JList l = (JList) e.getSource();
					int index = l.locationToIndex(e.getPoint());
					try {
						container.openEditor(projectName, (String)model.getElementAt(index), true, false);
					} catch (UniformAppletException ex) {}
				}
			}

			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		JScrollPane scroll = new JScrollPane(list);
		this.setLayout(new BorderLayout());
		this.add(scroll, BorderLayout.CENTER);
	}
	
	

	public void addFile(String projectName, String fileName) {
		this.projectName = projectName;
		if(!model.contains(fileName)) {
			model.addElement(fileName);
		}
	}

	public void setProjectContainer(ProjectContainer pContainer) {
		this.container = pContainer;
	}

	public void addProject(String projectName) {
	}



	public void setActiveProject(String projectName) {
	}



	public void closeProject(String projectName) {
	}



	public List<String> getAllProjects() {
		return new ArrayList<String>();
	}



	public List<String> getFilesByProject(String projectName) {
		if(!this.projectName.equals(projectName)) {
			return null;
		}
		
		List<String> fileNames = new ArrayList<String>();
		for(int index = 0; index < model.size(); index++) {
			String name = (String) model.getElementAt(index);
			fileNames.add(name);
		}
		return fileNames;
	}



	public void removeFile(String projectName, String fileName) {
		if(!this.projectName.equals(projectName)) {
			return;
		}
		model.removeElement(fileName);
	}



	public void removeProject(String projectName) {
		for(String name : getFilesByProject(projectName)) {
			removeFile(projectName, name);
		}
	}



	public String getActiveProject() {
		return projectName;
	}
	
}