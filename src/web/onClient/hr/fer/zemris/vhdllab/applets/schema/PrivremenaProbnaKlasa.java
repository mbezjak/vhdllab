package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.PrivremeniProbniSklop;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_AND;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_XOR;

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
		
		Sklop_XOR sklopi = new Sklop_XOR("Prvi XOR");
		Sklop_MUX2nNA1 muxi = new Sklop_MUX2nNA1("Moj muxic");
		
		PropertyPanel panel = new PropertyPanel(muxi);
		frame.add(panel);
		frame.validate();
		
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
