package hr.fer.zemris.vhdllab.platform.manager.compilation;

import hr.fer.zemris.vhdllab.service.result.CompilationMessage;

import java.util.EventListener;
import java.util.List;

public interface CompilationListener extends EventListener {

    void compiled(List<CompilationMessage> messages);

}
