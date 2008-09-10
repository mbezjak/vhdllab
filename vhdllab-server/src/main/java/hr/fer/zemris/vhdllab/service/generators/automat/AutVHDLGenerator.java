package hr.fer.zemris.vhdllab.service.generators.automat;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;

public class AutVHDLGenerator implements VHDLGenerator {
	
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
     * @see hr.fer.zemris.vhdllab.service.VHDLGenerator#execute(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public VHDLGenerationResult execute(File file) throws ServiceException {
		AUTParser aut=new AUTParser();
		
		try {
			aut.AUTParse(file.getContent());
		} catch (Exception e) {
			throw new ServiceException(StatusCodes.SERVER_ERROR, e.getMessage());
		}
		
		LinkedList<Stanje> stanja = aut.stanja;
		HashSet<Prijelaz> prijelazi = aut.prijelazi;
		AUTPodatci podatci = aut.podatci;
		
		String parsedVHDL;
		if(provjera(stanja, podatci)){
			IAutomatVHDLGenerator inter=null;
			if(podatci.tip.toUpperCase().equals("MOORE"))
			 	inter=new MooreParser(stanja,podatci,prijelazi);
			else inter=new MealyParser(stanja,podatci,prijelazi);
			parsedVHDL=inter.getData();
		}else
			throw new ServiceException(StatusCodes.SERVICE_CANT_GENERATE_VHDL_CODE, "nemoguce generirati VHDL");
		return new VHDLGenerationResult(true, new ArrayList<VHDLGenerationMessage>(0), parsedVHDL);
	}

	private boolean provjera(LinkedList<Stanje> stanja, AUTPodatci podatci) {
		if(stanja.size()==0||podatci.pocetnoStanje.equals(""))return false;
		return true;
	}
	
}
