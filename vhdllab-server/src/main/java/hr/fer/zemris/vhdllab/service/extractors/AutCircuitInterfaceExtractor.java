package hr.fer.zemris.vhdllab.service.extractors;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.generators.automat.AUTParser;
import hr.fer.zemris.vhdllab.service.generators.automat.AUTPodatci;

import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

public class AutCircuitInterfaceExtractor implements CircuitInterfaceExtractor {

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.Functionality#configure(java.util.Properties)
     */
    @Override
    public void configure(Properties properties) {
    }

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor#extractCircuitInterface(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public CircuitInterface execute(File file) throws ServiceException {
		AUTParser aut=new AUTParser();
		try {
			aut.AUTParse(file.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		AUTPodatci podatci = aut.podatci;
		
		StringBuffer buffer=new StringBuffer();
		buffer.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
		
		buffer=addEntity(buffer, podatci);
		buffer.append("\nARCHITECTURE Behavioral OF ").append(podatci.ime)
		.append(" IS\nBEGIN\nEND Behavioral;");
		
		String VHDL=buffer.toString();
		SourceExtractor sourceExtractor = new SourceExtractor();
		return sourceExtractor.extractCircuitInterface(VHDL);
	}

	private StringBuffer addEntity(StringBuffer buffer, AUTPodatci podatci) {
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
