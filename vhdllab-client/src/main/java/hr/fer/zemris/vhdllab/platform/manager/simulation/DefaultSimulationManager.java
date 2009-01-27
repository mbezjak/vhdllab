package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.simulations.WaveAppletMetadata;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.service.Simulator;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSimulationManager extends
        AbstractEventPublisher<SimulationListener> implements SimulationManager {

    @Autowired
    private ISystemContainer systemContainer;
    @Autowired
    private Simulator simulator;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private EditorManagerFactory editorManagerFactory;

    private FileInfo lastSimulatedFile;

    public DefaultSimulationManager() {
        super(SimulationListener.class);
    }

    @Override
    public void simulate(FileInfo file) {
        Validate.notNull(file, "File can't be null");
        SimulationResult result = simulator.simulate(file);
        lastSimulatedFile = file;
        fireSimulated(result);
        openSimulationEditor(file, result);
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
        FileIdentifier identifier = systemContainer.showSimulationRunDialog();
        simulate(mapper.getFile(identifier));
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
            editorManagerFactory.get(identifier).open();
        }
    }

}
