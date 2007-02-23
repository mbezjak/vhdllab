package hr.fer.zemris.vhdllab.applets.editor.automat;

import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.service.generator.automat.VHDLGenerator;

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
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		BufferedReader reader=new BufferedReader(new FileReader("./src/web/onClient/hr/fer/zemris/vhdllab/applets/editor/automat/automat1.xml"));
		//BufferedReader reader=new BufferedReader(new FileReader("c:\\dd.txt"));
		String xmlAut=new String();
		String pom=new String();
		try {
			while((pom=reader.readLine())!=null) xmlAut=new StringBuffer(xmlAut).append(pom).append("\n").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(xmlAut);
	JButton b1=new JButton("Ispisi interni kod");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println(aut.getData()+"\n"+aut.isModified());
				//Automat aut2=new Automat();
				//aut2.setFileContent(new FileContent("df","safas",data));
				//getContentPane().add(aut2,BorderLayout.EAST);*/
				VHDLGenerator parser=new VHDLGenerator(aut.getData());
				System.out.println(parser.getParsedVHDL());
			};
		});
		this.add(b1,BorderLayout.NORTH);
		
		aut = new Automat();
		//FileContent fc=new FileContent("ljd","skadh",xmlAut);
		//aut.setFileContent(fc);
		aut.setProjectContainer(null);
		FileContent fc=aut.getInitialFileContent(b1, "default project");
		if (fc!=null){
			aut.init();
			aut.setFileContent(fc);
			this.getContentPane().add((Component) aut,BorderLayout.CENTER);
		}
	
		this.setSize(((Component) aut).getPreferredSize());
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Testpan tp=new Testpan();
		tp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tp.pack();
		tp.setVisible(true);	
	}
}
