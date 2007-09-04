/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;

/**
 * @author Miro Bezjak
 */
public interface IExplicitSave {
	
	void save(IEditor editor, ISystemContainer container) throws UniformAppletException;

}
