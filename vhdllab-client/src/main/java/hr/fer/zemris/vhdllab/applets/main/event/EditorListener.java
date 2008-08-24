/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main.event;

import java.util.EventListener;

/**
 * The listener interface for editors.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 12/9/2007
 */
public interface EditorListener extends EventListener {

	/**
	 * Indicates that editor is either modified or not (i.e. editor has been
	 * saved).
	 * 
	 * @param projectName
	 *            a name of a project that file belongs to
	 * @param fileName
	 *            a name of a file that editor represents
	 * @param flag
	 *            <code>true</code> if editor has been modified or
	 *            <code>false</code> otherwise (i.e. an editor has just been
	 *            saved)
	 */
	void modified(String projectName, String fileName, boolean flag);

}
