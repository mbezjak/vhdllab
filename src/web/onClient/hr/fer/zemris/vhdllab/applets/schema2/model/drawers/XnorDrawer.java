package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;

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
public class XnorDrawer extends GateDrawer implements IComponentDrawer {
	
	
	/* static fields */
	public static final int PORT_SIZE = 4;
	public static final int NEGATE_SIZE = 4;
	public static final int PIN_LENGTH = 15;
	public static final int INSIGNIA_SIZE = 20;
	public static final String INSIGNIA = "=1";
	
	
	/* private fields */
	private ISchemaComponent comp_to_draw;

	
	
	
	public XnorDrawer(ISchemaComponent componentToDraw) {
		super(componentToDraw);
		comp_to_draw = componentToDraw;
	}
	
	
	public void draw(Graphics2D graphics) {
		super.draw(graphics, true);
		
		int w = comp_to_draw.getWidth();
		int h = comp_to_draw.getHeight();
		
		// draw insignia
		Font oldf = graphics.getFont(), f = new Font("Serif", Font.PLAIN, INSIGNIA_SIZE);
		graphics.setFont(f);
		graphics.drawString(INSIGNIA, w / 2 - f.getSize() * INSIGNIA.length() / 4, h / 2 + f.getSize() / 2);
		graphics.setFont(oldf);
	}
	
}



















