package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SComponentBar;
import hr.fer.zemris.vhdllab.applets.schema.SPropertyBar;
import hr.fer.zemris.vhdllab.applets.schema.SSchemaComponentBar;
import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/**
 * Ovaj enkapsulira kompletni UI, ukljucujuci i canvas.
 * 
 * @author Axel
 *
 */

public class SchemaMainFrame extends JFrame {
	public static final int DEFAULT_CURSOR_TYPE = 1;
	public static final int CROSSHAIR_CURSOR_TYPE = 2;
	
	private SComponentBar compbar;
	private SPropertyBar propbar;
	private JScrollPane scrpan;
	public SchemaDrawingCanvas drawingCanvas;

	public SchemaMainFrame(String arg0) throws HeadlessException {
		super(arg0);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setMinimumSize(new Dimension(350, 200));
		this.setSize(new Dimension(550, 400));
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		initUI();
		this.validate();
	}
	
	private void initUI() {
		drawingCanvas = new SchemaDrawingCanvas(new SchemaColorProvider(), this);
		compbar = new SComponentBar(this);
		propbar = new SPropertyBar();
		scrpan = new JScrollPane(drawingCanvas);
		this.add(compbar, BorderLayout.PAGE_START);
		this.add(propbar, BorderLayout.EAST);
		this.add(scrpan, BorderLayout.CENTER);
	}
	
	
	
	// methods called by events
	
	public void handleLeftClickOnSchema(MouseEvent e) {
		String selectedStr = compbar.getSelectedComponentName();
		if (selectedStr == null) {
			AbstractSchemaComponent comp = drawingCanvas.getSchemaComponentAt(e.getX(), e.getY());
			if (comp != null) propbar.generatePropertiesAndSetAsSelected(comp);
		} else {
			try {
				AbstractSchemaComponent compi = ComponentFactory.getSchemaComponent(selectedStr);
				drawingCanvas.addComponent(compi, e.getPoint());
				propbar.generatePropertiesAndSetAsSelected(compi);
			} catch (ComponentFactoryException e1) {
				System.out.println("Nemoguce izgenerirati novu komponentu - " +
						"ComponentFactory ne prepoznaje ime." + selectedStr);
				e1.printStackTrace();
			}
		}
	}
	
	public void handleMouseDownOnSchema(MouseEvent e) {
		
	}

	public void handleRightClickOnSchema(MouseEvent e) {
		compbar.selectNone();
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void changeCursor(int type) {
		if (type == DEFAULT_CURSOR_TYPE) {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		if (type == CROSSHAIR_CURSOR_TYPE) {
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
	
}
