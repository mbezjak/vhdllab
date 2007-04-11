package hr.fer.zemris.vhdllab.applets.schema2.misc;



/**
 * Port neke komponente.
 * 
 * @author Axel
 *
 */
public class SchemaPort {
	
	private XYLocation loc;
	private String name;
	
	public SchemaPort() {
		loc = new XYLocation();
		name = "";
	}
	
	public SchemaPort(int xoffset, int yoffset) {
		loc = new XYLocation(xoffset, yoffset);
		name = "";
	}
	
	public SchemaPort(int xoffset, int yoffset, String portname) {
		loc = new XYLocation(xoffset, yoffset);
		name = portname;
	}
	
	/**
	 * Vraca offset porta od gornjeg
	 * lijevog kuta komponente.
	 * 
	 * @return
	 */
	XYLocation getOffset() {
		return loc;
	}
	
	
	/**
	 * Postavlja offset porta.
	 * 
	 * @param offset
	 */
	void setOffset(XYLocation offset) {
		loc = offset;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}




