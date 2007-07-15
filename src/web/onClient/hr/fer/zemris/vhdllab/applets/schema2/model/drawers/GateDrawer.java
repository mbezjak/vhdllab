package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

import java.awt.Color;
import java.awt.Graphics2D;







public abstract class GateDrawer {
	
	
	/* static fields */
	public static final int PORT_SIZE = 4;
	public static final int NEGATE_SIZE = 4;
	public static final int PIN_LENGTH = 15;
	
	
	/* private fields */
	private ISchemaComponent comp_to_draw;

	
	
	
	public GateDrawer(ISchemaComponent componentToDraw) {
		comp_to_draw = componentToDraw;
	}
	
	
	protected void draw(Graphics2D graphics, boolean detectNegations) {
		int w = comp_to_draw.getWidth();
		int h = comp_to_draw.getHeight();
		int specialh = 0;
		
		// draw ports and wires to those ports
		for (SchemaPort port : comp_to_draw.getSchemaPorts()) {
			Caseless mapping = port.getMapping();
			XYLocation offset = port.getOffset();
			
			if (offset.x == 0 || offset.x == w) {
				graphics.drawLine(offset.x, offset.y, w/2, offset.y);
				if (offset.x == w) specialh = offset.y;
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
		graphics.fillRect(PIN_LENGTH, PIN_LENGTH, w - 2 * PIN_LENGTH, h - 2 * PIN_LENGTH);
		if (detectNegations) graphics.fillOval(w - PIN_LENGTH, specialh - NEGATE_SIZE / 2, NEGATE_SIZE, NEGATE_SIZE);
		graphics.setColor(c);
		graphics.drawRect(PIN_LENGTH, PIN_LENGTH, w - 2 * PIN_LENGTH, h - 2 * PIN_LENGTH);
		if (detectNegations) graphics.drawOval(w - PIN_LENGTH, specialh - NEGATE_SIZE / 2, NEGATE_SIZE, NEGATE_SIZE);
		
		
	}
	
}



















