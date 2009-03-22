package hr.fer.zemris.vhdllab.platform.manager.compilation;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.gui.dialog.run.RunContext;
import hr.fer.zemris.vhdllab.platform.gui.dialog.run.RunDialog;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCompilationManager extends
        AbstractEventPublisher<CompilationListener> implements
        CompilationManager {

    private static final String COMPILED_MESSAGE = "notification.compiled";

    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private Compiler compiler;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Resource(name = "standaloneLocalizationSource")
    private LocalizationSource localizationSource;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private IProjectExplorer projectExplorer;

    private File lastCompiledFile;

    public DefaultCompilationManager() {
        super(CompilationListener.class);
    }

    @Override
    public void compile(File file) {
        Validate.notNull(file, "File can't be null");
        if (!file.getType().isCompilable()) {
            logger.info(file.getName() + " isn't compilable");
            return;
        }
        Project project = mapper.getProject(file.getProjectId());
        EditorManager em = editorManagerFactory
                .getAllAssociatedWithProject(project.getName());
        boolean shouldCompile = em.save(true, SaveContext.COMPILE_AFTER_SAVE);
        if (shouldCompile) {
            List<CompilationMessage> messages = compiler.compile(file);
            lastCompiledFile = file;
            fireCompiled(messages);
            logger.info(localizationSource.getMessage(COMPILED_MESSAGE,
                    new Object[] { file.getName(), project.getName() }));
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
        FileIdentifier identifier = showCompilationRunDialog();
        if (identifier != null) {
            compile(mapper.getFile(identifier));
        }
    }

    private void fireCompiled(List<CompilationMessage> messages) {
        for (CompilationListener l : getListeners()) {
            l.compiled(messages);
        }
    }

    private FileIdentifier showCompilationRunDialog() {
        Caseless projectName = projectExplorer.getSelectedProject();
        if (projectName == null) {
            return null;
        }
        Project project = mapper.getProject(new ProjectIdentifier(
                projectName));
        List<File> files = workspaceManager.getFilesForProject(project);
        List<FileIdentifier> identifiers = new ArrayList<FileIdentifier>(files
                .size());
        for (File file : files) {
            if (file.getType().isCompilable()) {
                identifiers
                        .add(new FileIdentifier(projectName, file.getName()));
            }
        }
        if (files.isEmpty()) {
            return null;
        }
        RunDialog dialog = new RunDialog(localizationSource,
                RunContext.COMPILATION);
        dialog.setRunFiles(identifiers);
        dialog.startDialog();
        return dialog.getResult();
    }

}
