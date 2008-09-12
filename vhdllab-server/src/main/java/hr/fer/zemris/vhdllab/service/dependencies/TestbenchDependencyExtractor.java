package hr.fer.zemris.vhdllab.service.dependencies;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
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
    public Set<String> execute(File file) throws ServiceException {
		Testbench tb = null;
        
		String source = file.getContent();
		try {
			tb = TestbenchParser.parseXml(source);
		} catch (UniformTestbenchParserException e) {
			e.printStackTrace();
			return null;
		}
		
        Set<String> dependencies = new HashSet<String>(1);
        dependencies.add(tb.getSourceName());

        return dependencies;
	}

}
