package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

import java.awt.Color;
import java.awt.Graphics2D;






/**
 * Crta komponentu u obliku pravokutnika.
 * Pretpostavlja da je svaki port na rubu bounding-boxa
 * komponente. Ako neki port nije na rubu bounding-boxa
 * komponente, onda ce za njega biti nacrtana samo tockica,
 * ali ne i poveznica s tijelom komponente (pravokutnikom).
 * 
 * Port NE SMIJE biti unutar samog pravokutnika komponente.
 * Ako se nade unutar pravokutnika, a ne na rubu, do njega
 * nece biti povucena zicica (ovo je samo stvar prikaza,
 * tocka koja pripada portu i dalje ce biti iscrtana).
 * 
 * @author Axel
 *
 */
public class DefaultComponentDrawer implements IComponentDrawer {
	
	
	/* static fields */
	public static final int PORT_SIZE = 4;
	public static final int PIN_LENGTH = 20;
	public static final int EDGE_OFFSET = Constants.GRID_SIZE * 4;
	public static final int PER_PORT_SIZE = Constants.GRID_SIZE * 2;
	
	
	
	
	/* private fields */
	private ISchemaComponent comp_to_draw;

	
	
	
	public DefaultComponentDrawer(ISchemaComponent componentToDraw) {
		comp_to_draw = componentToDraw;
	}
	
	
	public void draw(Graphics2D graphics) {
		int w = comp_to_draw.getWidth();
		int h = comp_to_draw.getHeight();
		XYLocation offset;
		
		// draw ports and wires to those ports
		for (SchemaPort port : comp_to_draw.getSchemaPorts()) {
			Caseless mapping = port.getMapping();
			
			offset = port.getOffset();
			if (offset.x == 0 || offset.x == w) {
				graphics.drawLine(offset.x, offset.y, w/2, offset.y);
			}
			if (offset.y == 0 || offset.y == h) {
				graphics.drawLine(offset.x, offset.y, offset.x, h/2);
			}
			
			if (!Caseless.isNullOrEmpty(mapping)) continue;
			
			Color c = graphics.getColor();
			graphics.setColor(Color.WHITE);
			graphics.fillOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE);
			graphics.setColor(c);
			graphics.drawOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE);
		}
		
		
		// draw a rectangle
		Color c = graphics.getColor();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(EDGE_OFFSET - PER_PORT_SIZE, EDGE_OFFSET - PER_PORT_SIZE,
				w - 2 * (EDGE_OFFSET - PER_PORT_SIZE), h - 2 * (EDGE_OFFSET - PER_PORT_SIZE));
		graphics.setColor(c);
		graphics.drawRect(EDGE_OFFSET - PER_PORT_SIZE, EDGE_OFFSET - PER_PORT_SIZE,
				w - 2 * (EDGE_OFFSET - PER_PORT_SIZE), h - 2 * (EDGE_OFFSET - PER_PORT_SIZE));
	}
	
}



















