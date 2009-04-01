package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.DrawingProperties;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Rect2d;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SimpleSchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SimpleSchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.DeleteSegmentAndDivideCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.DeleteWireCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.InstantiateComponentCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.MoveComponentCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries.FindConnectedPins;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	
	
	private static final int MIN_COMPONENT_DISTANCE = 10;

	
	/**
	 * Image used to create double-buffer effect.
	 */
	protected BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
	
	/**
	 * Collection containing all components in design.
	 */
	protected ISchemaComponentCollection components = new SimpleSchemaComponentCollection();
	
	/**
	 * Collection containing all wires in design.
	 */
	protected ISchemaWireCollection wires = new SimpleSchemaWireCollection();

	/**
	 * controller na razini schema-editora
	 */
	protected ISchemaController controller;
	
	/**
	 * controller na razini GUI-a
	 */
	protected ILocalGuiController localController;
	
	/**
	 * Stanje u kojem se canvas nalazi.
	 */
	protected ECanvasState state;
	
	/**
	 * komponenta koja se iscrtava kao komponenta za dodati
	 */
	protected ISchemaComponent addComponentComponent = null;

	/**
	 * Lokacija komponente za dodati
	 */
	protected int addComponentX = 0, addComponentY = 0;
	
	/**
	 * Sadr�ani svi podatci o zici za dodati...
	 */
	protected IWirePreLocator preLoc = null;
	
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
	protected CriticalPoint wireBeginning = null;
	
	/**
	 * critical point gdije je zica zavrsila
	 */
	protected CriticalPoint wireEnding = null;
	
	private DrawingProperties drawProperties = null;
	
	private Timer timer = null;
	protected Caseless componentToMove = null;
	
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
		drawProperties = new DrawingProperties();
		
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
	@Override
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
				g.fillOval(i*dist, j*dist, 1, 1);
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
			comp.getDrawer().draw(g, drawProperties);
			g.translate(-componentLocation.x, -componentLocation.y);
			
//			String text = name.toString();
			Rectangle rect = components.getComponentBounds(name);
//			FontMetrics fm = g.getFontMetrics();
//			int tx=rect.x+rect.width/2-fm.stringWidth(text)/2;
//			int ty=rect.y;
//			g.drawString(text,tx,ty);

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
			wires.fetchWire(name).getDrawer().draw(g, drawProperties);
			
			Rect2d rect = wires.getBounds(name);
			if(rect.left + rect.width + 10 > sizeX)sizeX =rect.left + rect.width + 10;
			if(rect.top + rect.height + 10 > sizeY)sizeY =rect.top + rect.height + 10;
			g.setColor(cl);
		}
		
		if(addComponentComponent!= null){
			Color temp = g.getColor();
			g.setColor(CanvasColorProvider.COMPONENT_TO_ADD);
			g.translate(addComponentX, addComponentY);
			addComponentComponent.getDrawer().draw(g, drawProperties);
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

		if(type == CanvasToolbarLocalGUIController.TYPE_WIRE 
				&& !Caseless.isNullOrEmpty(sel)){
			drawConnectedPins(g,sel);
		}
		
		if(type == CanvasToolbarLocalGUIController.TYPE_COMPONENT 
				&& !Caseless.isNullOrEmpty(sel)){
			ISchemaComponent comp = components.fetchComponent(sel);
			Color cl = g.getColor();
			g.setColor(CanvasColorProvider.SIGNAL_LINE_SELECTED_ONCOMP);
			for (SchemaPort sp : comp.getSchemaPorts()) {
				Caseless mappedto = sp.getMapping();
				if (Caseless.isNullOrEmpty(mappedto)) continue;
				else {
					wires.fetchWire(mappedto).getDrawer().draw(g, drawProperties);
					drawConnectedPins(g, mappedto);
				}
			}
			g.setColor(cl);
		}

		setCanvasSize(sizeX,sizeY);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	
	@SuppressWarnings("unchecked")
	private void drawConnectedPins(Graphics2D g, Caseless sel) {
		IQuery q = new FindConnectedPins(sel);
		IQueryResult r = controller.send(q);
		List<XYLocation> locList = (List<XYLocation>) r.get(FindConnectedPins.KEY_PIN_LOCATIONS);
		Color cl = g.getColor();
		g.setColor(CanvasColorProvider.SIGNAL_LINE_SELECTED_PORT);
		for(XYLocation xy:locList){
			g.fillOval(xy.x-4, xy.y-4, 8, 8);
		}
		g.setColor(cl);		
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
//		System.out.println("Canvas registered:"+evt.getPropertyName());

		if(evt.getPropertyName().equals(EPropertyChange.CANVAS_CHANGE.toString())){
			drawComponents();
		}else if(evt.getPropertyName().equalsIgnoreCase("GRID")){
			isGridOn = localController.isGridON();
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_SELECTION)){
//			System.out.println("Canvas registered: localControler.PROPERTY_CHANGE_SELECTION");
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
//			System.out.println("Canvas registered:" + tempCont);
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
	protected class Mouse1 implements MouseListener{
		
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
					int distToComp = -1;
					if(comp != null){
						//+5 je zbog crtanja komponenti
						distToComp = 5 + Math.abs(components.distanceTo(comp.getName(), e.getX(), e.getY()));
					}
					ISchemaWire wire = wires.fetchWire(e.getX(), e.getY(), 10);
					int distToWire = -1;
					if(wire != null){
						distToWire = Math.abs(wires.distanceTo(wire.getName(), e.getX(), e.getY()));
					}
					
//					System.err.println("############ : -- "+distToComp + " ----- "+ distToWire);
					
					if(distToComp != -1){
						if(distToWire != -1){
							if(distToComp<distToWire){
//								ICommand instantiate = new DeleteComponentCommand(comp.getName());
//								controller.send(instantiate);
							}else{
								int segNo = SMath.calcClosestSegment(new XYLocation(e.getX(), e.getY()), 10, wire.getSegments());
								WireSegment seg = wire.getSegments().get(segNo);
								ICommand deleteSegment = new DeleteSegmentAndDivideCommand(wire.getName(),seg);
								controller.send(deleteSegment);
							}
						}else{
//							ICommand instantiate = new DeleteComponentCommand(comp.getName());
//							controller.send(instantiate);
						}
					}else{
						if(distToWire != -1){
							int segNo = SMath.calcClosestSegment(new XYLocation(e.getX(), e.getY()), 10, wire.getSegments());
							WireSegment seg = wire.getSegments().get(segNo);
							ICommand deleteSegment = new DeleteSegmentAndDivideCommand(wire.getName(),seg);
							controller.send(deleteSegment);
						}
					}
//					if(comp != null){
//						ICommand instantiate = new DeleteComponentCommand(comp.getName());
//						ICommandResponse response = controller.send(instantiate);
//						System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
//					}else{
//						ISchemaWire wire = wires.fetchWire(e.getX(), e.getY(), 10);
//						if(wire != null){
//							int segNo = SMath.calcClosestSegment(new XYLocation(e.getX(), e.getY()), 10, wire.getSegments());
//							WireSegment seg = wire.getSegments().get(segNo);
//							ICommand deleteSegment = new DeleteSegmentAndDivideCommand(wire.getName(),seg);
//							ICommandResponse response = controller.send(deleteSegment);
//							System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
//						}
//					}
				}
				else if(state.equals(ECanvasState.MOVE_STATE)){
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY(), MIN_COMPONENT_DISTANCE);
					int distToComp = -1;
					if(comp != null){
						//+5 je zbog crtanja komponenti...
						distToComp = 5 + Math.abs(components.distanceTo(comp.getName(), e.getX(), e.getY()));
					}
					ISchemaWire wire = wires.fetchWire(e.getX(), e.getY(), 10);
					int distToWire = -1;
					if(wire != null){
						distToWire = Math.abs(wires.distanceTo(wire.getName(), e.getX(), e.getY()));
					}
					
					//System.err.println("############ : -- "+distToComp + " ----- "+ distToWire);
					
					if(distToComp != -1){
						if(distToWire != -1){
							if(distToComp<distToWire){
								localController.setSelectedComponent(comp.getName(),CanvasToolbarLocalGUIController.TYPE_COMPONENT);
							}else{
								localController.setSelectedComponent(wire.getName(), CanvasToolbarLocalGUIController.TYPE_WIRE);
							}
						}else{
							localController.setSelectedComponent(comp.getName(),CanvasToolbarLocalGUIController.TYPE_COMPONENT);
						}
					}else{
						if(distToWire != -1){
							localController.setSelectedComponent(wire.getName(), CanvasToolbarLocalGUIController.TYPE_WIRE);
						}
					}
//					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY(), MIN_COMPONENT_DISTANCE);
//					if(comp != null){
//						localController.setSelectedComponent(comp.getName(),CanvasToolbarLocalGUIController.TYPE_COMPONENT);
//					}else{
//						ISchemaWire wir = wires.fetchWire(e.getX(), e.getY(),10);
//						if(wir!=null)
//							localController.setSelectedComponent(wir.getName(), CanvasToolbarLocalGUIController.TYPE_WIRE);
//					}
					
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
						if(doDeleteWire(SchemaCanvas.this)){
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
					if(localController.isSmartConectON()){
						preLoc = new AdvancedWirePreLocator(new ArrayList<WireSegment>(),controller);
						preLoc.setX1(x);
						preLoc.setY1(y);
					}else{
						preLoc = new WirePreLocator(x,y,x,y);
					}
				}else if(state.equals(ECanvasState.MOVE_STATE)){
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY(), MIN_COMPONENT_DISTANCE);
					if(comp!=null){
						componentToMove = comp.getName();
						localController.setSelectedComponent(comp.getName(),CanvasToolbarLocalGUIController.TYPE_COMPONENT);
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
					preLoc.revalidateWire();
				
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
	
	protected class Mose2 implements MouseMotionListener{

		public void mouseDragged(MouseEvent e) {
			if(state.equals(ECanvasState.ADD_WIRE_STATE)&&preLoc!=null){
				preLoc.setX2(alignToGrid(e.getX()));
				preLoc.setY2(alignToGrid(e.getY()));

				point = getCriticalPoint(e.getX(), e.getY());
				preLoc.setWireInstantiable(wireBeginning,point);

				preLoc.revalidateWire();
				
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
					controller.send(move);
					//System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());
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

	public static boolean doDeleteWire(Component comp) {
		String[] options={"Yes",
				"No"
		};
		JLabel name=new JLabel("Delete entire wire?");
		JOptionPane optionPane=new JOptionPane(name,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options,options[0]);
		JDialog dialog=optionPane.createDialog(comp,"VHDLLAB");
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
			SchemaPort port = comp.getSchemaPort(x-rect.x, y-rect.y, MIN_COMPONENT_DISTANCE);
			if(port != null){
				if(port.getMapping()==null||port.getMapping().equals(new Caseless(""))){
					point = new CriticalPoint(port.getOffset().getX()+rect.x,port.getOffset().getY()+rect.y,
							CriticalPoint.ON_COMPONENT_PLUG,comp.getName(),port.getName());
					decrementer.reset();
				}else{
//					Caseless c = port.getMapping();//TODO pitaj aleksa za ovaj getMapping!!
//					point = new CriticalPoint(wires.fetchWire(port.getMapping()), alignToGrid(x), alignToGrid(y));
					point = null;
				}
			}else{
				point = null;
			}

		}
		
		if(point == null){		
			ISchemaWire wire = wires.fetchWire(x, y, Constants.GRID_SIZE/2-1);
			if(wire != null){
				point = new CriticalPoint(wire, alignToGrid(x), alignToGrid(y));
			}else{
				point = null;
			}
		}
		
		
		return point;
	}
	
	protected int alignToGrid(int x){
		if(isGridOn)
			return Math.round((float)x/Constants.GRID_SIZE)*Constants.GRID_SIZE;
		return x;
	}

}