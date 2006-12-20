package hr.fer.zemris.vhdllab.service.generator.automat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class VHDLGeneratorTester {
public static void main(String[] args) {
	BufferedReader reader = null;
	try {
		reader = new BufferedReader(new FileReader("./src/web/onClient/hr/fer/zemris/vhdllab/applets/automat/automat1.xml"));
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
	//BufferedReader reader=new BufferedReader(new FileReader("c:\\dd.txt"));
	String xmlAut=new String();
	String pom=new String();
	try {
		while((pom=reader.readLine())!=null) xmlAut=new StringBuffer(xmlAut).append(pom).append("\n").toString();
	} catch (IOException e) {
		e.printStackTrace();
	}
	VHDLGenerator parser=new VHDLGenerator(xmlAut);
	System.out.println(parser.getParsedVHDL());
}
}
