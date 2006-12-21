package hr.fer.zemris.vhdllab.service.dependency.automat;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;

import java.util.LinkedList;
import java.util.List;

public class AUTDependancy implements IDependency{

	public List<File> extractDependencies(File f, VHDLLabManager labman) throws ServiceException {
		return new LinkedList<File>();
	}

}
