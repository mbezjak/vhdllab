package hr.fer.zemris.vhdllab.applets.automat;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;

import java.awt.BorderLayout;
import java.awt.Component;
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
		
		BufferedReader reader=new BufferedReader(new FileReader("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/automat1.xml"));
		String xmlAut=new String();
		String pom=new String();
		try {
			while((pom=reader.readLine())!=null) xmlAut=new StringBuffer(xmlAut).append(pom).append("\n").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(xmlAut);
		
		FileContent fc=new FileContent("proba","test","");
		
		Automat aut = new Automat();
		aut.setFileContent(fc);
		this.getContentPane().add((Component) aut,BorderLayout.CENTER);
		this.setSize(((Component) aut).getPreferredSize());
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Testpan tp=new Testpan();
		tp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tp.pack();
		tp.setVisible(true);
	}
}
