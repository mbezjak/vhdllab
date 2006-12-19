package hr.fer.zemris.vhdllab.applets.schema.wires;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Color;
import java.awt.Point;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.digester.Digester;


public abstract class AbstractSchemaWire {
	// Svaki wire spaja se na odredeni port odredenog sklopa
	// a buduci da je ime instance sklopa jedinstveno, kao sto je i
	// redni broj porta za odredeni sklop jedinstven, svaka
	// zica mora pamtiti imena komponenti i redni broj porta na koje je spojena.
	// Nadalje, svaka zica moze biti spojena na vise od jedne komponente, i zato
	// je tu i HashSet.
	//
	// Poznavajuci ime komponente i redni broj porta, moguce je jednoznacno odrediti
	// koordinate porta. Bez obzira na to, tu je dodana tocka koja odreduje port
	// iz razloga sto to pojednostavljuje stvari pri promjeni koordinata porta.
	//
	// Takoder, ono sto svaka zica ima jest niz parova tocaka od kojih se sastoji. Izmedu
	// tih parova vuku se crte, te se na taj nacin zica iscrtava. Iz tog razloga je tu i
	// jedan ArrayList parova koordinata.
	//
	// Ako se zica sastoji od vise dijelova, onda ona ima i cvorove - tockice kod kojih se
	// zica grana.
	//
	// Konacno, buduci da svaki wire reprezentira jedan signal, nuzno je da wire ima i jedinstveno ime.
	// Za to se brine dolje navedena static hashmapa, koja pamti dosad iskoristena imena.
	// I na kraju - serijalizacija, treba dodati podrsku za pohranu zice u obliku stringa,
	// i, obratno, rekonstrukciju njenih svojstava na temelju stringa.
	//
	// Svaka zica se moze i sama iscrtavati pozivom metode draw.
	public class WireConnection {
		public String componentInstanceName;
		public int portIndex;
		public Point portCoord;
	
		public WireConnection(String componentInstanceName, int portIndex, Point portCoord) {
			super();
			this.componentInstanceName = componentInstanceName;
			this.portIndex = portIndex;
			this.portCoord = portCoord;
		}

		@Override
		public int hashCode() {
			return componentInstanceName.hashCode() + Integer.valueOf(portIndex).hashCode();
		}
	}
	
	public HashSet<WireConnection> connections;
	public HashSet<SPair<Point>> wireLines;
	public HashSet<Point> nodes;
	
	private String wireName;
	private int thickness;
	public String getWireName() {
		return wireName;
	}
	public void setWireName(String name) throws SchemaWireException {
		if (name.charAt(0) >= '0' && name.charAt(0) <= '9') name = '_' + name;
		name = name.replaceAll("[^a-zA-Z0-9_]", "_");
		if (name == this.wireName) return;
		if (nameSet.contains(name)) {
			name = generateNewName(name);
		}
		if (nameSet.contains(name)) throw new SchemaWireException("Ime zice vec postoji!!");
		nameSet.remove(wireName);
		nameSet.add(name);
		this.wireName = name;
	}
	
	static protected HashSet<String> nameSet;
	static protected Integer counter;
	static {
		nameSet = new HashSet<String>();
		counter = 0;
	}
	static protected String generateNewName(String oldname) {
		counter++;
		return oldname + counter.toString();
	}
	
	// KONSTRUKTORI
	
	public AbstractSchemaWire(String wireName) {
		super();
		try {
			setWireName(wireName);
		} catch (SchemaWireException e) {
			e.printStackTrace();
		}
		connections = new HashSet<WireConnection>();
		wireLines = new HashSet<SPair<Point>>();
		nodes = new HashSet<Point>();
		thickness = 1;
	}
	
	// ISCRTAVANJE
	
	public void draw(SchemaDrawingAdapter adapter) {
		for (SPair<Point> par : wireLines) {
			adapter.drawLine(par.first.x, par.first.y, par.second.x, par.second.y, thickness);
		}
		for (WireConnection conn : connections) {
			// iscrtavanje mjesta connectiona
		}
		for (Point p : nodes) {
			adapter.drawCursorPoint(p.x, p.y, Color.BLACK);
		}
	}
	
	
	// SERIJALIZACIJA
	
	protected void setConnections(String connStr) {
		connections.clear();
		String [] connArray = connStr.split("#");
		for (String s : connArray) {
			if (s.compareTo("") == 0) continue;
			String [] sfield = s.split(";");
			WireConnection c = new WireConnection(sfield[0], Integer.parseInt(sfield[1]),
					new Point(Integer.parseInt(sfield[2]), Integer.parseInt(sfield[3])));
			connections.add(c);
		}
	}
	
	protected String getConnections() {
		StringBuilder builder = new StringBuilder();
		for (WireConnection c : connections) {
			builder.append('#').append(c.componentInstanceName).append(';').append(c.portIndex);
			builder.append(';').append(c.portCoord.x).append(';').append(c.portCoord.y);
		}
		return builder.toString();
	}
	
	protected String getNodesStr() {
		StringBuilder builder = new StringBuilder();
		for (Point node : nodes) {
			builder.append('#').append(node.x).append(';').append(node.y);
		}
		return builder.toString();
	}
	
	protected void setNodesStr(String nodeStr) {
		nodes.clear();
		String [] nodeArr = nodeStr.split("#");
		for (String s : nodeArr) {
			if (s.compareTo("") == 0) continue;
			String [] sf = s.split(";");
			Point n = new Point(Integer.parseInt(sf[0]), Integer.parseInt(sf[1]));
			nodes.add(n);
		}
	}
	
	protected String getWireLinesStr() {
		StringBuilder builder = new StringBuilder();
		for (SPair<Point> par : wireLines) {
			builder.append('#').append(par.first.x).append(";").append(par.first.y);
			builder.append(";").append(par.second.x).append(";").append(par.second.y);
		}
		return builder.toString();
	}
	
	protected void setWireLinesStr(String wireStr) {
		System.out.println(wireStr);
		wireLines.clear();
		String [] wireArr = wireStr.split("#");
		for (String s : wireArr) {
			if (s.compareTo("") == 0) continue;
			String [] sf = s.split(";");
			SPair<Point> par = new SPair<Point>(
					new Point(Integer.parseInt(sf[0]), Integer.parseInt(sf[1])),
					new Point(Integer.parseInt(sf[2]), Integer.parseInt(sf[3]))
					);
			wireLines.add(par);
		}
	}
	
	public String serialize() {
		StringBuilder builder = new StringBuilder("<wire>");
		builder.append("<wireName>").append(wireName).append("</wireName>");
		
		builder.append("<connections>");
		builder.append(getConnections());
		builder.append("</connections>");
		
		builder.append("<wireLines>");
		builder.append(getWireLinesStr());
		builder.append("</wireLines>");
		
		builder.append("<nodes>");
		builder.append(getNodesStr());
		builder.append("</nodes>");
		
		builder.append("<wireSpecific>");
		builder.append(serializeSpecific());
		builder.append("</wireSpecific>");
		
		builder.append("</wire>");
		return builder.toString();
	}
	
	public void performDeserialization(String wireNameStr, String connStr, String wireLineStr, String nodeStr, String specificStr) {
		try {
			setWireName(wireNameStr);
		} catch (SchemaWireException e) {
			e.printStackTrace();
		}
		System.out.println(connStr);
		System.out.println(wireLineStr);
		System.out.println(nodeStr);
		setConnections(connStr);
		setWireLinesStr(wireLineStr);
		setNodesStr(nodeStr);
		deserializeSpecific(specificStr);
	}
	
	public boolean deserialize(String serial) {
		Digester digester=new Digester();
		
		digester.push(this);
		
		// prvo deserijaliziraj opcenite stvari
		digester.addCallMethod("wire", "performDeserialization", 5);
		digester.addCallParam("wire/wireName", 0);
		digester.addCallParam("wire/connections", 1);
		digester.addCallParam("wire/wireLines", 2);
		digester.addCallParam("wire/nodes", 3);
		// deserijaliziraj specificno
		digester.addCallParam("wire/wireSpecific", 4);
		
		try {
			digester.parse(new StringReader(serial));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	protected abstract String serializeSpecific();
	protected abstract boolean deserializeSpecific(String serial);
	
	
	// neki getteri i setteri
	
	public int getThickness() {
		return thickness;
	}
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
	
	public static Integer getCounter() {
		return counter;
	}
	public static void setCounter(Integer counter) {
		AbstractSchemaWire.counter = counter;
	}
	

	// Ovo dalje je za upravljanje kolekcijama wireLines i connections	
	
	public boolean add_Connections(WireConnection arg0) {
		return connections.add(arg0);
	}
	public boolean addAll_Connections(Collection<? extends WireConnection> arg0) {
		return connections.addAll(arg0);
	}
	public void clear_Connections() {
		connections.clear();
	}
	public Object clone_Connections() {
		return connections.clone();
	}
	public boolean contains_Connections(Object arg0) {
		return connections.contains(arg0);
	}
	public boolean containsAll_Connections(Collection<?> arg0) {
		return connections.containsAll(arg0);
	}
	public boolean equals_Connections(Object arg0) {
		return connections.equals(arg0);
	}
	public int hashCode_Connections() {
		return connections.hashCode();
	}
	public boolean isEmpty_Connections() {
		return connections.isEmpty();
	}
	public Iterator<WireConnection> iterator_Connections() {
		return connections.iterator();
	}
	public boolean remove_Connections(Object arg0) {
		return connections.remove(arg0);
	}
	public boolean removeAll_Connections(Collection<?> arg0) {
		return connections.removeAll(arg0);
	}
	public boolean retainAll_Connections(Collection<?> arg0) {
		return connections.retainAll(arg0);
	}
	public int size_Connections() {
		return connections.size();
	}
	public Object[] toArray_Connections() {
		return connections.toArray();
	}
	public <T> T[] toArray_Connections(T[] arg0) {
		return connections.toArray(arg0);
	}
	public String toString_Connections() {
		return connections.toString();
	}
	
	
	public boolean add_WireLines(SPair<Point> arg0) {
		return wireLines.add(arg0);
	}
	public boolean add_WireLines(Point p1, Point p2) {
		return add_WireLines(new SPair<Point>(p1, p2));
	}
	public boolean addAll_WireLines(Collection<? extends SPair<Point>> arg0) {
		return wireLines.addAll(arg0);
	}
	public void clear_WireLines() {
		wireLines.clear();
	}
	public Object clone_WireLines() {
		return wireLines.clone();
	}
	public boolean contains_WireLines(Object arg0) {
		return wireLines.contains(arg0);
	}
	public boolean containsAll_WireLines(Collection<?> arg0) {
		return wireLines.containsAll(arg0);
	}
	public boolean equals_WireLines(Object arg0) {
		return wireLines.equals(arg0);
	}
	public int hashCode_WireLines() {
		return wireLines.hashCode();
	}
	public boolean isEmpty_WireLines() {
		return wireLines.isEmpty();
	}
	public Iterator<SPair<Point>> iterator_WireLines() {
		return wireLines.iterator();
	}
	public boolean remove_WireLines(Object arg0) {
		return wireLines.remove(arg0);
	}
	public boolean removeAll_WireLines(Collection<?> arg0) {
		return wireLines.removeAll(arg0);
	}
	public boolean retainAll_WireLines(Collection<?> arg0) {
		return wireLines.retainAll(arg0);
	}
	public int size_WireLines() {
		return wireLines.size();
	}
	public Object[] toArray_WireLines() {
		return wireLines.toArray();
	}
	public <T> T[] toArray_WireLines(T[] arg0) {
		return wireLines.toArray(arg0);
	}
	public String toString_WireLines() {
		return wireLines.toString();
	}
	
	
}
