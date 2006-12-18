package hr.fer.zemris.vhdllab.applets.main.interfaces;

import java.awt.Component;


public interface IWizard {
	void setProjectContainer(ProjectContainer pContainer);
	FileContent getInitialFileContent(Component parent);
}
