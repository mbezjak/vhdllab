package hr.fer.zemris.vhdllab.service.filetype.automaton;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AutomatonVhdlGenerator implements VhdlGenerator {

    @Override
    public VHDLGenerationResult generate(FileInfo file)
            throws VhdlGenerationException {
        AUTParser aut=new AUTParser();
        
        try {
            aut.AUTParse(file.getData());
        } catch (Exception e) {
            throw new VhdlGenerationException(e.getMessage());
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
            throw new VhdlGenerationException("nemoguce generirati VHDL");
        return new VHDLGenerationResult(true, new ArrayList<VHDLGenerationMessage>(0), parsedVHDL);
    }

    private boolean provjera(LinkedList<Stanje> stanja, AUTPodatci podatci) {
        if(stanja.size()==0||podatci.pocetnoStanje.equals(""))return false;
        return true;
    }
    
}
