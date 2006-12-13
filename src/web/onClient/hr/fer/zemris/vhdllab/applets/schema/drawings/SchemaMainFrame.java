package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SComponentBar;
import hr.fer.zemris.vhdllab.applets.schema.SOptionBar;
import hr.fer.zemris.vhdllab.applets.schema.SPropertyBar;
import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.applets.schema.wires.SimpleSchemaWire;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Ovaj enkapsulira kompletni UI, ukljucujuci i canvas.
 * 
 * @author Axel
 *
 */

public class SchemaMainFrame extends JFrame {
	public static final int DEFAULT_CURSOR_TYPE = 1;
	public static final int CROSSHAIR_CURSOR_TYPE = 2;
	public static final int DRAW_WIRE_STATE_NOTHING = 0;
	public static final int DRAW_WIRE_STATE_DRAWING = 1;
	
	private SComponentBar compbar;
	private SOptionBar optionbar;
	private SPropertyBar propbar;
	private JScrollPane scrpan;
	private JPanel optionpanel, canvaspanel;
	public SchemaDrawingCanvas drawingCanvas;
	private int freePlaceEvalCounter = 0;
	private boolean lastEvalResult = false;
	private int drawWireState = DRAW_WIRE_STATE_NOTHING;
	private AbstractSchemaWire wireBeingDrawed = null;

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
		this.setLayout(new BorderLayout());
		
		drawingCanvas = new SchemaDrawingCanvas(new SchemaColorProvider(), this);
		
		optionpanel = new JPanel(new BorderLayout());
		canvaspanel = new JPanel(new BorderLayout());
		
		optionbar = new SOptionBar(this);
		compbar = new SComponentBar(this);
		propbar = new SPropertyBar(this);
		scrpan = new JScrollPane(drawingCanvas);
		
		optionpanel.add(optionbar, BorderLayout.PAGE_START);
		canvaspanel.add(compbar, BorderLayout.PAGE_START);
		canvaspanel.add(propbar, BorderLayout.EAST);
		canvaspanel.add(scrpan, BorderLayout.CENTER);
		
		this.add(canvaspanel, BorderLayout.CENTER);
		this.add(optionpanel, BorderLayout.NORTH);
		
		// tipke
		this.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			public void keyPressed(KeyEvent arg0) {
				handleKeyPressed(arg0);
			}

			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	private boolean checkForIntersection(int x1, int y1, AbstractSchemaComponent c1, SchemaDrawingComponentEnvelope env) {
		SchemaDrawingAdapter adapter = drawingCanvas.getAdapter();
		int x2 = x1 + adapter.virtualToReal(c1.getComponentWidth());
		int y2 = y1 + adapter.virtualToReal(c1.getComponentHeight());
		int xs = env.getPosition().x;
		int ys = env.getPosition().y;
		int xe = xs + adapter.virtualToReal(env.getComponent().getComponentWidth());
		int ye = ys + adapter.virtualToReal(env.getComponent().getComponentHeight());
		if (x1 >= xs && x1 <= xe && y1 >= ys && y1 <= ye) return true;
		if (x2 >= xs && x2 <= xe && y2 >= ys && y2 <= ye) return true;
		if (x1 >= xs && x1 <= xe && y2 >= ys && y2 <= ye) return true;
		if (x2 >= xs && x2 <= xe && y1 >= ys && y1 <= ye) return true;
		if (xs >= x1 && xs <= x2 && ys >= y1 && ys <= y2) return true;
		if (xe >= x1 && xe <= x2 && ye >= y1 && ye <= y2) return true;
		if (xe >= x1 && xe <= x2 && ys >= y1 && ys <= y2) return true;
		if (xs >= x1 && xs <= x2 && ye >= y1 && ye <= y2) return true;
		return false;
	}
	
	private boolean evaluateIfPlaceIsFreeForSelectedComponent(int x, int y) {
		ArrayList<SchemaDrawingComponentEnvelope> clist = drawingCanvas.getComponentList();
		for (SchemaDrawingComponentEnvelope env : clist) {
			if (checkForIntersection(x, y, compbar.getSelectedComponent(), env)) {
				lastEvalResult = false;
				return false;
			}
		}
		lastEvalResult = true;
		return true;
	}
	
	
	
	// methods called by events
	
	public void handleLeftClickOnSchema(MouseEvent e) {
		if (optionbar.isDrawWireSelected()) {
			if (drawWireState == DRAW_WIRE_STATE_NOTHING) {
				wireBeingDrawed = new SimpleSchemaWire(null);
				drawWireState = DRAW_WIRE_STATE_DRAWING;
				
			}
			if (drawWireState == DRAW_WIRE_STATE_DRAWING) {
				// TODO: dovrsiti ovo ovdje
			}
			return;
		}
		String selectedInstStr = compbar.getSelectedComponentName();
		if (selectedInstStr == null) {
			AbstractSchemaComponent comp = drawingCanvas.getSchemaComponentAt(e.getX(), e.getY());
			if (comp != null) propbar.generatePropertiesAndSetAsSelected(comp);
			else return;
			drawingCanvas.setSelectedName(comp.getComponentInstanceName());
		} else {
			if (!evaluateIfPlaceIsFreeForSelectedComponent(e.getX(), e.getY())) return;
			try {
				AbstractSchemaComponent compi = ComponentFactory.getSchemaComponent(selectedInstStr);
				drawingCanvas.addComponent(compi, e.getPoint());
				propbar.generatePropertiesAndSetAsSelected(compi);
			} catch (ComponentFactoryException e1) {
				System.out.println("Nemoguce izgenerirati novu komponentu - " +
						"ComponentFactory ne prepoznaje ime." + selectedInstStr);
				e1.printStackTrace();
			}
		}
	}
	
	public void handleMouseDownOnSchema(MouseEvent e) {
	}
	
	public void handleMouseOverSchema(MouseEvent e) {
		String selectedStr = compbar.getSelectedComponentName();
		if (selectedStr != null) {
			//iteriraj kroz listu i odluci da li sklop prekriva neki vec postojeci
			//medutim, nemoj to raditi precesto - ucestalost neka bude obrnuto proporcionalna
			//broju sklopova na shemi (druga bi varijanta bio inteligentno drvo, al to je prevec posla)
			freePlaceEvalCounter++;
			boolean ok = lastEvalResult;
			if (drawingCanvas.getComponentList().size() == 0) ok = true;
			else if (freePlaceEvalCounter % drawingCanvas.getComponentList().size() == 0)
				ok = evaluateIfPlaceIsFreeForSelectedComponent(e.getX(), e.getY());
			
			drawingCanvas.addRectToStack(e.getX(), e.getY(), 
					compbar.getSelectedComponent().getComponentWidth(),
					compbar.getSelectedComponent().getComponentHeight(), ok);
		}
	}

	public void handleRightClickOnSchema(MouseEvent e) {
		if (optionbar.isDrawWireSelected()) {
			optionbar.selectNoOption();
			wireBeingDrawed = null;
			drawWireState = DRAW_WIRE_STATE_NOTHING;
		}
		drawWireState = 0;
		compbar.selectNone();
		drawingCanvas.setSelectedName(null);
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
	
	public void handleKeyPressed(KeyEvent kev) {
		System.out.println("STISNUL SI: " + kev.getKeyChar());
		if (kev.getKeyChar() == KeyEvent.VK_DELETE) {
			String toBeRemoved = propbar.getSelectedComponentInstanceName();
			if (toBeRemoved != null) {
				if (drawingCanvas.removeComponentInstance(toBeRemoved)) {
					propbar.showNoProperties();
				}
			}
		}
	}
	
	public void handleDrawWiresSelected() {
		this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		compbar.selectNone();
	}
	
	
	public void handleComponentSelected() {
		optionbar.selectNoOption();
		drawWireState = DRAW_WIRE_STATE_NOTHING;
	}
}








