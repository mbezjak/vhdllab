package hr.fer.zemris.vhdllab.applets.automat;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Testpan extends JFrame {

	private static final long serialVersionUID = 3329611576585955404L;
	
	Automat aut=null;

	public Testpan() throws FileNotFoundException{
		super();
		this.getContentPane().setLayout(new BorderLayout());
		
		BufferedReader reader=new BufferedReader(new FileReader("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/automat1.xml"));
		//BufferedReader reader=new BufferedReader(new FileReader("c:\\dd.txt"));
		String xmlAut=new String();
		String pom=new String();
		try {
			while((pom=reader.readLine())!=null) xmlAut=new StringBuffer(xmlAut).append(pom).append("\n").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(xmlAut);
		
		FileContent fc=new FileContent("proba","test","");
		
		aut = new Automat();
		aut.setFileContent(fc);
		this.getContentPane().add((Component) aut,BorderLayout.CENTER);
		JButton b1=new JButton("Ispisi interni kod");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println(aut.getData());
			};
		});
		this.add(b1,BorderLayout.NORTH);
		this.setSize(((Component) aut).getPreferredSize());
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Testpan tp=new Testpan();
		tp.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		tp.pack();
		tp.setVisible(true);	
	}
}
