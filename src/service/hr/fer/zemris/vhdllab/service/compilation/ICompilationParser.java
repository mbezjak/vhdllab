package hr.fer.zemris.vhdllab.service.compilation;

import hr.fer.zemris.vhdllab.vhdl.CompilationResult;

public interface ICompilationParser {

	CompilationResult parseResult(String result);
	
}
