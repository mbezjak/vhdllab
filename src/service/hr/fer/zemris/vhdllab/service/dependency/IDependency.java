package hr.fer.zemris.vhdllab.service.dependency;

import hr.fer.zemris.vhdllab.service.VHDLLabManager;

public interface IDependency {

	List<File> extractDependencies(File f, VHDLLabManager labman);
}
