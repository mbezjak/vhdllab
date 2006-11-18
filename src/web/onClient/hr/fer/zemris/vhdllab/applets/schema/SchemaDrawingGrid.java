package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * SchemaDrawingGrid je 2. od slojeva koji se brine o iscrtavanju
 * komponenti na povrsinu + iscrtavanje samog grid-a
 * 
 * @see 
 * @author Tommy
 *
 */

public class SchemaDrawingGrid {
	
	private Graphics2D graphics = null;
	private SchemaDrawingAdapter adapter = null;
	private SchemaColorProvider colors = null;
	private double magnificationFactor = 1.d;
	
	public SchemaDrawingGrid(Graphics g,SchemaColorProvider colors){
		graphics=(Graphics2D)g;
		this.colors=colors;
		adapter=new SchemaDrawingAdapter(graphics,colors,magnificationFactor);
	}
	
	public SchemaDrawingGrid(Graphics g,SchemaColorProvider colors, double magnificationFactor){
		graphics=(Graphics2D)g;
		this.colors=colors;
		this.magnificationFactor=magnificationFactor;
		adapter=new SchemaDrawingAdapter(graphics,colors,magnificationFactor);		
	}

	public double getMagnificationFactor() {
		return magnificationFactor;
	}

	public void setMagnificationFactor(double magnificationFactor) {
		if(adapter==null)return;
		this.magnificationFactor = magnificationFactor;
		adapter.setMagnificationFactor(magnificationFactor);		
	}

	public SchemaDrawingAdapter getAdapter() {
		return adapter;
	}

	public void setColors(SchemaColorProvider colors) {
		if(adapter==null)return;
		this.colors = colors;
		adapter.setColors(colors);
	}

}
