package hr.fer.zemris.vhdllab.applets.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.DrawingProperties;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;



public class InvalidatedDrawer implements IComponentDrawer {
	

	/* static fields */
	public static final int EDGE_OFFSET = (int) (Constants.GRID_SIZE * 0.5);
	private static final String INVALIDATED = "INVALIDATED";
	private static final String ETC_STRING = "...";
	private static final int SMALL_FONT_SIZE = Constants.FONT_CANVAS_SMALL.getSize();
	private static final int INV_SIGN_LEN = INVALIDATED.length() * SMALL_FONT_SIZE;
	
	
	
	
	/* private fields */
	private ISchemaComponent cmptodraw;
	private String componentName;
	
	
	
	public InvalidatedDrawer(ISchemaComponent componentToDraw) {
		cmptodraw = componentToDraw;
		componentName = null;
	}
	
	
	public void draw(Graphics2D graphics, DrawingProperties properties) {
		Color startingcol = graphics.getColor();
		Font startingfont = graphics.getFont();
		graphics.setColor(Constants.COLOR_ERROR);
		graphics.setFont(Constants.FONT_CANVAS_SMALL);
		
		int w = cmptodraw.getWidth();
		int h = cmptodraw.getHeight();
		if (componentName == null) componentName = cmptodraw.getTypeName().toString();
		
		// draw a rectangle
		Color c = graphics.getColor();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		graphics.setColor(c);
		graphics.drawRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		
		// draw invalidated sign
		int innerwid = (w - 2 * EDGE_OFFSET);
		if (innerwid < INV_SIGN_LEN) {
			String invsign = INVALIDATED;
			int nlen = innerwid / SMALL_FONT_SIZE - 3;
			if (nlen > 2) {
				invsign = invsign.substring(0, nlen - 3).concat(ETC_STRING);
				graphics.drawString(invsign, EDGE_OFFSET + 2, EDGE_OFFSET + 2);
			}
		} else {
			graphics.drawString(INVALIDATED, EDGE_OFFSET + 2, EDGE_OFFSET + 2);
		}
		
		// draw component type name and instance name
		if (properties.drawingComponentNames) {
			Font oldf = graphics.getFont();
			Color oldc = graphics.getColor();
			
			Font f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_NORMAL_FONT_SIZE);
			graphics.setFont(f);
			graphics.drawString(cmptodraw.getName().toString(), 0, -Constants.TEXT_NORMAL_FONT_SIZE);
	
			f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_SMALL_FONT_SIZE);
			int r = oldc.getRed() + 140; r = (r > 230) ? (230) : (r);
			int g = oldc.getGreen() + 140; g = (g > 230) ? (230) : (g);
			int b = oldc.getBlue() + 140; b = (b > 230) ? (230) : (b);
			graphics.setColor(new Color(r, g, b));
			graphics.setFont(f);
			graphics.drawString(componentName, 0, 0);
	
			graphics.setFont(oldf);
			graphics.setColor(oldc);
		}

		graphics.setColor(startingcol);
		graphics.setFont(startingfont);
	}


}














