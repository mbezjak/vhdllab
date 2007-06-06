package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

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
public class DefaultComponentDrawer implements IComponentDrawer {
	
	private ISchemaComponent comp_to_draw;

	
	
	
	public DefaultComponentDrawer(ISchemaComponent componentToDraw) {
		comp_to_draw = componentToDraw;
	}
	
	
	public void draw(Graphics2D graphics) {
		int w = comp_to_draw.getWidth();
		int h = comp_to_draw.getHeight();
		XYLocation offset;
		
		// iscrtaj izlaze
		for (SchemaPort port : comp_to_draw.getPorts()) {
			offset = port.getOffset();
			if (offset.x == 0 || offset.x == w) {
				graphics.drawLine(offset.x, offset.y, w/2, offset.y);
			}
			if (offset.y == 0 || offset.y == h) {
				graphics.drawLine(offset.x, offset.y, offset.x, h/2);
			}
			graphics.drawOval(offset.x, offset.y, 3, 3);
		}
		
		// iscrtaj pravokutnik
		graphics.drawRect(w / 10, h / 10, w * 9 / 10, h * 9 / 10);
	}
	
}



















