package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.FileInfo;

public interface VhdlGenerator {

    VHDLGenerationResult generate(FileInfo file) throws VhdlGenerationException;

}
