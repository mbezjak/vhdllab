package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.DrawingProperties;
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
	public static final int PIN_LENGTH = Constants.GRID_SIZE * 2;
	public static final int EDGE_OFFSET = (int) (Constants.GRID_SIZE * 1.5);
//	public static final int PER_PORT_SIZE = Constants.GRID_SIZE * 2;
	
	
	
	
	/* private fields */
	private ISchemaComponent comp_to_draw;
	private String componentName;
	
	
	
	public DefaultComponentDrawer(ISchemaComponent componentToDraw) {
		comp_to_draw = componentToDraw;
		componentName = null;
	}
	
	
	public void draw(Graphics2D graphics, DrawingProperties properties) {
		int w = comp_to_draw.getWidth();
		int h = comp_to_draw.getHeight();
		XYLocation offset;
		if (componentName == null) componentName = comp_to_draw.getTypeName().toString();
		
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
		graphics.fillRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		graphics.setColor(c);
		graphics.drawRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		
		// draw component type name and instance name
		if (properties.drawingComponentNames) {
			Font oldf = graphics.getFont();
			Color oldc = graphics.getColor();
			
			Font f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_NORMAL_FONT_SIZE);
			graphics.setFont(f);
			graphics.drawString(comp_to_draw.getName().toString(), 0, -Constants.TEXT_NORMAL_FONT_SIZE / 2);
	
			f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_SMALL_FONT_SIZE);
			int r = oldc.getRed() + 140; r = (r > 230) ? (230) : (r);
			int g = oldc.getGreen() + 140; g = (g > 230) ? (230) : (g);
			int b = oldc.getBlue() + 140; b = (b > 230) ? (230) : (b);
			graphics.setColor(new Color(r, g, b));
			graphics.setFont(f);
			graphics.drawString(componentName, 0, Constants.TEXT_NORMAL_FONT_SIZE / 2);
	
			graphics.setFont(oldf);
			graphics.setColor(oldc);
		}
	}
	
}



















