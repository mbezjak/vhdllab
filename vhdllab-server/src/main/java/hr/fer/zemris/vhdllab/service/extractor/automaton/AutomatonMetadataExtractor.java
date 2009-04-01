package hr.fer.zemris.vhdllab.service.extractor.automaton;

import hr.fer.zemris.vhdllab.applets.editor.automaton.AUTParser;
import hr.fer.zemris.vhdllab.applets.editor.automaton.AUTPodatci;
import hr.fer.zemris.vhdllab.applets.editor.automaton.Prijelaz;
import hr.fer.zemris.vhdllab.applets.editor.automaton.Stanje;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.extractor.AbstractMetadataExtractor;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.xml.sax.SAXException;

public class AutomatonMetadataExtractor extends AbstractMetadataExtractor {

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        AUTParser aut = new AUTParser();
        try {
            aut.AUTParse(file.getData());
        } catch (IOException e) {
            throw new CircuitInterfaceExtractionException(e);
        } catch (SAXException e) {
            throw new CircuitInterfaceExtractionException(e);
        }
        AUTPodatci podatci = aut.podatci;

        StringBuffer buffer = new StringBuffer();
        buffer.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");

        buffer = addEntity(buffer, podatci);
        buffer.append("\nARCHITECTURE Behavioral OF ").append(podatci.ime)
                .append(" IS\nBEGIN\nEND Behavioral;");

        String VHDL = buffer.toString();
        File source = new File(file.getName(), FileType.SOURCE, VHDL);
        return metadataExtractor.extractCircuitInterface(source);
    }

    private StringBuffer addEntity(StringBuffer buffer, AUTPodatci podatci) {
        buffer.append("ENTITY ").append(podatci.ime).append(" IS PORT(\n")
                .append("\tclock: IN std_logic;\n\treset: IN std_logic;");
        String[] redovi = podatci.interfac.split("\n");
        for (int i = 0; i < redovi.length; i++) {
            buffer.append("\n\t");
            String[] rijeci = redovi[i].split(" ");
            buffer.append(rijeci[0]).append(": ").append(
                    rijeci[1].toUpperCase()).append(" ").append(rijeci[2]);
            if (rijeci[2].toUpperCase().equals("STD_LOGIC_VECTOR")) {
                if (Integer.parseInt(rijeci[3]) < Integer.parseInt(rijeci[4]))
                    buffer.append("(").append(Integer.parseInt(rijeci[3]))
                            .append(" TO ").append(Integer.parseInt(rijeci[4]))
                            .append(")");
                else
                    buffer.append("(").append(Integer.parseInt(rijeci[3]))
                            .append(" DOWNTO ").append(
                                    Integer.parseInt(rijeci[4])).append(")");
            }
            buffer.append(";");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(");\nEND ").append(podatci.ime).append(";\n");
        return buffer;
    }

    @Override
    protected CircuitInterface doExtractCircuitInterface(String data)
            throws CircuitInterfaceExtractionException {
        throw new NotImplementedException();
    }

    @Override
    protected Set<String> doExtractDependencies(String data)
            throws DependencyExtractionException {
        return Collections.emptySet();
    }

    @Override
    protected Result doGenerateVhdl(String data) throws VhdlGenerationException {
        AUTParser aut = new AUTParser();

        try {
            aut.AUTParse(data);
        } catch (Exception e) {
            throw new VhdlGenerationException(e.getMessage());
        }

        LinkedList<Stanje> stanja = aut.stanja;
        HashSet<Prijelaz> prijelazi = aut.prijelazi;
        AUTPodatci podatci = aut.podatci;

        String parsedVHDL;
        if (provjera(stanja, podatci)) {
            IAutomatVHDLGenerator inter = null;
            if (podatci.tip.toUpperCase().equals("MOORE"))
                inter = new MooreParser(stanja, podatci, prijelazi);
            else
                inter = new MealyParser(stanja, podatci, prijelazi);
            parsedVHDL = inter.getData();
        } else
            throw new VhdlGenerationException("nemoguce generirati VHDL");
        return new Result(parsedVHDL);
    }

    private boolean provjera(LinkedList<Stanje> stanja, AUTPodatci podatci) {
        if (stanja.size() == 0 || podatci.pocetnoStanje.equals(""))
            return false;
        return true;
    }

}
