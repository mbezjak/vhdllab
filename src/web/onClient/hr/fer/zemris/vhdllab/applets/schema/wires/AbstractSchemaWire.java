package hr.fer.zemris.vhdllab.applets.schema.wires;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

import java.awt.Point;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.digester.Digester;


public abstract class AbstractSchemaWire {
	// Svaki wire spaja se na odredeni port odredenog sklopa
	// a buduci da je ime instance sklopa jedinstveno, kao sto je i
	// redni broj porta za odredeni sklop jedinstven, svaka
	// zica mora pamtiti imena komponenti i redni broj porta na koje je spojena.
	// Nadalje, svaka zica moze biti spojena na vise od jedne komponente, i zato
	// je tu i HashSet.
	// Poznavajuci ime komponente i redni broj porta, moguce je jednoznacno odrediti
	// koordinate porta.
	// Takoder, ono sto svaka zica ima jest niz parova tocaka od kojih se sastoji. Izmedu
	// tih parova vuku se crte, te se na taj nacin zica iscrtava. Iz tog razloga je tu i
	// jedan ArrayList parova koordinata.
	// Konacno, buduci da svaki wire reprezentira jedan signal, nuzno je da wire ima i jedinstveno ime.
	// Za to se brine dolje navedena static hashmapa, koja pamti dosad iskoristena imena.
	// I na kraju - serijalizacija, treba dodati podrsku za pohranu zice u obliku stringa,
	// i, obratno, rekonstrukciju njenih svojstava na temelju stringa.
	protected class Connection {
		public String componentInstanceName;
		public int portIndex;
		
		public Connection(String componentInstanceName, int portIndex) {
			super();
			this.componentInstanceName = componentInstanceName;
			this.portIndex = portIndex;
		}

		@Override
		public int hashCode() {
			return componentInstanceName.hashCode() + Integer.valueOf(portIndex).hashCode();
		}
	}
	
	public HashSet<Connection> connections;
	public ArrayList<SPair<Point>> wireLines;
	
	private String wireName;
	public String getWireName() {
		return wireName;
	}
	public void setWireName(String name) throws SchemaWireException {
		if (name == this.wireName) return;
		if (nameSet.contains(name)) throw new SchemaWireException("Ime zice vec postoji!!");
		nameSet.remove(wireName);
		nameSet.add(name);
		this.wireName = name;
	}
	
	static protected HashSet<String> nameSet;
	static {
		nameSet = new HashSet<String>();
	}
	
	
	// SERIJALIZACIJA
	
	protected void setConnections(String connStr) {
		connections.clear();
		String [] connArray = connStr.split("#");
		for (String s : connArray) {
			String [] sfield = s.split("$");
			Connection c = new Connection(sfield[0], Integer.parseInt(sfield[1]));
			connections.add(c);
		}
	}
	
	protected String getConnections() {
		StringBuilder builder = new StringBuilder();
		for (Connection c : connections) {
			builder.append('#').append(c.componentInstanceName).append('$').append(c.portIndex);
		}
		return builder.toString();
	}
	
	protected String getWireLinesStr() {
		StringBuilder builder = new StringBuilder();
		for (SPair<Point> par : wireLines) {
			builder.append('#').append(par.first.x).append('$').append(par.first.y).append('$');
			builder.append("$").append(par.second.x).append('$').append(par.second.y);
		}
		return builder.toString();
	}
	
	protected void setWireLinesStr(String wireStr) {
		wireLines.clear();
		String [] wireArr = wireStr.split("#");
		for (String s : wireArr) {
			String [] sf = s.split("$");
			SPair<Point> par = new SPair(new Point(Integer.parseInt(sf[0]), Integer.parseInt(sf[1])),
					new Point(Integer.parseInt(sf[2]), Integer.parseInt(sf[3])));
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
		
		builder.append("<wireSpecific>");
		builder.append(serializeSpecific());
		builder.append("</wireSpecific>");
		
		builder.append("</wire>");
		return builder.toString();
	}
	
	public boolean deserialize(String serial) {
		Digester digester=new Digester();
		
		digester.push(this);
		
		// prvo deserijaliziraj opcenite stvari
		digester.addCallMethod("wire", "setWireName", 1);
		digester.addCallParam("wire/wireName", 0);
		digester.addCallMethod("wire", "setConnections", 1);
		digester.addCallParam("wire/connections", 0);
		digester.addCallMethod("wire", "setWireLineStr", 1);
		digester.addCallParam("wire/wireLines", 0);
		
		// deserijaliziraj specificno
		digester.addCallMethod("wire", "deserializeSpecific", 1);
		digester.addCallParam("wire/wireSpecific", 0);
		
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
}
