package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
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
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.DeleteComponentCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.DeleteSegmentCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.DeleteWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.InstantiateComponentCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.MoveComponentCommand;

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

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SchemaCanvas extends JPanel implements PropertyChangeListener, ISchemaCanvas {
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -179843489371055255L;
	
	
	private static final int MIN_COMPONENT_DISTANCE = 15;

	
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
	 * Sadr�ani svi podatci o zici za dodati...
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
	private Caseless componentToMove = null;
	
	private boolean isGridOn =true;
	
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
		decrementer = new Decrementer(20, this);
		timer = new Timer(70,decrementer);
		
		this.addMouseListener(new Mouse1());
		this.addMouseMotionListener(new Mose2());
		this.setOpaque(true);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	}

	//##########################
	
	
	//#########################PROTECTED METHODS###################################
	
	/**
	 * Overriden method for painting the canvas.
	 */
	protected void paintComponent(Graphics g) {
		Insets in = this.getInsets();
		if(img == null) {
			img=new BufferedImage(this.getWidth()-in.left-in.right,this.getHeight()-in.top-in.bottom
					, BufferedImage.TYPE_3BYTE_BGR);
			drawComponents();
		} else {
			if (img.getHeight()!=this.getHeight()-in.top-in.bottom ||
					img.getWidth()!=this.getWidth()-in.left-in.right){
				img=new BufferedImage(this.getWidth()-in.left-in.right,this.getHeight()-in.top-in.bottom
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
		
		g.setColor(CanvasColorProvider.CANVAS_BACKGROUND);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		
		int dist = Constants.GRID_SIZE;
		g.setColor(CanvasColorProvider.GRID_DOT);
		for(int i = 0; i<=img.getWidth()/dist;i++)
			for(int j=0;j<=img.getHeight()/dist;j++){
				g.fillOval(i*dist-1, j*dist-1, 1, 1);
			}
		
		
		Set<Caseless> names = components.getComponentNames();
		
		int sizeX = 0;
		int sizeY = 0;
		
		Caseless sel = localController.getSelectedComponent();
		int type = localController.getSelectedType();
		
		for(Caseless name: names){
			ISchemaComponent comp = components.fetchComponent(name);
			XYLocation componentLocation = components.getComponentLocation(name);
			Color cl = g.getColor();
			g.setColor(type == CanvasToolbarLocalGUIController.TYPE_COMPONENT && name.equals(sel)?
					CanvasColorProvider.COMPONENT_SELECTED:
						CanvasColorProvider.COMPONENT);
			
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
			g.setColor(cl);
		}
		
		
		names=wires.getWireNames();
		for(Caseless name: names){
			Color cl = g.getColor();
			g.setColor(type == CanvasToolbarLocalGUIController.TYPE_WIRE && name.equals(sel)?
					CanvasColorProvider.SIGNAL_LINE_SELECTED:
						CanvasColorProvider.SIGNAL_LINE);
			wires.fetchWire(name).getDrawer().draw(g);
			Rectangle rect = wires.getBounds(name);
			if(rect.x + rect.width + 10 > sizeX)sizeX =rect.x + rect.width + 10;
			if(rect.y + rect.height + 10 > sizeY)sizeY =rect.y + rect.height + 10;
			g.setColor(cl);
		}
		
		if(addComponentComponent!= null){
			Color temp = g.getColor();
			g.setColor(CanvasColorProvider.COMPONENT_TO_ADD);
			g.translate(addComponentX, addComponentY);
			addComponentComponent.getDrawer().draw(g);
			g.translate(-addComponentX, -addComponentY);
			g.setColor(temp);
		}
		
		if(preLoc != null){
			Color temp = g.getColor();
			g.setColor(preLoc.isWireInstantiable()?
					CanvasColorProvider.SIGNAL_LINE_TO_ADD:
						CanvasColorProvider.SIGNAL_LINE_ERROR);
			preLoc.draw(g);
			g.setColor(temp);			
		}
		
		if(point!=null){
			point.draw(g, decrementer.getIznos());
		}
		
		setCanvasSize(sizeX,sizeY);
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
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
		}else if(evt.getPropertyName().equalsIgnoreCase("GRID")){
			isGridOn = localController.isGridON();
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_SELECTION)){
			System.out.println("Canvas registered: localControler.PROPERTY_CHANGE_SELECTION");
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE)
				||evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_COMPONENT_TO_ADD)){
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			modifyTimerStatus();
			localController.setSelectedComponent(new Caseless(""), CanvasToolbarLocalGUIController.TYPE_NOTHING_SELECTED);
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
					ICommand instantiate = new InstantiateComponentCommand(comp, alignToGrid(addComponentX), alignToGrid(addComponentY));
					ICommandResponse response = controller.send(instantiate);
					System.out.println ("canvas report| component instantiate succesful: "+response.isSuccessful());
				}
				else if(state.equals(ECanvasState.DELETE_STATE)){
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY(), MIN_COMPONENT_DISTANCE);
					if(comp != null){
						ICommand instantiate = new DeleteComponentCommand(comp.getName());
						ICommandResponse response = controller.send(instantiate);
						System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
					}else{
						ISchemaWire wire = wires.fetchWire(e.getX(), e.getY(), 10);
						if(wire != null){
							int segNo = SMath.calcClosestSegment(new XYLocation(e.getX(), e.getY()), 10, wire.getSegments());
							WireSegment seg = wire.getSegments().get(segNo);
							ICommand deleteSegment = new DeleteSegmentCommand(wire.getName(),seg);
							ICommandResponse response = controller.send(deleteSegment);
							System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
						}
					}
				}
				else if(state.equals(ECanvasState.MOVE_STATE)){
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY(), MIN_COMPONENT_DISTANCE);
					if(comp != null){
						localController.setSelectedComponent(comp.getName(),CanvasToolbarLocalGUIController.TYPE_COMPONENT);
					}else{
						ISchemaWire wir = wires.fetchWire(e.getX(), e.getY(),10);
						if(wir!=null)
							localController.setSelectedComponent(wir.getName(), CanvasToolbarLocalGUIController.TYPE_WIRE);
					}
					
				}
			}else if(e.getButton()==MouseEvent.BUTTON3){
				if(state.equals(ECanvasState.MOVE_STATE)){
					localController.setSelectedComponent(new Caseless(""), CanvasToolbarLocalGUIController.TYPE_NOTHING_SELECTED);
				}else if(state.equals(ECanvasState.ADD_WIRE_STATE)){
					preLoc = null;
					wireBeginning = null;
					wireEnding = null;
					repaint();
				}else if(state.equals(ECanvasState.DELETE_STATE)){
					ISchemaWire wire = wires.fetchWire(e.getX(), e.getY(), 10);
					if(wire != null){
						if(doDeleteWire()){
							ICommand delete = new DeleteWireCommand(wire.getName());
							ICommandResponse response = controller.send(delete);
							System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
						}
					}
				}
			}
				
		}

		public void mouseEntered(MouseEvent e) {
		}
		
		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1){
				if(state.equals(ECanvasState.ADD_WIRE_STATE)){
					wireBeginning = getCriticalPoint(e.getX(),e.getY());
					int x = alignToGrid(e.getX());
					int y = alignToGrid(e.getY());
					if(wireBeginning!=null){
						x=wireBeginning.getX();
						y=wireBeginning.getY();
					}
					preLoc = new WirePreLocator(x,y,x,y);
				}else if(state.equals(ECanvasState.MOVE_STATE)){
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY(), MIN_COMPONENT_DISTANCE);
					if(comp!=null){
						componentToMove = comp.getName();
					}
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(e.getButton()==MouseEvent.BUTTON1){
				if(state.equals(ECanvasState.ADD_WIRE_STATE)&&preLoc!=null){
					wireEnding = getCriticalPoint(e.getX(),e.getY());
					int x = alignToGrid(e.getX());
					int y = alignToGrid(e.getY());
					if(wireEnding!=null){
						x=wireEnding.getX();
						y=wireEnding.getY();
					}
					preLoc.setX2(x);
					preLoc.setY2(y);
					preLoc.setWireInstantiable(wireBeginning,wireEnding);

					if(preLoc.isWireInstance()){
						preLoc.instantiateWire(controller, wireBeginning, wireEnding);
					}
					preLoc = null;
					wireBeginning = null;
					wireEnding = null;
					repaint();
				}else if(state.equals(ECanvasState.MOVE_STATE)){
					componentToMove = null;
				}
			}
		}

	}
	
	private class Mose2 implements MouseMotionListener{

		public void mouseDragged(MouseEvent e) {
			if(state.equals(ECanvasState.ADD_WIRE_STATE)&&preLoc!=null){
				preLoc.setX2(alignToGrid(e.getX()));
				preLoc.setY2(alignToGrid(e.getY()));

				point = getCriticalPoint(e.getX(), e.getY());
				preLoc.setWireInstantiable(wireBeginning,point);
				preLoc.setWireOrientation();
				modifyTimerStatus();
				repaint();
			}else if(state.equals(ECanvasState.MOVE_STATE)&&componentToMove!=null){
				ISchemaComponent comp = components.fetchComponent(componentToMove);
				if(e.getX()>comp.getWidth()/2&&e.getY()>comp.getHeight()/2){
					ICommand move = new MoveComponentCommand(
							componentToMove,new XYLocation(
									alignToGrid(e.getX()-comp.getWidth()/2),alignToGrid(e.getY()-comp.getHeight()/2)
							)
					);
					ICommandResponse response = controller.send(move);
					System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
				}
			}
		}

		public void mouseMoved(MouseEvent e) {
			
			if(state.equals(ECanvasState.ADD_COMPONENT_STATE)){
				int x = e.getX();
				int y = e.getY();
				if(x > addComponentComponent.getWidth()/2 && y > addComponentComponent.getHeight()/2 && x < img.getWidth() && y < img.getHeight()) {
					addComponentX = alignToGrid(x-addComponentComponent.getWidth()/2);
					addComponentY = alignToGrid(y-addComponentComponent.getHeight()/2);
					repaint();
				}
			}
			
			if(state.equals(ECanvasState.ADD_WIRE_STATE)){
				point = getCriticalPoint(e.getX(), e.getY());
				modifyTimerStatus();
				repaint();
			}
			
		}
		
	}

	private boolean doDeleteWire() {
		String[] options={"Yes",
				"No"
		};
		JLabel name=new JLabel("Delete entire wire?");
		JOptionPane optionPane=new JOptionPane(name,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options,options[0]);
		JDialog dialog=optionPane.createDialog(this,"VHDLLAB");
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setVisible(true);
		Object selected=optionPane.getValue();
		return selected.equals(options[0]);
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
		ISchemaComponent comp = components.fetchComponent(x,y, MIN_COMPONENT_DISTANCE);
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
			ISchemaWire wire = wires.fetchWire(x, y, Constants.GRID_SIZE/2-1);
			if(wire != null){
				point = new CriticalPoint(wire, alignToGrid(x), alignToGrid(y));
			}else{
				return null;
			}
		}
		return point;
	}
	
	private int alignToGrid(int x){
		if(isGridOn)
			return Math.round((float)x/Constants.GRID_SIZE)*Constants.GRID_SIZE;
		else
			return x;
	}

}