package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * SchemaDrawingGrid je 2. od slojeva koji se brine o iscrtavanju
 * komponenti na povrsinu + iscrtavanje samog grid-a
 * 
 * @see 
 * @author Tommy
 *
 */

public class SchemaDrawingGrid {
	
	private BufferedImage graphics = null;
	private SchemaDrawingAdapter adapter = null;
	private SchemaColorProvider colors = null;
	private double magnificationFactor = 100.d;
	private int gridSpace = 5; //razmak izmedju iscrtanog grida (u pixelima)
	
	public SchemaDrawingGrid(SchemaColorProvider colors, BufferedImage canvas){
		this.colors=colors;
		this.graphics=canvas;
		
		initGRID();
	}
	
	public SchemaDrawingGrid(SchemaColorProvider colors, BufferedImage canvas, double magnificationFactor){
		this.colors=colors;
		this.magnificationFactor=magnificationFactor;
		this.graphics=canvas;
		
		initGRID();
	}
	
	public void initGRID(){
		adapter=new SchemaDrawingAdapter(colors,magnificationFactor);
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
		DrawGrid();		
		return adapter;
	}
	
	private void DrawGrid(){
		int x = graphics.getWidth();
		int y = graphics.getHeight();
		Dimension size=new Dimension(x/gridSpace,y/gridSpace);
		Graphics2D g=(Graphics2D) graphics.getGraphics();
		
		
		
		for(int i=0;i<size.getWidth();i++){
			g.drawLine(i*gridSpace,0,i*gridSpace,y);	
		}
		
		for(int j=0;j<size.getHeight();j++){
			g.drawLine(0,j*gridSpace,x,j*gridSpace);
		}
								
	}

	public void setColors(SchemaColorProvider colors) {
		if(adapter==null)return;
		this.colors = colors;
		adapter.setColors(colors);
	}


	/**height();
	 * metoda za osvjezavanje svih crtkarija  
	 */
	public void repaint(){
		DrawGrid();
	}	

}
