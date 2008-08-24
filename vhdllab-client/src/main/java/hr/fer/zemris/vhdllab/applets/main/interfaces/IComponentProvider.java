package hr.fer.zemris.vhdllab.applets.main.interfaces;

import javax.swing.JTabbedPane;

import hr.fer.zemris.vhdllab.applets.main.ComponentPlacement;
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

	/**
	 * @param placement
	 * @return
	 */
	JTabbedPane getTabbedPane(ComponentPlacement placement);

}