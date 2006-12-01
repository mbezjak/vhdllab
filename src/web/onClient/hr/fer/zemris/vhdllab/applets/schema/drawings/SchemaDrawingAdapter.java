package hr.fer.zemris.vhdllab.applets.schema.drawings;

import hr.fer.zemris.vhdllab.applets.schema.SchemaColorProvider;
import hr.fer.zemris.vhdllab.applets.schema.SchemaConnectionPoint;

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
		this.virtualGridFactor = 1; //fiksno samo da radi...trebat ce jos dosta mozganja...
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
		
		graph.fillOval(virtualToRealRelativeX(realX-3), virtualToRealRelativeY(realY-3), 6, 6);			
	}
	
	//TODO ovo jos treba prepraviti jer nisam stigo istestirat to... :(
	public void drawOval(int virtualX, int virtualY, int virtualXRadius, int virtualYRadius) {
		if (gph == null) return;
		
		Graphics2D graph=(Graphics2D) gph.getGraphics();
		
		graph.setColor(colors.ADAPTER_LINE);

		graph.drawOval(virtualToReal(virtualX),
				virtualToReal(virtualY),
				virtualToReal(virtualXRadius),
				virtualToReal(virtualYRadius));
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
	
}





