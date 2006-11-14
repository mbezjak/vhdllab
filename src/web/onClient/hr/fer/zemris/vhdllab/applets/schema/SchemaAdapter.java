package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JWindow;


/**
 * Klasa koja se prenosi svakoj komponenti
 * pri pozivu metode draw, a sluzi kao adapter.
 * Komponenta se pomocu adaptera iscrtava na grid. 
 * 
 * Koristenje: podesi se magnification factor
 * pozivom setMagnificationFactor(double). Potom
 * se prenese referenca na objekt Graphics pozivom
 * setGraphics(Graphics).
 * Po potrebi se podese pocetne koordinate pozivom
 * setStartCoordinates(int, int) - time se odreduje
 * otkud se relativno pocinje crtati bilo sto.
 * Tad se pozivaju metode drawLine, drawOval, drawRect... 
 * @author Axel
 *
 */
public class SchemaAdapter {

	private double magnificationFactor;
	private int xStart, yStart;
	private Graphics gph;
	
	public SchemaAdapter() {
		magnificationFactor = 1.d;
		xStart = 0;
		yStart = 0;
		gph = null;
	}
	
	public SchemaAdapter(double mag) {
		magnificationFactor = mag;
		xStart = 0;
		yStart = 0;
		gph = null;
	}
	
	public void setMagnificationFactor(double mag) {
		this.magnificationFactor = mag;
	}
	
	public void setStartingCoordinates(int xs, int ys) {
		xStart = xs;
		yStart = ys;
	}
	
	public void setGraphics(Graphics g) {
		gph = g;
	}
	
	public void drawLine(int x1, int y1, int x2, int y2) {
		if (gph == null) return;
		
		gph.drawLine(xStart + (int)(x1 * magnificationFactor),
				yStart + (int)(y1 * magnificationFactor),
				xStart + (int)(x2 * magnificationFactor),
				yStart + (int)(y2 * magnificationFactor));		
	}
	
	public void drawOval(int xCenter, int yCenter, int xRadius, int yRadius) {
		if (gph == null) return;
		
		gph.drawOval(xStart + (int)((xCenter - xRadius) * magnificationFactor),
				yStart + (int)((yCenter - yRadius) * magnificationFactor),
				(int)(xRadius * 2 * magnificationFactor),
				(int)(yRadius * 2 * magnificationFactor));
	}
	
	public void drawRect(int x1, int y1, int wid, int hgt) {
		if (gph == null) return;
		
		gph.drawRect(xStart + (int)(x1 * magnificationFactor), 
				yStart + (int)(y1 * magnificationFactor),
				(int)(wid * magnificationFactor),
				(int)(hgt * magnificationFactor));
	}
}





