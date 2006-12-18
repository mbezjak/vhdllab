package hr.fer.zemris.vhdllab.compilers;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;

import java.util.List;

public interface ICompiler {
	CompilationResult compile(List<File> dbFiles, List<File> otherFiles, File compileFile, VHDLLabManager vhdlman);
}
