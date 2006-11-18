package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.PrivremeniProbniSklop;

import javax.swing.JFrame;

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
		
		PrivremeniProbniSklop probni = new PrivremeniProbniSklop();
		
		PropertyPanel panel = new PropertyPanel(probni.getPropertyList());
		frame.add(panel);
		frame.validate();
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
			System.out.println(probni.getComponentInstanceName());
		}
	}

}
