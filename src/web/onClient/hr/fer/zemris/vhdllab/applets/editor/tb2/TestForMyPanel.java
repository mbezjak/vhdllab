package hr.fer.zemris.vhdllab.applets.editor.tb2;

import hr.fer.zemris.vhdllab.applets.main.model.FileContent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class TestForMyPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5822850690261731875L;

	public TestForMyPanel() {
		super();
		TestBenchEditor2 te=new TestBenchEditor2();
		te.init();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("./bin/hr/fer/zemris/vhdllab/init/mux41_tb.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String xmlAut=new String();
		String pom=new String();
		try {
			while((pom=reader.readLine())!=null) xmlAut=new StringBuffer(xmlAut).append(pom).append("\n").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(xmlAut);
		
		FileContent fc=new FileContent("ljd","skadh",xmlAut);
		te.setFileContent(fc);
		
		this.setLayout(new BorderLayout());
		this.add(te,BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		TestForMyPanel test=new TestForMyPanel();
		test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		test.setSize(new Dimension(300,300));
		test.setVisible(true);
	}
}
