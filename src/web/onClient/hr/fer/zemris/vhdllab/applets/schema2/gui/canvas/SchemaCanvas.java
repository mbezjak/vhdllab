package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.JPanel;

public class SchemaCanvas extends JPanel {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -179843489371055255L;

	
	/**
	 * Image used to create double-buffer effect.
	 */
	private BufferedImage img = null;
	
	/**
	 * Collection containing all components in design.
	 */
	private ISchemaComponentCollection components = null;
	
	/**
	 * Collection containing all wires in design.
	 */
	private ISchemaWireCollection wires = null;
	

	public SchemaCanvas(ISchemaCore core) {
		this.setOpaque(true);
		
		components = core.getSchemaInfo().getComponents();
		wires = core.getSchemaInfo().getWires();
		
		repaint();
	}
	
	
	public SchemaCanvas() {
		repaint();
	}


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

	/**
	 * Private method responsible for drawing all components and wires contained in components
	 * and wires collections.
	 *
	 */
	private void drawComponents() {
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Color prevColor = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		g.setColor(prevColor);
		//TODO nacrtaj mrezu, pitaj za sensitivnost mreze, neznam, jos nesto, sjeti se!!
		
		
		Set<Caseless> names = components.getComponentNames();
		for(Caseless name: names){
			components.fetchComponent(name).getDrawer().draw(g);
		}
		
		/*TODO:problem, kako dohvatiti poziciju komponente? treba za resize!!!
		 * kako iteriram kroz zice? fali mi getWireNames!!!!
		 * kako se registriram na listener
		 * dali postoje default draweri?
		 */
		
		repaint();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}