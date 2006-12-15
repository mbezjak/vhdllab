package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainFrame;

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
		Sklop_MUX2nNA1 muxi = new Sklop_MUX2nNA1("Moj muxic2");
		SchemaMainFrame mframe = new SchemaMainFrame("Milivoje sine...");
		
		mframe.drawingCanvas.addComponent(muxi, new Point(400, 190));
		ComponentFactory.registerComponent(new Sklop_AND("Sklop_AND"));
		try {
			mframe.drawingCanvas.addComponent(ComponentFactory.getSchemaComponent("AND sklop"), new Point(320, 20));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//propbar.getPropertyPanel().setLinkToComponent(muxi);
		
		Set<String> sviSklopovi = ComponentFactory.getAvailableComponents();
		for (String s : sviSklopovi) {
			System.out.println(s);
		}
		
		String s = muxi.serializeComponent();
		AbstractSchemaComponent compi = null;
		try {
			compi = ComponentFactory.getSchemaComponent("MUX2^nNA1", s);
			mframe.drawingCanvas.addComponent(compi, new Point(15, 15));
			mframe.drawingCanvas.addComponent(ComponentFactory.getSchemaComponent("XOR sklop"), new Point(120, 175));
		} catch (ComponentFactoryException e1) {
			e1.printStackTrace();
		}
		
		//compbar.remanufactureComponents();
		//compbar2.remanufactureComponents();
		
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				break;
			}
			//System.out.println();
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
