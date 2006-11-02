package hr.fer.zemris.vhdllab.service.dependency;

import java.util.List;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

public interface IDependency {

	/**
	 * This method will extract all first level (and only first level) dependencies for given file.
	 * For each supported filetype an implementation of this interface must be provided,
	 * and registered in VHDLLabManager, where method {@linkplain VHDLLabManager#extractDependencies(File)}
	 * uses those extractors. Also, that method will resolve recursive dependencies, so this method does
	 * not need to take them into account.
	 * @param f file which dependencies are to be extracted
	 * @param labman VHDLLabManager whose services may be used during dependency extraction
	 * @return a list of first level dependencies (so called "direct dependencies")
	 * @throws ServiceException
	 */
	List<File> extractDependencies(File f, VHDLLabManager labman) throws ServiceException;
	
}
