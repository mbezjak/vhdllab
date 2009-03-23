package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.simulations.WaveAppletMetadata;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.gui.dialog.run.RunContext;
import hr.fer.zemris.vhdllab.platform.gui.dialog.run.RunDialog;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.Simulator;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSimulationManager extends
        AbstractEventPublisher<SimulationListener> implements SimulationManager {

    private static final String COMPILED_MESSAGE = "notification.compiled";
    private static final String SIMULATED_MESSAGE = "notification.simulated";

    @Autowired
    private Simulator simulator;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Resource(name = "standaloneLocalizationSource")
    private LocalizationSource localizationSource;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private IProjectExplorer projectExplorer;

    private File lastCompiledFile;
    private File lastSimulatedFile;

    public DefaultSimulationManager() {
        super(SimulationListener.class);
    }

    @Override
    public void compile(File file) {
        Validate.notNull(file, "File can't be null");
        if (!file.getType().isCompilable()) {
            logger.info(file.getName() + " isn't compilable");
            return;
        }
        Project project = file.getProject();
        EditorManager em = editorManagerFactory
                .getAllAssociatedWithProject(project);
        boolean shouldCompile = em.save(true, SaveContext.COMPILE_AFTER_SAVE);
        if (shouldCompile) {
            List<CompilationMessage> messages = simulator.compile(file.getId());
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
        File identifier = showCompilationRunDialog();
        if (identifier != null) {
            compile(identifier);
        }
    }

    private void fireCompiled(List<CompilationMessage> messages) {
        for (SimulationListener l : getListeners()) {
            l.compiled(messages);
        }
    }

    private File showCompilationRunDialog() {
        Project project = projectExplorer.getSelectedProject();
        if (project == null) {
            return null;
        }
        Set<File> files = workspaceManager.getFilesForProject(project);
        List<File> identifiers = new ArrayList<File>(files.size());
        for (File file : files) {
            if (file.getType().isCompilable()) {
                identifiers.add(file);
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

    @Override
    public void simulate(File file) {
        Validate.notNull(file, "File can't be null");
        if (!file.getType().isSimulatable()) {
            logger.info(file.getName() + " isn't simulatable");
            return;
        }
        Project project = file.getProject();
        EditorManager em = editorManagerFactory
                .getAllAssociatedWithProject(project);
        boolean shouldSimulate = em.save(true, SaveContext.COMPILE_AFTER_SAVE);
        if (shouldSimulate) {
            Result result = simulator.simulate(file.getId());
            lastSimulatedFile = file;
            fireSimulated(result);
            openSimulationEditor(file, result);
            logger.info(localizationSource.getMessage(SIMULATED_MESSAGE,
                    new Object[] { file.getName(), project.getName() }));
        }
    }

    @Override
    public void simulateLast() {
        if (lastSimulatedFile == null) {
            simulateWithDialog();
        } else {
            simulate(lastSimulatedFile);
        }
    }

    @Override
    public void simulateWithDialog() {
        File identifier = showSimulationRunDialog();
        if (identifier != null) {
            simulate(identifier);
        }
    }

    private void fireSimulated(Result result) {
        for (SimulationListener l : getListeners()) {
            l.simulated(result);
        }
    }

    private void openSimulationEditor(File file, Result result) {
        String waveform = result.getData();
        if (!StringUtils.isBlank(waveform)) {
            File simulationFile = new File(file.getName(), FileType.SIMULATION,
                    waveform);
            simulationFile.setProject(file.getProject());
            EditorIdentifier identifier = new EditorIdentifier(
                    new WaveAppletMetadata(), simulationFile);
            EditorManager simulationEditor = editorManagerFactory
                    .get(identifier);
            if (simulationEditor.isOpened()) {
                simulationEditor.close();
            }
            simulationEditor.open();
        }
    }

    private File showSimulationRunDialog() {
        Project project = projectExplorer.getSelectedProject();
        if (project == null) {
            return null;
        }
        Set<File> files = workspaceManager.getFilesForProject(project);
        List<File> identifiers = new ArrayList<File>(files.size());
        for (File file : files) {
            if (file.getType().isSimulatable()) {
                identifiers.add(file);
            }
        }
        if (files.isEmpty()) {
            return null;
        }
        RunDialog dialog = new RunDialog(localizationSource,
                RunContext.SIMULATION);
        dialog.setRunFiles(identifiers);
        dialog.startDialog();
        return dialog.getResult();
    }

}
