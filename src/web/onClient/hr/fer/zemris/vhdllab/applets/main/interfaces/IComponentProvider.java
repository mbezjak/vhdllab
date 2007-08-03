package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;

/**
 * @author Miro Bezjak
 */
public interface IComponentProvider {

	/**
	 * Returns a status bar.
	 * 
	 * @return a status bar
	 */
	IStatusBar getStatusBar();

}