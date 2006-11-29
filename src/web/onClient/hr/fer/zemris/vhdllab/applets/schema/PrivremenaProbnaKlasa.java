package hr.fer.zemris.vhdllab.applets.schema;

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
		frame2.setSize(250, 150);
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
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
			System.out.println(muxi.getBrojPodUlaza() + " " + muxi.getSmjer());
			canvas.getAdapter().drawLine(0, 0, 100, 100);
		}
	}

}
