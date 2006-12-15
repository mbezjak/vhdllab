package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;

import java.util.ArrayList;
import java.util.List;

public class TestBenchDependencyExtractor implements IDependency {

	public List<File> extractDependencies(File f, VHDLLabManager labman)
			throws ServiceException {
		String imeSklopa = null;
		String content = f.getContent();
		final String START_TAG = "<file>";
		final String END_TAG = "</file>";
		int start = content.indexOf(START_TAG);
		start += START_TAG.length();
		int end = content.indexOf(END_TAG);
		imeSklopa = content.substring(start, end);
		
		List<File> list = new ArrayList<File>(1);
		File sklop = findMatchingFile(f.getProject(), imeSklopa);
		if(sklop==null) {
			throw new ServiceException("Invalid testbench!");
		}
		list.add(sklop);
		return list;
	}

	/**
	 * Helper method which searches all project files for file with given filename,
	 * ignoring filename casing.
	 * @param p project
	 * @param filename filename
	 * @return requested file if found, or null otherwise
	 */
	private File findMatchingFile(Project p, String filename) {
		for(File f : p.getFiles()) {
			if(f.getFileName().equalsIgnoreCase(filename)) return f;
		}
		return null;
	}

}
