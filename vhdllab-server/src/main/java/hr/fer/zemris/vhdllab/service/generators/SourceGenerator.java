package hr.fer.zemris.vhdllab.service.generators;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * A generator for a {@link FileTypes#VHDL_SOURCE} file type.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class SourceGenerator implements VHDLGenerator {

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
        List<VHDLGenerationMessage> messages = Collections.emptyList();
        String vhdl = file.getContent();
        VHDLGenerationResult result = new VHDLGenerationResult(true, messages,
                vhdl);
        return result;
    }

}
