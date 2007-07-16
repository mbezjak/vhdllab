package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.model.FileContent;

/**
 * Interface that describes communication between MainApplet and instance that implemets this interface.
 */
public interface IEditor {
	
	/**
	 * Sets a FileContent that mostly represents internal format that is used in Editor.
	 * Note that {@linkplain hr.fer.zemris.vhdllab.constants.FileTypes#FT_VHDL_SOURCE} is also an
	 * "internal" format. This method is also used to initialize the component that uses internal
	 * format.
	 * @param content FileContent 
	 */
	void setFileContent(FileContent content);
	/**
	 * Returns an internal format.
	 * @return an internal format.
	 */
	String getData();
	String getProjectName();
	String getFileName();
	boolean isModified();
	void setSavable(boolean flag);
	boolean isSavable();
	void setReadOnly(boolean flag);
	boolean isReadOnly();
	void highlightLine(int line);
	void setProjectContainer(ProjectContainer container);
	void init();
	IWizard getWizard();
	void dispose();
}
