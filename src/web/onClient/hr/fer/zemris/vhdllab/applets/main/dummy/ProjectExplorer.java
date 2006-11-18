package hr.fer.zemris.vhdllab.applets.main.dummy;

import hr.fer.zemris.vhdllab.applets.main.interfaces.Explorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

public class ProjectExplorer extends JPanel implements Explorer {

	private static final long serialVersionUID = 4932799790563214089L;
	private ProjectContainter container;
	private JList list;
	private DefaultListModel model;
	private String projectName;

	public ProjectExplorer() {
		model = new DefaultListModel();
		list = new JList(model);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setPreferredSize(new Dimension(100,100));
		this.add(list,BorderLayout.CENTER);
		list.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JList l = (JList) e.getSource();
					int index = l.locationToIndex(e.getPoint());
					container.openFile(projectName, (String)model.getElementAt(index));
				}
			}

			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
	}
	
	

	public void addFile(String projectName, String fileName) {
		this.projectName = projectName;
		model.addElement(fileName);
	}

	public void setProjectContainer(ProjectContainter pContainer) {
		this.container = pContainer;
	}
	
}