package hr.fer.zemris.vhdllab.platform.manager.compilation;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.service.Compiler;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCompilationManager extends
        AbstractEventPublisher<CompilationListener> implements
        CompilationManager {

    @Autowired
    private ISystemContainer systemContainer;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private Compiler compiler;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;

    private FileInfo lastCompiledFile;

    public DefaultCompilationManager() {
        super(CompilationListener.class);
    }

    @Override
    public void compile(FileInfo file) {
        Validate.notNull(file, "File can't be null");
        ProjectInfo project = mapper.getProject(file.getProjectId());
        EditorManager em = editorManagerFactory
                .getAllAssociatedWithProject(project.getName());
        boolean shouldCompile = em.save(true, SaveContext.COMPILE_AFTER_SAVE);
        if(shouldCompile) {
            CompilationResult result = compiler.compile(file);
            lastCompiledFile = file;
            fireCompiled(result);
        }
    }

    @Override
    public void compileLast() {
        if (lastCompiledFile == null) {
            compileWithDialog();
        } else {
            compile(lastCompiledFile);
        }
    }

    @Override
    public void compileWithDialog() {
        FileIdentifier identifier = systemContainer.showCompilationRunDialog();
        compile(mapper.getFile(identifier));
    }

    private void fireCompiled(CompilationResult result) {
        for (CompilationListener l : getListeners()) {
            l.compiled(result);
        }
    }

}
