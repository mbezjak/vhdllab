package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.DeleteComponentCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.DeleteWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.InstantiateComponentCommand;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

public class SchemaCanvas extends JPanel implements PropertyChangeListener, ISchemaCanvas {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -179843489371055255L;

	
	/**
	 * Image used to create double-buffer effect.
	 */
	private BufferedImage img = new BufferedImage(1, 1
			, BufferedImage.TYPE_3BYTE_BGR);
	
	/**
	 * Collection containing all components in design.
	 */
	private ISchemaComponentCollection components = new SimpleSchemaComponentCollection();
	
	/**
	 * Collection containing all wires in design.
	 */
	private ISchemaWireCollection wires = new SimpleSchemaWireCollection();

	/**
	 * controller na razini schema-editora
	 */
	private ISchemaController controller;
	
	/**
	 * controller na razini GUI-a
	 */
	private ILocalGuiController localController;
	
	/**
	 * Stanje u kojem se canvas nalazi.
	 */
	private ECanvasState state;
	
	/**
	 * komponenta koja se iscrtava kao komponenta za dodati
	 */
	private ISchemaComponent addComponentComponent = null;

	/**
	 * Lokacija komponente za dodati
	 */
	private int addComponentX = 0, addComponentY = 0;
	
	/**
	 * Sadržani svi podatci o zici za dodati...
	 */
	private WirePreLocator preLoc = null;
	
	/**
	 * trenutni critical point nad kojim je mis
	 */
	private CriticalPoint point = null;
	
	/**
	 * akcija za animaciju critical point-a
	 */
	private Decrementer decrementer = null;
	
	/**
	 * critical point sa kojeg je zica krenula
	 */
	private CriticalPoint wireBeginning = null;
	
	/**
	 * critical point gdije je zica zavrsila
	 */
	private CriticalPoint wireEnding = null;
	
	private Timer timer = null;
	
	//constrictors
	public SchemaCanvas(ISchemaCore core) {
		this();
		
		components = core.getSchemaInfo().getComponents();
		wires = core.getSchemaInfo().getWires();
		
		repaint();
	}
	
	/**
	 * Dummy constructor za testiranje panela
	 *
	 */
	public SchemaCanvas(ISchemaComponentCollection comp, ISchemaWireCollection wir) {
		this();
		components = comp;
		wires = wir;
		repaint();
	}


	public SchemaCanvas() {
		state = ECanvasState.MOVE_STATE;	//init state
		decrementer = new Decrementer(20);
		timer = new Timer(70,decrementer);
		
		this.addMouseListener(new Mouse1());
		this.addMouseMotionListener(new Mose2());
		this.setOpaque(true);
	}

	//##########################
	
	
	//#########################PROTECTED METHODS###################################
	
	/**
	 * Overriden method for painting the canvas.
	 */
	protected void paintComponent(Graphics g) {
		Insets in = this.getInsets();
		if(img == null) {
			img=new BufferedImage(this.getWidth()-in.left-in.right,this.getHeight()-in.left-in.right
					, BufferedImage.TYPE_3BYTE_BGR);
			drawComponents();
		} else {
			if (img.getHeight()!=this.getHeight()-in.left-in.right ||
					img.getWidth()!=this.getWidth()-in.left-in.right){
				img=new BufferedImage(this.getWidth()-in.left-in.right,this.getHeight()-in.left-in.right
						, BufferedImage.TYPE_3BYTE_BGR);
			}
			drawComponents();
			g.drawImage(img, in.left, in.right, img.getWidth(), img.getHeight(), null);
		}
	}
	

	//##################PRIVATE METHODS#############################

	/**
	 * Private method responsible for drawing all components and wires contained in components
	 * and wires collections.
	 *
	 */
	private void drawComponents() {
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		components = controller.getSchemaInfo().getComponents();
		wires = controller.getSchemaInfo().getWires(); 
		
		g.setColor(Color.WHITE);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		g.setColor(Color.BLACK);
		
		//TODO nacrtaj mrezu, pitaj za sensitivnost mreze, neznam, jos nesto, sjeti se!!
		
		
		Set<Caseless> names = components.getComponentNames();
		
		int sizeX = 0;
		int sizeY = 0;
		
		for(Caseless name: names){
			ISchemaComponent comp = components.fetchComponent(name);
			XYLocation componentLocation = components.getComponentLocation(name);
			g.translate(componentLocation.x, componentLocation.y);
			comp.getDrawer().draw(g);
			g.translate(-componentLocation.x, -componentLocation.y);
			
			String text = name.toString();
			Rectangle rect = components.getComponentBounds(name);
			FontMetrics fm = g.getFontMetrics();
			int tx=rect.x+rect.width/2-fm.stringWidth(text)/2;
			int ty=rect.y;
			g.drawString(text,tx,ty);

			if(rect.x + rect.width + 10 > sizeX)sizeX =rect.x + rect.width + 10;
			if(rect.y + rect.height + 10 > sizeY)sizeY =rect.y + rect.height + 10;
		}
		
		
		names=wires.getWireNames();
		for(Caseless name: names){
			wires.fetchWire(name).getDrawer().draw(g);
			Rectangle rect = wires.getBounds(name);
			if(rect.x + rect.width + 10 > sizeX)sizeX =rect.x + rect.width + 10;
			if(rect.y + rect.height + 10 > sizeY)sizeY =rect.y + rect.height + 10;
		}
		
		if(addComponentComponent!= null){
			Color temp = g.getColor();
			g.setColor(Color.RED);
			g.translate(addComponentX, addComponentY);
			addComponentComponent.getDrawer().draw(g);
			g.translate(-addComponentX, -addComponentY);
			g.setColor(temp);
		}
		
		if(preLoc != null){
			Color temp = g.getColor();
			g.setColor(Color.CYAN);
			preLoc.draw(g);
			g.setColor(temp);			
		}
		
		if(point!=null){
			point.draw(g, decrementer.getIznos());
		}
		
		if(state.equals(ECanvasState.MOVE_STATE) && localController.getSelectedComponent()!=null){
			drawSelection(localController.getSelectedComponent(),g);
		}
		
		setCanvasSize(sizeX,sizeY);
		
		repaint();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	
	private void drawSelection(Caseless selectedComponent, Graphics2D g) {
		Rectangle rect = components.getComponentBounds(selectedComponent);
		g.setColor(Color.RED);
		g.drawArc(rect.x-10, rect.y-10, rect.width+20, rect.height+20, 0, 360);
	}

	private void setCanvasSize(int sizeX, int sizeY) {
		Insets in = this.getInsets();
		this.setPreferredSize(new Dimension(sizeX+in.left+in.right, sizeY+in.top+in.bottom));
		this.revalidate();
	}

	//###########################PUBLIC METHODS##########################
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ISchemaCanvas#setDummyStuff(hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection, hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection)
	 */
	public void setDummyStuff(ISchemaComponentCollection components, ISchemaWireCollection wires) {
		this.components = components;
		this.wires = wires;
		repaint();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("Canvas registered:"+evt.getPropertyName());

		if(evt.getPropertyName().equals(EPropertyChange.CANVAS_CHANGE.toString())){
			drawComponents();
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_COMPONENT_TO_ADD)){
			System.out.println("Canvas registered: localControler.PROPERTY_CHANGE_COMPONENT_TO_ADD");
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_SELECTION)){
			System.out.println("Canvas registered: localControler.PROPERTY_CHANGE_SELECTION");
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE)){
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			modifyTimerStatus();
			addComponentComponent = null;
			preLoc = null;
			point = null;
			addComponentX = 0;
			addComponentY = 0;
			ILocalGuiController tempCont = (CanvasToolbarLocalGUIController) evt.getSource();
			state = tempCont.getState();
			if(state.equals(ECanvasState.ADD_COMPONENT_STATE)){
				this.setCursor(new Cursor(Cursor.HAND_CURSOR));
				try {
					addComponentComponent = controller.getSchemaInfo().getPrototyper().clonePrototype(tempCont.getComponentToAdd(), new HashSet<Caseless>());
				} catch (UnknownComponentPrototypeException e) {
					System.out.println("Canvas Property change | illegal action on component initialization.");
					e.printStackTrace();
				}
			}
			System.out.println("Canvas registered:" + tempCont);
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ISchemaCanvas#setConteroler(hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController)
	 */
	public void registerSchemaController(ISchemaController controller) {
		this.controller=controller;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ISchemaCanvas#registerLocalController(hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController)
	 */
	public void registerLocalController(ILocalGuiController cont){
		localController = cont;
	}
	
	
	
	//#############NESTED CLASSES##############
	private class Mouse1 implements MouseListener{
		
		public void mouseClicked(MouseEvent e) {
			if(e.getButton()==MouseEvent.BUTTON1){
				if(state.equals(ECanvasState.ADD_COMPONENT_STATE)){
					Caseless comp = localController.getComponentToAdd();
					ICommand instantiate = new InstantiateComponentCommand(comp, e.getX(), e.getY());
					ICommandResponse response = controller.send(instantiate);
					System.out.println ("canvas report| component instantiate succesful: "+response.isSuccessful());
				}
				else if(state.equals(ECanvasState.DELETE_STATE)){
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY());
					if(comp != null){
						ICommand instantiate = new DeleteComponentCommand(comp.getName());
						ICommandResponse response = controller.send(instantiate);
						System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
					}else{
						ISchemaWire wire = wires.fetchWire(e.getX(), e.getY(), 10);
						if(wire != null){
							ICommand instantiate = new DeleteWireCommand(wire.getName());
							ICommandResponse response = controller.send(instantiate);
							System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
						}
					}
				}
				else if(state.equals(ECanvasState.MOVE_STATE)){
					//TODO move
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY());
					if(comp != null){
						localController.setSelectedComponent(comp.getName());
					}
					
				}
			}else if(e.getButton()==MouseEvent.BUTTON3){
				dummyStateChanger();
			}
				
		}

		public void mouseEntered(MouseEvent e) {
		}
		
		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			if(state.equals(ECanvasState.ADD_WIRE_STATE)&&e.getButton()==MouseEvent.BUTTON1){
				wireBeginning = getCriticalPoint(e.getX(),e.getY());
				int x = e.getX();
				int y = e.getY();
				if(wireBeginning!=null){
					x=wireBeginning.getX();
					y=wireBeginning.getY();
				}
				preLoc = new WirePreLocator(x,y,x,y);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(state.equals(ECanvasState.ADD_WIRE_STATE)&&e.getButton()==MouseEvent.BUTTON1){
				wireEnding = getCriticalPoint(e.getX(),e.getY());
				int x = e.getX();
				int y = e.getY();
				if(wireEnding!=null){
					x=wireEnding.getX();
					y=wireEnding.getY();
				}
				preLoc.setX2(x);
				preLoc.setY2(y);
				if(preLoc.isWireInstance()){
					preLoc.instantiateWire(controller, wireBeginning, wireEnding);
				}
				preLoc = null;
				repaint();
			}
		}

	}
	
	private class Mose2 implements MouseMotionListener{

		public void mouseDragged(MouseEvent e) {
			if(state.equals(ECanvasState.ADD_WIRE_STATE)&&preLoc!=null){
				preLoc.setX2(e.getX());
				preLoc.setY2(e.getY());
				
				if(state.equals(ECanvasState.ADD_WIRE_STATE)){
					point = getCriticalPoint(e.getX(), e.getY());
					modifyTimerStatus();
				}
			}
		}

		public void mouseMoved(MouseEvent e) {
			
			if(state.equals(ECanvasState.ADD_COMPONENT_STATE)){
				int x = e.getX();
				int y = e.getY();
				if(x > 0 && y > 0 && x < img.getWidth() && y < img.getHeight()) {
					addComponentX = x;
					addComponentY = y;
					repaint();
				}
			}
			
			if(state.equals(ECanvasState.ADD_WIRE_STATE)){
				point = getCriticalPoint(e.getX(), e.getY());
				modifyTimerStatus();
			}
			
		}
		
	}

	private void dummyStateChanger() {
		if(state.equals(ECanvasState.ADD_COMPONENT_STATE))
			localController.setState(ECanvasState.ADD_WIRE_STATE);
		else if(state.equals(ECanvasState.ADD_WIRE_STATE))
			localController.setState(ECanvasState.DELETE_STATE);
		else if(state.equals(ECanvasState.DELETE_STATE))
			localController.setState(ECanvasState.MOVE_STATE);
		else if(state.equals(ECanvasState.MOVE_STATE))
			localController.setState(ECanvasState.ADD_COMPONENT_STATE);
	}

	public void modifyTimerStatus() {
		if(point==null)timer.stop();
		else{
			decrementer.reset();
			timer.start();
		}
	}

	public CriticalPoint getCriticalPoint(int x, int y) {
		CriticalPoint point = null;
		ISchemaComponent comp = components.fetchComponent(x,y);
		if(comp!=null){
			Rectangle rect = components.getComponentBounds(comp.getName());
			SchemaPort port = comp.getSchemaPort(x-rect.x, y-rect.y, 5);
			if(port != null){
				point = new CriticalPoint(port.getOffset().getX()+rect.x,port.getOffset().getY()+rect.y,
						CriticalPoint.ON_COMPONENT_PLUG,comp.getName(),port.getName());
				decrementer.reset();
			}else{
				point = null;
			}
		}else{
			ISchemaWire wire = wires.fetchWire(x, y, 10);
			if(wire != null){
				point = new CriticalPoint(wire, x, y);
			}else{
				return null;
			}
		}
		return point;
	}
	

}