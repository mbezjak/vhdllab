package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.FileInfo;

public class VhdlGenerationService extends AbstractFileTypeFactory implements
        VhdlGenerator {

    @Override
    public VHDLGenerationResult generate(FileInfo file)
            throws VhdlGenerationException {
        VhdlGenerator generator = getBean(file.getType(), VhdlGenerator.class);
        return generator.generate(file);
    }

}
