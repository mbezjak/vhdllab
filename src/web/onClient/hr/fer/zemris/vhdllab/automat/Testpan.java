package hr.fer.zemris.vhdllab.automat;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Testpan extends JFrame {

	private static final long serialVersionUID = 3329611576585955404L;

	public Testpan() throws FileNotFoundException{
		super();
		this.getContentPane().setLayout(new BorderLayout());
		AutoDrawer aut=new AutoDrawer();
		this.getContentPane().add(aut,BorderLayout.CENTER);
		this.setSize(aut.getPreferredSize());
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Testpan tp=new Testpan();
		tp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tp.pack();
		tp.setVisible(true);
	}
}
