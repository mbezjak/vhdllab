package hr.fer.zemris.vhdllab.applets.editor.schema2.model.drawers;

import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.DrawingProperties;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;

import java.awt.Color;
import java.awt.Font;
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
	private static final int EMPTY_EDGE_OFFSET = Constants.GRID_SIZE * 2;
	private static final int PER_PORT_SIZE = Constants.GRID_SIZE * 2;
	private static final int FONT_SIZE = 8;

	/* private fields */
	private InOutSchemaComponent cmp;

	/* ctors */

	public InOutComponentDrawer(ISchemaComponent cmpToDraw) {
		if (!(cmpToDraw instanceof InOutSchemaComponent))
			throw new IllegalArgumentException("This drawer can only draw an instance of '" +
				cmpToDraw.getClass().getName() + "'.");
		cmp = (InOutSchemaComponent) cmpToDraw;
	}

	/* methods */

	public void draw(Graphics2D graphics, DrawingProperties properties) {
		int w = cmp.getWidth();
		int h = cmp.getHeight();

		// draw wires and ports
		for (SchemaPort sp : cmp.getSchemaPorts()) {
			Caseless mapping = sp.getMapping();

			XYLocation offset = sp.getOffset();

			if (offset.x == 0 || offset.x == w) {
				graphics.drawLine(offset.x, offset.y, w / 2, offset.y);
			}
			if (offset.y == 0 || offset.y == h) {
				graphics.drawLine(offset.x, offset.y, offset.x, h / 2);
			}

			Color c = graphics.getColor();
			if (!Caseless.isNullOrEmpty(mapping)) {
				// pin connected
				graphics.setColor(Color.BLACK);
			} else {
				// pin not connected
				graphics.setColor(Color.WHITE);
			}
			graphics.fillOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE
					/ 2, PORT_SIZE, PORT_SIZE);
			graphics.setColor(c);
			graphics.drawOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE
					/ 2, PORT_SIZE, PORT_SIZE);
		}

		// draw component body with triangles and port nums
		Port port;
		Iterator<Port> it = cmp.portIterator();
		try {
			port = it.next();
		} catch (NoSuchElementException e) {
			throw new IllegalStateException(
					"In-out component must have at least one port.", e);
		}
		PortDirection dir = port.getDirection();
		Type tp = port.getType();
		boolean downto;
		if(tp.getRange().isVector()) {
		    downto = tp.getRange().getDirection().isDOWNTO();
		} else {
		    downto = false; // FFS! hate when this happens
		}
		int rangeoffset = (tp.getRange().isScalar()) ? (0) : (tp.getRange().getFrom());

		if (dir.equals(PortDirection.IN)) {
			Color c = graphics.getColor();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, EMPTY_EDGE_OFFSET + PER_PORT_SIZE / 2, w - 2
					* PIN_LENGTH, h - 2 * EMPTY_EDGE_OFFSET - PER_PORT_SIZE);
			graphics.setColor(c);
			graphics.drawRect(0, EMPTY_EDGE_OFFSET + PER_PORT_SIZE / 2, w - 2
					* PIN_LENGTH, h - 2 * EMPTY_EDGE_OFFSET - PER_PORT_SIZE);

			int[] xps = new int[3];
			int[] yps = new int[3];
			xps[0] = w - 2 * PIN_LENGTH;
			xps[1] = w - 2 * PIN_LENGTH;
			xps[2] = w - 2 * PIN_LENGTH + TRIANGLE_SIDE * 7 / 8;
			List<SchemaPort> schports = cmp.getSchemaPorts();
			int i = 0;
			boolean szbgt1 = schports.size() > 1;
			for (SchemaPort sp : schports) {
				yps[2] = sp.getOffset().y;
				yps[0] = yps[2] - TRIANGLE_SIDE / 2;
				yps[1] = yps[0] + TRIANGLE_SIDE;
				graphics.fillPolygon(xps, yps, 3);

				// write port index
				if (szbgt1) graphics.drawString(String.valueOf((downto) ? (rangeoffset - i)
						: (i + rangeoffset)), xps[0] - FONT_SIZE * 2,
						(yps[0] + yps[1]) / 2 + FONT_SIZE / 2);

				i++;
			}
		} else if (dir.equals(PortDirection.OUT)) {
			Color c = graphics.getColor();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(PIN_LENGTH,
					EMPTY_EDGE_OFFSET + PER_PORT_SIZE / 2, w - 2 * PIN_LENGTH,
					h - 2 * EMPTY_EDGE_OFFSET - PER_PORT_SIZE);
			graphics.setColor(c);
			graphics.drawRect(PIN_LENGTH,
					EMPTY_EDGE_OFFSET + PER_PORT_SIZE / 2, w - 2 * PIN_LENGTH,
					h - 2 * EMPTY_EDGE_OFFSET - PER_PORT_SIZE);

			int[] xps = new int[3];
			int[] yps = new int[3];
			xps[0] = w - PIN_LENGTH;
			xps[1] = w - PIN_LENGTH;
			xps[2] = w - PIN_LENGTH + TRIANGLE_SIDE * 7 / 8;
			List<SchemaPort> schports = cmp.getSchemaPorts();
			int i = 0;
			boolean szbgt1 = schports.size() > 1;
			for (SchemaPort sp : schports) {
				yps[2] = sp.getOffset().y;
				yps[0] = yps[2] - TRIANGLE_SIDE / 2;
				yps[1] = yps[0] + TRIANGLE_SIDE;
				graphics.fillPolygon(xps, yps, 3);

				// write port index
				if (szbgt1) graphics.drawString(String.valueOf((downto) ? (rangeoffset - i)
						: (i + rangeoffset)), PIN_LENGTH + FONT_SIZE / 2,
						(yps[0] + yps[1]) / 2 + FONT_SIZE / 2);

				i++;
			}
		} else {
			throw new NotImplementedException("Direction '" + dir.toString() + "' not implemented.");
		}

		// draw component name
		if (properties.drawingComponentNames) {
			Font oldf = graphics.getFont();
			Color oldc = graphics.getColor();
			
			Font f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_NORMAL_FONT_SIZE);
			graphics.setFont(f);
			graphics.drawString(cmp.getName().toString(), 0, Constants.TEXT_NORMAL_FONT_SIZE * 3 / 2);
	
//			f = new Font(Constants.TEXT_FONT_CANVASNAMES, Font.PLAIN, Constants.TEXT_SMALL_FONT_SIZE);
//			int r = oldc.getRed() + 140; r = (r > 230) ? (230) : (r);
//			int g = oldc.getGreen() + 140; g = (g > 230) ? (230) : (g);
//			int b = oldc.getBlue() + 140; b = (b > 230) ? (230) : (b);
//			graphics.setColor(new Color(r, g, b));
//			graphics.setFont(f);
//			graphics.drawString(componentName, 0, Constants.TEXT_FONT_SIZE * 3 / 2);
	
			graphics.setFont(oldf);
			graphics.setColor(oldc);
		}
	}

}
