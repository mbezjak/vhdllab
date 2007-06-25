package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.SimpleSchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.InstantiateComponentCommand;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
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
		for(Caseless name: names){
			ISchemaComponent comp = components.fetchComponent(name);
			XYLocation componentLocation = components.getComponentLocation(name);
			g.translate(componentLocation.x, componentLocation.y);
			comp.getDrawer().draw(g);
			g.translate(-componentLocation.x, -componentLocation.y);
		}
		
		
		names=wires.getWireNames();
		for(Caseless name: names){
			wires.fetchWire(name).getDrawer().draw(g);
		}
		
		/*TODO:problem, kako dohvatiti poziciju komponente? treba za resize!!!
		 * kako iteriram kroz zice? fali mi getWireNames!!!! RIJESENO
		 * kako se registriram na listener
		 * dali postoje default draweri?
		 * dogovoriti potrebno sucelje i ostatak GUI-a kako ce biti izveden, tko ce ga raditi
		 * 		i kako ce izgledati.
		 */
		

		
		repaint();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
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
		System.out.println(evt.getPropertyName());
		
		ISchemaInfo inf = ((ISchemaController) evt.getSource()).getSchemaInfo();
		components = inf.getComponents();
		wires = inf.getWires();
		drawComponents();
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
					System.out.println (response.isSuccessful());
				}
				else if(state.equals(ECanvasState.ADD_WIRE_STATE)){
					//TODO add wire
				}
				else if(state.equals(ECanvasState.DELETE_STATE)){
					//TODO delete
				}
				else if(state.equals(ECanvasState.MOVE_STATE)){
					//TODO move
				}
			}
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
		
	}
	private class Mose2 implements MouseMotionListener{

		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}