package hr.fer.zemris.vhdllab.applets.editor.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IWireDrawer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.DrawingProperties;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Rect2d;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;





public class DefaultWireDrawer implements IWireDrawer {

	/* static fields */
	public static final int NODE_SIZE = 5;
	private static final String ETC_STRING = "...";
	private static final int MIN_DIST_BETWEEN_SEGS = 250;
	private static final int NAME_FONT_SIZE = Constants.FONT_CANVAS_SMALL.getSize();
	private static final int MIN_SEG_DIM = NAME_FONT_SIZE * 5;

	/* private fields */
	private ISchemaWire wire_to_draw;
	private String name;
	private int ndim, nlength;

	
	/* ctors */
	
	public DefaultWireDrawer(ISchemaWire wireToDraw) {
		wire_to_draw = wireToDraw;
	}

	
	
	/* methods */
	
	public void draw(Graphics2D graphics, DrawingProperties properties) {
		
		/* draw nodes */
		for (XYLocation node : wire_to_draw.getNodes()) {
			graphics.fillOval(node.x - NODE_SIZE / 2, node.y - NODE_SIZE / 2,
					NODE_SIZE, NODE_SIZE);
		}

		/* draw segments */
		if (!properties.drawingWireNames)
			for (WireSegment segment : wire_to_draw.getSegments()) {
				/* just draw lines */
				XYLocation start = segment.getStart(), end = segment.getEnd();
				graphics.drawLine(start.x, start.y, end.x, end.y);
			}
		else {
			/* draw lines and names */
			Rect2d bounds = wire_to_draw.getBounds();
			int centerx = bounds.left + bounds.width / 2, centery = bounds.top + bounds.height / 2;
			int xmid = 0, ymid = 0, currentmin = 0;
			int xul = 0, yul = 0, xur = 0, yur = 0;
			int xdl = 0, ydl = 0, xdr = 0, ydr = 0;
			WireSegment sul = null, sur = null, sdl = null, sdr = null, smid = null;
			boolean found = false;
			name = wire_to_draw.getName().toString();
			nlength = name.length();
			ndim = nlength * NAME_FONT_SIZE;

			/* draw segments and find places for name of the wire */
			for (WireSegment segment : wire_to_draw.getSegments()) {
				XYLocation start = segment.getStart(), end = segment.getEnd();
				int segcntrx = (start.x + end.x) / 2, segcntry = (start.y + end.y) / 2;
				
				graphics.drawLine(start.x, start.y, end.x, end.y);
				
				if (segment.length() >= MIN_SEG_DIM) {
					if (!found) {
						xul = xur = xdl = xdr = xmid = segcntrx;
						yul = yur = ydl = ydr = ymid = segcntry;
						sul = sur = sdl = sdr = smid = segment;
						currentmin = Math.abs(xmid - centerx) + Math.abs(ymid - centery);
						found = true;
					} else {
						if (segcntrx < xul && segcntry < yul) {
							xul = segcntrx; yul = segcntry; sul = segment;
						}
						if (segcntrx > xur && segcntry < yur) {
							xur = segcntrx; yur = segcntry; sur = segment;
						}
						if (segcntrx < xdl && segcntry > ydl) {
							xdl = segcntrx; ydl = segcntry; sdl = segment;
						}
						if (segcntrx > xdr && segcntry > ydr) {
							xdr = segcntrx; ydr = segcntry; sdr = segment;
						}
						int tmpmin = Math.abs(segcntrx - centerx) + Math.abs(segcntry - centery);
						if (tmpmin < currentmin) {
							xmid = segcntrx;
							ymid = segcntry;
							smid = segment;
							currentmin = tmpmin;
						}
					}
				}
			}
			
			/* place wire name */
			if (found) {
				Font oldf = graphics.getFont();
				Color oldc = graphics.getColor();
				
				graphics.setFont(Constants.FONT_CANVAS_SMALL);
				int r = oldc.getRed() + 170; r = (r > 240) ? (240) : (r);
				int g = oldc.getGreen() + 170; g = (g > 240) ? (240) : (g);
				int b = oldc.getBlue() + 170; b = (b > 240) ? (240) : (b);
				graphics.setColor(new Color(r, g, b));
				
				/* place middle */
				placeName(graphics, xmid, ymid, smid);
				
				/* place upper left */
				if ((Math.abs(xmid - xul) + Math.abs(ymid - yul)) > MIN_DIST_BETWEEN_SEGS) {
					placeName(graphics, xul, yul, sul);
				}
				
				/* place upper right */
				if ((Math.abs(xmid - xur) + Math.abs(ymid - yur)) > MIN_DIST_BETWEEN_SEGS
						&& (Math.abs(xur - xul) + Math.abs(yur - yul)) > MIN_DIST_BETWEEN_SEGS)
				{
					placeName(graphics, xur, yur, sur);
				}
				
				/* place lower right */
				if ((Math.abs(xmid - xdr) + Math.abs(ymid - ydr)) > MIN_DIST_BETWEEN_SEGS
						&& (Math.abs(xur - xdr) + Math.abs(yur - ydr)) > MIN_DIST_BETWEEN_SEGS)
				{
					placeName(graphics, xdr, ydr, sdr);
				}

				/* place lower left */
				if ((Math.abs(xmid - xdl) + Math.abs(ymid - ydl)) > MIN_DIST_BETWEEN_SEGS
						&& (Math.abs(xul - xdl) + Math.abs(yul - ydl)) > MIN_DIST_BETWEEN_SEGS
						&& (Math.abs(xdr - xdl) + Math.abs(ydr - ydl)) > MIN_DIST_BETWEEN_SEGS)
				{
					placeName(graphics, xdl, ydl, sdl);
				}
				
				graphics.setFont(oldf);
				graphics.setColor(oldc);
			}
		}
	}
	
	private void placeName(Graphics2D g, int x, int y, WireSegment segment) {
		String nname = name;
		int seglen = segment.length(), nndim = ndim, nnlength = nlength;
		
		/* shorten string if necessary */
		if (seglen <= ndim) {
			/* string is surely longer than 4 chars, that was checked before */
			int avchars = seglen / NAME_FONT_SIZE;
			nname = name.substring(0, avchars - 3).concat(ETC_STRING);
			nnlength = nname.length();
			nndim = nnlength * NAME_FONT_SIZE;
		}
		
		/* draw string */
		if (segment.isVertical()) {
			x++;
			for (int i = 0, yp = y - nndim / 2; i < nnlength; i++, yp += NAME_FONT_SIZE) {
				char c = nname.charAt(i);
				g.drawString(String.valueOf(c), x, yp);
			}
		} else {
			g.drawString(nname, x - nndim / 3, y - 2);
		}
	}

}








