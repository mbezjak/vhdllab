package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

import javax.swing.JFrame;


// Ovo ne dirati.
//
// aleksandar
public class PrivremenaProbnaKlasa {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SchemaMainPanel mpanel = new SchemaMainPanel();
		
		JFrame frame = new JFrame("Povratak milivoja");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(350, 200));
		frame.setSize(new Dimension(550, 400));
		frame.add(mpanel);
		frame.validate();
		
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
