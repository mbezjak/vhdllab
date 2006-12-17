package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * SchemaDrawingGrid je 2. od slojeva koji se brine o iscrtavanju
 * komponenti na povrsinu + iscrtavanje samog grid-a
 * 
 * @see 
 * @author Tommy
 *
 */

public class SchemaDrawingGrid extends JComponent {
	
	private BufferedImage graphics = null;
	private SchemaDrawingAdapter adapter = null;
	private SchemaColorProvider colors = null;
	
	private double magnificationFactor = 100.d; //nemam pojma kamo da ga smjestim...
	
	private int gridSpace = 10; //razmak izmedju iscrtanog grida (u pixelima), ujedno (bi trebalo bit) je to i align grid...
	
	
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
		adapter=new SchemaDrawingAdapter(colors,graphics,magnificationFactor);
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
	
	/**
	 * Iscrtavanje crtace mreze.
	 *
	 */
	private void DrawGrid(){
		int x = graphics.getWidth()+50;
		int y = graphics.getHeight()+50;
		Dimension size=new Dimension(x/gridSpace,y/gridSpace);
		Graphics2D g=(Graphics2D) graphics.getGraphics();
		
		g.setColor(colors.GRID_LINES);
		
		for(int i=0;i<size.getWidth();i++){
			g.drawLine(i*gridSpace,0,i*gridSpace,y);	
		}
		
		for(int j=0;j<size.getHeight();j++){
			g.drawLine(0,j*gridSpace,x,j*gridSpace);
			g.drawLine(0,j*gridSpace,x,j*gridSpace);
		}
	}
	
	/**
	 * Iscrtava "tockicu" na sjecistu dviju linija grid-a.
	 * @param mousePosition Trenutna pozicija misa.
	 */
	public void ShowCursorPoint(Point mousePosition){
		int gridX,gridY;
		
		gridX=Math.round((float)mousePosition.x/10)*10;
		gridY=Math.round((float)mousePosition.y/10)*10;
		
		//TODO za kasnije, treba upogonit Adapter da crta tockicu
		//adapter.
		
		adapter.drawCursorPoint(gridX, gridY, null);
	
	}
	
	public Point getAlignedPonter(Point mousePosition){
		int gridX,gridY;
		
		gridX=Math.round((float)mousePosition.x/10)*10;
		gridY=Math.round((float)mousePosition.y/10)*10;
		
		return new Point(gridX,gridY);
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
