package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.SchemaConnectionPoint;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;


/**
 * Klasa koja se prenosi svakoj komponenti
 * pri pozivu metode draw, a sluzi kao adapter izmedju stvarnog
 * iscrtavanja piksela po povrsini i "relativnog" crtanja
 * prilagodjenog crtanju komponenti. 
 * 
 * Po potrebi se podese pocetne koordinate pozivom
 * setStartCoordinates(int, int) - time se odreduje
 * otkud se relativno pocinje crtati bilo sto.
 * Tad se pozivaju metode drawLine, drawOval, drawRect... 
 * @author Axel
 * @author Tommy
 */
public class SchemaDrawingAdapter {

	private double magnificationFactor;
	private int xStart, yStart;
	private Dimension2D canvasDimension = null;
	private Graphics2D gph;
	private SchemaColorProvider colors;
	
	public SchemaDrawingAdapter(SchemaColorProvider colors,double mag) {
		magnificationFactor = mag;
		xStart = 0;
		yStart = 0;
		this.colors=colors;
	}
	
	public void startDrawing(Graphics2D g){
		gph=g;
	}
	
	public void endDrawing(){
		gph=null;
	}
	
	public void setMagnificationFactor(double mag) {
		this.magnificationFactor = mag;
	}
	
	public void setStartingCoordinates(int xs, int ys) {
		xStart = xs;
		yStart = ys;
	}
	
	public void drawLine(int x1, int y1, int x2, int y2) {
		if (gph == null) return;
		
		gph.setColor(colors.ADAPTER_LINE);
		
		gph.drawLine(xStart + (int)(x1 * magnificationFactor),
				yStart + (int)(y1 * magnificationFactor),
				xStart + (int)(x2 * magnificationFactor),
				yStart + (int)(y2 * magnificationFactor));		
	}
	
	public void drawOval(int xCenter, int yCenter, int xRadius, int yRadius) {
		if (gph == null) return;
		
		gph.setColor(colors.ADAPTER_LINE);
		
		gph.drawOval(xStart + (int)((xCenter - xRadius) * magnificationFactor),
				yStart + (int)((yCenter - yRadius) * magnificationFactor),
				(int)(xRadius * 2 * magnificationFactor),
				(int)(yRadius * 2 * magnificationFactor));
	}
	
	public void drawRect(int x1, int y1, int wid, int hgt) {
		if (gph == null) return;
		
		gph.setColor(colors.ADAPTER_LINE);
		
		gph.drawRect(xStart + (int)(x1 * magnificationFactor), 
				yStart + (int)(y1 * magnificationFactor),
				(int)(wid * magnificationFactor),
				(int)(hgt * magnificationFactor));
	}
	
	public void drawConnectionPoint(SchemaConnectionPoint point){
		if(gph==null) return;
	}

	public void setColors(SchemaColorProvider colors) {
		this.colors = colors;
	}
	
}





