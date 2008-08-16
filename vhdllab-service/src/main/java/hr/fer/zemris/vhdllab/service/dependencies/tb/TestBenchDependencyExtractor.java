package hr.fer.zemris.vhdllab.service.dependencies.tb;

import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class TestBenchDependencyExtractor implements DependencyExtractor {

	/**
	 * Helper method which searches all project files for file with given filename,
	 * ignoring filename casing.
	 * @param p project
	 * @param filename filename
	 * @return requested file's name if found, or null otherwise
	 */
	private String findMatchingFile(Project p, String filename) {
		for(File f : p.getFiles()) {
			if(f.getName().equalsIgnoreCase(filename)) return f.getName();
		}
		return null;
	}

    @Override
    public Set<String> execute(File file) throws ServiceException {
        String imeSklopa = null;
        String content = file.getContent();
        final String START_TAG = "<file>";
        final String END_TAG = "</file>";
        int start = content.indexOf(START_TAG);
        start += START_TAG.length();
        int end = content.indexOf(END_TAG);
        imeSklopa = content.substring(start, end);
        
        Set<String> set = new HashSet<String>(1);
        String sklop = findMatchingFile(file.getProject(), imeSklopa);
        if(sklop==null) {
            // current implementation
            return new HashSet<String>(0);
            // previous implementation
//          throw new ServiceException("Invalid testbench!");
        }
        set.add(sklop);
        return set;
    }

    @Override
    public void configure(Properties properties) {
    }

}
