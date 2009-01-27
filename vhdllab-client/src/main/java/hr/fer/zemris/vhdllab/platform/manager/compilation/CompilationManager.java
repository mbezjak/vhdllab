package hr.fer.zemris.vhdllab.platform.manager.compilation;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

public interface CompilationManager extends EventPublisher<CompilationListener> {

    void compileWithDialog();

    void compileLast();

    void compile(FileInfo file);

}
