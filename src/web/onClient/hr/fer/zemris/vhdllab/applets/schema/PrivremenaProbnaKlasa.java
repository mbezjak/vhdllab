package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_XOR;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainFrame;
import hr.fer.zemris.vhdllab.applets.schema.wires.SPair;
import hr.fer.zemris.vhdllab.applets.schema.wires.SimpleSchemaWire;
import hr.fer.zemris.vhdllab.applets.schema.wires.AbstractSchemaWire.WireConnection;

import java.awt.Point;
import java.io.StringReader;
import java.util.Set;

import org.apache.commons.digester.Digester;


// Ovo ne dirati.
//
// aleksandar
public class PrivremenaProbnaKlasa {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SchemaMainFrame mframe = new SchemaMainFrame("Milivoje sine, diko...");
		
		// proba serijalizacije/deserijalizacije
//		SimpleSchemaWire wire = new SimpleSchemaWire("ZicoMoja");
//		
//		wire.connections.add(wire.new WireConnection("MilivojeDeSi", 0));
//		SPair<Point> lin = new SPair<Point>();
//		lin.first = new Point(10, 10);
//		lin.second = new Point(10, 50);
//		wire.wireLines.add(lin);
//		lin = new SPair<Point>();
//		lin.first = new Point(10, 50);
//		lin.second = new Point(50, 50);
//		wire.wireLines.add(lin);
//		wire.nodes.add(new Point(10, 40));
//		
//		mframe.drawingCanvas.addWire(wire);
//		
//		String serial = wire.serialize();
//		System.out.println(serial);
//		SimpleSchemaWire wire2 = new SimpleSchemaWire("ZicaNasaSvagdanja");
//		wire2.deserialize(serial);
//		
//		mframe.drawingCanvas.addWire(wire2);
		// kraj - cini se da sljaka
		
		Set<String> sviSklopovi = ComponentFactory.getAvailableComponents();
		for (String s : sviSklopovi) {
			System.out.println(s);
		}
	}

}
