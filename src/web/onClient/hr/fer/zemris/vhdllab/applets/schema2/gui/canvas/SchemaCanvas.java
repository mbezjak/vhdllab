package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.AddWireCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.DeleteComponentCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.InstantiateComponentCommand;

import java.awt.Color;
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
import java.util.Set;

import javax.swing.JPanel;

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
	 * broj klikova za dodavanje zice.
	 */
	private int clickNumber=0;

	
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
		state = ECanvasState.ADD_COMPONENT_STATE;	//init state
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
				drawComponents();
			}
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
		
		//Color prevColor = g.getColor();
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
			Rectangle rect = components.getComponentBounds(name);
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
		
		/*TODO:problem, kako dohvatiti poziciju komponente? treba za resize!!!
		 * kako iteriram kroz zice? fali mi getWireNames!!!! RIJESENO
		 * kako se registriram na listener
		 * dali postoje default draweri?
		 * dogovoriti potrebno sucelje i ostatak GUI-a kako ce biti izveden, tko ce ga raditi
		 * 		i kako ce izgledati.
		 */
		
		setCanvasSize(sizeX,sizeY);
		
		repaint();
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
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_COMPONENT_TO_ADD)){
			System.out.println("Canvas registered: localControler.PROPERTY_CHANGE_COMPONENT_TO_ADD");
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_SELECTION)){
			System.out.println("Canvas registered: localControler.PROPERTY_CHANGE_SELECTION");
		}else if(evt.getPropertyName().equalsIgnoreCase(CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE)){
			ILocalGuiController tempCont = (CanvasToolbarLocalGUIController) evt.getSource();
			state = tempCont.getState();
			clickNumber = 0;
			System.out.println("Canvas registered:" + tempCont);
		}
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
		
		private int x0 = 0;
		private int y0 = 0;;
		
		public void mouseClicked(MouseEvent e) {
			if(e.getButton()==MouseEvent.BUTTON1){
				if(state.equals(ECanvasState.ADD_COMPONENT_STATE)){
					Caseless comp = localController.getComponentToAdd();
					ICommand instantiate = new InstantiateComponentCommand(comp, e.getX(), e.getY());
					ICommandResponse response = controller.send(instantiate);
					System.out.println ("canvas report| component instantiate succesful: "+response.isSuccessful());
				}
				else if(state.equals(ECanvasState.ADD_WIRE_STATE)){
					if(clickNumber == 0){
						clickNumber++;
						x0 = e.getX();
						y0 = e.getY();
					}else{
						clickNumber = 0;
						instantiateWire(x0,y0,e.getX(),e.getY());
					}
				}
				else if(state.equals(ECanvasState.DELETE_STATE)){
					ISchemaComponent comp = components.fetchComponent(e.getX(), e.getY());
					if(comp != null){
						ICommand instantiate = new DeleteComponentCommand(comp.getName());
						ICommandResponse response = controller.send(instantiate);
						System.out.println ("canvas report| component delete succesful: "+response.isSuccessful());

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

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
		
	}
	
	private class Mose2 implements MouseMotionListener{

		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub ima lažnu funkcionalnost!!!
		}

		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private void instantiateWire(int x1, int y1, int x2, int y2) {
		if (x1 != x2 && y1 != y2) {
			ICommand instantiate = new AddWireCommand(createName(x1,y1,x1,y2),x1,y1,x1,y2);
			ICommandResponse response = controller.send(instantiate);
			System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());
			
			instantiate = new AddWireCommand(createName(x1, y2, x2, y2),x1,y2,x2,y2);
			response = controller.send(instantiate);
			System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());
		}
		else{
			ICommand instantiate = new AddWireCommand(createName(x1, y1, x2, y2),x1,y1,x2,y2);
			ICommandResponse response = controller.send(instantiate);
			System.out.println ("canvas report| wire instantiate succesful: "+response.isSuccessful());
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

	private Caseless createName(int x1, int y1, int x2, int y2) {
		StringBuilder build = new StringBuilder("WIRE");
		build.append(x1).append("-").append(y1).append("-")
			.append(x2).append("-").append("y2");
		return new Caseless(build.toString());
	}
}