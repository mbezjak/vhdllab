package hr.fer.zemris.vhdllab.service.filetype.automaton;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.filetype.source.SourceCircuitInterfaceExtractor;

import java.io.IOException;

import org.xml.sax.SAXException;

public class AutomatonCircuitInterfaceExtractor implements
        CircuitInterfaceExtractor {

    @Override
    public CircuitInterface extract(File file)
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
        SourceCircuitInterfaceExtractor sourceExtractor = new SourceCircuitInterfaceExtractor();
        return sourceExtractor.extractCircuitInterface(VHDL);
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

}
