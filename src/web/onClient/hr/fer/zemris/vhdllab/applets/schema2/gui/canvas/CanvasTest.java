package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class CanvasTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8933593424534057949L;


	public CanvasTest() {
		SchemaCanvas can = new SchemaCanvas();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(can);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CanvasTest ct = new CanvasTest();
		ct.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		ct.pack();
		ct.setVisible(true);
	}

}
