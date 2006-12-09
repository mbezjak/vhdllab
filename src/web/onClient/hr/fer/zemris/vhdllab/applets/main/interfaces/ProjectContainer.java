package hr.fer.zemris.vhdllab.applets.main.interfaces;


import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.List;
import java.util.ResourceBundle;

public interface ProjectContainer {
	List<String> getAllCircuits(String projectName) throws UniformAppletException;
	CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) throws UniformAppletException;
	String getOptions(String type) throws UniformAppletException;
	ResourceBundle getResourceBundle();
	void openEditor(String projectName, String fileName, boolean isSavable, boolean isReadOnly) throws UniformAppletException;
	void resetEditorTitle(boolean contentChanged, String projectName, String fileName);
	boolean existsFile(String projectName, String fileName) throws UniformAppletException;
	boolean existsProject(String projectName) throws UniformAppletException;
	IEditor getEditor(String projectName, String fileName) throws UniformAppletException;
}