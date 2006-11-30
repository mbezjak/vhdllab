package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.SchemaConnectionPoint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;


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
	private int virtualX, virtualY;
	private BufferedImage gph;
	private SchemaColorProvider colors;
	private double virtualGridFactor;//hm..neznam sto bi sa magnificationFactor-om, ali znam sto cu s ovim...uglavnom,bilo bi dobro da je
	//virtualGridFactor visekratnik gridSpace-a (u razredu ...Grid)
	
	
	private float virtualToReal(double virtualPoint){
		return (float) (virtualPoint*virtualGridFactor);
	}
	
	public SchemaDrawingAdapter(SchemaColorProvider colors,BufferedImage canvas,double mag) {				
		this.colors=colors;
		this.gph=canvas;
		setMagnificationFactor(mag);
		setStartingCoordinates(0, 0);
	}
		
	public void setMagnificationFactor(double mag) {
		this.magnificationFactor = mag;
		this.virtualGridFactor = mag;
	}
	
	public void setStartingCoordinates(int virtualX, int virtualY) {
		this.virtualX=virtualX;
		this.virtualY=virtualY;
	}
	
	public void drawLine(int virtualX1, int virtualY1, int virtualX2, int virtualY2) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);
		
		Line2D.Float line=new Line2D.Float();
		
		line.x1=virtualToReal(virtualX+virtualX1);
		line.y1=virtualToReal(virtualY+virtualY1);
		line.x2=virtualToReal(virtualX+virtualX2);
		line.y2=virtualToReal(virtualY+virtualY2);
		
		graph.draw(line);
	}
	
	public void drawOval(int xCenter, int yCenter, int xRadius, int yRadius) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);
		
		graph.drawOval(virtualX + (int)((xCenter - xRadius) * magnificationFactor),
				virtualY + (int)((yCenter - yRadius) * magnificationFactor),
				(int)(xRadius * 2 * magnificationFactor),
				(int)(yRadius * 2 * magnificationFactor));
	}
	
	public void drawRect(int x1, int y1, int wid, int hgt) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);
		
		graph.drawRect(virtualX + (int)(x1 * magnificationFactor), 
				virtualY + (int)(y1 * magnificationFactor),
				(int)(wid * magnificationFactor),
				(int)(hgt * magnificationFactor));
	}
	
	/**
	 * Metoda kojom se crta "connection point" komponente
	 * @param point
	 */
	public void drawConnectionPoint(SchemaConnectionPoint point){
		if(gph==null) return;
	}

	public void setColors(SchemaColorProvider colors) {
		this.colors = colors;
	}
	
}





