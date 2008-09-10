package hr.fer.zemris.vhdllab.service.dependencies;

import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

public class SimulationDependancy implements DependencyExtractor {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.Functionality#configure(java.util.Properties
     * )
     */
    @Override
    public void configure(Properties properties) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.DependencyExtractor#execute(hr.fer.zemris
     * .vhdllab.entities.File)
     */
    @Override
    public Set<String> execute(File file) throws ServiceException {
        return Collections.emptySet();
    }

}
