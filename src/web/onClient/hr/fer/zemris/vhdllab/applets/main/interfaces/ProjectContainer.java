package hr.fer.zemris.vhdllab.applets.main.interfaces;


import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.List;
import java.util.ResourceBundle;

public interface ProjectContainer {
	List<String> getAllCircuits(String projectName);
	CircuitInterface getCircuitInterfaceFor(String projectName, String fileName);
	String getOptions();
	ResourceBundle getResourceBundle();
	void openFile(String projectName, String fileName);
	boolean existsFile(String projectName, String fileName);
	boolean existsProject(String projectName);
}
