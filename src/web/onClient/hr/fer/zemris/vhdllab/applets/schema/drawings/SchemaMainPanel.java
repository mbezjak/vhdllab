package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.schema.SComponentBar;
import hr.fer.zemris.vhdllab.applets.schema.SOptionBar;
import hr.fer.zemris.vhdllab.applets.schema.SPropertyBar;
import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.components.misc.Sklop_PORT;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;
import hr.fer.zemris.vhdllab.applets.schema.wires.SPair;
import hr.fer.zemris.vhdllab.applets.schema.wires.SchemaWireException;
import hr.fer.zemris.vhdllab.applets.schema.wires.SimpleSchemaWire;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire.WireConnection;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Ovaj enkapsulira kompletni UI, ukljucujuci i canvas.
 * 
 * @author Axel
 *
 */

public class SchemaMainPanel extends JPanel implements IEditor {
	public static final int DEFAULT_CURSOR_TYPE = 1;
	public static final int CROSSHAIR_CURSOR_TYPE = 2;
	public static final int DRAW_WIRE_STATE_NOTHING = 0;
	public static final int DRAW_WIRE_STATE_DRAWING = 1;
	
	class PopupListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupChoicesMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
    }
	
	private SComponentBar compbar;
	private SOptionBar optionbar;
	private SPropertyBar propbar;
	private JScrollPane scrpan;
	private JPanel optionpanel, canvaspanel;
	public SchemaDrawingCanvas drawingCanvas;
	private int freePlaceEvalCounter = 0;
	private boolean lastEvalResult = false;
	private int drawWireState = DRAW_WIRE_STATE_NOTHING;
	private SPair<Point> currentLine = null;
	private SPair<Point> lastLine = null;
	private AbstractSchemaWire wireBeingDrawed = null;
	private AbstractSchemaWire wireSelected = null;
	private JPopupMenu popupChoicesMenu = null;
	private JPopupMenu popupRenameWireMenu = null;
	private JPopupMenu popupWirePropertiesMenu = null;
	private JTextField rwmTextField = null;
	private Point nodeConfirmed = null;
	private boolean tStateVert = false;
	private CircuitInterface circuitInterface = null;
	//private SchemaModelledComponentEntity entity = null; ovo ipak necemo trebat, koristit cemo CircuitInterface

	public SchemaMainPanel() throws HeadlessException {
		
		// ovo cemo maknut kad pretvorimo ovo u panel
		
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// ovo ce trebat maknut
		//init();
	}
	
	private boolean checkForIntersection(int x1, int y1, AbstractSchemaComponent c1, SchemaDrawingComponentEnvelope env) {
		SchemaDrawingAdapter adapter = drawingCanvas.getAdapter();
		int x2 = x1 + adapter.virtualToReal(c1.getComponentWidthSpecific());
		int y2 = y1 + adapter.virtualToReal(c1.getComponentHeightSpecific());
		int xs = env.getPosition().x;
		int ys = env.getPosition().y;
		int xe = xs + adapter.virtualToReal(env.getComponent().getComponentWidthSpecific());
		int ye = ys + adapter.virtualToReal(env.getComponent().getComponentHeightSpecific());
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
	
	private boolean evaluateIfWireCanBeDrawn() {
		// ovdje ide provjera da li zica ide preko neke komponente, itd.
		// ukoliko je to potrebno
		return true;
	}
	
	public void recreateComponentBar(ArrayList<String> cmplist) {
		if (cmplist == null) compbar.remanufactureAllComponents();
		else compbar.remanufactureComponents(cmplist);
	}
	
	private void deleteComponent(String toBeRemoved) {
		schemaHasBeenModified = true;
		if (projectContainer != null && schemaFile != null) 
			projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
		if (toBeRemoved == null) return;
		if (drawingCanvas.removeComponentInstance(toBeRemoved)) {
			propbar.showNoProperties();
		}
		
		// pretrazi sve zice i makni im konekcije na ovu komponentu
		ArrayList<AbstractSchemaWire> wlist = drawingCanvas.getWireList();
		for (AbstractSchemaWire wire : wlist) {
			HashSet<WireConnection> subset = new HashSet<WireConnection>();
			for (WireConnection conn : wire.connections) {
				System.out.println(conn.componentInstanceName);
				if (conn.componentInstanceName.compareTo(toBeRemoved) == 0) {
					subset.add(conn);
				}
			}
			wire.connections.removeAll(subset);
//			System.out.print(wire.getWireName() + " - ");
//			for (WireConnection conn : wire.connections) {
//				System.out.println("(" + conn.componentInstanceName + ", " + conn.portIndex + ") ");
//			}
		}
	}
	
	private void deleteWire() {
		schemaHasBeenModified = true;
		if (projectContainer != null && schemaFile != null) 
			projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
		if (wireSelected != null) {
			drawingCanvas.removeWire(wireSelected.getWireName());
			wireSelected = null;
			drawingCanvas.setSelectedWireName(null);
		}
	}
	
	private Integer findPortIndexClosestToXY(SchemaDrawingComponentEnvelope env, int x, int y) {
		SchemaDrawingAdapter ad = drawingCanvas.getAdapter();
		AbstractSchemaComponent comp = env.getComponent();
		x = ad.realToVirtualRelativeX(x);
		y = ad.realToVirtualRelativeY(y);
		int cx = ad.realToVirtualRelativeX(env.getPosition().x), cy = ad.realToVirtualRelativeY(env.getPosition().y);
		int minDist = comp.getComponentHeightSpecific();
		int dist = 0;
		Integer minPort = null;
		for (int i = 0; i < comp.getNumberOfPorts(); i++) {
			AbstractSchemaPort port = comp.getSchemaPort(i);
			if (port.getTipPorta() == "_boxing") continue;
			int px = port.getCoordinate().x, py = port.getCoordinate().y;
			dist = (int) Math.sqrt((cx + px - x) * (cx + px - x) + (cy + py - y) * (cy + py - y));
			//System.out.println(dist + " " + minDist + " " + port.getCoordinate().x + " " + port.getCoordinate().y + " " + cx + " " + cy + " " + x + " " + y);
			if (dist < minDist) {
				minDist = dist;
				minPort = i;
			}
		}
		return minPort;
	}
	
	private boolean checkIfThisPortIsAlreadyOccupied(String s, Integer portInd) {
		// ovo se moze rijesiti i pracenjem svih connectiona svih zica, ali buduci da ne
		// ocekujemo jako velik broj zica, zasad moze i ovako
		if (wireBeingDrawed != null) {
			for (WireConnection conn : wireBeingDrawed.connections) {
				if (conn.componentInstanceName.compareTo(s) == 0 && conn.portIndex == portInd)
					return true;
			}
		}
		ArrayList<AbstractSchemaWire> wlist = drawingCanvas.getWireList();
		for (AbstractSchemaWire wire : wlist) {
			for (WireConnection conn : wire.connections) {
				if (conn.componentInstanceName.compareTo(s) == 0 && conn.portIndex == portInd)
					return true;
			}
		}
		return false;
	}
	
	private boolean checkIfPointIsWireEnd(AbstractSchemaWire wire, int x, int y) {
		Point expand = new Point(x, y);
		boolean found = false;
		for (SPair<Point> lin : wire.wireLines) {
			System.out.println("Hej dzo!" + x + " " + y + " " + lin.first.x + " " + lin.first.y + " " + lin.second.x + " " + lin.second.y);
			if (lin.first.equals(expand) || lin.second.equals(expand)) {
				if (found) return false;
				found = true;
			}
		}
		return found;
	}
	
	private boolean almostEqual(int a, int b) {
		if (Math.abs(a - b) <= 2) return true;
		return false;
	}
	
	private boolean coordinateWithinLine(int x, int y, SPair<Point> line) {
		//System.out.println("x = " + x + "; y = " + y + ";");
		//System.out.println("linija: (" + line.first.x + ", " + line.first.y + ") (" + line.second.x + ", " + line.second.y + ")");
		if (
				(x == line.first.x && x == line.second.x 
				&& ((y >= line.first.y && y <= line.second.y) || (y <= line.first.y && y >= line.second.y)))
				|| 
				(y == line.first.y && y == line.second.y
				&& ((x >= line.first.x && x <= line.second.x) || (x <= line.first.x && x >= line.second.x)))
				)
						return true;
		return false;
	}
	
	private AbstractSchemaWire extractWireAt(int x, int y) {
		ArrayList<AbstractSchemaWire> wlist = drawingCanvas.getWireList();
		AbstractSchemaWire wireToExtract = null;
		boolean exitLoop = false;
		for (AbstractSchemaWire wire : wlist) {
			for (SPair<Point> lin : wire.wireLines) {
				if (coordinateWithinLine(x, y, lin)) {
					System.out.println("Stisno si zicu! " + wire.getWireName());
					wireToExtract = wire;
					exitLoop = true;
					break;
				}
			}
			if (exitLoop) break;
		}
		return wireToExtract;
	}
	
	private void selectWire(MouseEvent e) {
		SchemaDrawingAdapter ad = drawingCanvas.getAdapter();
		int x = ad.realToVirtualRelativeX(e.getX());
		int y = ad.realToVirtualRelativeY(e.getY());
		wireSelected = extractWireAt(x, y);
		if (wireSelected != null) {
			drawingCanvas.setSelectedWireName(wireSelected.getWireName());
			propbar.showNoProperties();
		}
	}
	
	private void selectComponent(MouseEvent e) {
		AbstractSchemaComponent comp = drawingCanvas.getSchemaComponentAt(e.getX(), e.getY());
		if (comp != null) {
			propbar.generatePropertiesAndSetAsSelected(comp);
			drawingCanvas.setSelectedCompName(comp.getComponentInstanceName());
			wireSelected = null;
			drawingCanvas.setSelectedWireName(null);
		}
		else {
			drawingCanvas.setSelectedCompName(null);
			selectWire(e);			
		}
	}
	
	public void startWireExpansion(int x, int y, String wireName) {
		SchemaDrawingAdapter ad = drawingCanvas.getAdapter();
		x = ad.realToVirtualRelativeX(x);
		y = ad.realToVirtualRelativeY(y);
		ArrayList<AbstractSchemaWire> wlist = drawingCanvas.getWireList();
		AbstractSchemaWire extwire = null;
		for (AbstractSchemaWire wire : wlist) {
			if (wire.getWireName().compareTo(wireName) == 0) {
				extwire = wire;
			}
		}
		if (extwire != null) {
			wireBeingDrawed = extwire;
			drawingCanvas.removeWire(extwire.getWireName());
			if (!checkIfPointIsWireEnd(extwire, x, y)) {
				nodeConfirmed = new Point(x, y);
				if (!wireBeingDrawed.nodes.contains(nodeConfirmed)) wireBeingDrawed.nodes.add(nodeConfirmed);
				else nodeConfirmed = null;
			}
			currentLine = new SPair<Point>();
			currentLine.first = new Point(x, y);
			optionbar.selectDrawWire();
			drawWireState = DRAW_WIRE_STATE_DRAWING;
		}
	}
	
	private void renameWire(String oldName, String newName) {
		schemaHasBeenModified = true;
		if (projectContainer != null && schemaFile != null) 
			projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
		ArrayList<AbstractSchemaWire> wlist = drawingCanvas.getWireList();
		AbstractSchemaWire wire = null;
		for (int i = 0; i < wlist.size(); i++) {
			if (wlist.get(i).getWireName().compareTo(oldName) == 0) {
				wire = wlist.get(i);
				break;
			}
		}
		if (wire != null) {
			try {
				wire.setWireName(newName);
				//System.out.println("Hej boj!" + newName + " " + oldName);
			} catch (SchemaWireException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ActionedWireRenamer implements ActionListener {
		private String oldName;
		private JTextField tf;
		public ActionedWireRenamer(String wireName, JTextField tfield) {
			oldName = wireName;
			tf = tfield;
		}
		public void actionPerformed(ActionEvent ae) {
			// preimenuj zicu
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			renameWire(oldName, tf.getText());
		}
	}
	
	private class KeyedWireRenamer implements KeyListener {
		private String wname;
		public KeyedWireRenamer(String wireName) {
			wname = wireName;
		}
		public void keyTyped(KeyEvent arg0) {
			if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
				schemaHasBeenModified = true;
				if (projectContainer != null && schemaFile != null) 
					projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
				renameWire(wname, rwmTextField.getText());
			}
		}
		public void keyPressed(KeyEvent arg0) {
		}
		public void keyReleased(KeyEvent arg0) {
		}
	}
	
	public void createRenameWirePopup(String wireName, int xp, int yp) {
		popupRenameWireMenu.removeAll();
		
		rwmTextField = new JTextField(wireName);
		rwmTextField.addKeyListener(new KeyedWireRenamer(wireName));
		popupRenameWireMenu.add(rwmTextField);
		
		JButton butt = new JButton("Rename");
		butt.addActionListener(new ActionedWireRenamer(wireName, rwmTextField));
		popupRenameWireMenu.add(butt);
		
		popupRenameWireMenu.show(drawingCanvas, xp, yp);
	}
	
	private class DeleteComponentListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			deleteComponent(propbar.getSelectedComponentInstanceName());
		}
	}
	
	private class DeleteWireListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			deleteWire();
		}
	}
	
	private class RenameWireListener implements ActionListener {
		private String oldName;
		private int xp, yp;
		public RenameWireListener(String wireName, int x, int y) {
			oldName = wireName;
			xp = x;
			yp = y;
		}
		public void actionPerformed(ActionEvent ae) {
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			createRenameWirePopup(oldName, xp, yp);
		}
	}
	
	private class ExpandWireListener implements ActionListener {
		private int xp, yp;
		private String wireName;
		public ExpandWireListener(int x, int y, String wireInstanceName) {
			xp = x;
			yp = y;
			wireName = wireInstanceName;
		}
		public void actionPerformed(ActionEvent ae) {
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			startWireExpansion(xp, yp, wireName);
		}
	}
	
	
	
	private class RemoveConnectionListener implements ActionListener {
		private WireConnection connection;
		private AbstractSchemaWire wire;
		private int xp, yp;
		public RemoveConnectionListener(AbstractSchemaWire w, WireConnection conn, int x, int y) {
			connection = conn;
			wire = w;
			xp = x;
			yp = y;
		}
		public void actionPerformed(ActionEvent ae) {
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			wire.connections.remove(connection);
			createWirePropertiesPopup(wire, xp, yp);
			popupWirePropertiesMenu.validate();
			popupWirePropertiesMenu.repaint();
		}
	}
	
	private void createWirePropertiesPopup(AbstractSchemaWire wire, int x, int y) {
		popupWirePropertiesMenu.removeAll();
		popupWirePropertiesMenu.setLayout(new GridLayout(0, 2, 2, 2));
		
		JLabel lab = new JLabel(" Wire name:  ");
		popupWirePropertiesMenu.add(lab);
		
		lab = new JLabel(wire.getWireName());
		popupWirePropertiesMenu.add(lab);
		
		lab = new JLabel(" Connection list: ");
		popupWirePropertiesMenu.add(lab);
		
		lab = new JLabel((wire.connections.size() > 0) ? "" : "(empty)");
		popupWirePropertiesMenu.add(lab);
		
		JButton butt = null;
		for (WireConnection conn : wire.connections) {
			lab = new JLabel(" " + conn.componentInstanceName + "; (" + conn.portCoord.x  + ",  " + conn.portCoord.y + ") ");
			popupWirePropertiesMenu.add(lab);
			butt = new JButton("Remove");
			butt.addActionListener(new RemoveConnectionListener(wire, conn, x, y));
			popupWirePropertiesMenu.add(butt);
		}
		
		popupWirePropertiesMenu.show(drawingCanvas, x, y);
	}
	
	private class WirePropertiesListener implements ActionListener {
		AbstractSchemaWire wire;
		int x, y;
		public WirePropertiesListener(AbstractSchemaWire wire, int x, int y) {
			this.wire = wire;
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent arg0) {
			createWirePropertiesPopup(wire, x, y);
		}
	}
	
	private void createChoicesPopup(MouseEvent e) {
		deletePopup(e);
		selectWire(e);
		if (wireSelected != null) {
			JMenuItem item = new JMenuItem("Rename...");
			item.addActionListener(new RenameWireListener(wireSelected.getWireName(), e.getX(), e.getY()));
			popupChoicesMenu.add(item);
			
			item = new JMenuItem("Expand");
			item.addActionListener(new ExpandWireListener(e.getX(), e.getY(), wireSelected.getWireName()));
			popupChoicesMenu.add(item);
			
			item = new JMenuItem("Delete");
			item.addActionListener(new DeleteWireListener());
			popupChoicesMenu.add(item);
			
			item = new JMenuItem("Properties");
			item.addActionListener(new WirePropertiesListener(wireSelected, e.getX(), e.getY()));
			popupChoicesMenu.add(item);
			
			popupChoicesMenu.show(e.getComponent(), e.getX(), e.getY());
		}
		else {
			selectComponent(e);
			if (propbar.getSelectedComponentInstanceName() != null) {
				JMenuItem item = new JMenuItem("Delete");
				item.addActionListener(new DeleteComponentListener());
				popupChoicesMenu.add(item);
				
				popupChoicesMenu.show(e.getComponent(), e.getX(), e.getY());
			}
			else {
				JMenuItem item = new JMenuItem("(none)");
				popupChoicesMenu.add(item);
				
				popupChoicesMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	private void deletePopup(MouseEvent e) {
		if (popupChoicesMenu != null) {
			popupChoicesMenu.removeAll();
		}
	}
	
	
	// methods called by events
	
	public void handleLeftClickOnSchema(MouseEvent e) {
		deletePopup(e);
		if (optionbar.isDrawWireSelected()) {
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			SchemaDrawingAdapter ad = drawingCanvas.getAdapter();
			int x = e.getX(), y = e.getY();
			x = ad.realToVirtualRelativeX(x);
			y = ad.realToVirtualRelativeY(y);
			if (drawWireState == DRAW_WIRE_STATE_NOTHING) {
				lastLine = null;
				wireBeingDrawed = new SimpleSchemaWire("Wire0");
				drawWireState = DRAW_WIRE_STATE_DRAWING;
				currentLine = new SPair<Point>();
				currentLine.first = new Point(x, y);
				
				// spajanja s komponentama
				SchemaDrawingComponentEnvelope env = drawingCanvas.getSchemaComponentEnvelopeAt(e.getX(), e.getY());
				if (env != null) {
					Integer portIndex = findPortIndexClosestToXY(env, e.getX(), e. getY());
					if (portIndex != null) {
						//System.out.println("Hej nasel sam ga!");
						if (!checkIfThisPortIsAlreadyOccupied(env.getComponent().getComponentInstanceName(), portIndex)) {
							WireConnection conn = wireBeingDrawed.new WireConnection(env.getComponent().getComponentInstanceName(), portIndex,
									new Point(env.getComponent().getSchemaPort(portIndex).getCoordinate()));
							wireBeingDrawed.connections.add(conn);
							currentLine.first.x = ad.realToVirtualRelativeX(env.getPosition().x) + 
								env.getComponent().getSchemaPort(portIndex).getCoordinate().x;
							currentLine.first.y = ad.realToVirtualRelativeX(env.getPosition().y) + 
								env.getComponent().getSchemaPort(portIndex).getCoordinate().y;
						}
					}
				}
				return;
			}
			if (drawWireState == DRAW_WIRE_STATE_DRAWING) {
				// spajanja s komponentama
				SchemaDrawingComponentEnvelope env = drawingCanvas.getSchemaComponentEnvelopeAt(e.getX(), e.getY());
				boolean over = false;
				if (env != null) {
					//System.out.println("Opet");
					Integer portIndex = findPortIndexClosestToXY(env, e.getX(), e. getY());
					if (portIndex != null) {
						if (!checkIfThisPortIsAlreadyOccupied(env.getComponent().getComponentInstanceName(), portIndex)) {
							WireConnection conn = wireBeingDrawed.new WireConnection(env.getComponent().getComponentInstanceName(), portIndex,
									new Point(env.getComponent().getSchemaPort(portIndex).getCoordinate()));
							wireBeingDrawed.connections.add(conn);
							x = ad.realToVirtualRelativeX(env.getPosition().x) + env.getComponent().getSchemaPort(portIndex).getCoordinate().x;
							y = ad.realToVirtualRelativeX(env.getPosition().y) + env.getComponent().getSchemaPort(portIndex).getCoordinate().y;
							over = true;
						}
					}
				}
				
				currentLine.second = new Point(x, y);
				SPair<Point> t = new SPair<Point>();
				SPair<Point> s = new SPair<Point>();
				if (lastLine != null) { 
					if (tStateVert) {
						if (lastLine.first.y > lastLine.second.y) {
							if (currentLine.first.y < currentLine.second.y) tStateVert = false;
						} else {
							if (currentLine.first.y > currentLine.second.y) tStateVert = false;
						}
					} else {
						if (lastLine.first.x < lastLine.second.x) {
							if (currentLine.first.x > currentLine.second.x) tStateVert = true;
						} else {
							if (currentLine.first.x < currentLine.second.x) tStateVert = true;
						}
					}
				}
				if (tStateVert) {
					t.first = new Point(currentLine.first);
					t.second = new Point(currentLine.first.x, currentLine.second.y);
					s.first = new Point(currentLine.first.x, currentLine.second.y);
					s.second = new Point(currentLine.second);
					tStateVert = false;
				} else {
					t.first = new Point(currentLine.first);
					t.second = new Point(currentLine.second.x, currentLine.first.y);
					s.first = new Point(currentLine.second.x, currentLine.first.y);
					s.second = new Point(currentLine.second);
					tStateVert = true;
				}
				if (!t.first.equals(t.second)) wireBeingDrawed.wireLines.add(t);
				if (!s.first.equals(s.second)) wireBeingDrawed.wireLines.add(s);
				lastLine = new SPair<Point>();
				lastLine.first = new Point(s.first);
				lastLine.second = new Point(s.second);
				//System.out.println(lastLine.first.x + " " + lastLine.first.y + " " + lastLine.second.x + " " + lastLine.second.y);
				currentLine.first = currentLine.second;
				if (nodeConfirmed != null) nodeConfirmed = null;
				if (over) handleRightClickOnSchema(e);
			}
			return;
		}
		String selectedInstStr = compbar.getSelectedComponentName();
		if (selectedInstStr != null) {
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			if (!evaluateIfPlaceIsFreeForSelectedComponent(e.getX(), e.getY())) return;
			SchemaDrawingAdapter ad = drawingCanvas.getAdapter();
			try {
				AbstractSchemaComponent compi = ComponentFactory.getSchemaComponent(selectedInstStr);
				drawingCanvas.addComponent(compi, 
						new Point(ad.virtualToRealRelativeX(ad.realToVirtual(e.getX())),
								ad.virtualToRealRelativeY(ad.realToVirtual(e.getY()))) );
				propbar.generatePropertiesAndSetAsSelected(compi);
			} catch (ComponentFactoryException e1) {
				System.out.println("Nemoguce izgenerirati novu komponentu - " +
						"ComponentFactory ne prepoznaje ime." + selectedInstStr);
				e1.printStackTrace();
			}
		} else {
			selectComponent(e);
		}
	}
	
	public void handleMouseDownOnSchema(MouseEvent e) {
	}
	
	public void handleMouseOverSchema(MouseEvent e) {
		if (optionbar.isDrawWireSelected()) {
			if (currentLine != null) {
				SchemaDrawingAdapter ad = drawingCanvas.getAdapter();
				int x = ad.realToVirtualRelativeX(e.getX()), y = ad.realToVirtualRelativeY(e.getY());
				for (SPair<Point> lin : wireBeingDrawed.wireLines) {
					drawingCanvas.addLineToStack(
							lin.first.x,
							lin.first.y,
							lin.second.x,
							lin.second.y,
							evaluateIfWireCanBeDrawn());
				}
				currentLine.second = new Point(x, y);
				drawingCanvas.addLineToStack(
						currentLine.first.x,
						currentLine.first.y,
						currentLine.second.x,
						currentLine.second.y,
						evaluateIfWireCanBeDrawn());
			}
			// uokviri sklop iznad kojeg je kursor
			SchemaDrawingComponentEnvelope env = drawingCanvas.getSchemaComponentEnvelopeAt(e.getX(), e.getY());
			if (env != null)
				drawingCanvas.addRectToStack(env.getPosition().x, env.getPosition().y, 
						env.getComponent().getComponentWidthSpecific(),
						env.getComponent().getComponentHeightSpecific(), true);
			return;
		}
		String selectedStr = compbar.getSelectedComponentName();
		if (selectedStr != null) {
			//iteriraj kroz listu i odluci da li sklop prekriva neki vec postojeci
			//medutim, nemoj to raditi precesto - ucestalost neka bude obrnuto proporcionalna
			//broju sklopova na shemi (druga bi varijanta bio drvo ili hashmapa s modificiranim
			//equals i compare, al to je prevec posla)
			freePlaceEvalCounter++;
			boolean ok = lastEvalResult;
			if (drawingCanvas.getComponentList().size() == 0) ok = true;
			else if (freePlaceEvalCounter % drawingCanvas.getComponentList().size() == 0)
				ok = evaluateIfPlaceIsFreeForSelectedComponent(e.getX(), e.getY());
			
			drawingCanvas.addRectToStack(e.getX(), e.getY(), 
					compbar.getSelectedComponent().getComponentWidthSpecific(),
					compbar.getSelectedComponent().getComponentHeightSpecific(), ok);
		}
	}

	public void handleRightClickOnSchema(MouseEvent e) {
		if (nodeConfirmed != null) {
			if (wireBeingDrawed != null) wireBeingDrawed.nodes.remove(nodeConfirmed);
			nodeConfirmed = null;
		}
		boolean noPopup = false;
		if (optionbar.isDrawWireSelected()) {
			if (wireBeingDrawed != null && wireBeingDrawed.wireLines.size() > 0) {
				drawingCanvas.addWire(wireBeingDrawed);
			}
			optionbar.selectNoOption();
			wireBeingDrawed = null;
			currentLine = null;
			drawWireState = DRAW_WIRE_STATE_NOTHING;
			noPopup = true;
		}
		if (compbar.getSelectedComponentName() != null) noPopup = true;
		drawWireState = 0;
		compbar.selectNone();
		drawingCanvas.setSelectedCompName(null);
		propbar.showNoProperties();
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		wireSelected = null;
		drawingCanvas.setSelectedWireName(null);
		if (!noPopup) createChoicesPopup(e);
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
			schemaHasBeenModified = true;
			if (projectContainer != null && schemaFile != null) 
				projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
			deleteComponent(propbar.getSelectedComponentInstanceName());
			deleteWire();
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
	
	public void handleComponentPropertyChanged(String cmpInstName) {
		schemaHasBeenModified = true;
		if (projectContainer != null && schemaFile != null) 
			projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
		
		// nadi komponentu koja je promijenjena
		AbstractSchemaComponent comp = null;
		Point pcomp = null;
		ArrayList<SchemaDrawingComponentEnvelope> clist = drawingCanvas.getComponentList();
		for (SchemaDrawingComponentEnvelope env : clist) {
			if (env.getComponent().getComponentInstanceName().compareTo(cmpInstName) == 0) {
				comp = env.getComponent();
				pcomp = env.getPosition();
			}
		}
		if (comp == null) return;
		SchemaDrawingAdapter ad = drawingCanvas.getAdapter();
		pcomp = new Point(pcomp);
		pcomp.x = ad.realToVirtualRelativeX(pcomp.x);
		pcomp.y = ad.realToVirtualRelativeX(pcomp.y);
		// prodi sve zice i ako referenciraju ovu komponentu, prilagodi ih po potrebi
		// makni im konekcije ako taj port ne postoji
		// a ako mu se promijenila koordinata, onda produzi zicu tako da su spojeni
		ArrayList<AbstractSchemaWire> wlist = drawingCanvas.getWireList();
		for (AbstractSchemaWire wire : wlist) {
			HashSet<WireConnection> subset = new HashSet<WireConnection>();
			for (WireConnection conn : wire.connections) {
				if (conn.componentInstanceName.compareTo(cmpInstName) == 0) {
					if (conn.portIndex >= comp.getNumberOfPorts()) subset.add(conn);
					else {
						Point pch = comp.getSchemaPort(conn.portIndex).getCoordinate();
						if (!conn.portCoord.equals(pch)) {
							subset.add(conn);
//							SPair<Point> lin = new SPair<Point>();
//							lin.first = new Point(pcomp.x + conn.portCoord.x, pcomp.y + conn.portCoord.y);
//							lin.second = new Point(pcomp.x + pch.x, pcomp.y + pch.y);
//							SPair<Point> t = new SPair<Point>();
//							t.first = new Point(lin.first.x, lin.second.y);
//							t.second = new Point(lin.second.x, lin.second.y);
//							lin.second.x = lin.first.x;
//							wire.wireLines.add(lin);
//							conn.portCoord = new Point(pch);
						}
					}
				}
			}
			wire.connections.removeAll(subset);
		}
	}
	
	public void handleComponentNameChanged(String oldName, String newName) {
		schemaHasBeenModified = true;
		if (projectContainer != null && schemaFile != null) 
			projectContainer.resetEditorTitle(true, schemaFile.getProjectName(), schemaFile.getFileName());
		
		// prodi sve zice i ako referenciraju komponentu starog imena, prilagodi ih po potrebi
		ArrayList<AbstractSchemaWire> wlist = drawingCanvas.getWireList();
		for (AbstractSchemaWire wire : wlist) {
			HashSet<WireConnection> subset = new HashSet<WireConnection>();
			for (WireConnection conn : wire.connections) {
				if (conn.componentInstanceName.compareTo(oldName) == 0) {
					subset.add(conn);
				}
			}
			wire.connections.removeAll(subset);
			for (WireConnection conn : subset) {
				conn.componentInstanceName = newName;
				wire.connections.add(conn);
			}
		}
	}	
	
	public CircuitInterface getCircuitInterface() {
		return circuitInterface;
	}

	/**
	 * Treba vidjeti koji sklopovi su vec na platnu, a koji nisu,
	 * te skladno tome napraviti promjene na canvasu.
	 * @param interf
	 */
	public void setCircuitInterface(CircuitInterface interf) {
		List<Port> portlist = null;
		if (circuitInterface != null) {
			portlist = circuitInterface.getPorts();
			ArrayList<SchemaDrawingComponentEnvelope> complist = drawingCanvas.getComponentList();
			for (Port port : portlist) {
				String pname = port.getName();
				if (drawingCanvas.existComponent(pname)) {
					deleteComponent(pname);
				}
			}
		}
		portlist = interf.getPorts();
		int counterLeft = 1, counterRight = 1;
		for (Port port : portlist) {
			Sklop_PORT portsklop = new Sklop_PORT(port, interf);
			Point p = null;
			if (port.getDirection() == Direction.IN) {
				p = new Point(20, 10 * counterLeft);
				counterLeft += portsklop.getComponentHeight();
			} else if (port.getDirection() == Direction.OUT) {
				p = new Point(720, 10 * counterRight);
				counterRight += portsklop.getComponentHeight() + 2;
			}
			drawingCanvas.addComponent(portsklop, p);
		}
		circuitInterface = interf;
	}
	
	

	/**
	 * Ovdje se generira sadrzaj schematica - tj. sadrzaj drawingCanvasa i sadrzaj
	 * entitya.
	 *
	 */
	private void generateSchemaContentFromSchemaFile() {
		// sve sto se znalo o panelu sad se brise!
		drawingCanvas = new SchemaDrawingCanvas(new SchemaColorProvider(), this);
		circuitInterface = null;
		
		if (propbar != null) propbar.showNoProperties();
		
//		 TODO Ovdje treba izgenerirati shemu iz predanog filea, ali nikako ne i interface
		if (schemaFile != null) {
			// ovdje izgenerirati sadrzaj na temelju predanog filea
		}
	}
	
	private void refreshComponentsDependentOnCircuitInterface() {
		
	}
	
	
	/**
	 * Ova metoda vraca vhdl kod izgeneriran na temelju sklopa
	 * na shemi. Ona je tu samo privremeno.
	 * @return
	 */
	private String generateVHDLCode() {
		return null;
	}
	
	
	
	
	
	// OVO DALJE JE IMPLEMENTACIJA ZA IEditor
	
	private boolean schemaHasBeenModified = false;
	private ProjectContainer projectContainer = null;
	private FileContent schemaFile = null;
	private boolean miro_saveable = false;
	private boolean miro_readOnly = false;

	public void setFileContent(FileContent content) {
		schemaFile = content;
		generateSchemaContentFromSchemaFile();
	}

	public String getProjectName() {
		if (schemaFile != null) return schemaFile.getProjectName();
		return null;
	}

	public String getFileName() {
		if (schemaFile != null) return schemaFile.getFileName();
		return null;
	}

	public boolean isModified() {
		return schemaHasBeenModified;
	}

	public void setSavable(boolean flag) {
		miro_saveable = flag;
	}

	public boolean isSavable() {
		return miro_saveable;
	}

	public void setReadOnly(boolean flag) {
		miro_readOnly = flag;
	}

	public boolean isReadOnly() {
		return miro_readOnly;
	}

	public void highlightLine(int line) {
		// Ovu metodu po Mirinom nalogu ignoriramo. Hvaljen Isus.
	}
	
	public void init() {
		this.setLayout(new BorderLayout());
		
		generateSchemaContentFromSchemaFile();
		
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
		
		popupChoicesMenu = new JPopupMenu("Menu");
		popupRenameWireMenu = new JPopupMenu("Rename");
		popupWirePropertiesMenu = new JPopupMenu("Wire properties");
		
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
	
	// Tu se samo postavlja project container.
	public void setProjectContainer(ProjectContainer container) {
		projectContainer = container;
	}
	
	
	// ovo dvoje treba zavrsit
	
	// Ovo je Rajakovic reko da ce napravit - radi se o pohranjivanju u interni format, ako sam dobro shvatio.
	// Dio toga sam vec napravio - sto se tice serijalizacije pojedinih komponenti.
	public String getData() {
		// TODO Ovdje moramo izgenerirat podatke koje cemo vratit
		
		// stogod da tu napravio, moras promijeniti modification stanje u false
		schemaHasBeenModified = false;
		return null;
	}
	
	// logikom stvari, ovo Rajakovic mora napravit, jer on radi pohranu u interni format
	// U svakom slucaju, wizard za Schematic bi trebao izgenerirati sucelje sklopa koji se
	// modelira - dakle ulaze i izlaze, te njima pripadne tipove...
	public IWizard getWizard() {
		// TODO Auto-generated method stub
		return null;
	}
}








