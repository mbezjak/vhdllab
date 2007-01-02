package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.model.FileContent;

import java.awt.Component;


public interface IWizard {
	void setProjectContainer(ProjectContainer pContainer);
	FileContent getInitialFileContent(Component parent);
}
