package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entity.File;

public interface VhdlGenerator {

    VHDLGenerationResult generate(File file) throws VhdlGenerationException;

}
