package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.ComponentPlacement;

import javax.swing.JTabbedPane;

/**
 * @author Miro Bezjak
 */
public interface IComponentProvider {

	JTabbedPane getTabbedPane(ComponentPlacement placement);

}