package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.compilation.CompilationManager;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlatformContainer extends AbstractLocalizationSource {

    @Autowired
    private WizardManager systemContainer;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private IProjectExplorer projectExplorer;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private CompilationManager compilationManager;
    @Autowired
    private SimulationManager simulationManager;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private VhdlGenerator vhdlGenerator;
    @Autowired
    private CircuitInterfaceExtractor circuitInterfaceExtractor;

    public WizardManager getSystemContainer() {
        return systemContainer;
    }

    public EditorManagerFactory getEditorManagerFactory() {
        return editorManagerFactory;
    }

    public IProjectExplorer getProjectExplorer() {
        return projectExplorer;
    }

    public IdentifierToInfoObjectMapper getMapper() {
        return mapper;
    }

    public CompilationManager getCompilationManager() {
        return compilationManager;
    }

    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    public WorkspaceManager getWorkspaceManager() {
        return workspaceManager;
    }

    public VhdlGenerator getVhdlGenerator() {
        return vhdlGenerator;
    }

    public CircuitInterfaceExtractor getCircuitInterfaceExtractor() {
        return circuitInterfaceExtractor;
    }

}
