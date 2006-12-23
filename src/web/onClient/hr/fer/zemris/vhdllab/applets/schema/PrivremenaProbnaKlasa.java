package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.automat.entityTable.EntityTable;
import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaPort;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.misc.Sklop_PORT;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.Type;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


// Ovo ne dirati.
//
// aleksandar
public class PrivremenaProbnaKlasa {
	
	public static Port createTestPort() {
		return new Port() {
			public String getName() {
				return "Probni_port";
			}

			public Direction getDirection() {
				return Direction.IN;
			}

			public Type getType() {
				return new Type() {

					public String getTypeName() {
						return "std_logic_vector";
					}

					public int getRangeFrom() {
						return 7;
					}

					public int getRangeTo() {
						return 2;
					}

					public String getVectorDirection() {
						throw new IllegalStateException();
					}

					public boolean isScalar() {
						return false;
					}

					public boolean isVector() {
						return true;
					}

					public boolean hasVectorDirectionDOWNTO() {
						return true;
					}

					public boolean hasVectorDirectionTO() {
						return false;
					}
					
				};
			}
			
		};
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SchemaMainPanel mpanel = new SchemaMainPanel();
		mpanel.init();
		
		JFrame frame = new JFrame("Povratak milivoja");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(350, 200));
		frame.setSize(new Dimension(550, 400));
		frame.add(mpanel);
		frame.validate();
		
		Port port = createTestPort();
		//Sklop_PORT portic = new Sklop_PORT(port, mpanel.getCircuitInterface());
		//mpanel.drawingCanvas.addComponent(portic, new Point(160, 20));
		
		JDialog dialogEntitySetup = null;
		dialogEntitySetup = new JDialog(frame, "Entity Setup", true);
		String[] st = {"Name","Direction","Type","From","To"};
		EntityTable table = new EntityTable("Entity declaration:",st,"Entity name: ");
		dialogEntitySetup.add(table);
		dialogEntitySetup.setBounds(0, 0, 480, 230);
		dialogEntitySetup.setLocation(150, 150);
		dialogEntitySetup.setVisible(true);
		CircuitInterface circint = table.getCircuitInterface();
		mpanel.setCircuitInterface(circint);
		
//		List<Port> plist = circint.getPorts();
//		int counter = 2;
//		for (Port p : plist) {
//			System.out.println(p.getName());
//			System.out.println(p.getDirection());
//			System.out.println(p.getType().isScalar());
//			System.out.println(p.getType().isVector());
//			System.out.println(p.getType().hasVectorDirectionTO());
//			System.out.println(p.getType().hasVectorDirectionDOWNTO());
//			System.out.println(p.getType().getTypeName());
//			mpanel.drawingCanvas.addComponent(new Sklop_PORT(p, circint), new Point(10 * counter, 20));
//			counter += 12;
//		}
		
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
