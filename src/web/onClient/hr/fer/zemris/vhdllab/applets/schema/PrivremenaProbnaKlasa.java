package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.StringReader;
import java.util.Set;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.components.SchemaComponentException;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_NOT;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_OR;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_XOR;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingCanvas;
import hr.fer.zemris.vhdllab.applets.schema.wires.SimpleSchemaWire;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import org.apache.commons.digester.Digester;


// Ovo ne dirati.
//
// aleksandar
public class PrivremenaProbnaKlasa {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(250, 150);
		frame.setLocation(300, 100);
		frame.setTitle("Jovan.");
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.add(new JButton("Prva"));
		panel.add(new JButton("Prva1"));
		panel.add(new JButton("Prva2"));
		panel.add(new JButton("Prva3"));
		JScrollPane scrpane = new JScrollPane(panel);
		frame.add(scrpane);
		frame.validate();
		
		JFrame frame2 = new JFrame();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.pack();
		frame2.setSize(550, 350);
		frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame2.setLocation(300, 100);
		frame2.setTitle("Moj milivoje.");
		frame2.setVisible(true);
		
		SPropertyBar propbar = new SPropertyBar();
		
		SComponentBar compbar = new SComponentBar();
		
		SchemaDrawingCanvas canvas = new SchemaDrawingCanvas(new SchemaColorProvider());
		//canvas.setPreferredSize(new Dimension(500, 500));
		frame2.setLayout(new BorderLayout());
		frame2.add(canvas, BorderLayout.CENTER);
		frame2.add(propbar, BorderLayout.EAST);
		frame2.add(compbar, BorderLayout.PAGE_START);
		frame2.validate();
		
		Sklop_XOR sklopi = new Sklop_XOR("Prvi XOR");
		Sklop_MUX2nNA1 muxi = new Sklop_MUX2nNA1("Moj muxic2");
		Sklop_OR skloporce = new Sklop_OR("Zika");
		Sklop_NOT sklopnotche = new Sklop_NOT("Nebojsha");
		
		SchemaDrawingAdapter ad = canvas.getAdapter();
		ad.setStartingCoordinates(0, 0);
		
		canvas.addComponent(muxi, new Point(400, 190));
		ComponentFactory.registerComponent(new Sklop_AND("Sklop_AND"));
		try {
			canvas.addComponent(ComponentFactory.getSchemaComponent("AND sklop"), new Point(320, 20));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		propbar.getPropertyPanel().setLinkToComponent(muxi);
		
		Set<String> sviSklopovi = ComponentFactory.getAvailableComponents();
		for (String s : sviSklopovi) {
			System.out.println(s);
		}
		
		SimpleSchemaWire wire = new SimpleSchemaWire("Moja zica");
		wire.add_WireLines(new Point(15, 15), new Point(25, 15));
		canvas.addWire(wire);
		
		String s = muxi.serializeComponent();
		AbstractSchemaComponent compi = null;
		try {
			compi = ComponentFactory.getSchemaComponent("MUX2^nNA1", s);
			canvas.addComponent(compi, new Point(50, 80));
		} catch (ComponentFactoryException e1) {
			e1.printStackTrace();
		}
		
		compbar.remanufactureComponents();
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
			//System.out.println(muxi.getBrojPodUlaza() + " " + muxi.getSmjer());
		}
	}
	
	
	
	static protected void digest(String par) {
		class Proba {
			@SuppressWarnings("unused")
			public void ispis(String s) {
				System.out.println("Ispisao sam: " + s);
			}
		}
		
		Digester digester=new Digester();
		
		digester.push(new Proba());
		
		digester.addCallMethod("komponenta/componentSpecific", "ispis", 1);
		digester.addCallParam("komponenta/componentSpecific/brojSelUlaza", 0);
		
		try {
			System.out.println("Pocinje..." + par);
			digester.parse(new StringReader(par));
		} catch (Exception e) {
			return;
		}
	}	

}
