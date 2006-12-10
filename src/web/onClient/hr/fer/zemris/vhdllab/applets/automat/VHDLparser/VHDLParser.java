package hr.fer.zemris.vhdllab.applets.automat.VHDLparser;

import hr.fer.zemris.vhdllab.applets.automat.AUTParser;
import hr.fer.zemris.vhdllab.applets.automat.AUTPodatci;
import hr.fer.zemris.vhdllab.applets.automat.Prijelaz;
import hr.fer.zemris.vhdllab.applets.automat.Stanje;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import org.xml.sax.SAXException;

public class VHDLParser {
	protected String parsedVHDL;
	
	public VHDLParser(String data) {
		super();
		AUTParser aut=new AUTParser();
		try {
			aut.AUTParse(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		LinkedList<Stanje> stanja = aut.stanja;
		HashSet<Prijelaz> prijelazi = aut.prijelazi;
		AUTPodatci podatci = aut.podatci;
		
		IAutomatVHDLParser inter=null;
		if(podatci.tip.toUpperCase().equals("MOORE"))
			 inter=new MooreParser(stanja,podatci,prijelazi);
		else inter=new MealyParser(stanja,podatci,prijelazi);
		parsedVHDL=inter.getData();
	}
	
	public String getParsedVHDL() {
		return parsedVHDL;
	}
	
}
