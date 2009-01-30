package hr.fer.zemris.vhdllab.platform.manager.view;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.platform.manager.compilation.CompilationManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlatformContainer {

    @Autowired
    private ISystemContainer systemContainer;
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

    public ISystemContainer getSystemContainer() {
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

}
