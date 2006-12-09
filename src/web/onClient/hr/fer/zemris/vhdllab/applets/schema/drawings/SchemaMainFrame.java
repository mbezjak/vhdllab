package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SComponentBar;
import hr.fer.zemris.vhdllab.applets.schema.SPropertyBar;
import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * Ovaj enkapsulira kompletni UI, ukljucujuci i canvas.
 * 
 * @author Axel
 *
 */

public class SchemaMainFrame extends JFrame {
	private SComponentBar compbar;
	private SPropertyBar propbar;
	public SchemaDrawingCanvas drawingCanvas;

	public SchemaMainFrame(String arg0) throws HeadlessException {
		super(arg0);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		initUI();
		this.validate();
	}
	
	private void initUI() {
		drawingCanvas = new SchemaDrawingCanvas(new SchemaColorProvider());
		compbar = new SComponentBar();
		propbar = new SPropertyBar();
		this.add(compbar, BorderLayout.PAGE_START);
		this.add(propbar, BorderLayout.EAST);
		this.add(drawingCanvas, BorderLayout.CENTER);
	}
	
}
