package hr.fer.zemris.vhdllab.service.dependency;

import java.util.List;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

public interface IDependency {

	List<File> extractDependencies(File f, VHDLLabManager labman);
	
}
