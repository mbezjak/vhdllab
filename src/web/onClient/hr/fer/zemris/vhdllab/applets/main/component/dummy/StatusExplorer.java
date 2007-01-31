package hr.fer.zemris.vhdllab.applets.main.component.dummy;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusExplorer extends JPanel implements IView {

	private static final long serialVersionUID = -7253178039704067567L;
	
	public StatusExplorer() {
		JLabel label = new JLabel("This is status explorer area!");
		this.add(label, BorderLayout.CENTER);
		this.setBackground(Color.BLUE);
	}

	public void setData(Object data) {}

	public void setProjectContainer(ProjectContainer pContainer) {}
	
	public void appendData(Object data) {}
}
