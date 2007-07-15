package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;






/**
 * Crta komponentu u obliku pravokutnika.
 * Pretpostavlja da je svaki port na rubu bounding-boxa
 * komponente. Ako neki port nije na rubu bounding-boxa
 * komponente, onda ce za njega biti nacrtana samo tockica,
 * ali ne i poveznica s tijelom komponente (pravokutnikom).
 * 
 * Port NE SMIJE biti unutar samog pravokutnika komponente.
 * 
 * @author Axel
 *
 */
public class NotDrawer implements IComponentDrawer {
	
	
	/* static fields */
	public static final int PORT_SIZE = 4;
	public static final int NEGATE_SIZE = 4;
	public static final int PIN_LENGTH = 15;
	public static final int INSIGNIA_SIZE = 18;
	public static final String INSIGNIA = "=1";
	
	
	/* private fields */
	private ISchemaComponent comp_to_draw;

	
	
	
	public NotDrawer(ISchemaComponent componentToDraw) {
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
		
		// draw a rectangle and not circle
		Color c = graphics.getColor();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(PIN_LENGTH, PIN_LENGTH, w - 2 * PIN_LENGTH, h - 2 * PIN_LENGTH);
		graphics.fillOval(w - PIN_LENGTH, (h - NEGATE_SIZE) / 2, NEGATE_SIZE, NEGATE_SIZE);
		graphics.setColor(c);
		graphics.drawRect(PIN_LENGTH, PIN_LENGTH, w - 2 * PIN_LENGTH, h - 2 * PIN_LENGTH);
		graphics.drawOval(w - PIN_LENGTH, (h - NEGATE_SIZE) / 2, NEGATE_SIZE, NEGATE_SIZE);
		
		
		// draw insignia
		Font oldf = graphics.getFont(), f = new Font("Serif", Font.PLAIN, INSIGNIA_SIZE);
		graphics.setFont(f);
		graphics.drawString(INSIGNIA, w / 2 - f.getSize() * INSIGNIA.length() / 4, h / 2 + f.getSize() / 2);
		graphics.setFont(oldf);
		
	}
	
}



















