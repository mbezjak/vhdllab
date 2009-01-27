package hr.fer.zemris.vhdllab.platform.manager.compilation;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;

import java.util.EventListener;

public interface CompilationListener extends EventListener {

    void compiled(CompilationResult result);

}
