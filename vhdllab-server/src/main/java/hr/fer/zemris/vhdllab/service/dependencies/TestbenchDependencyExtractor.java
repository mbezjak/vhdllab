package hr.fer.zemris.vhdllab.service.dependencies;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class TestbenchDependencyExtractor implements DependencyExtractor {

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
     * @see hr.fer.zemris.vhdllab.service.DependencyExtractor#execute(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public Set<Caseless> execute(File file) throws ServiceException {
		Testbench tb = null;
        
		String source = file.getData();
		try {
			tb = TestbenchParser.parseXml(source);
		} catch (UniformTestbenchParserException e) {
			throw new ServiceException(StatusCodes.SERVICE_CANT_EXTRACT_DEPENDENCIES, e);
		}
		
        Set<Caseless> dependencies = new HashSet<Caseless>(1);
        dependencies.add(new Caseless(tb.getSourceName()));

        return dependencies;
	}

}
