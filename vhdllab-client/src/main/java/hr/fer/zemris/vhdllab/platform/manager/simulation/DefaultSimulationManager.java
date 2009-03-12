package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.simulations.WaveAppletMetadata;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.entity.FileType;
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
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.service.Simulator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSimulationManager extends
        AbstractEventPublisher<SimulationListener> implements SimulationManager {

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

    private FileInfo lastSimulatedFile;

    public DefaultSimulationManager() {
        super(SimulationListener.class);
    }

    @Override
    public void simulate(FileInfo file) {
        Validate.notNull(file, "File can't be null");
        if (!file.getType().isSimulatable()) {
            logger.info(file.getName() + " isn't simulatable");
            return;
        }
        ProjectInfo project = mapper.getProject(file.getProjectId());
        EditorManager em = editorManagerFactory
                .getAllAssociatedWithProject(project.getName());
        boolean shouldSimulate = em.save(true, SaveContext.COMPILE_AFTER_SAVE);
        if (shouldSimulate) {
            SimulationResult result = simulator.simulate(file);
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
        FileIdentifier identifier = showSimulationRunDialog();
        if (identifier != null) {
            simulate(mapper.getFile(identifier));
        }
    }

    private void fireSimulated(SimulationResult result) {
        for (SimulationListener l : getListeners()) {
            l.simulated(result);
        }
    }

    private void openSimulationEditor(FileInfo file, SimulationResult result) {
        String waveform = result.getWaveform();
        if (waveform != null && !waveform.equals("")) {
            ProjectInfo project = mapper.getProject(file.getProjectId());
            FileInfo simulationFile = new FileInfo(FileType.SIMULATION, file
                    .getName(), waveform, project.getId());
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

    private FileIdentifier showSimulationRunDialog() {
        Caseless projectName = projectExplorer.getSelectedProject();
        if (projectName == null) {
            return null;
        }
        ProjectInfo project = mapper.getProject(new ProjectIdentifier(
                projectName));
        List<FileInfo> files = workspaceManager.getFilesForProject(project);
        List<FileIdentifier> identifiers = new ArrayList<FileIdentifier>(files
                .size());
        for (FileInfo file : files) {
            if (file.getType().isSimulatable()) {
                identifiers
                        .add(new FileIdentifier(projectName, file.getName()));
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
