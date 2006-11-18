package hr.fer.zemris.vhdllab.vhdl;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VHDLDependencyExtractor implements IDependency {

	public VHDLDependencyExtractor() {
		super();
	}

	/* (non-Javadoc)
	 * TODO: In this method obtaining File from component name is solved rather bad. The problem is that
	 * vhdl is not case sensitive, so names of components can be extracted with unknown casing. This is why
	 * manager must be updated to support File retrieval based on case-insensitive filename. Also, this
	 * method assumes that names of components are with no extensions. If this is not the case, then search
	 * which is independent of filename extension sould be added. Finally, it seems to me that a method
	 * which would list all filenames from project is missing. 
	 * @see hr.fer.zemris.vhdllab.service.dependency.IDependency#extractDependencies(hr.fer.zemris.vhdllab.model.File, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public List<File> extractDependencies(File f, VHDLLabManager labman) throws ServiceException {
		List<File> result = new ArrayList<File>();
		String  source = f.getContent();
		Set<String> usedComponents = Extractor.extractUsedComponents(source);
		for(String componentName : usedComponents) {
			File component  = findMatchingFile(f.getProject(), componentName);
			if(component==null) return null;
			result.add(component);
		}
		return result;
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
