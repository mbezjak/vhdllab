package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;












/**
 * A drawer that draws only InOutSchemaComponents.
 * 
 * @author brijest
 *
 */
public class InOutComponentDrawer implements IComponentDrawer {

	/* static fields */
	private static final int PIN_LENGTH = 15;
	private static final int PORT_SIZE = 4;
	private static final int TRIANGLE_SIDE = 21;
	private static final int EMPTY_EDGE_OFFSET = 40;
	private static final int FONT_SIZE = 8;

	
	/* private fields */
	private InOutSchemaComponent cmp;
	
	
	
	/* ctors */
	
	public InOutComponentDrawer(ISchemaComponent cmpToDraw) {
		if (!(cmpToDraw instanceof InOutSchemaComponent))
			throw new IllegalArgumentException("This drawer can only draw an instance of '"
					+ cmpToDraw.getClass().getName() + "'.");
		cmp = (InOutSchemaComponent)cmpToDraw;
	}
	
	

	/* methods */

	public void draw(Graphics2D graphics) {
		int w = cmp.getWidth();
		int h = cmp.getHeight();
		
		// draw wires and ports
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			XYLocation offset = sp.getOffset();
			
			if (offset.x == 0 || offset.x == w) {
				graphics.drawLine(offset.x, offset.y, w/2, offset.y);
			}
			if (offset.y == 0 || offset.y == h) {
				graphics.drawLine(offset.x, offset.y, offset.x, h/2);
			}
			
			Color c = graphics.getColor();
			graphics.setColor(Color.WHITE);
			graphics.fillOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE);
			graphics.setColor(c);
			graphics.drawOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE);
		}
		
		// draw component body with triangles and port nums
		Port port;
		Iterator<Port> it = cmp.portIterator();
		try {
			port = it.next();
		} catch (NoSuchElementException e) {
			throw new IllegalStateException("In-out component must have at least one port.", e);
		}
		Direction dir = port.getDirection();
		boolean downto = port.getType().hasVectorDirectionDOWNTO();
		
		if (dir.equals(Direction.IN)) {
			Color c = graphics.getColor();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, EMPTY_EDGE_OFFSET, w - 2 * PIN_LENGTH, h - 2 * EMPTY_EDGE_OFFSET);
			graphics.setColor(c);
			graphics.drawRect(0, EMPTY_EDGE_OFFSET, w - 2 * PIN_LENGTH, h - 2 * EMPTY_EDGE_OFFSET);
			
			int[] xps = new int[3];
			int[] yps = new int[3];
			xps[0] = w - 2 * PIN_LENGTH;
			xps[1] = w - 2 * PIN_LENGTH;
			xps[2] = w - 2 * PIN_LENGTH + TRIANGLE_SIDE * 7 / 8;
			List<SchemaPort> schports = cmp.getSchemaPorts();
			int i = 0, siz = schports.size();
			for (SchemaPort sp : schports) {
				yps[2] = sp.getOffset().y;
				yps[0] = yps[2] - TRIANGLE_SIDE / 2;
				yps[1] = yps[0] + TRIANGLE_SIDE;
				graphics.fillPolygon(xps, yps, 3);
				
				graphics.drawString(String.valueOf((downto) ? (siz - i - 1) : (i)),
						xps[0] - FONT_SIZE, (yps[0] + yps[1]) / 2 + FONT_SIZE / 2);
				
				i++;
			}
		} else if (dir.equals(Direction.OUT)) {
			Color c = graphics.getColor();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(PIN_LENGTH, EMPTY_EDGE_OFFSET, w - 2 * PIN_LENGTH, h - 2 * EMPTY_EDGE_OFFSET);
			graphics.setColor(c);
			graphics.drawRect(PIN_LENGTH, EMPTY_EDGE_OFFSET, w - 2 * PIN_LENGTH, h - 2 * EMPTY_EDGE_OFFSET);
			
			int[] xps = new int[3];
			int[] yps = new int[3];
			xps[0] = w - PIN_LENGTH;
			xps[1] = w - PIN_LENGTH;
			xps[2] = w - PIN_LENGTH + TRIANGLE_SIDE * 7 / 8;
			List<SchemaPort> schports = cmp.getSchemaPorts();
			int i = 0, siz = schports.size();
			for (SchemaPort sp : cmp.getSchemaPorts()) {
				yps[2] = sp.getOffset().y;
				yps[0] = yps[2] - TRIANGLE_SIDE / 2;
				yps[1] = yps[0] + TRIANGLE_SIDE;
				graphics.fillPolygon(xps, yps, 3);
				
				graphics.drawString(String.valueOf((downto) ? (siz - i - 1) : (i)),
						PIN_LENGTH + FONT_SIZE / 2, (yps[0] + yps[1]) / 2 + FONT_SIZE / 2);
				
				i++;
			}
		} else {
			throw new NotImplementedException("Direction '" + dir.toString() + "' not implemented.");
		}
	}
	
}












