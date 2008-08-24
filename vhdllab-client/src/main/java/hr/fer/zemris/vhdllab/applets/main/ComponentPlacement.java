package hr.fer.zemris.vhdllab.applets.main;

/**
 * Defines enums whose purpose is to identify which tabbed pane is to be used in
 * a component container.
 * 
 * @author Miro Bezjak
 */
public enum ComponentPlacement {
	/*
	 * This enum is directly liked to several properties. Therefor a change in
	 * the name of a property will need to result in change of these properties.
	 * They are located on server and are stored as global files. Also notice
	 * that enums are case sensitive!
	 * 
	 * For example: a property named "system.default.editor.placement" contains
	 * all values listed here for purpose of automatically placing editors to
	 * specified tabbed pane.
	 */

	LEFT, RIGHT, BOTTOM, CENTER;

}
