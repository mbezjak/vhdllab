/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JComponent;

/**
 * Crtaca ploha komponenti sa svom pameti (reagira na misa, akcije, itd..)
 * @author Tommy
 *
 */
public class SchemaDrawingCanvas extends JComponent {
	
	// pravokutnik s boolean varijablom
	class RectWithBool {
		public Rectangle r;
		public boolean b;
	}
	
	// linija s boolean varijablom
	class LineWithBool {
		public int x1, y1, x2, y2;
		public boolean b;
	}

	private static final long serialVersionUID = 168392906688186429L;
	
	private Dimension dimension = null;
	private SchemaDrawingGrid grid = null;
	private ArrayList<SchemaDrawingComponentEnvelope> components = null;
	private ArrayList<AbstractSchemaWire> wires = null;
	private SchemaColorProvider colors = null;	
	private BufferedImage canvas = null;
	private SchemaDrawingCanvasListeners listeners = null;
	private SchemaMainPanel mainframe = null;
	private Stack<RectWithBool> rectangleStack = null;
	private Stack<LineWithBool> lineStack = null;
	private String selectedCompName;
	private String selectedWireName;
	
	public Point mousePosition;

	
	
	public SchemaDrawingCanvas(SchemaColorProvider colors, SchemaMainPanel parent) {
		mainframe = parent;
		components=new ArrayList<SchemaDrawingComponentEnvelope>();
		wires = new ArrayList<AbstractSchemaWire>();
		rectangleStack = new Stack();
		lineStack = new Stack();
		this.colors=colors;		
		initGUI();
		initListeners();
	}
	
	public SchemaDrawingCanvas(SchemaColorProvider colors, Dimension dimension, SchemaMainPanel parent){
		mainframe = parent;
		this.dimension=dimension;
		initGUI();
		initListeners();
	}
	
	public SchemaDrawingAdapter getAdapter(){
		return grid.getAdapter();
	}
			

	/**
	 * Inicijalizacija GUIa
	 */
	private void initGUI() {
		if(dimension==null){
			dimension=new Dimension(1280, 800);//ovo je samo privremeno...
		}
		
		this.canvas=new BufferedImage(dimension.width,dimension.height,BufferedImage.TYPE_3BYTE_BGR);
		this.grid=new SchemaDrawingGrid(colors,canvas);
		
		this.setLayout(new BorderLayout());
		this.add(grid, BorderLayout.CENTER);
	}
	
	/**
	 * Inicijalizacija listenera za ovaj razred
	 */
	private void initListeners() {
		listeners=new SchemaDrawingCanvasListeners(this);
	}
	
	
	/**
	 * Dodavanje komponente na crtacu povrsinu...
	 * @param component Komponenta koja se dodaje.
	 */	
	public void addComponent(AbstractSchemaComponent component, Point position){
		SchemaDrawingComponentEnvelope envelope=new SchemaDrawingComponentEnvelope(component,position);
		
		components.add(envelope);
		this.repaint();
	}
	
	public boolean existComponent(String componentInstanceName) {
		int posdel = -1;
		for (int i = 0; i < components.size(); i++) {
			if (componentInstanceName.compareTo(components.get(i).getComponent().getComponentInstanceName()) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public void addWire(AbstractSchemaWire wire) {
		SchemaDrawingAdapter adapter = this.getAdapter();
		wires.add(wire);
		this.repaint();
	}
	
	public void removeWire(String wireInstanceName) {
		int posdel = -1;
		for (int i = 0; i < wires.size(); i++) {
			if (wireInstanceName.compareTo(wires.get(i).getWireName()) == 0) {
				posdel = i;
				break;
			}
		}
		if (posdel != -1) {
			wires.remove(posdel);
		}
	}
	
	public ArrayList<SchemaDrawingComponentEnvelope> getComponentEnvList(){
		return this.components;
	}
	
	public ArrayList<AbstractSchemaWire> getWireList() {
		return this.wires;
	}
	
	/**
	 * Koristi se za iscrtavanje pravokutnika pri
	 * pozicioniranju komponente. Svi pravokutnici
	 * sa stoga iscrtat ce se tijekom sljedeceg
	 * poziva metode paintComponent - dakle samo jednom.
	 *
	 */
	public void addRectToStack(int x, int y, int w, int h, boolean ok) {
		RectWithBool rb = new RectWithBool();
		rb.r = new Rectangle(x, y, w, h);
		rb.b = ok;
		rectangleStack.push(rb);
	}
	
	/**
	 * Koristi se za iscrtavanje linija pri crtanju
	 * zica.
	 */
	public void addLineToStack(int x1, int y1, int x2, int y2, boolean ok) {
		LineWithBool lb = new LineWithBool();
		lb.x1 = x1;
		lb.y1 = y1;
		lb.x2 = x2;
		lb.y2 = y2;
		lb.b = ok;
		lineStack.push(lb);
	}
	
	/**
	 * Instanca sklopa koja je izabrana mora oko sebe imati pravokutnik.
	 * @return
	 */
	public String getSelectedCompName() {
		return selectedCompName;
	}

	public void setSelectedCompName(String selectedName) {
		this.selectedCompName = selectedName;
	}
	
	public String getSelectedWireName() {
		return selectedWireName;
	}

	public void setSelectedWireName(String selectedWireName) {
		this.selectedWireName = selectedWireName;
	}

	/**
	 * Metoda za iscrtavanje plohe
	 */
	protected void paintComponent(Graphics gr){
		super.paintComponent(gr);
		if(canvas==null){
			try {
				throw new Exception("Za sada jedan veliki problem - konstruktor ne radi kako spada");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		//transformiraj BufferedImage u nekaj korisno
		Graphics2D graph=(Graphics2D) canvas.getGraphics();
		
		//ocisti crtacu plohu
		graph.setColor(colors.GRID_BG);
		graph.fillRect(0,0,canvas.getWidth(),canvas.getHeight());		
		
		
		//graph.setColor(new Color(15,200,200));
		//graph.drawString(listeners.getX()+", "+listeners.getY(),listeners.getX(),listeners.getY());
		
		// ovo sam ja dodo, ti prouci i reci kaj mislis. Btw, ovo:
		// grid.getAdapter().drawLine(0, 0, 100, 100);
		// bi ovdje bar trebalo raditi, al ne radi.
		// Ono sto je zanimljivo - grid.getAdapter().drawLine(0, 0, 150, 100); radi savrseno.
		// 
		// Uglavnom, ti ovo promijeni kako mislis da je potrebno.
		SchemaDrawingAdapter adapter = grid.getAdapter();
		
		//mousePosition = new Point(listeners.getX(),listeners.getY());
		//grid.ShowCursorPoint(mousePosition);
		
		int sx = adapter.getStartingX(), sy = adapter.getStartingY();
		for (SchemaDrawingComponentEnvelope envelope : components) {
			Point p = envelope.getPosition();
			adapter.setStartingCoordinates(p.x, p.y);
			envelope.getComponent().setDrawingFrame(false);
			if (envelope.getComponent().getComponentInstanceName() == selectedCompName) {
				envelope.getComponent().setDrawingFrame(true);
			}
			envelope.getComponent().draw(adapter);
		}
		adapter.setStartingCoordinates(sx, sy);
		
		for (AbstractSchemaWire wire : wires) {
			if (wire.getWireName() == selectedWireName) {
				wire.setThickness(2);
				wire.draw(adapter);
				wire.setThickness(1);
			}
			else {
				wire.draw(adapter);
			}
		}
		
		// iscrtaj sve pravokutnike koji su dodani
		while (!rectangleStack.empty()) {
			RectWithBool rb = rectangleStack.pop();
			if (rb.b) graph.setColor(Color.GREEN);
			else graph.setColor(Color.RED);
			rb.r.width = adapter.virtualToReal(rb.r.width);
			rb.r.height = adapter.virtualToReal(rb.r.height);
			graph.draw(rb.r);
		}
		
		// iscrtaj sve linije koje su dodane
		while (!lineStack.empty()) {
			LineWithBool lb = lineStack.pop();
			if (lb.b) graph.setColor(Color.BLACK);
			else graph.setColor(Color.RED);
			lb.x1 = adapter.virtualToRealRelativeX(lb.x1);
			lb.y1 = adapter.virtualToRealRelativeY(lb.y1);
			lb.x2 = adapter.virtualToRealRelativeX(lb.x2);
			lb.y2 = adapter.virtualToRealRelativeY(lb.y2);
			graph.drawLine(lb.x1, lb.y1, lb.x2, lb.y2);
		}
		
		//iscrtaj BufferedImage
		gr.drawImage(canvas,0,0,canvas.getWidth(),canvas.getHeight(),null);
		
	}

	@Override
	public void repaint(Rectangle arg0) {
		super.repaint(arg0);
		grid.repaint();
	}
	
	public SchemaMainPanel getMainframe() {
		return mainframe;
	}

	public void setMainframe(SchemaMainPanel mainframe) {
		this.mainframe = mainframe;
	}

	public AbstractSchemaComponent getSchemaComponentAt(int x, int y) {
		SchemaDrawingAdapter adapter = grid.getAdapter();
		//x = adapter.realToVirtual(x);
		//y = adapter.realToVirtual(y);
		//System.out.println("Klik na virtualnu koordinatu " + x + ", " + y);
		for (SchemaDrawingComponentEnvelope env : components) {
			int xr = env.getPosition().x + adapter.virtualToReal(env.getComponent().getComponentWidthSpecific());
			int yr = env.getPosition().y + adapter.virtualToReal(env.getComponent().getComponentHeightSpecific());
			//System.out.println("Ovaj sklop jeste na koord'nati: " + env.getPosition().x + ", " + env.getPosition().y);
			//System.out.println("Ovaj sklop idje do koord'nate: " + xr + ", " + yr);
			if (x >= env.getPosition().x && x <= xr
					&& y >= env.getPosition().y && y <= yr)
			{
				return env.getComponent();
			}
		}
		
		return null;
	}
	public SchemaDrawingComponentEnvelope getSchemaComponentEnvelopeAt(int x, int y) {
		SchemaDrawingAdapter adapter = grid.getAdapter();
		//x = adapter.realToVirtual(x);
		//y = adapter.realToVirtual(y);
		//System.out.println("Klik na virtualnu koordinatu " + x + ", " + y);
		for (SchemaDrawingComponentEnvelope env : components) {
			int xr = env.getPosition().x + adapter.virtualToReal(env.getComponent().getComponentWidthSpecific());
			int yr = env.getPosition().y + adapter.virtualToReal(env.getComponent().getComponentHeightSpecific());
			//System.out.println("Ovaj sklop jeste na koord'nati: " + env.getPosition().x + ", " + env.getPosition().y);
			//System.out.println("Ovaj sklop idje do koord'nate: " + xr + ", " + yr);
			if (x >= env.getPosition().x && x <= xr
					&& y >= env.getPosition().y && y <= yr)
			{
				return env;
			}
		}
		
		return null;
	}
	
	public boolean removeComponentInstance(String componentInstanceName) {
		for (SchemaDrawingComponentEnvelope env : components) {
			if (env.getComponent().getComponentInstanceName() == componentInstanceName) {
				components.remove(env);
				return true;
			}
		}
		return false;
	}
}
