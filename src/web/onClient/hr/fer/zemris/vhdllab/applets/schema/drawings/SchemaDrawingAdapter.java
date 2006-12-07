package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.SchemaConnectionPoint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
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
	private BufferedImage gph = null;
	private SchemaColorProvider colors = null;
	private double virtualGridFactor;//hm..neznam sto bi sa magnificationFactor-om, ali znam sto cu s ovim...uglavnom,bilo bi dobro da je
	//virtualGridFactor visekratnik gridSpace-a (u razredu ...Grid)
	
	
	/**
	 * Pretvara virtualne koordinate u realne koordinate.
	 */
	@SuppressWarnings("unused")
	private float virtualToReal(double virtualPoint){
		return (float) (virtualPoint*virtualGridFactor);
	}
	
	private int virtualToReal(int virtualPoint){
		return (int)(virtualPoint*virtualGridFactor);
	}
	
	/**
	 * Pretvara virtualnu tocku u realnu i dodaje relativni pomak po X osi (virtualX).
	 * @param virtualPoint Virtualna tocka
	 * @return Realnu tocku pomaknutu za relanu udaljenost virtualX
	 */
	private int virtualToRealRelativeX(int virtualPoint){
		return (int)((virtualPoint+virtualX)*virtualGridFactor);
	}
	
	/**
	 * Pretvara virtualnu tocku u realnu i dodaje relativni pomak po Y osi (virtualY).
	 * @param virtualPoint Virtualna tocka
	 * @return Realnu tocku pomaknutu za relanu udaljenost virtualY
	 */
	private int virtualToRealRelativeY(int virtualPoint){
		return (int)((virtualPoint+virtualY)*virtualGridFactor);
	}
	
	/**
	 * Pretvara realne koordinate u virtualne.
	 * @param realPoint Stvarna (pixel) koordinata
	 * @return Virtualnu koordinatu
	 */
	private int realToVirtual(int realPoint){
		return (int) Math.round(realPoint/virtualGridFactor);
	}
	
	public SchemaDrawingAdapter(SchemaColorProvider colors,BufferedImage canvas,double mag) {				
		this.colors=colors;
		this.gph=canvas;
		setMagnificationFactor(mag);
		setStartingCoordinates(0, 0);
	}
	
	@Deprecated
	public void setMagnificationFactor(double mag) {
		this.magnificationFactor = mag;//beskorisno
		this.virtualGridFactor = 10; //fiksno samo da radi...trebat ce jos dosta mozganja...
	}
	
	/**
	 * Postavljanje pocetnih koordinata u svrhu stvaranja novog "virtualnog" koordinatnog sustava za olaksano iscrtavanje komponente.
	 * <h3><b>Vazno!</b></h3> realX i realY bi trebale bit "poravnate" sa gridom.
	 * @param realX
	 * @param realY
	 */
	public void setStartingCoordinates(int realX, int realY) {
		this.virtualX=realToVirtual(realX);
		this.virtualY=realToVirtual(realY);
	}
	
	/**
	 * 
	 * Crta liniju sa relativnim koordinatama s obzirom na pocetne koordinate (virtualX, virtualY).
	 * 
	 * 
	 * @param virtualX1 Pocetna x koordinata
	 * @param virtualY1 Pocetna y koordinata
	 * @param virtualX2 Krajnja x koordinata
	 * @param virtualY2 Krajnja y koordinata
	 * @see hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter#setStartingCoordinates(int, int)
	 */
	public void drawLine(int virtualX1, int virtualY1, int virtualX2, int virtualY2) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		//postavljanje boje crtanja
		graph.setColor(colors.ADAPTER_LINE);
		
		
		Line2D.Float line=new Line2D.Float();
		
		line.x1=virtualToRealRelativeX(virtualX1);
		line.y1=virtualToRealRelativeY(virtualY1);
		
		line.x2=virtualToRealRelativeX(virtualX2);
		line.y2=virtualToRealRelativeY(virtualY2);
		
		graph.draw(line);
	}
	
	/**
	 * Crta onu tockicu koja daje neku naznaku align-a po grid-u.
	 * @param realX
	 * @param realY
	 */
	public void drawCursorPoint(int realX,int realY){
		if(gph==null)return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_CURSOR_POINT);
		
		// NAPOMENA: stogod da napravis s ovih 6 - to je promjer tockice,
		// pazi da stavis da se od koordinata oduzima pola promjera - da 
		// tockica bude na sredini porta
		graph.fillOval(virtualToRealRelativeX(realX) - 3, virtualToRealRelativeY(realY) - 3, 6, 6);			
	}
	
	//TODO ovo jos treba prepraviti jer nisam stigo istestirat to... :(
	public void drawOval(int virtualX, int virtualY, int virtualXRadius, int virtualYRadius) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);

		graph.drawOval(virtualToRealRelativeX(virtualX),
				virtualToRealRelativeY(virtualY),
				virtualToReal(virtualXRadius),
				virtualToReal(virtualYRadius));
	}
	
	public void drawOvalSegment(int virtXCenter, int virtYCenter, int virtXRadius, int virtYRadius, int arcStart, int arcAdded) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);

		graph.drawArc(virtualToRealRelativeX(virtXCenter),
				virtualToRealRelativeY(virtYCenter),
				virtualToReal(virtXRadius),
				virtualToReal(virtYRadius),
				arcStart, arcAdded);
	}
	
	public void fillOvalSegment(int virtXCenter, int virtYCenter, int virtXRadius, int virtYRadius, int arcStart, int arcAdded) {
		if (gph == null) return;
		
		Graphics2D graph = (Graphics2D) gph.getGraphics();
		
		graph.setColor(Color.WHITE);

		graph.fillArc(virtualToRealRelativeX(virtXCenter),
				virtualToRealRelativeY(virtYCenter),
				virtualToReal(virtXRadius),
				virtualToReal(virtYRadius),
				arcStart, arcAdded);
	}
	
	
	/**
	 * Crta pravokutnik sa relativnim koordinatama s obzirom na pocetne koordinate (virtualX, virtualY).
	 * 
	 * @param virtualX1 X koordinata gornjeg lijevog vrha.
	 * @param virtualY1 Y koordinata gornjeg lijevog vrha.
	 * @param virtualWid Širina (virtualna).
	 * @param virtualHgt Visina (virtualna).
	 * @see hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter#setStartingCoordinates(int, int)
	 */
	public void drawRect(int virtualX1, int virtualY1, int virtualWid, int virtualHgt) {
		if (gph == null) return;
		
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);
		
		graph.drawRect(virtualToRealRelativeX(virtualX1),
					   virtualToRealRelativeY(virtualY1),
					   virtualToReal(virtualWid),
					   virtualToReal(virtualHgt));
	}
	
	public void fillRect(int virtualX1, int virtualY1, int virtualWid, int virtualHgt) {
		if (gph == null) return;
		
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(Color.WHITE);
		
		graph.fillRect(virtualToRealRelativeX(virtualX1),
					   virtualToRealRelativeY(virtualY1),
					   virtualToReal(virtualWid),
					   virtualToReal(virtualHgt));
	}
	
	public void draw4gon(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		if (gph == null) return;
		
		Graphics2D graph = (Graphics2D) gph.getGraphics();
		
		graph.setColor(Color.WHITE);
		
		int [] xarr = new int[4];
		int [] yarr = new int[4];
		xarr[0] = virtualToRealRelativeX(x1);
		xarr[1] = virtualToRealRelativeX(x2);
		xarr[2] = virtualToRealRelativeX(x3);
		xarr[3] = virtualToRealRelativeX(x4);
		yarr[0] = virtualToRealRelativeY(y1);
		yarr[1] = virtualToRealRelativeY(y2);
		yarr[2] = virtualToRealRelativeY(y3);
		yarr[3] = virtualToRealRelativeY(y4);
		
		graph.fillPolygon(xarr, yarr, 4);
		graph.setColor(colors.ADAPTER_LINE);
		graph.drawPolygon(xarr, yarr, 4);
	}
	
	/**
	 * Metoda za crtanje horizontalnih (vodoravnih) zica od porta.
	 * @param virtualX
	 */
	public void drawHorizontalPortWire(int virtualX,int virtualY){
		if(gph==null)return;
		//TODO moram se dogovorit sa Alexom		
	}
	
	/**
	 * Metoda za crtanje vertikalnih (okomitih) zica od porta.
	 * @param virtualX
	 */
	public void drawVerticalPortWire(int virtualX,int virtualY){
		if(gph==null)return;
		//TODO moram se dogovorit sa Alexom
	}
	
	
	/**
	 * Metoda kojom se crta "connection point" komponente
	 * @param point
	 */
	public void drawConnectionPoint(SchemaConnectionPoint point){
		if(gph==null) return;
	}

	
	
	
	/**
	 * Postavljanje/mijenjanje "skin-a".
	 * @param colors Novi set boja koristen u Schema Drawings-u.
	 */
	public void setColors(SchemaColorProvider colors) {
		this.colors = colors;
	}
	
	
	public void drawString(String s, int virtx, int virty) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);
		
		graph.drawString(s, virtualToRealRelativeX(virtx),
				   virtualToRealRelativeY(virty));
	}
	
}





