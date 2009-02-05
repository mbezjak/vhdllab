package hr.fer.zemris.vhdllab.platform.manager.view;

import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.compilation.CompilationManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.WizardManager;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlatformContainer extends AbstractLocalizationSource {

    @Autowired
    private WizardManager systemContainer;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private ViewManager viewManager;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private FileManager fileManager;
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

    public ViewManager getViewManager() {
        return viewManager;
    }

    public IdentifierToInfoObjectMapper getMapper() {
        return mapper;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public FileManager getFileManager() {
        return fileManager;
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
