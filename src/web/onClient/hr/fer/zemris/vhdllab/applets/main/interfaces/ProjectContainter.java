package hr.fer.zemris.vhdllab.applets.main.interfaces;

import java.util.List;
import java.util.ResourceBundle;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

public interface ProjectContainter {
	List<String> getAllCircuits();
	CircuitInterface getCircuitInterfaceFor(String projectName, String fileName);
	String getOptions();
	ResourceBundle getResourceBundle();
	void openFile(String projectName, String fileName);
}
