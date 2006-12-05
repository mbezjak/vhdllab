package hr.fer.zemris.vhdllab.applets.schema.components;

import java.awt.Point;

public abstract class AbstractSchemaPort {
	public enum PortDirection { IN, OUT }
	public enum PortOrientation { NORTH, SOUTH, WEST, EAST } 
	
	private Ptr<Object> pName;
	private Ptr<Object> pDir;
	private Ptr<Object> pOrientation;
	private Point coordinate;
	private String tipPorta;
	
	public AbstractSchemaPort() {
		pName = new Ptr<Object>();
		pName.val = new String("Ime porta.");
		pDir = new Ptr<Object>();
		pDir.val = PortDirection.IN;
		pOrientation = new Ptr<Object>();
		pOrientation.val = PortOrientation.WEST;
		coordinate = new Point();
		coordinate.x = 0;
		coordinate.y = 0;
	}
	
	/**
	 * S ovim dobivas broj linija
	 * ako se radi o vektoru. Ako se ne
	 * radi, uvijek ces dobiti 1.
	 *
	 */
	public int getPortSize() {
		return 1;
	}
	
	/**
	 * Nema efekta ako radis s
	 * obicnim portom. Kod vektorskog
	 * porta ima smisla - to je broj
	 * signala u vektoru.
	 *
	 */
	public void setPortSize(int ps) {
	}

	/**
	 * @return Returns the direction.
	 */
	public PortDirection getDirection() {
		return (PortDirection) pDir.val;
	}

	/**
	 * @param direction The direction to set.
	 */
	public void setDirection(PortDirection direction) {
		this.pDir.val = direction;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return (String) pName.val;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.pName.val = name;
	}

	/**
	 * @return Returns the orientation.
	 */
	public PortOrientation getOrientation() {
		return (PortOrientation) pOrientation.val;
	}

	/**
	 * @param orientation The orientation to set.
	 */
	public void setOrientation(PortOrientation orientation) {
		this.pOrientation.val = orientation;
	}
	
	/**
	 * @return Returns the coordinate.
	 */
	public Point getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate The coordinate to set.
	 */
	public void setCoordinate(Point coordinate) {
		this.coordinate = coordinate;
	}	

	/**
	 * @return Returns the tipPorta.
	 */
	public String getTipPorta() {
		return tipPorta;
	}

	/**
	 * @param tipUlaza The tipPorta to set.
	 */
	public void setTipPorta(String tipUlaza) {
		this.tipPorta = tipUlaza;
	}
	
	
	public abstract String getTypeID();
	
	public String serialize() {
		StringBuilder builder = new StringBuilder();
		
		// opcenite stvari
		builder.append("<imePorta>").append(this.getName()).append("</imePorta>");
		builder.append("<smjerPorta>").append(this.getDirection()).append("</smjerPorta>");
		builder.append("<orijentacijaPorta>").append(this.getOrientation()).append("</orijentacijaPorta>");
		builder.append("<Xporta>").append(this.coordinate.x).append("</Xporta>");
		builder.append("<Yporta>").append(this.coordinate.y).append("</Yporta>");
		builder.append("<tipPorta>").append(this.tipPorta).append("</tipPorta>");
		
		// specificno za naslijedeni port
		builder.append("<portSpecific>").append(serializeSpecific()).append("</portSpecific>");
		
		return builder.toString();
	}	
	public void deserialize(String code) {
		// opcenite stvari
		String s = code.substring(code.indexOf("<imePorta>") + 10, code.indexOf("</imePorta>"));
		this.setName(s);
		s = code.substring(code.indexOf("<smjerPorta>") + 12, code.indexOf("</smjerPorta>"));
		this.setDirection(PortDirection.valueOf(s));
		s = code.substring(code.indexOf("<orijentacijaPorta>") + 19, code.indexOf("</orijentacijaPorta>"));
		this.setOrientation(PortOrientation.valueOf(s));
		Point p = new Point();
		s = code.substring(code.indexOf("<XPorta>") + 8, code.indexOf("</XPorta>"));
		p.x = Integer.parseInt(s);
		s = code.substring(code.indexOf("<YPorta>") + 8, code.indexOf("</YPorta>"));
		p.y = Integer.parseInt(s);
		this.setCoordinate(p);
		s = code.substring(code.indexOf("<tipPorta>") + 10, code.indexOf("</tipPorta>"));
		this.setTipPorta(s);
		
		// specificno za naslijedeni port
		s = code.substring(code.indexOf("<portSpecific>") + 14, code.indexOf("</portSpecific>"));
		deserializeSpecific(s);
	}
	
	protected abstract String serializeSpecific();
	protected abstract void deserializeSpecific(String code);
	

	// dalje nemoj gledat, nije bitno
	static AbstractSchemaPort generatePort(String typeID) {
		if (typeID == "SchemaPort") return new SchemaPort();
		if (typeID == "SchemaVectorPort") return new SchemaVectorPort();
		return null;
	}
	
	
	public Ptr<Object> getNamePtr() {
		return pName;
	}
	
	public Ptr<Object> getDirectionPtr() {
		return pDir;
	}
	
	public Ptr<Object> getOrientationPtr() {
		return pOrientation;
	}
}





