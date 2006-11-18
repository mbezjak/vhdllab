package hr.fer.zemris.vhdllab.applets.main.interfaces;


public interface IWizard {
	void setupWizard();
	void setProjectContainer(ProjectContainter pContainer);
	FileContent getInitialFileContent();
}
