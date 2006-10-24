package hr.fer.zemris.vhdllab.automat;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Testpan extends JFrame {

	private static final long serialVersionUID = 3329611576585955404L;

	public Testpan() throws FileNotFoundException{
		super();
		this.getContentPane().setLayout(new BorderLayout());
		
		BufferedReader reader=new BufferedReader(new FileReader("./src/web/onClient/hr/fer/zemris/vhdllab/automat/automat1.xml"));
		String xmlAut=new String();
		String pom=new String();
		try {
			while((pom=reader.readLine())!=null) xmlAut=new StringBuffer(xmlAut).append(pom).append("\n").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(xmlAut);
		
		Automat aut=new Automat(xmlAut);
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
