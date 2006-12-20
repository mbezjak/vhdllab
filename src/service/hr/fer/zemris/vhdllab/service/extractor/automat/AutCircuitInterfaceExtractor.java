package hr.fer.zemris.vhdllab.service.extractor.automat;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.extractor.ICircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.generator.automat.AUTParser;
import hr.fer.zemris.vhdllab.service.generator.automat.AUTPodatci;
import hr.fer.zemris.vhdllab.service.generator.automat.Prijelaz;
import hr.fer.zemris.vhdllab.service.generator.automat.Stanje;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;

public class AutCircuitInterfaceExtractor implements ICircuitInterfaceExtractor {

	public CircuitInterface extractCircuitInterface(File f) throws ServiceException {
		AUTParser aut=new AUTParser();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f.getFileName()));
		} catch (FileNotFoundException e1) {
			// TODO sto ak nema fajla?
			e1.printStackTrace();
		}
		
		
		aut.AUTParse(reader);
		
		LinkedList<Stanje> stanja = aut.stanja;
		HashSet<Prijelaz> prijelazi = aut.prijelazi;
		AUTPodatci podatci = aut.podatci;
	
		return null;//TODO vrati novi circuitinterface
	}

}
