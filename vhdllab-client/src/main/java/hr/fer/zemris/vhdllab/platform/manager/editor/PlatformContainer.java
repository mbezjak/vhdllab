package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;
import hr.fer.zemris.vhdllab.view.explorer.ProjectExplorer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlatformContainer extends AbstractLocalizationSource {

    @Autowired
    private WizardManager systemContainer;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private ProjectExplorer projectExplorer;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private SimulationManager simulationManager;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private MetadataExtractionService metadataExtractionService;

    public WizardManager getSystemContainer() {
        return systemContainer;
    }

    public EditorManagerFactory getEditorManagerFactory() {
        return editorManagerFactory;
    }

    public ProjectExplorer getProjectExplorer() {
        return projectExplorer;
    }

    public IdentifierToInfoObjectMapper getMapper() {
        return mapper;
    }

    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    public WorkspaceManager getWorkspaceManager() {
        return workspaceManager;
    }

    public MetadataExtractionService getMetadataExtractionService() {
        return metadataExtractionService;
    }

}
