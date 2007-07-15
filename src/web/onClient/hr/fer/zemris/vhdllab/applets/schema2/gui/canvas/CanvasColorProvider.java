package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import java.awt.Color;

public class CanvasColorProvider {
	
	/**
	 * The color of the canvas` background
	 */
	public static final Color CANVAS_BACKGROUND = Color.WHITE;
	
	/**
	 * The color of a grid dot
	 */
	public static final Color GRID_DOT = new Color(35,240,250);
	
	/**
	 * Color used for marking critical points on compoinent ports
	 */
	public static final Color CRITICAL_PORT = new Color(250, 150, 0);
	
	/**
	 * Color used for marking critical points on wires
	 */
	public static final Color CRITICAL_WIRE = Color.BLUE;
	
	
	//###############################SIGNAL LINES########################
	
	/**
	 * Color of signal lines to be added to the model
	 */
	public static final Color SIGNAL_LINE_TO_ADD = new Color(50, 167, 20);
	
	/**
	 * If the schematic determines that the signal cannot be added this color is used
	 *  to mark that line.
	 */
	public static final Color SIGNAL_LINE_ERROR = Color.RED;
	
	/**
	 * The color used to mark selected signal lines
	 */
	public static final Color SIGNAL_LINE_SELECTED = Color.GREEN;
	
	/**
	 * The color used for drawing in-model signal lines 
	 */
	public static final Color SIGNAL_LINE = Color.BLACK;
	
	
	//###########################COMPONENTS################################
	
	/**
	 * Color of a selected component
	 */
	public static final Color COMPONENT_SELECTED = Color.GREEN;
	
	/**
	 * Color of an in-model component
	 */
	public static final Color COMPONENT = Color.BLACK;
	
	/**
	 * Color of a component to be added to the model
	 */
	public static final Color COMPONENT_TO_ADD = Color.RED;
}
