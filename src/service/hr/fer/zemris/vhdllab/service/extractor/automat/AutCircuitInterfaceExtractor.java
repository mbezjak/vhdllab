package hr.fer.zemris.vhdllab.service.extractor.automat;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.extractor.ICircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.generator.automat.AUTParser;
import hr.fer.zemris.vhdllab.service.generator.automat.AUTPodatci;
import hr.fer.zemris.vhdllab.service.generator.automat.IAutomatVHDLGenerator;
import hr.fer.zemris.vhdllab.service.generator.automat.Prijelaz;
import hr.fer.zemris.vhdllab.service.generator.automat.Stanje;
import hr.fer.zemris.vhdllab.service.generator.automat.VHDLGenerator;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;

public class AutCircuitInterfaceExtractor implements ICircuitInterfaceExtractor {

	private AUTPodatci podatci;

	public CircuitInterface extractCircuitInterface(File f) throws ServiceException {
		AUTParser aut=new AUTParser();
		try {
			aut.AUTParse(f.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		podatci = aut.podatci;
		
		StringBuffer buffer=new StringBuffer();
		buffer.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
		
		buffer=addEntity(buffer);
		buffer.append("\nARCHITECTURE Behavioral OF ").append(podatci.ime)
		.append(" IS\nBEGIN\nEND Behavioral;");
		
		String VHDL=buffer.toString();
		CircuitInterface circuit=Extractor.extractCircuitInterface(VHDL);
		return circuit;
	}

	private StringBuffer addEntity(StringBuffer buffer) {
		buffer.append("ENTITY ").append(podatci.ime).append(" IS PORT(\n")
		.append("\tclock: IN std_logic;\n\treset: IN std_logic;");
		String[] redovi=podatci.interfac.split("\n");
		for(int i=0;i<redovi.length;i++){
			buffer.append("\n\t");
			String[] rijeci=redovi[i].split(" ");
			buffer.append(rijeci[0]).append(": ").append(rijeci[1].toUpperCase())
			.append(" ").append(rijeci[2]);
			if(rijeci[2].toUpperCase().equals("STD_LOGIC_VECTOR")){
				if(Integer.parseInt(rijeci[3])<Integer.parseInt(rijeci[4]))
					buffer.append("(").append(Integer.parseInt(rijeci[3])).append(" TO ").append(Integer.parseInt(rijeci[4])).append(")");
				else buffer.append("(").append(Integer.parseInt(rijeci[3])).append(" DOWNTO ").append(Integer.parseInt(rijeci[4])).append(")");
			}
			buffer.append(";");
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append(");\nEND ").append(podatci.ime).append(";\n");
		return buffer;
	}
}
