package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.platform.manager.compilation.CompilationManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import javax.swing.JPanel;

public abstract class AbstractView extends JPanel implements View {

    private static final long serialVersionUID = 1L;

    private ISystemContainer systemContainer;
    private EditorManagerFactory editorManagerFactory;
    private ViewManager viewManager;
    private IdentifierToInfoObjectMapper mapper;
    private ProjectManager projectManager;
    private FileManager fileManager;
    private CompilationManager compilationManager;
    private SimulationManager simulationManager;

    @Override
    public ISystemContainer getSystemContainer() {
        return systemContainer;
    }

    @Override
    public void setSystemContainer(ISystemContainer systemContainer) {
        this.systemContainer = systemContainer;
    }

    @Override
    public EditorManagerFactory getEditorManagerFactory() {
        return editorManagerFactory;
    }

    @Override
    public void setEditorManagerFactory(
            EditorManagerFactory editorManagerFactory) {
        this.editorManagerFactory = editorManagerFactory;
    }

    @Override
    public ViewManager getViewManager() {
        return viewManager;
    }

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @Override
    public IdentifierToInfoObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public void setMapper(IdentifierToInfoObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    @Override
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public CompilationManager getCompilationManager() {
        return compilationManager;
    }

    @Override
    public void setCompilationManager(CompilationManager compilationManager) {
        this.compilationManager = compilationManager;
    }

    @Override
    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    @Override
    public void setSimulationManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}
