package hr.fer.zemris.vhdllab.service.dependencies;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.Collections;
import java.util.List;

/**
 * A dependency extractor for a {@link FileTypes#VHDL_PREDEFINED} file type.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class PredefinedDependency implements DependencyExtractor {

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.DependencyExtractor#execute(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public List<String> execute(File file) throws ServiceException {
        return Collections.emptyList();
    }

}
