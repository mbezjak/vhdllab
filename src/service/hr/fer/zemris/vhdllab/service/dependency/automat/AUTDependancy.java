package hr.fer.zemris.vhdllab.service.dependency.automat;

import java.util.List;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;

public class AUTDependancy implements IDependency{

	public List<File> extractDependencies(File f, VHDLLabManager labman) throws ServiceException {
		return null;
	}

}
