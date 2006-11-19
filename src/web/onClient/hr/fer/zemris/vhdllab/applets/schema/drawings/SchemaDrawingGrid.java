package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;

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
	
	public SchemaDrawingGrid(SchemaColorProvider colors){
		this.colors=colors;
	}
	
	public SchemaDrawingGrid(SchemaColorProvider colors, double magnificationFactor){
		this.colors=colors;
		this.magnificationFactor=magnificationFactor;		
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
	
	/**
	 * Metoda koju treba pozvati prije svakog iscrtavanja komponenti
	 * @param g Graphics parametar SchemaDrawingGrid komponente koja iscrtava
	 * svu grafiku.\
	 * @see hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingGrid#endDrawing()
	 */
	public void startDrawing(Graphics g){
		graphics=(Graphics2D)g;
		adapter.startDrawing(graphics);
	}
	
	/**
	 * Metoda kojom se zavrsava iscrtavanja komponenti.
	 * Mora biti u kombinaciji sa startDrawing(Graphics) metodom.
	 * @see hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingGrid#startDrawing(Graphics)
	 *
	 */
	public void  endDrawing(){
		adapter.endDrawing();
		graphics=null;
	}

}
