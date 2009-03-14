package hr.fer.zemris.vhdllab.service.filetype.source;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

import java.util.Collections;
import java.util.List;

public class SourceVhdlGenerator implements VhdlGenerator {

    @Override
    public VHDLGenerationResult generate(FileInfo file)
            throws VhdlGenerationException {
        List<VHDLGenerationMessage> messages = Collections.emptyList();
        String vhdl = file.getData();
        VHDLGenerationResult result = new VHDLGenerationResult(true, messages,
                vhdl);
        return result;
    }

}
