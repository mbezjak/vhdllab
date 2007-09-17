package hr.fer.zemris.vhdllab.applets.editor.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.DrawingProperties;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;



public class InvalidatedDrawer implements IComponentDrawer {
	

	/* static fields */
	public static final int EDGE_OFFSET = (int) (Constants.GRID_SIZE * 0.5);
	private static final int BORDER_LINES_STEP = Constants.GRID_SIZE / 2;
	
	
	
	
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
		graphics.setColor(Color.WHITE);
		graphics.fillRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		graphics.setColor(Constants.COLOR_ERROR);
		graphics.drawRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		
		// draw invalidated sign
		graphics.drawLine(EDGE_OFFSET, EDGE_OFFSET, w - EDGE_OFFSET, h - EDGE_OFFSET);
		graphics.drawLine(EDGE_OFFSET, h - EDGE_OFFSET, w - EDGE_OFFSET, EDGE_OFFSET);
		
		// draw border
		int wmeo = w - EDGE_OFFSET;
		int hmeo = h - EDGE_OFFSET;
		for (int j = BORDER_LINES_STEP * 2; j <= hmeo; j += BORDER_LINES_STEP) {
			graphics.drawLine(0, j, EDGE_OFFSET, j - BORDER_LINES_STEP);
			graphics.drawLine(wmeo, j, w, j - BORDER_LINES_STEP);
		}
		for (int i = 0; i <= wmeo; i += BORDER_LINES_STEP) {
			graphics.drawLine(i, EDGE_OFFSET, i + BORDER_LINES_STEP, 0);
			graphics.drawLine(i, h, i + BORDER_LINES_STEP, hmeo);
		}
		
		// draw component type name and instance name
		if (properties.drawingComponentNames) {
			Font oldf = graphics.getFont();
			Color oldc = graphics.getColor();
			
			Font f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_NORMAL_FONT_SIZE);
			graphics.setFont(f);
			graphics.drawString(cmptodraw.getName().toString(), 0, -Constants.TEXT_NORMAL_FONT_SIZE * 1.5f);
	
			f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_SMALL_FONT_SIZE);
			int r = oldc.getRed() + 140; r = (r > 230) ? (230) : (r);
			int g = oldc.getGreen() + 140; g = (g > 230) ? (230) : (g);
			int b = oldc.getBlue() + 140; b = (b > 230) ? (230) : (b);
			graphics.setColor(new Color(r, g, b));
			graphics.setFont(f);
			graphics.drawString(componentName, 0, -Constants.TEXT_NORMAL_FONT_SIZE * 0.5f);
	
			graphics.setFont(oldf);
			graphics.setColor(oldc);
		}

		graphics.setColor(startingcol);
		graphics.setFont(startingfont);
	}


}














