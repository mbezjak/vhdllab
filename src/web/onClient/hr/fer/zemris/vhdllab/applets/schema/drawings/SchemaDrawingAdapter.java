package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.SchemaConnectionPoint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
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
	public float virtualToReal(double virtualPoint){
		return (float) (virtualPoint*virtualGridFactor);
	}
	
	public int virtualToReal(int virtualPoint){
		return (int)(virtualPoint*virtualGridFactor);
	}
	
	/**
	 * Pretvara virtualnu tocku u realnu i dodaje relativni pomak po X osi (virtualX).
	 * @param virtualPoint Virtualna tocka
	 * @return Realnu tocku pomaknutu za relanu udaljenost virtualX
	 */
	public int virtualToRealRelativeX(int virtualPoint){
		return (int)((virtualPoint+virtualX)*virtualGridFactor);
	}
	
	public int virtualToRealRelativeX(float virtualPoint){
		return (int)((virtualPoint+virtualX)*virtualGridFactor);
	}
	
	/**
	 * Pretvara virtualnu tocku u realnu i dodaje relativni pomak po Y osi (virtualY).
	 * @param virtualPoint Virtualna tocka
	 * @return Realnu tocku pomaknutu za relanu udaljenost virtualY
	 */
	public int virtualToRealRelativeY(int virtualPoint){
		return (int)((virtualPoint+virtualY)*virtualGridFactor);
	}
	
	public int virtualToRealRelativeY(float virtualPoint){
		return (int)((virtualPoint+virtualY)*virtualGridFactor);
	}
	
	/**
	 * Pretvara realne koordinate u virtualne.
	 * @param realPoint Stvarna (pixel) koordinata
	 * @return Virtualnu koordinatu
	 */
	public int realToVirtual(int realPoint){
		return (int) Math.round(realPoint/virtualGridFactor);
	}
	
	public int realToVirtualRelativeX(int realX) {
		return (int) Math.round(realX/virtualGridFactor - virtualX);
	}
	
	public int realToVirtualRelativeY(int realY) {
		return (int) Math.round(realY/virtualGridFactor - virtualY);
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
	
	public void setVirtualGridFactor(double vgf) {
		this.virtualGridFactor = vgf;
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
	
	public int getStartingX() {
		return virtualX;
	}
	
	public int getStartingY() {
		return virtualY;
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
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		//postavljanje boje crtanja
		graph.setColor(colors.ADAPTER_LINE);
		
		
		Line2D.Float line=new Line2D.Float();
		
		line.x1=virtualToRealRelativeX(virtualX1);
		line.y1=virtualToRealRelativeY(virtualY1);
		line.x2=virtualToRealRelativeX(virtualX2);
		line.y2=virtualToRealRelativeY(virtualY2);
		
		graph.draw(line);
	}
	
	public void drawLine(int virtualX1, int virtualY1, int virtualX2, int virtualY2, int thick) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		//postavljanje boje crtanja
		graph.setColor(colors.ADAPTER_LINE);
		graph.setStroke(new BasicStroke(thick));
		
		
		Line2D.Float line=new Line2D.Float();
		
		line.x1=virtualToRealRelativeX(virtualX1);
		line.y1=virtualToRealRelativeY(virtualY1);
		line.x2=virtualToRealRelativeX(virtualX2);
		line.y2=virtualToRealRelativeY(virtualY2);
		
		graph.draw(line);
	}
	
	public void drawLine(int virtualX1, int virtualY1, float virtualX2, float virtualY2) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		//postavljanje boje crtanja
		graph.setColor(colors.ADAPTER_LINE);
		
		
		Line2D.Float line=new Line2D.Float();
		
		line.x1=virtualToRealRelativeX(virtualX1);
		line.y1=virtualToRealRelativeY(virtualY1);
		
		line.x2=virtualToRealRelativeX(virtualX2);
		line.y2=virtualToRealRelativeY(virtualY2);
		
		graph.draw(line);
	}
	
	public void drawThickLine(int virtualX1, int virtualY1, int virtualX2, int virtualY2, float thickness) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		//postavljanje boje crtanja
		graph.setColor(colors.ADAPTER_LINE);
		
		
		Line2D.Float line=new Line2D.Float();
		
		line.x1=virtualToRealRelativeX(virtualX1);
		line.y1=virtualToRealRelativeY(virtualY1);
		
		line.x2=virtualToRealRelativeX(virtualX2);
		line.y2=virtualToRealRelativeY(virtualY2);
		
		Stroke stroke = graph.getStroke();
		graph.setStroke(new BasicStroke(thickness));
		graph.draw(line);
		graph.setStroke(stroke);
	}
	
	/**
	 * Crta onu tockicu koja daje neku naznaku align-a po grid-u.
	 * @param realX
	 * @param realY
	 */
	public void drawCursorPoint(int realX,int realY, Color c) {
		if(gph==null)return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (c == null) graph.setColor(colors.ADAPTER_CURSOR_POINT);
		else graph.setColor(c);
		
		// NAPOMENA: stogod da napravis s ovih 6 - to je promjer tockice,
		// pazi da stavis da se od koordinata oduzima pola promjera - da 
		// tockica bude na sredini porta
		graph.fillOval(virtualToRealRelativeX(realX) - 3, virtualToRealRelativeY(realY) - 3, 6, 6);			
	}
	
	//TODO ovo jos treba prepraviti jer nisam stigo istestirat to... :(
	public void drawOval(int virtualX, int virtualY, int virtualXRadius, int virtualYRadius) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(colors.ADAPTER_LINE);
		
		graph.drawOval(virtualToRealRelativeX(virtualX),
				virtualToRealRelativeY(virtualY),
				virtualToReal(virtualXRadius),
				virtualToReal(virtualYRadius));
	}
	
	public void drawOvalSegment(int virtXCenter, int virtYCenter, int virtXRadius, int virtYRadius, int arcStart, int arcAdded) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
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
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
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
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(colors.ADAPTER_LINE);
		
		graph.drawRect(virtualToRealRelativeX(virtualX1),
					   virtualToRealRelativeY(virtualY1),
					   virtualToReal(virtualWid),
					   virtualToReal(virtualHgt));
	}
	
	public void fillRect(int virtualX1, int virtualY1, int virtualWid, int virtualHgt) {
		if (gph == null) return;
		
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(Color.WHITE);
		
		graph.fillRect(virtualToRealRelativeX(virtualX1),
					   virtualToRealRelativeY(virtualY1),
					   virtualToReal(virtualWid),
					   virtualToReal(virtualHgt));
	}
	
	public void draw4gon(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		if (gph == null) return;
		
		Graphics2D graph = (Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
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
	
	public Shape drawCubic(int x1, int y1, float xc1, float yc1, float xc2, float yc2, int x2, int y2) {
		if (gph == null) return null;

		Graphics2D graph = (Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(colors.ADAPTER_LINE);
		
		x1 = virtualToRealRelativeX(x1);
		y1 = virtualToRealRelativeY(y1);
		xc1 = virtualToRealRelativeX(xc1);
		yc1 = virtualToRealRelativeY(yc1);
		xc2 = virtualToRealRelativeX(xc2);
		yc2 = virtualToRealRelativeY(yc2);
		x2 = virtualToRealRelativeX(x2);
		y2 = virtualToRealRelativeY(y2);
		
		CubicCurve2D cubic = new CubicCurve2D.Float();
		cubic.setCurve(x1, y1, xc1, yc1, xc2, yc2, x2, y2);
		
		graph.draw(cubic);
		return cubic;
	}
	
	public void fillRightBlackTriangle(int virtualX1, int virtualY1, int virtualWid, int virtualHgt) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(Color.BLACK);
		
		int [] xcoord = new int[3];
		int [] ycoord = new int[3];
		
		xcoord[0] = virtualToRealRelativeX(virtualX1);
		xcoord[1] = virtualToRealRelativeX(virtualX1);
		xcoord[2] = virtualToRealRelativeX(virtualX1 + virtualWid);
		
		ycoord[0] = virtualToRealRelativeY(virtualY1);
		ycoord[1] = virtualToRealRelativeY(virtualY1 + virtualHgt);
		ycoord[2] = virtualToRealRelativeY(virtualY1 + virtualHgt / 2);
		
		graph.fillPolygon(xcoord, ycoord, 3);
	}
	
	public void draw(Shape shape) {
		if (gph == null) return;

		Graphics2D graph = (Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(colors.ADAPTER_LINE);
		
		graph.draw(shape);
	}
	
	public void fill(Shape shape) {
		if (gph == null) return;

		Graphics2D graph = (Graphics2D) gph.getGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(Color.WHITE);
		
		graph.fill(shape);
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
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		graph.setColor(colors.ADAPTER_LINE);
		
		if (s != null) graph.drawString(s, virtualToRealRelativeX(virtx),
				   virtualToRealRelativeY(virty));
	}
	
}





