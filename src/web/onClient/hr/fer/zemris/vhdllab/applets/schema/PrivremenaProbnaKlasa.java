package hr.fer.zemris.vhdllab.applets.schema;

import java.awt.Point;

import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_XOR;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingCanvas;

import javax.swing.JFrame;


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
		frame.setVisible(true);
		
		JFrame frame2 = new JFrame();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.pack();
		frame2.setSize(450, 350);
		frame2.setLocation(400, 100);
		frame2.setVisible(true);
		
		SchemaDrawingCanvas canvas = new SchemaDrawingCanvas(new SchemaColorProvider());
		frame2.add(canvas);
		frame2.validate();
		
		Sklop_XOR sklopi = new Sklop_XOR("Prvi XOR");
		Sklop_MUX2nNA1 muxi = new Sklop_MUX2nNA1("Moj muxic2");
		PropertyPanel panel = new PropertyPanel(muxi);
		frame.add(panel);
		frame.validate();
		
		SchemaDrawingAdapter ad = canvas.getAdapter();
		ad.setMagnificationFactor(1.d);
		ad.setStartingCoordinates(0, 0);
		
		canvas.addComponent(muxi, new Point(150, 150));
		ComponentFactory factory = new ComponentFactory();
		factory.registerComponent(new Sklop_AND("Sklop_AND"));
		try {
			canvas.addComponent(factory.getSchemaComponent("AND sklop"), new Point(20, 20));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
			System.out.println(muxi.getBrojPodUlaza() + " " + muxi.getSmjer());
		}
	}

}
